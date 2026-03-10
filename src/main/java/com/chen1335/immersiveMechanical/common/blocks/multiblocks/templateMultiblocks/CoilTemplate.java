package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class CoilTemplate extends IETemplateMultiblock {
    private final MultiblockRegistration<CoilLogic.State> logic;

    public CoilTemplate(ResourceLocation loc, MultiblockRegistration<CoilLogic.State> logic) {
        super(loc, new BlockPos(1, 0, 2), new BlockPos(1, 0, 2), new BlockPos(3, 1, 3), logic);
        this.logic = logic;
    }

    @Override
    public float getManualScale() {
        return 9;
    }

    public MultiblockRegistration<CoilLogic.State> getLogic() {
        return logic;
    }
}
