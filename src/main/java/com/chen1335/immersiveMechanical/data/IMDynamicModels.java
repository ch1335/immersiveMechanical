package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.client.models.obj.callback.DynamicSubmodelCallbacks;
import blusunrize.immersiveengineering.data.DynamicModels;
import blusunrize.immersiveengineering.data.blockstates.MultiblockStates;
import blusunrize.immersiveengineering.data.models.IEOBJBuilder;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IMDynamicModels extends DynamicModels {
    public IMDynamicModels(MultiblockStates multiblocks, PackOutput output, ExistingFileHelper existingFileHelper) {
        super(multiblocks, output, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        getBuilder(ImmersiveMechanical.id("laser_turret").toString())
                .customLoader(IEOBJBuilder::begin)
                .modelLocation(ImmersiveMechanical.id("models/block/metal_device/turret_laser.obj.ie"))
                .callback(DynamicSubmodelCallbacks.INSTANCE)
                .end();
    }
}
