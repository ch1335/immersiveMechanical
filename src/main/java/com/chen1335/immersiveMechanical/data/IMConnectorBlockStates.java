package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.data.blockstates.ConnectorBlockStates;
import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.google.common.collect.ImmutableMap;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IMConnectorBlockStates extends ConnectorBlockStates {
    public IMConnectorBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.createAllRotatedBlock(IMBlocks.CONNECTOR_EHV, this.obj("block/connector/connector_ehv", ImmersiveMechanical.id("block/connector/connector_ehv.obj"), ImmutableMap.of("texture", this.modLoc("block/connector/connector_ehv")), this.models()));

    }
}
