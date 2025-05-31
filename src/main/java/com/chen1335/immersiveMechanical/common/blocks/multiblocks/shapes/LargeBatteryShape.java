package com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class LargeBatteryShape implements Function<BlockPos, VoxelShape> {

    @Override
    public VoxelShape apply(BlockPos blockPos) {
        if (blockPos.getY() > 0 && blockPos.getY() < 4) {
            if (blockPos.getX() == 0 && blockPos.getZ() == 0) {
                return Shapes.box(0.25, 0, 0.25, 1, 1, 1);
            } else if (blockPos.getX() == 2 && blockPos.getZ() == 0) {
                return Shapes.box(0, 0, 0.25, 0.75, 1, 1);
            } else if (blockPos.getX() == 0 && blockPos.getZ() == 2) {
                return Shapes.box(0.25, 0, 0, 1, 1, 0.75);
            } else if (blockPos.getX() == 2 && blockPos.getZ() == 2) {
                return Shapes.box(0, 0, 0, 0.75, 1, 0.75);
            } else if ((blockPos.getX() == 1 && blockPos.getZ() == 0)) {
                return Shapes.box(0, 0, 0.25, 1, 1, 1);
            }else if ((blockPos.getX() == 1 && blockPos.getZ() == 2)) {
                return Shapes.box(0, 0, 0, 1, 1, 0.75);
            }else if ((blockPos.getX() == 0 && blockPos.getZ() == 1)) {
                return Shapes.box(0.25, 0, 0, 1, 1, 1);
            }else if ((blockPos.getX() == 2 && blockPos.getZ() == 1)) {
                return Shapes.box(0, 0, 0, 0.75, 1, 1);
            }
        }
        return Shapes.block();
    }
}
