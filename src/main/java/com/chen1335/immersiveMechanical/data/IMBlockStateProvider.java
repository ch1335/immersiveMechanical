package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.data.blockstates.BlockStates;
import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IMBlockStateProvider extends BlockStates {
    public IMBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper);
    }


    @Override
    protected void registerStatesAndModels() {
        simpleBlock(IMBlocks.LARGE_BATTERY_CORE_BLOCK.value());

        turret(IMBlocks.TURRET_LASER, "block/metal_device/gun_turret.obj.ie");
    }
}
