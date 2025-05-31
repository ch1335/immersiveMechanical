package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.data.blockstates.MultiblockStates;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.IMMultiblocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IMMultiblockStates extends MultiblockStates {
    public IMMultiblockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.createMultiblock(this.innerObj("block/metal_multiblock/large_battery.obj"), IMMultiblocks.LARGE_BATTERY);
    }
}
