package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class CoilLogic implements IMultiblockLogic<CoilLogic.State> {
    @Override
    public CoilLogic.State createInitialState(IInitialMultiblockContext<CoilLogic.State> capabilitySource) {
        return new State();
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return blockPos -> Shapes.block();
    }

    public static class State implements IMultiblockState {

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {

        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {

        }
    }
}
