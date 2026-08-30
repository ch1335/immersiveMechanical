package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.IFlyWheelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class EndPointLogic implements IMultiblockLogic<EndPointLogic.State>, IServerTickableComponent<EndPointLogic.State>, IClientTickableComponent<EndPointLogic.State> {

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
            state.masterState = state.getMasterState();
        }
        state.angleOld = state.angle;
        state.angle += 50;
//        if (state.angle >= 360F) {
//            state.angle -= 360F;
//        }
    }

    @Override
    public void tickServer(IMultiblockContext<EndPointLogic.State> context) {

    }

    public static class State implements IMultiblockState, IFlyWheelPart {
        public boolean isMaster = false;

        private BlockPos masterPos = null;
        private State masterState = null;
        private float angle;
        private float angleOld;
        private float angularVelocity;
        private final Supplier<@Nullable Level> levelSupplier;

        public State(IInitialMultiblockContext<State> context) {
            levelSupplier = context.levelSupplier();
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            writeMasterNBT(nbt, provider);
            nbt.putFloat("angle", angle);
            nbt.putFloat("angularVelocity", angularVelocity);
            nbt.putBoolean("isMaster", isMaster);
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            readMasterNBT(nbt, provider);
            angle = nbt.getFloat("angle");
            angularVelocity = nbt.getFloat("angularVelocity");
            isMaster = nbt.getBoolean("isMaster");
        }


        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            writeMasterNBT(nbt, provider);
            nbt.putFloat("angle", angle);
            nbt.putFloat("angularVelocity", angularVelocity);
            nbt.putBoolean("isMaster", isMaster);
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            readMasterNBT(nbt, provider);
            angle = nbt.getFloat("angle");
            angularVelocity = nbt.getFloat("angularVelocity");
            isMaster = nbt.getBoolean("isMaster");
        }

        @Override
        public void setMasterPos(BlockPos masterPose) {
            this.masterPos = masterPose;
        }

        @Override
        public BlockPos getMasterPos() {
            return masterPos;
        }

        public float getAngle() {
            if (!isMaster && masterState != null) {
                return masterState.angle;
            }
            return angle;
        }

        public float getAngleOld() {
            if (!isMaster && masterState != null) {
                return masterState.angleOld;
            }
            return angleOld;
        }

        public State getMasterState() {
            if (isMaster) {
                return this;
            } else {
                if (masterState != null) {
                    return masterState;
                } else if (masterPos != null) {
                    Level level = levelSupplier.get();
                    if (level != null) {
                        BlockEntity blockEntity = level.getBlockEntity(masterPos);
                        if (blockEntity instanceof IMultiblockBE<?> be) {
                            IMultiblockState iMultiblockState = be.getHelper().getContext().getState();
                            if (iMultiblockState instanceof State state) {
                                masterState = state;
                                return masterState;
                            }
                        }
                    }
                }
            }
            return this;
        }
    }
}
