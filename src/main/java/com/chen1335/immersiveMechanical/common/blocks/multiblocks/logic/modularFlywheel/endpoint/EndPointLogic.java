package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.CapabilityPosition;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.MultiblockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.RelativeBlockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.chen1335.immersiveMechanical.API.energy.ReSizeAbleEnergyStorage;
import com.chen1335.immersiveMechanical.API.energy.WrappedEnergy;
import com.chen1335.immersiveMechanical.API.objects.IMSounds;
import com.chen1335.immersiveMechanical.API.sound.DynamicMultiblockSound;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPartLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlywheelMaterial;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.blockEntities.EndPointDummy;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel.FlyWheelLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes.EndPointShape;
import com.chen1335.immersiveMechanical.config.IMServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EndPointLogic extends FlyWheelPartLogic<EndPointLogic.State> implements IServerTickableComponent<EndPointLogic.State>, IClientTickableComponent<EndPointLogic.State> {
    private static final Set<CapabilityPosition> ENERGY_INTERFACE = Set.of(
            new CapabilityPosition(0, 0, 1, RelativeBlockFace.RIGHT),
            new CapabilityPosition(0, 1, 1, RelativeBlockFace.RIGHT),
            new CapabilityPosition(2, 0, 1, RelativeBlockFace.LEFT),
            new CapabilityPosition(2, 1, 1, RelativeBlockFace.LEFT)
    );


    @Override
    public EndPointLogic.State createInitialState(IInitialMultiblockContext<EndPointLogic.State> context) {
        return new State(context);
    }

    @Override
    public ItemInteractionResult click(IMultiblockContext<State> ctx, BlockPos posInMultiblock, Player player, InteractionHand hand, BlockHitResult absoluteHit, boolean isClient) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.is(IEItems.Tools.HAMMER.asItem())) {
            State state = ctx.getState();
            if (!isClient) {
                state.connectionType = state.connectionType == ConnectionType.INPUT ? ConnectionType.OUTPUT : ConnectionType.INPUT;
                ctx.markDirtyAndSync();
            }
            return ItemInteractionResult.SUCCESS;
        }
        return super.click(ctx, posInMultiblock, player, hand, absoluteHit, isClient);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return new EndPointShape();
    }

    @Override
    public void tickClient(IMultiblockContext<EndPointLogic.State> context) {
        State state = context.getState();
        if (!state.init) {
            state.updateMasterState();
            state.init = true;
        }
        if (context.getLevel().shouldTickModulo(10) && state.masterState == null) {
            state.updateMasterState();
        }

        state.angleOld = state.angle;
        state.angle += state.angularVelocity;
//        if (state.angle >= 360F) {
//            state.angle -= 360F;
//        }

        if (!state.isPlayingSound.getAsBoolean()) {
            state.isPlayingSound = DynamicMultiblockSound.startSound(
                    () -> true, context.isValid(), Vec3.atLowerCornerOf(context.getLevel().getAbsoluteOrigin()), IMSounds.FLY_WHEEL, () -> {
                        int energyStored = state.energyHandler.getEnergyStored();
                        int maxEnergyStored = state.energyHandler.getMaxEnergyStored();
                        if (maxEnergyStored == 0) {
                            return 0F;
                        }
                        return (float) energyStored / maxEnergyStored * 0.25F;
                    }, 0.5f
            );
        }
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<State> register) {
        register.register(Capabilities.EnergyStorage.BLOCK, (state, position) -> {
            if (position.side() == null || ENERGY_INTERFACE.contains(position)) {
                if (state.masterState == null) {
                    state.updateMasterState();
                }
                if (state.masterState != null) {
                    return state.energyHandler;
                }
            }
            return null;
        });
    }

    @Override
    public void tickServer(IMultiblockContext<EndPointLogic.State> context) {
        State state = context.getState();
        if (state.isMaster) {
            if (!state.init) {
                state.updateMasterState();
                state.init = true;
                state.innerEnergy.setMaxEnergyStored(0);
                state.linkedParts.forEach(blockPos -> {
                    BlockEntity blockEntity = context.getLevel().getRawLevel().getBlockEntity(blockPos);
                    if (blockEntity instanceof IMultiblockBE<?> be && be.getHelper().getState() instanceof FlyWheelLogic.State state1) {
                        FlywheelMaterial.get(context.getLevel().getRawLevel(), state1.material).ifPresent(holder -> {
                            state.innerEnergy.setMaxEnergyStored(state.innerEnergy.getMaxEnergyStored() + holder.value().maxEnergyStored() * IMServerConfig.MACHINES.flywheel_energy_storage_coefficient.get());
                        });
                    }
                });
            }
            if (context.getLevel().shouldTickModulo(20)) {
                if (state.isMaster) {
                    ReSizeAbleEnergyStorage masterEnergy = state.getMasterEnergy();
                    int toExtract = (int) (masterEnergy.getEnergyStored() * 0.01 / 3600);
                    masterEnergy.extractEnergy(toExtract, false);
                }
                context.markDirtyAndSync();
            }
            int maxEnergyStored = state.innerEnergy.getMaxEnergyStored();
            if (maxEnergyStored !=0) {
                state.angularVelocity = (float) state.innerEnergy.getEnergyStored() / maxEnergyStored * 100;
            }
        }

        if (state.energyHandler.getMaxEnergyStored() != 0) {
            int maxTransfer = IMServerConfig.MACHINES.flywheel_maximum_energy_transfer.get();
            maxTransfer = Mth.lerpInt((float) Math.min(((float) state.energyHandler.getEnergyStored() / state.energyHandler.getMaxEnergyStored()) / IMServerConfig.MACHINES.flywheel_maximum_transfer_requirement.get(), 1), IMServerConfig.MACHINES.flywheel_basic_energy_transfer.get(), maxTransfer);
            state.receiveRemaining = maxTransfer;
            state.extractRemaining = maxTransfer;
        }

        if (state.connectionType == ConnectionType.OUTPUT) {
            for (Supplier<IEnergyStorage> iEnergyStorageSupplier : context.getState().energyOutputs) {
                IEnergyStorage iEnergyStorage = iEnergyStorageSupplier.get();
                if (iEnergyStorage != null) {
                    int received = iEnergyStorage.receiveEnergy(Integer.MAX_VALUE, false);
                    state.energyHandler.extractEnergy(received, false);
                }
            }
        }
    }


    public static class State extends FlyWheelPart {
        public boolean isMaster = false;
        private float angle;
        private float angleOld;
        private float angularVelocity;
        private boolean init = false;
        private BooleanSupplier isPlayingSound = () -> false;

        public int receiveRemaining = 0;
        public int extractRemaining = 0;

        public static final Set<MultiblockFace> ENERGY_OUTS = Set.of(
                new MultiblockFace(-1, 0, 1, RelativeBlockFace.LEFT),
                new MultiblockFace(-1, 1, 1, RelativeBlockFace.LEFT),
                new MultiblockFace(3, 0, 1, RelativeBlockFace.RIGHT),
                new MultiblockFace(3, 1, 1, RelativeBlockFace.RIGHT)
        );
        public final Set<Supplier<IEnergyStorage>> energyOutputs;
        public Set<EndPointDummy> dummyBEs = new HashSet<>();
        public ConnectionType connectionType = ConnectionType.INPUT;

        public final ReSizeAbleEnergyStorage innerEnergy = new ReSizeAbleEnergyStorage(0,
                IMServerConfig.MACHINES.flywheel_maximum_energy_transfer.get(),
                IMServerConfig.MACHINES.flywheel_maximum_energy_transfer.get());

        public WrappedEnergy<ReSizeAbleEnergyStorage> energyHandler = new WrappedEnergy<>(this::getMasterEnergy) {

            @Override
            public boolean canExtract() {
                return connectionType == ConnectionType.OUTPUT;
            }

            @Override
            public boolean canReceive() {
                return connectionType == ConnectionType.INPUT;
            }

            @Override
            public int extractEnergy(int toExtract, boolean simulate) {
                if (!simulate) {
                    toExtract = Math.min(toExtract, extractRemaining);
                    extractRemaining -= toExtract;
                }
                return super.extractEnergy(toExtract, simulate);
            }

            @Override
            public int receiveEnergy(int toReceive, boolean simulate) {
                if (!simulate) {
                    toReceive = Math.min(toReceive, receiveRemaining);
                    receiveRemaining -= toReceive;
                }
                return super.receiveEnergy(toReceive, simulate);
            }
        };


        public State(IInitialMultiblockContext<? extends FlyWheelPart> context) {
            super(context);
            this.energyOutputs = ENERGY_OUTS.stream().map(face -> context.getCapabilityAt(Capabilities.EnergyStorage.BLOCK, face)).collect(Collectors.toSet());
        }


        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.writeSaveNBT(nbt, provider);
            nbt.putFloat("angle", angle);
            nbt.putFloat("angularVelocity", angularVelocity);
            nbt.putBoolean("isMaster", isMaster);
            nbt.putInt("EnergyStored", innerEnergy.getEnergyStored());
            nbt.putString("connectionType", connectionType.name());
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.readSaveNBT(nbt, provider);
            angle = nbt.getFloat("angle");
            angularVelocity = nbt.getFloat("angularVelocity");
            isMaster = nbt.getBoolean("isMaster");
            innerEnergy.setStoredEnergy(nbt.getInt("EnergyStored"));
            connectionType = ConnectionType.valueOf(nbt.getString("connectionType"));
        }


        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.writeSyncNBT(nbt, provider);
            nbt.putFloat("angularVelocity", angularVelocity);
            nbt.putBoolean("isMaster", isMaster);
            nbt.putString("connectionType", connectionType.name());
            nbt.putInt("maxEnergy", innerEnergy.getMaxEnergyStored());
            nbt.putInt("energy", innerEnergy.getEnergyStored());
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.readSyncNBT(nbt, provider);
            angularVelocity = nbt.getFloat("angularVelocity");
            isMaster = nbt.getBoolean("isMaster");
            connectionType = ConnectionType.valueOf(nbt.getString("connectionType"));
            innerEnergy.setMaxEnergyStored(nbt.getInt("maxEnergy"));
            innerEnergy.setStoredEnergy(nbt.getInt("energy"));
            for (EndPointDummy dummyBE : dummyBEs) {
                if (dummyBE.getLevel() != null) {
                    dummyBE.getLevel().sendBlockUpdated(dummyBE.getBlockPos(), dummyBE.getBlockState(), dummyBE.getBlockState(), 2);
                }
            }
        }

        public float getAngle() {
            return masterState != null ? masterState.angle : angle;
        }

        public float getAngleOld() {
            return masterState != null ? masterState.angleOld : angleOld;
        }

        @Override
        public void updateMasterState() {
            if (isMaster) {
                masterState = this;
                return;
            }
            super.updateMasterState();
        }

        public ReSizeAbleEnergyStorage getMasterEnergy() {
            if (isMaster) {
                return innerEnergy;
            } else if (masterState != null) {
                return masterState.innerEnergy;
            }
            return innerEnergy;
        }
    }

    public enum ConnectionType {
        INPUT,
        OUTPUT;
    }
}
