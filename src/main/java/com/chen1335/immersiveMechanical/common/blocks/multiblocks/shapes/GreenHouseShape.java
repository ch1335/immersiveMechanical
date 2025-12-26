package com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class GreenHouseShape implements Function<BlockPos, VoxelShape> {
    @Override
    public VoxelShape apply(BlockPos blockPos) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        if (blockPos.getY() > 0) {
            if (0 < blockPos.getZ() && blockPos.getZ() < 4) {
                if (blockPos.getX() == 0) {
                    if (y == 4) {
                        return Shapes.or(Shapes.box(0.25, 0, 0, 0.3, 1, 1), Shapes.box(0.25, 0.95, 0, 1, 1, 1));
                    }
                    return Shapes.box(0.25, 0, 0, 0.3, 1, 1);
                } else if (blockPos.getX() == 4) {
                    if (y == 4) {
                        return Shapes.or(Shapes.box(0.7, 0, 0, 0.75, 1, 1), Shapes.box(0, 0.95, 0, 0.75, 1, 1));
                    }
                    return Shapes.box(0.7, 0, 0, 0.75, 1, 1);
                }
            } else if (0 < blockPos.getX() && blockPos.getX() < 4) {
                if (blockPos.getZ() == 0) {
                    if (y == 4) {
                        return Shapes.or(Shapes.box(0, 0, 0.25, 1, 1, 0.3), Shapes.box(0, 0.95, 0.25, 1, 1, 1));
                    }
                    return Shapes.box(0, 0, 0.25, 1, 1, 0.3);
                } else if (blockPos.getZ() == 4) {
                    if (y == 4) {
                        return Shapes.or(Shapes.box(0, 0, 0.7, 1, 1, 0.75), Shapes.box(0, 0.95, 0, 1, 1, 0.75));
                    }
                    return Shapes.box(0, 0, 0.7, 1, 1, 0.75);
                }
            } else if (x == 0 && z == 0) {
                VoxelShape shape = Shapes.or(
                        Shapes.box(0.25, 0, 0.25, 1, 1, 0.3),
                        Shapes.box(0.25, 0, 0.25, 0.3, 1, 1)
                );
                if (y == 4) {
                    shape = Shapes.or(shape, Shapes.box(0.25, 0.95, 0.25, 1, 1, 1));
                }
                return shape;
            } else if (x == 0 && z == 4) {

                VoxelShape shape = Shapes.or(
                        Shapes.box(0.25, 0, 0, 0.3, 1, 0.7),
                        Shapes.box(0.25, 0, 0.7, 1, 1, 0.75)
                );
                if (y == 4) {
                    shape = Shapes.or(shape, Shapes.box(0.25, 0.95, 0, 1, 1, 0.75));
                }
                return shape;
            } else if (x == 4 && z == 4) {
                VoxelShape shape = Shapes.or(
                        Shapes.box(0, 0, 0.7, 0.75, 1, 0.75),
                        Shapes.box(0.7, 0, 0, 0.75, 1, 0.75)
                );
                if (y == 4) {
                    shape = Shapes.or(shape, Shapes.box(0, 0.95, 0, 0.75, 1, 0.75));
                }
                return shape;
            } else if (x == 4 && z == 0) {
                VoxelShape shape = Shapes.or(
                        Shapes.box(0, 0, 0.25, 0.75, 1, 0.3),
                        Shapes.box(0.7, 0, 0.25, 0.75, 1, 1)
                );
                if (y == 4) {
                    shape = Shapes.or(shape, Shapes.box(0, 0.95, 0.25, 0.75, 1, 1));

                }
                return shape;
            }

            if (y == 4) {
                return Shapes.box(0, 0.95, 0, 1, 1, 1);
            }
            if (x == 2 && y == 3 && z == 2) {
                return Shapes.empty();
            }

        }
        return Shapes.block();
    }
}
