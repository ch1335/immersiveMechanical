package com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class BearingShape implements Function<BlockPos, VoxelShape> {
    @Override
    public VoxelShape apply(BlockPos blockPos) {
        return Shapes.box(0.3, 0.3, 0, 0.7, 0.7, 1);
    }
}
