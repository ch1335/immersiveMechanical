package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

//TODO 整一个燃气轮机
public class GasTurbine implements IMultiblockLogic<GasTurbine.State>, IServerTickableComponent<GasTurbine.State> {
    @Override
    public void tickServer(IMultiblockContext<GasTurbine.State> context) {

    }

    @Override
    public GasTurbine.State createInitialState(IInitialMultiblockContext<GasTurbine.State> capabilitySource) {
        return null;
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return null;
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
