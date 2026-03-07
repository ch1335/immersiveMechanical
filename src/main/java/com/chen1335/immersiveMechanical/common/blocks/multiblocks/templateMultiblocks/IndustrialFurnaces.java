package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.core.BlockPos;

public class IndustrialFurnaces extends IETemplateMultiblock {
    public IndustrialFurnaces() {
        super(ImmersiveMechanical.id("multiblocks/industrial_furnaces"), new BlockPos(1, 0, 1), new BlockPos(1, 0, 2), new BlockPos(3, 3, 3), IMMultiblockLogic.INDUSTRIAL_FURNACES);
    }

    @Override
    public float getManualScale() {
        return 9;
    }
}
