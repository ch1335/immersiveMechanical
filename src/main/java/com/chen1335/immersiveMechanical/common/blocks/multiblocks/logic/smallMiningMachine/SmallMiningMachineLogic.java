package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.smallMiningMachine;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.AveragingEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.*;
import blusunrize.immersiveengineering.api.tool.upgrade.UpgradeEffect;
import blusunrize.immersiveengineering.common.items.DrillItem;
import blusunrize.immersiveengineering.common.items.DrillheadItem;
import blusunrize.immersiveengineering.common.register.IEFluids;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.inventory.WrappingItemHandler;
import blusunrize.immersiveengineering.common.util.sound.MultiblockSound;
import com.chen1335.immersiveMechanical.common.IMItemHandlerHelper;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes.SmallMiningMachineShape;
import com.chen1335.immersiveMechanical.config.IMServerConfig;
import com.chen1335.immersiveMechanical.mixins.immersive_mechanical.DrillHeadPermAccessor;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SmallMiningMachineLogic implements IMultiblockLogic<SmallMiningMachineLogic.State>, IServerTickableComponent<SmallMiningMachineLogic.State>, IClientTickableComponent<SmallMiningMachineLogic.State> {
    public static final int ENERGY_CAPACITY = 64000;
    private static final CapabilityPosition ENERGY_INPUT = new CapabilityPosition(1, 2, 2, RelativeBlockFace.UP);
    private static final GameProfile MINER = new GameProfile(UUID.fromString("c870399f-003d-4f94-892d-140bb095f381"), "[SmallMiningMachineMiner]");
    private static final BlockPos CENTER = new BlockPos(1, 0, 1);
    private static final CapabilityPosition ITEM_OUTPUT = new CapabilityPosition(1, 2, 2, RelativeBlockFace.BACK);


    @Override
    public void tickClient(IMultiblockContext<State> context) {
        State state = context.getState();
        BlockPos absolute = context.getLevel().toAbsolute(CENTER);
        Vec3 pos = new Vec3(absolute.getX() + 0.5, absolute.getY() + 0.5, absolute.getZ() + 0.5);
        if (state.active) {
            state.rotate(20);
            context.getLevel().getRawLevel().playLocalSound(pos.x, pos.y, pos.z, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.5F, 1, true);
        } else {
            state.rotate(0);
        }
        if (!state.isPlayingSound.getAsBoolean()) {
            state.isPlayingSound = MultiblockSound.startSound(
                    () -> state.active, context.isValid(), pos, IESounds.drill_harvest, 0.5f
            );
        }
    }

    @Override
    public void tickServer(IMultiblockContext<State> context) {
        State state = context.getState();
        BlockPos.MutableBlockPos currentMinedPos = state.currentMinedPos;
        Level rawLevel = context.getLevel().getRawLevel();
        IItemHandler itemHandler = state.output.get();
        state.full = state.full();
        if (context.getLevel().shouldTickModulo(8)) {
            for (int j = 4; j < 13; j++) {
                ItemStack slot = state.inventory.getStackInSlot(j);
                if (!slot.isEmpty()) {
                    state.inventory.setStackInSlot(j, ItemHandlerHelper.insertItemStacked(itemHandler, slot, false));
                }
            }
        }

        boolean available = available(state);
        if (!available) {
            if (state.active) {
                state.active = false;
                state.syncRunnable.run();
            }
            return;
        }
        if (currentMinedPos == null) {
            BlockPos absolute = context.getLevel().toAbsolute(CENTER);
            ChunkAccess chunk = rawLevel.getChunk(absolute);
            state.chunkPos = chunk.getPos();
            if (state.inventory.getStackInSlot(0).getItem() instanceof DrillheadItem drillheadItem) {
                state.resize(((DrillHeadPermAccessor) drillheadItem.perms).IM$getDrillSize());
            }
            state.currentMinedPos = new BlockPos.MutableBlockPos(absolute.getX(), absolute.getY() - 1, absolute.getZ());
            currentMinedPos = state.currentMinedPos;
        }
        if (!state.active) {
            state.active = true;
            state.syncRunnable.run();
        }
        Integer damage = DrillItem.getUpgradesStatic(state.drill).get(UpgradeEffect.DAMAGE);
        FakePlayer fakePlayer = FakePlayerFactory.get((ServerLevel) rawLevel, MINER);
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, state.drill);
        state.energy.extractEnergy(IMServerConfig.MACHINES.small_mining_machine_consumption.get(), false);

        int defaultSpeed = IMServerConfig.MACHINES.small_mining_machine_default_speed.getAsInt();
        int additionSpeed = IMServerConfig.MACHINES.small_mining_machine_addition_speed_per_augers.getAsInt();
        for (int i = 0; i < defaultSpeed + (damage * additionSpeed); i++) {
            currentMinedPos.setX(Mth.clamp(currentMinedPos.getX(), state.minX, state.maxX));
            currentMinedPos.setZ(Mth.clamp(currentMinedPos.getZ(), state.minZ, state.maxZ));
            BlockState blockState = rawLevel.getBlockState(currentMinedPos);
            if (!blockState.isAir() && blockState.is(Tags.Blocks.ORES) && blockState.canHarvestBlock(rawLevel, currentMinedPos, fakePlayer)) {
                List<ItemStack> drops = Block.getDrops(blockState, (ServerLevel) rawLevel, currentMinedPos, rawLevel.getBlockEntity(currentMinedPos), fakePlayer, state.drill);
                for (ItemStack drop : drops) {
                    drop = drop.copy();
                    if (itemHandler != null) {
                        drop = ItemHandlerHelper.insertItemStacked(itemHandler, drop, false);
                    }
                    IMItemHandlerHelper.insertItemStacked(state.inventory, drop, 4, 13, false);
                }
                rawLevel.removeBlock(currentMinedPos, false);
                damageDrillHead(state);
            }

            currentMinedPos.setX(currentMinedPos.getX() + 1);
            if (currentMinedPos.getX() >= state.maxX) {
                currentMinedPos.setX(state.minX);
                currentMinedPos.setZ(currentMinedPos.getZ() + 1);
                if (currentMinedPos.getZ() >= state.maxZ) {
                    currentMinedPos.setZ(state.minZ);
                    currentMinedPos.setY(currentMinedPos.getY() - 1);
                    if (currentMinedPos.getY() < rawLevel.getMinBuildHeight()) {
                        currentMinedPos.setY(rawLevel.getMinBuildHeight());
                        state.finished = true;
                    }
                }
            }
        }
    }

    public boolean available(State state) {
        boolean energyAvailable = state.getEnergy().getEnergyStored() >= IMServerConfig.MACHINES.small_mining_machine_consumption.get();
        boolean drillHeadAvailable = false;
        ItemStack drillHead = state.inventory.getStackInSlot(0);
        if (drillHead.getItem() instanceof DrillheadItem drillheadItem && drillheadItem.getHeadDamage(drillHead) < drillheadItem.getMaximumHeadDamage(drillHead)) {
            drillHeadAvailable = true;
        }
        boolean inventoryAvailable = !state.full;
        boolean finishedAvailable = !state.finished;
        return energyAvailable && drillHeadAvailable && inventoryAvailable && finishedAvailable;
    }

    public void damageDrillHead(State state) {
        ItemStack drillHead = state.inventory.getStackInSlot(0);
        if (drillHead.getItem() instanceof DrillheadItem drillheadItem) {
            DrillItem drillItem = (DrillItem) state.drill.getItem();
            if (!drillItem.getUpgrades(state.drill).has(UpgradeEffect.OILED) || ApiUtils.RANDOM.nextInt(4) == 0) {
                drillheadItem.damageHead(drillHead, 1);
            }
        }
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<SmallMiningMachineLogic.State> register) {
        register.register(Capabilities.EnergyStorage.BLOCK, (state, position) -> {
            return position.side() == null || ENERGY_INPUT.equals(position) ? state.energy : null;
        });

        register.register(Capabilities.ItemHandler.BLOCK, (state, position) -> {
            return position.side() == null || ITEM_OUTPUT.equals(position) ? state.outputHandler : null;
        });
    }


    @Override
    public void dropExtraItems(State state, Consumer<ItemStack> drop) {
        MBInventoryUtils.dropItems(state.inventory, drop);
    }

    @Override
    public State createInitialState(IInitialMultiblockContext<State> context) {
        return new State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return new SmallMiningMachineShape();
    }

    public static class State implements IMultiblockState {
        private final AveragingEnergyStorage energy = new AveragingEnergyStorage(ENERGY_CAPACITY);
        private final Runnable syncRunnable;
        private final Supplier<Level> levelSupplier;
        public boolean active = false;
        public float angleO = 0;
        public float angle = 0;
        public ItemStack drill = IEItems.Tools.DRILL.get().getDefaultInstance();
        private boolean full = false;
        public int minX = 0;
        public int maxX = 0;
        public int minZ = 0;
        public int maxZ = 0;
        public boolean finished = false;
        public BlockPos.MutableBlockPos currentMinedPos = null;
        public final Supplier<IItemHandler> output;
        public final WrappingItemHandler outputHandler;
        private BooleanSupplier isPlayingSound = () -> false;
        public ItemStackHandler inventory = new ItemStackHandler(13) {
            @Override
            public int getSlotLimit(int slot) {
                if (slot == 0) {
                    return 1;
                }
                return 64;
            }

            @Override
            protected void onContentsChanged(int slot) {
                ItemStack stack = getStackInSlot(slot);
                if (slot == 0) {
                    if (stack.getItem() instanceof DrillheadItem drillheadItem) {
                        DrillItem.setHeadStatic(drill, stack);
                        resize(((DrillHeadPermAccessor) drillheadItem.perms).IM$getDrillSize());
                    } else {
                        DrillItem.setHeadStatic(drill, ItemStack.EMPTY);
                    }
                    syncRunnable.run();
                } else if (slot <= 3) {
                    IItemHandler capability = drill.getCapability(Capabilities.ItemHandler.ITEM);
                    if (capability != null) {
                        capability.extractItem(slot, 64, false);
                        capability.insertItem(slot, stack.copy(), false);
                        ((DrillItem) drill.getItem()).recalculateUpgrades(drill, levelSupplier.get(), null);
                        syncRunnable.run();
                    }

                }
            }
        };
        public ChunkPos chunkPos = new ChunkPos(0, 0);

        public void rotate(float angle) {
            this.angleO = this.angle;
            this.angle = this.angle + angle;
        }

        public static final MultiblockFace ITEM_OUT = new MultiblockFace(1, 2, 3, RelativeBlockFace.BACK);

        public State(IInitialMultiblockContext<State> context) {
            this.syncRunnable = context.getSyncRunnable();
            this.levelSupplier = context.levelSupplier();
            output = context.getCapabilityAt(Capabilities.ItemHandler.BLOCK, ITEM_OUT);

            this.outputHandler = new WrappingItemHandler(
                    inventory, false, true, new WrappingItemHandler.IntRange(4, 13)
            );

            IFluidHandler handler = FluidUtil.getFluidHandler(drill).orElseThrow(RuntimeException::new);
            handler.fill(new FluidStack(IEFluids.BIODIESEL.getStill(), 2000), IFluidHandler.FluidAction.EXECUTE);
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            drill = ItemStack.parse(provider, nbt.getCompound("drill")).orElse(drill);
            active = nbt.getBoolean("active");
        }

        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            nbt.put("drill", drill.save(provider));
            nbt.putBoolean("active", active);
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            nbt.put("inventory", inventory.serializeNBT(provider));
            nbt.put("drill", drill.save(provider));
            if (currentMinedPos != null) {
                nbt.putInt("x", currentMinedPos.getX());
                nbt.putInt("y", currentMinedPos.getY());
                nbt.putInt("z", currentMinedPos.getZ());
            }
            nbt.putInt("chunkPosX", chunkPos.x);
            nbt.putInt("chunkPosZ", chunkPos.z);
            nbt.putBoolean("finished", finished);
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            inventory.deserializeNBT(provider, nbt.getCompound("inventory"));
            drill = ItemStack.parse(provider, nbt.getCompound("drill")).orElse(drill);
            currentMinedPos = new BlockPos.MutableBlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
            chunkPos = new ChunkPos(nbt.getInt("chunkPosX"), nbt.getInt("chunkPosZ"));
            finished = nbt.getBoolean("finished");
            ItemStack drillHead = inventory.getStackInSlot(0);
            if (drillHead.getItem() instanceof DrillheadItem drillheadItem) {
                resize(((DrillHeadPermAccessor) drillheadItem.perms).IM$getDrillSize());
            }
        }

        public void resize(int drillSize) {
            int i = (drillSize - 1) / 2;
            this.minX = (chunkPos.x - i) << 4;
            this.maxX = (chunkPos.x - i + drillSize) << 4;
            this.minZ = (chunkPos.z - i) << 4;
            this.maxZ = (chunkPos.z - i + drillSize) << 4;
        }

        public boolean full() {
            for (int i = 4; i < 13; i++) {
                if (inventory.getStackInSlot(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public AveragingEnergyStorage getEnergy() {
            return energy;
        }

        public void reset() {
            currentMinedPos = null;
            finished = false;
        }
    }
}
