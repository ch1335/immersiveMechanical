package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.pyrolyseOven;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class PyrolyseOvenLogic implements IMultiblockLogic<PyrolyseOvenLogic.State>, IServerTickableComponent<PyrolyseOvenLogic.State>, IClientTickableComponent<PyrolyseOvenLogic.State> {

    @Override
    public void tickClient(IMultiblockContext<State> context) {

    }

    @Override
    public void tickServer(IMultiblockContext<State> context) {

    }

    @Override
    public State createInitialState(IInitialMultiblockContext<State> context) {
        return new State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return blockPos -> Shapes.block();
    }

    public static class State implements IMultiblockState {

        public State(IInitialMultiblockContext<State> context) {


        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {

        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {

        }
    }
}
