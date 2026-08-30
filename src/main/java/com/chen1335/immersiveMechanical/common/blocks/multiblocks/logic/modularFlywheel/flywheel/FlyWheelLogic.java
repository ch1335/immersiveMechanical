package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.IFlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.EndPointLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;
import java.util.function.Supplier;

public class FlyWheelLogic implements IMultiblockLogic<FlyWheelLogic.State>, IClientTickableComponent<FlyWheelLogic.State> {
    @Override
    public void tickClient(IMultiblockContext<FlyWheelLogic.State> context) {
        FlyWheelLogic.State state = context.getState();
        if (context.getLevel().shouldTickModulo(10) && state.masterState == null) {
            state.masterState = state.getMasterState();
        }
    }

    @Override
    public FlyWheelLogic.State createInitialState(IInitialMultiblockContext<FlyWheelLogic.State> context) {
        return new State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return blockPos -> Shapes.block();
    }

    public static class State implements IMultiblockState, IFlyWheelPart {
        private final Supplier<Level> levelSupplier;
        private BlockPos masterPos = null;

        private EndPointLogic.State masterState = null;

        public State(IInitialMultiblockContext<State> context) {
            levelSupplier = context.levelSupplier();
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            writeMasterNBT(nbt, provider);
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            readMasterNBT(nbt, provider);
        }

        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            writeMasterNBT(nbt, provider);
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            readMasterNBT(nbt, provider);
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
            return masterState != null ? masterState.getAngle() : 0;
        }

        public float getAngleOld() {
            return masterState != null ? masterState.getAngleOld() : 0;
        }

        public EndPointLogic.State getMasterState() {
            if (masterState != null) {
                return masterState;
            } else if (masterPos != null) {
                Level level = levelSupplier.get();
                if (level != null) {
                    BlockEntity blockEntity = level.getBlockEntity(masterPos);
                    if (blockEntity instanceof IMultiblockBE<?> be) {
                        IMultiblockState iMultiblockState = be.getHelper().getContext().getState();
                        if (iMultiblockState instanceof EndPointLogic.State state) {
                            masterState = state;
                            return masterState;
                        }
                    }
                }
            }
            return null;
        }
    }
}
