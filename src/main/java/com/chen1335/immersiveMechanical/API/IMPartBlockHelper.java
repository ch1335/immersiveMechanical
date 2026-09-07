package com.chen1335.immersiveMechanical.API;

import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@SuppressWarnings("unchecked")
public interface IMPartBlockHelper<S extends IMultiblockState> {
    private MultiblockPartBlock<S> self() {
        return (MultiblockPartBlock<S>) this;
    }

    default S getState(Level level, BlockPos blockPos) {
        if (level.getBlockEntity(blockPos) instanceof IMultiblockBE<?> be) {
            return (S) be.getHelper().getState();
        }
        return null;
    }
}
