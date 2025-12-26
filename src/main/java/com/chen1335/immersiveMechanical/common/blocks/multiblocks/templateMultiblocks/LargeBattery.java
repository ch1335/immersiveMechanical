package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.core.BlockPos;

public class LargeBattery extends IETemplateMultiblock {
    public LargeBattery() {
        super(ImmersiveMechanical.id("multiblocks/large_battery"), new BlockPos(1, 0, 1), new BlockPos(1, 0, 2), new BlockPos(3, 5, 3), IMMultiblockLogic.LARGE_BATTERY);
    }

    @Override
    public float getManualScale() {
        return 9;
    }
}
