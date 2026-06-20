package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.client.models.obj.callback.DynamicSubmodelCallbacks;
import blusunrize.immersiveengineering.data.blockstates.MultiblockStates;
import blusunrize.immersiveengineering.data.models.IEOBJBuilder;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.registrate.devData.IEDynamicModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Map;

public class IMDynamicModels {
    public static void init(IEDynamicModelProvider provider) {
        provider.getBuilder(ImmersiveMechanical.id("laser_turret").toString())
                .customLoader(IEOBJBuilder::begin)
                .modelLocation(ImmersiveMechanical.id("models/block/metal_device/turret_laser.obj.ie"))
                .callback(DynamicSubmodelCallbacks.INSTANCE)
                .end();



        for(Map.Entry<Block, ModelFile> multiblock : provider.getMultiblockStatesProvider().unsplitModels.entrySet()) {
            provider.withExistingParent(BuiltInRegistries.BLOCK.getKey(multiblock.getKey()).getPath(), multiblock.getValue().getLocation());
        }
    }
}
