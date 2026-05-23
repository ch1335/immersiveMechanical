package com.chen1335.immersiveMechanical.API.registrate;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record MultiblockDefinition<S extends IMultiblockState, L extends IMultiblockLogic<S>>(L logic,
                                                                                              IETemplateMultiblock multiblock,
                                                                                              MultiblockRegistration<S> registration) {

    public BlockEntityType<? extends MultiblockBlockEntityMaster<S>> getMasterBe() {
        return registration.masterBE().get();
    }

    public Block getBlock() {
        return registration.block().get();
    }

    public Item getBlockItem() {
        return registration.blockItem().get();
    }
}
