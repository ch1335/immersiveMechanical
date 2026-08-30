package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.bearing;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.pyrolyseOven.PyrolyseOvenLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class BearingLogic implements IMultiblockLogic<PyrolyseOvenLogic.State>, IClientTickableComponent<PyrolyseOvenLogic.State> {
    @Override
    public void tickClient(IMultiblockContext<PyrolyseOvenLogic.State> context) {

    }

    @Override
    public PyrolyseOvenLogic.State createInitialState(IInitialMultiblockContext<PyrolyseOvenLogic.State> capabilitySource) {
        return null;
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return null;
    }
}
