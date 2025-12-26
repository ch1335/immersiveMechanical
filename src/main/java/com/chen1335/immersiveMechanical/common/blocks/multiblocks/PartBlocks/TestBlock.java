package com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;

public class TestBlock<State extends IMultiblockState> extends MultiblockPartBlock<State> {

    public TestBlock(Properties properties, MultiblockRegistration<State> multiblock) {
        super(properties, multiblock);
    }

}
