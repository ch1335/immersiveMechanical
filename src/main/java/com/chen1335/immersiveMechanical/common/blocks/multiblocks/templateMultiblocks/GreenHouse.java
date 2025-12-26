package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.core.BlockPos;

public class GreenHouse extends IETemplateMultiblock {
    public GreenHouse() {
        super(ImmersiveMechanical.id("multiblocks/green_house"), new BlockPos(2, 0, 2), new BlockPos(2, 0, 4), new BlockPos(5, 5, 5), IMMultiblockLogic.GREEN_HOUSE);
    }

    @Override
    public float getManualScale() {
        return 7;
    }
}
