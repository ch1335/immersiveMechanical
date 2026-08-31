package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.CapabilityPosition;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.RelativeBlockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPartLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.Set;
import java.util.function.Function;

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
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return blockPos -> Shapes.block();
    }

    @Override
    public void tickClient(IMultiblockContext<EndPointLogic.State> context) {
        State state = context.getState();
        if (context.getLevel().shouldTickModulo(10) && state.masterState == null) {
            state.updateMasterState();
        }

        state.angleOld = state.angle;
        state.angle += state.angularVelocity;
//        if (state.angle >= 360F) {
//            state.angle -= 360F;
//        }
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<State> register) {
        register.register(Capabilities.EnergyStorage.BLOCK, (state, position) -> {
            if (position.side() == null || ENERGY_INTERFACE.contains(position)) {
                if (state.masterState == null) {
                    state.updateMasterState();
                }
                if (state.masterState != null) {
                    return state.masterState.innerEnergy;
                }
            }
            return null;
        });
    }

    @Override
    public void tickServer(IMultiblockContext<EndPointLogic.State> context) {
        if (context.getState().isMaster) {
            if (context.getLevel().shouldTickModulo(20)) {
                context.markDirtyAndSync();
            }
            State state = context.getState();
            state.angularVelocity = (float) state.innerEnergy.getEnergyStored() / state.innerEnergy.getMaxEnergyStored() * 120;
        }
    }


    public static class State extends FlyWheelPart {
        public boolean isMaster = false;
        private float angle;
        private float angleOld;
        private float angularVelocity;
        public MutableEnergyStorage innerEnergy = new MutableEnergyStorage(600000000);

        public State(IInitialMultiblockContext<? extends FlyWheelPart> context) {
            super(context);
        }


        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.writeSaveNBT(nbt, provider);
            nbt.putFloat("angle", angle);
            nbt.putFloat("angularVelocity", angularVelocity);
            nbt.putBoolean("isMaster", isMaster);
            nbt.putInt("EnergyStored", innerEnergy.getEnergyStored());
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.readSaveNBT(nbt, provider);
            angle = nbt.getFloat("angle");
            angularVelocity = nbt.getFloat("angularVelocity");
            isMaster = nbt.getBoolean("isMaster");
            innerEnergy.setStoredEnergy(nbt.getInt("EnergyStored"));
        }


        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.writeSyncNBT(nbt, provider);
            nbt.putFloat("angularVelocity", angularVelocity);
            nbt.putBoolean("isMaster", isMaster);
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.readSyncNBT(nbt, provider);
            angularVelocity = nbt.getFloat("angularVelocity");
            isMaster = nbt.getBoolean("isMaster");
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

        public float calculationAngularVelocity(int e, float i) {
            return (float) Math.toDegrees(Math.sqrt(e * 2 / i));
        }
    }
}
