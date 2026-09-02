package com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class EndPointShape implements Function<BlockPos, VoxelShape> {
    @Override
    public VoxelShape apply(BlockPos blockPos) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        if (z == 2) {
            if (y == 0) {
                return Shapes.box(0, 0, .5, 1, 1, 1);
            } else if (y == 1) {
                return Shapes.or(
                        Shapes.box(0, 0, .5, 1, 1, 1),
                        Shapes.box(0.3, 0.3, 0, 0.7, 0.7, 1)
                );
            }
        } else if (z == 1) {
            if (y == 0) {
                if (x == 0) {
                    return Shapes.box(0, 0, 0, 0.5, 1, 1);
                } else if (x == 2) {
                    return Shapes.box(0.5, 0, 0, 1, 1, 1);
                }
            } else if (y == 1) {
                if (x == 0) {
                    return Shapes.or(
                            Shapes.box(0, 0.689, 0, 1, 1, 1),
                            Shapes.box(0, 0, 0, 0.5, 1, 1)
                    );
                } else if (x == 1) {
                    return Shapes.or(
                            Shapes.box(0, 0.689, 0, 1, 1, 1),
                            Shapes.box(0.3, 0.3, 0, 0.7, 0.7, 1)
                    );
                } else if (x == 2) {
                    return Shapes.or(
                            Shapes.box(0, 0.689, 0, 1, 1, 1),
                            Shapes.box(0.5, 0, 0, 1, 1, 1)
                    );
                }
            }
        }
        return Shapes.box(0, 0, 0, 1, 1, 1);
    }
}
