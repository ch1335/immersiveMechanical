package com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class PyrolyseOvenShape implements Function<BlockPos, VoxelShape> {
    @Override
    public VoxelShape apply(BlockPos blockPos) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        
        if (x == 2 && y == 1 && z == 0) {
            return Shapes.empty();
        }
        if (y == 0) {
            if (x == 4 && z == 0) {
                return Shapes.block();
            }
            if ((z == 1 || z == 4) && x <= 2) {
                return Shapes.block();
            }
            return Shapes.box(0, 0, 0, 1, 0.5, 1);
        } else {
            if (x == 3 && z == 0) {
                return Shapes.box(0.42, 0, 0.44, 0.67, 1, 1);
            }
        }

        if (x==3 && z==1) {
            return Shapes.box(0.2, 0, 0.2, 1, 1, 1);
        } else if (x==4 && z==1) {
            return Shapes.box(0, 0, 0.2, 0.8, 1, 1);
        } else if (x==3 && z==2) {
            return Shapes.box(0.2, 0, 0, 1, 1, 0.8);
        } else if (x==4 && z==2) {
            return Shapes.box(0, 0, 0, 0.8, 1, 0.8);
        }

        return Shapes.block();
    }
}
