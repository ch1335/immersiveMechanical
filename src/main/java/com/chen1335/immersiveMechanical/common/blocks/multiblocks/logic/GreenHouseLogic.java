package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic;

import blusunrize.immersiveengineering.api.crafting.ClocheFertilizer;
import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.CapabilityPosition;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.MultiblockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.RelativeBlockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import blusunrize.immersiveengineering.common.blocks.multiblocks.blockimpl.InitialMultiblockContext;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler;
import blusunrize.immersiveengineering.common.util.CachedRecipe;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes.GreenHouseShape;
import com.chen1335.immersiveMechanical.network.GreenHouseGrowsPack;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class GreenHouseLogic implements IMultiblockLogic<GreenHouseLogic.State>, IServerTickableComponent<GreenHouseLogic.State>, IClientTickableComponent<GreenHouseLogic.State> {
    private static final float GROW_MULTIPLIER = 1.25F;
    private static final Set<CapabilityPosition> FLUID_INPUTS = Set.of(new CapabilityPosition(0, 0, 2, RelativeBlockFace.RIGHT), new CapabilityPosition(4, 0, 2, RelativeBlockFace.LEFT));

    private static final CapabilityPosition ENERGY_INPUT = new CapabilityPosition(2, 4, 2, RelativeBlockFace.UP);

    private static final CapabilityPosition ITEM_INPUT = new CapabilityPosition(2, 0, 0, RelativeBlockFace.FRONT);

    @Override
    public void tickServer(IMultiblockContext<State> context) {
        State state = context.getState();
        state.processUnits.values().forEach(ProcessUnit::tick);
        ServerLevel serverLevel = (ServerLevel) context.getLevel().getRawLevel();
        if (serverLevel.getGameTime() % 40 == 0) {
            PacketDistributor.sendToPlayersTrackingChunk(
                    (ServerLevel) context.getLevel().getRawLevel(), new ChunkPos(state.masterBlockPose), new GreenHouseGrowsPack(state.masterBlockPose, state.processUnits.values().stream().map(processUnit -> processUnit.growth).toList())
            );
        }

        int newEnergy = state.energyStorage.getEnergyStored();
        if (state.oldEnergy != newEnergy) {
            Level level = state.levelSupplier.get();
            BlockPos bulb = state.masterBlockPose.above(3);

            if (state.oldEnergy <= 0 && newEnergy > 0) {
                level.setBlock(bulb, level.getBlockState(bulb).setValue(BlockStateProperties.LIT, true), 2);
                state.syncRunnable.run();
            } else if (newEnergy <= 0 && state.oldEnergy > 0) {
                level.setBlock(bulb, level.getBlockState(bulb).setValue(BlockStateProperties.LIT, false), 2);
                state.syncRunnable.run();
            }

            state.oldEnergy = state.energyStorage.getEnergyStored();
        }
    }


    @Override
    public State createInitialState(IInitialMultiblockContext<State> iInitialMultiblockContext) {

        return new State(iInitialMultiblockContext);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType var1) {
        return new GreenHouseShape();
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<State> register) {
        register.register(Capabilities.FluidHandler.BLOCK, (state, position) -> {
            return position.side() == null || FLUID_INPUTS.contains(position) ? state.tankHandler : null;
        });

        register.register(Capabilities.EnergyStorage.BLOCK, (state, position) -> {
            return position.side() == null || ENERGY_INPUT.equals(position) ? state.energyStorage : null;
        });

        register.register(Capabilities.ItemHandler.BLOCK, (state, position) -> {
            return position.side() == null || ITEM_INPUT.equals(position) ? state.fertilizer : null;
        });
    }

    @Override
    public void tickClient(IMultiblockContext<State> context) {
        State state = context.getState();

        state.processUnits.values().forEach(ProcessUnit::tickClient);
    }

    public static class State implements IMultiblockState {
        public final Supplier<IItemHandler> output;

        public final BlockPos masterBlockPose;
        private final Supplier<Level> levelSupplier;
        public int fertilizerAmount;
        public float fertilizerMod = 1F;
        public final IntObjectMap<ProcessUnit> processUnits = new IntObjectHashMap<>(16);
        public int oldEnergy = 0;
        public final MutableEnergyStorage energyStorage = new MutableEnergyStorage(64000);
        public final FluidTank tank = new FluidTank(4000);
        public final ItemStackHandler seeds = new ItemStackHandler(16) {
            @Override
            protected void onContentsChanged(int slot) {
                processUnits.get(slot).seed = getStackInSlot(slot);
                processUnits.get(slot).checkAndStartRecipe();
                syncRunnable.run();
            }

            @Override
            protected void onLoad() {
                for (int slot = 0; slot < getSlots(); slot++) {
                    processUnits.get(slot).seed = getStackInSlot(slot);
                }
            }
        };
        public final ItemStackHandler soils = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                ItemStack soil = getStackInSlot(slot);
                setSoilInUnits(slot, soil);
                processUnits.values().forEach(ProcessUnit::checkAndStartRecipe);
                syncRunnable.run();
            }

            @Override
            protected void onLoad() {
                for (int slot = 0; slot < getSlots(); slot++) {
                    ItemStack soil = getStackInSlot(slot);
                    setSoilInUnits(slot, soil);
                }
            }
        };
        public final ItemStackHandler fertilizer = new ItemStackHandler() {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return ClocheFertilizer.isValidFertilizer(levelSupplier.get(), stack);
            }
        };
        public final ItemStackHandler products = new ItemStackHandler(16);

        public final IFluidHandler tankHandler;

        private final Runnable syncRunnable;

        public static final MultiblockFace ITEM_OUT = new MultiblockFace(2, 0, 5, RelativeBlockFace.BACK);

        public State(IInitialMultiblockContext<State> context) {
            output = context.getCapabilityAt(Capabilities.ItemHandler.BLOCK, ITEM_OUT);

            syncRunnable = context.getSyncRunnable();
            tankHandler = ArrayFluidHandler.fillOnly(tank, () -> {
                context.getMarkDirtyRunnable();
                processUnits.values().forEach(ProcessUnit::checkAndStartRecipe);
            });
            masterBlockPose = ((InitialMultiblockContext<State>) context).masterBE().getBlockPos();
            levelSupplier = context.levelSupplier();
            for (int i = 0; i < 16; i++) {
                processUnits.put(i, new ProcessUnit(i, this, context));
            }
        }


        private void setSoilInUnits(int slot, ItemStack soil) {
            for (ProcessUnit value : processUnits.values()) {
                int id = value.id;
                int j = slot * 4;
                if (j <= id && id < (j + 1) * 4) {
                    value.soil = soil;
                }
            }
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            CompoundTag tankTag = this.tank.writeToNBT(provider, new CompoundTag());
            nbt.put("tank", tankTag);
            EnergyHelper.serializeTo(this.energyStorage, nbt, provider);

            nbt.put("seeds", seeds.serializeNBT(provider));
            nbt.put("soils", soils.serializeNBT(provider));
            nbt.put("fertilizer", fertilizer.serializeNBT(provider));
            nbt.put("products", products.serializeNBT(provider));
            nbt.putInt("fertilizerAmount", fertilizerAmount);
            nbt.putFloat("fertilizerMod", fertilizerMod);

            ListTag listTag = new ListTag();
            processUnits.forEach((id, processUnit) -> {
                listTag.add(id, processUnit.save());
            });
            nbt.put("processInfo", listTag);
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            this.tank.readFromNBT(provider, nbt.getCompound("tank"));
            EnergyHelper.deserializeFrom(this.energyStorage, nbt, provider);

            seeds.deserializeNBT(provider, nbt.getCompound("seeds"));
            soils.deserializeNBT(provider, nbt.getCompound("soils"));
            fertilizer.deserializeNBT(provider, nbt.getCompound("fertilizer"));
            products.deserializeNBT(provider, nbt.getCompound("products"));
            fertilizerAmount = nbt.getInt("fertilizerAmount");
            fertilizerMod = nbt.getFloat("fertilizerMod");

            ListTag listTag = nbt.getList("processInfo", CompoundTag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                processUnits.get(i).load((CompoundTag) listTag.get(i));
            }
        }

        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            CompoundTag tankTag = this.tank.writeToNBT(provider, new CompoundTag());
            nbt.put("tank", tankTag);
            EnergyHelper.serializeTo(this.energyStorage, nbt, provider);
            nbt.put("soils", soils.serializeNBT(provider));
            nbt.put("seeds", seeds.serializeNBT(provider));
            nbt.putFloat("fertilizerMod", fertilizerMod);
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            this.tank.readFromNBT(provider, nbt.getCompound("tank"));
            EnergyHelper.deserializeFrom(this.energyStorage, nbt, provider);
            soils.deserializeNBT(provider, nbt.getCompound("soils"));
            seeds.deserializeNBT(provider, nbt.getCompound("seeds"));
            fertilizerMod = nbt.getFloat("fertilizerMod");
        }
    }

    public static class ProcessUnit {
        public final int id;
        private final State state;
        public ItemStack seed = ItemStack.EMPTY;
        public ItemStack soil = ItemStack.EMPTY;

        public float growth = 0;
        public int tickRequire = 0;
        public ClocheRecipe currentRecipe = null;

        public final Supplier<ClocheRecipe> cachedRecipe;

        public ProcessUnit(int id, State state, IInitialMultiblockContext<State> context) {
            this.id = id;
            this.state = state;

            cachedRecipe = CachedRecipe.cached(
                    ClocheRecipe::findRecipe, state.levelSupplier, () -> seed, () -> soil, state.tank::getFluid
            );
        }

        public void tick() {
            if (tickRequire != 0 && state.energyStorage.getEnergyStored() > 0) {
                if (currentRecipe == null) {
                    currentRecipe = cachedRecipe.get();
                }

                if (currentRecipe == null) {
                    growth = 0;
                    tickRequire = 0;
                    return;
                }


                if (state.fertilizerAmount <= 0) {
                    int outCount = (int) (IEServerConfig.MACHINES.cloche_fluid.get() * 1.5);
                    if (state.tank.getFluidAmount() >= outCount) {
                        state.tank.drain(outCount, IFluidHandler.FluidAction.EXECUTE);
                    } else {
                        return;
                    }
                    state.fertilizerMod = 1F;
                    ItemStack fertilizer = state.fertilizer.getStackInSlot(0);
                    if (!fertilizer.isEmpty()) {
                        float itemMod = ClocheFertilizer.getFertilizerGrowthModifier(state.levelSupplier.get(), fertilizer);
                        if (itemMod > 0) {
                            state.fertilizerMod *= itemMod;
                            fertilizer.shrink(1);
                            if (fertilizer.getCount() <= 0) {
                                state.fertilizer.setStackInSlot(0, ItemStack.EMPTY);
                            }
                        }
                    }

                    state.fertilizerAmount = IEServerConfig.MACHINES.cloche_fertilizer.get() * 3;
                    state.syncRunnable.run();
                }


                if (growth >= tickRequire) {
                    NonNullList<ItemStack> outputs = currentRecipe.getOutputs(seed, soil);
                    IItemHandler itemHandler = state.output.get();

                    for (int i = 0; i < state.products.getSlots(); i++) {
                        ItemStack stack = state.products.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            state.products.setStackInSlot(i, ItemHandlerHelper.insertItem(itemHandler, stack, false));
                        }
                    }

                    for (ItemStack outputStack : outputs) {
                        ItemStack output = outputStack.copy();
                        if (itemHandler != null) {
                            output = ItemHandlerHelper.insertItem(itemHandler, output, false);
                        }
                        ItemHandlerHelper.insertItem(state.products, output, false);
                    }

                    reset();
                }

                growth = (float) (growth + IEServerConfig.MACHINES.cloche_growth_mod.get() * state.fertilizerMod * GROW_MULTIPLIER);
                state.fertilizerAmount--;

                state.energyStorage.extractEnergy(10, false);
            }
        }

        public void checkAndStartRecipe() {
            ClocheRecipe old = currentRecipe;
            currentRecipe = cachedRecipe.get();
            if (old != currentRecipe) {
                reset();
            }
            if (currentRecipe != null) {
                tickRequire = currentRecipe.time;
            }
        }

        private void reset() {
            growth = 0;
        }

        public CompoundTag save() {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putFloat("growth", growth);
            compoundTag.putInt("tickRequire", tickRequire);
            return compoundTag;
        }

        public void load(CompoundTag compoundTag) {
            growth = compoundTag.getFloat("growth");
            tickRequire = compoundTag.getInt("tickRequire");
        }

        public void tickClient() {
            double addGrow = IEServerConfig.MACHINES.cloche_growth_mod.get() * state.fertilizerMod * GROW_MULTIPLIER;
            if (growth != 0 && state.energyStorage.getEnergyStored() > 0) {
                growth += (float) addGrow;
            }
        }
    }
}
