package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.core.BlockPos;

public class SmallMiningMachine extends IETemplateMultiblock {
    public SmallMiningMachine() {
        super(ImmersiveMechanical.id("multiblocks/small_mining_machine"), new BlockPos(1, 1, 1), new BlockPos(1, 1, 2), new BlockPos(3, 4, 3), IMMultiblockLogic.SMALL_MINING_MACHINE);
    }

    @Override
    public float getManualScale() {
        return 9;
    }
}
