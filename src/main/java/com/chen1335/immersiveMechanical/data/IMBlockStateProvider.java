package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.definitions.IMBlocks;
import com.chen1335.registrate.devData.IEBlockStateProvider;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.RenderType;

public class IMBlockStateProvider {
    public static void init(IEBlockStateProvider provider) {
        provider.simpleBlock(IMBlocks.LARGE_BATTERY_CORE.value());
        provider.simpleBlock(IMBlocks.CHROME_ORE.value());
        provider.simpleBlock(IMBlocks.DEEPSLATE_CHROME_ORE.value());
        provider.turret(IMBlocks.TURRET_LASER, "block/metal_device/turret_laser.obj.ie");
        provider.cubeSideVertical(IMBlocks.COIL_NICHROME, ImmersiveMechanical.id("block/metal_decoration/coil_nichrome_side"), ImmersiveMechanical.id("block/metal_decoration/coil_nichrome_top"));

        provider.createAllRotatedBlock(IMBlocks.CONNECTOR_EHV, provider.obj("block/connector/connector_ehv", ImmersiveMechanical.id("block/connector/connector_ehv.obj"), ImmutableMap.of("texture", provider.modLoc("block/connector/connector_ehv")), provider.models()));
        provider.createAllRotatedBlock(IMBlocks.CONNECTOR_EHV_RELAY, provider.obj("block/connector/connector_ehv_relay.obj",  RenderType.translucent()));
    }
}
