package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPartLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class FlyWheelLogic extends FlyWheelPartLogic<FlyWheelLogic.State> implements IClientTickableComponent<FlyWheelLogic.State> {
    @Override
    public void tickClient(IMultiblockContext<FlyWheelLogic.State> context) {
        FlyWheelLogic.State state = context.getState();
        if (context.getLevel().shouldTickModulo(10) && state.masterState == null) {
            state.updateMasterState();
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

    public static class State extends FlyWheelPart {

        public State(IInitialMultiblockContext<State> context) {
            super(context);
        }

        public float getAngle() {
            return masterState != null ? masterState.getAngle() : 0;
        }

        public float getAngleOld() {
            return masterState != null ? masterState.getAngleOld() : 0;
        }

    }
}
