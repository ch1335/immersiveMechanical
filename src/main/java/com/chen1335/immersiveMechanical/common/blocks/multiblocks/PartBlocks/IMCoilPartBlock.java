package com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;

public class IMCoilPartBlock extends MultiblockPartBlock<CoilLogic.State> {

    public IMCoilPartBlock(Properties properties, MultiblockRegistration<CoilLogic.State> multiblock) {
        super(properties, multiblock);
    }
}
