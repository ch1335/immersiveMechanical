package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class CoilTemplate extends IETemplateMultiblock {
    public CoilTemplate(ResourceLocation loc, MultiblockRegistration<?> logic) {
        super(loc, new BlockPos(1, 0, 2), new BlockPos(1, 0, 2), new BlockPos(3, 1, 3), logic);
    }

    @Override
    public float getManualScale() {
        return 9;
    }
}
