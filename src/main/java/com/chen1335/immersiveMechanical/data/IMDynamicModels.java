package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.client.models.obj.callback.DynamicSubmodelCallbacks;
import blusunrize.immersiveengineering.data.models.IEOBJBuilder;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.registrate.devData.IEDynamicModelProvider;

public class IMDynamicModels {
    public static void init(IEDynamicModelProvider provider) {
        provider.getBuilder(ImmersiveMechanical.id("laser_turret").toString())
                .customLoader(IEOBJBuilder::begin)
                .modelLocation(ImmersiveMechanical.id("models/block/metal_device/turret_laser.obj.ie"))
                .callback(DynamicSubmodelCallbacks.INSTANCE)
                .end();
    }
}
