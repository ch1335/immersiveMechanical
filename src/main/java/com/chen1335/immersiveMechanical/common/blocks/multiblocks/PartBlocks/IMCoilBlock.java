package com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class IMCoilBlock extends MultiblockPartBlock<CoilLogic.State> {
    private final Supplier<? extends Block> block;

    public IMCoilBlock(Properties properties, MultiblockRegistration<CoilLogic.State> multiblock, Supplier<? extends Block> block) {
        super(properties, multiblock);
        this.block = block;
    }

    public Block getMaterialBlock() {
        return block.get();
    }
}
