package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;
import java.util.function.Supplier;

public class CoilLogic implements IMultiblockLogic<CoilLogic.State> {
    private final Supplier<? extends Block> supplier;

    public CoilLogic(Supplier<? extends Block> supplier) {
        this.supplier = supplier;
    }

    @Override
    public CoilLogic.State createInitialState(IInitialMultiblockContext<CoilLogic.State> context) {
        return new State(context, supplier.get());
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return blockPos -> Shapes.block();
    }

    public static class State implements IMultiblockState {

        private final Block coilBlock;

        public State(IInitialMultiblockContext<State> context, Block block) {
            this.coilBlock = block;
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {

        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {

        }

        public Block getCoilBlock() {
            return coilBlock;
        }
    }
}
