package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.bearing;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPartLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes.BearingShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class BearingLogic extends FlyWheelPartLogic<BearingLogic.State> implements IClientTickableComponent<BearingLogic.State> {

    @Override
    public State createInitialState(IInitialMultiblockContext<State> context) {
        return new State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return new BearingShape();
    }

    @Override
    public void tickClient(IMultiblockContext<State> context) {
        State state = context.getState();
        if (context.getLevel().shouldTickModulo(10) && state.masterState == null) {
            state.updateMasterState();
        }
    }

    public static class State extends FlyWheelPart {
        public float getAngle() {
            return masterState != null ? masterState.getAngle() : 0;
        }

        public float getAngleOld() {
            return masterState != null ? masterState.getAngleOld() : 0;
        }

        public State(IInitialMultiblockContext<? extends FlyWheelPart> context) {
            super(context);
        }
    }
}
