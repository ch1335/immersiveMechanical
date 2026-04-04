package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.interfaces.MBMemorizeStructure;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;
import java.util.function.Function;

public class CoilLogic implements MBMemorizeStructure<CoilLogic.State> {

    @Override
    public CoilLogic.State createInitialState(IInitialMultiblockContext<CoilLogic.State> context) {
        return new State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return blockPos -> Shapes.block();
    }

    @Override
    public void setMemorizedBlockState(State state, BlockPos pos, BlockState blockState) {
        state.structureMemo.put(pos, blockState);
        state.syncRunnable.run();
    }

    @Override
    public BlockState getMemorizedBlockState(State state, BlockPos pos) {
        BlockState blockState = state.structureMemo.get(pos);
        if (blockState == null) {
            System.out.println("CoilLogic.State.setMemorizedBlockState: blockState is null");
        }
        return blockState;
    }

    public static class State implements IMultiblockState {

        private final StructureMemo structureMemo = new StructureMemo();
        private final Runnable syncRunnable;


        public State(IInitialMultiblockContext<State> context) {
            syncRunnable = context.getSyncRunnable();
        }

        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            structureMemo.writeSaveNBT(nbt, provider);
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            structureMemo.readSaveNBT(nbt, provider);
        }


        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            structureMemo.writeSaveNBT(nbt, provider);
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            structureMemo.readSaveNBT(nbt, provider);
        }

        public Block getCoilBlock() {
            return Objects.requireNonNullElse(structureMemo.get(new BlockPos(1, 0, 2)), IEBlocks.MetalDecoration.LV_COIL.get().defaultBlockState()).getBlock();
        }
    }
}
