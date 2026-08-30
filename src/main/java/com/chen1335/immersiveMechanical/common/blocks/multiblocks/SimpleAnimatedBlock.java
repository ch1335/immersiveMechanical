package com.chen1335.immersiveMechanical.common.blocks.multiblocks;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SimpleAnimatedBlock<State extends IMultiblockState> extends MultiblockPartBlock<State> {

    public SimpleAnimatedBlock(Properties properties, MultiblockRegistration<State> multiblock) {
        super(properties, multiblock);
    }
    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
