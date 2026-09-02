package com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class FlyWheelShape implements Function<BlockPos, VoxelShape> {
    @Override
    public VoxelShape apply(BlockPos blockPos) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        if (x == 1) {
            if (y == 2) {
                return Shapes.box(0, 0, 0.1, 1, 0.9, 0.9);
            } else if (y == 0) {
                return Shapes.box(0, 0.1, 0.1, 1, 1, 0.9);
            }
        } else if (y == 1) {
            if (x == 2) {
                return Shapes.box(0, 0, 0.1, 0.9, 1, 0.9);
            } else if (x == 0) {
                return Shapes.box(0.1, 0, 0.1, 1, 1, 0.9);
            }
        } else if (x == 0) {
            if (y == 0) {
                return Shapes.or(
                        Shapes.box(0.1, 0.5, 0.1, 1, 1, 0.9),
                        Shapes.box(0.5, 0.1, 0.1, 1, 1, 0.9)
                );
            } else if (y == 2) {
                return Shapes.or(
                        Shapes.box(0.1, 0, 0.1, 1, 0.5, 0.9),
                        Shapes.box(0.5, 0, 0.1, 1, 0.9, 0.9)
                );
            }
        } else if (x == 2) {
            if (y == 2) {
                return Shapes.or(
                        Shapes.box(0, 0, 0, 0.9, 0.5, 0.9),
                        Shapes.box(0, 0, 0, 0.5, 0.9, 0.9)
                );
            } else if (y == 0) {
                return Shapes.or(
                        Shapes.box(0, 0.5, 0.1, 0.9, 1, 0.9),
                        Shapes.box(0, 0.1, 0.1, 0.5, 1, 0.9)
                );
            }
        }
        return Shapes.or(
                Shapes.box(0, 0, 0.1, 1, 1, 0.9),
                Shapes.box(0.3, 0.3, 0, 0.7, 0.7, 1)
        );
    }
}
