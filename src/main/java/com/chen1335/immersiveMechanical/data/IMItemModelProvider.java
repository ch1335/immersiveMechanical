package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.definitions.IMBlocks;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import com.chen1335.registrate.devData.IEItemModelProvider;
import net.minecraft.client.renderer.RenderType;

public class IMItemModelProvider {
    public static void init(IEItemModelProvider provider) {
        provider.obj(IMBlocks.CONNECTOR_EHV.get(), ImmersiveMechanical.id("block/connector/connector_ehv.obj")).texture("texture", provider.modLoc("block/connector/connector_ehv")).transforms(ImmersiveMechanical.id("item/connector"));

        provider.obj(IMBlocks.CONNECTOR_EHV_RELAY.get(), ImmersiveMechanical.id("block/connector/connector_ehv_relay.obj")).texture("texture", provider.modLoc("block/connector/connector_ehv_relay")).transforms(ImmersiveMechanical.id("item/connector"));

        provider.obj(IMMultiblocks.LARGE_BATTERY.getBlockItem(), ImmersiveMechanical.id("block/metal_multiblock/large_battery.obj")).transforms(ImmersiveMechanical.id("item/large_battery"));

        provider.obj(IMMultiblocks.GREEN_HOUSE.getBlockItem(), ImmersiveMechanical.id("block/metal_multiblock/green_house.obj.ie")).transforms(ImmersiveMechanical.id("item/green_house")).renderType(RenderType.translucent().name);

        provider.obj(IMMultiblocks.INDUSTRIAL_FURNACES.getBlockItem(), ImmersiveMechanical.id("block/metal_multiblock/industrial_furnaces.obj")).transforms(ImmersiveMechanical.id("item/industrial_furnaces")).renderType(RenderType.cutout().name);

        provider.obj(IMMultiblocks.COIL.getBlockItem(), ImmersiveMechanical.id("block/metal_multiblock/coil.obj.ie")).transforms(ImmersiveMechanical.id("item/coil")).renderType(RenderType.cutout().name);

        provider.obj(IMMultiblocks.SMALL_MINING_MACHINE.getBlockItem(), ImmersiveMechanical.id("block/metal_multiblock/small_mining_machine.obj")).transforms(ImmersiveMechanical.id("item/small_mining_machine"));

        provider.obj(IMBlocks.TURRET_LASER.get(), ImmersiveMechanical.id("block/metal_device/turret_laser_inv.obj")).transforms(ImmersiveMechanical.id("item/turret"));

    }
}
