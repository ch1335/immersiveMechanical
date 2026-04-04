package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.data.ItemModels;
import blusunrize.immersiveengineering.data.blockstates.MultiblockStates;
import com.chen1335.immersiveMechanical.API.objects.IMItems;
import com.chen1335.immersiveMechanical.API.objects.metal.IMMetals;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IMItemModelProvider extends ItemModels {
    public IMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper, MultiblockStates blockStates) {
        super(output, existingFileHelper, blockStates);
    }

    @Override
    protected void registerModels() {
        this.obj(IMItems.CONNECTOR_EHV.get(), ImmersiveMechanical.id("block/connector/connector_ehv.obj")).texture("texture", this.modLoc("block/connector/connector_ehv")).transforms(ImmersiveMechanical.id("item/connector"));

        this.obj(IMItems.CONNECTOR_EHV_RELAY.get(), ImmersiveMechanical.id("block/connector/connector_ehv_relay.obj")).texture("texture", this.modLoc("block/connector/connector_ehv_relay")).transforms(ImmersiveMechanical.id("item/connector"));

        this.obj(IMMultiblockLogic.LARGE_BATTERY.blockItem().get(), ImmersiveMechanical.id("block/metal_multiblock/large_battery.obj")).transforms(ImmersiveMechanical.id("item/large_battery"));

        this.obj(IMMultiblockLogic.GREEN_HOUSE.blockItem().get(), ImmersiveMechanical.id("block/metal_multiblock/green_house.obj.ie")).transforms(ImmersiveMechanical.id("item/green_house")).renderType(RenderType.translucent().name);

        this.obj(IMMultiblockLogic.INDUSTRIAL_FURNACES.blockItem().get(), ImmersiveMechanical.id("block/metal_multiblock/industrial_furnaces.obj")).transforms(ImmersiveMechanical.id("item/industrial_furnaces")).renderType(RenderType.cutout().name);

        this.obj(IMMultiblockLogic.COIL.blockItem().get(), ImmersiveMechanical.id("block/metal_multiblock/coil.obj.ie")).transforms(ImmersiveMechanical.id("item/coil")).renderType(RenderType.cutout().name);

        this.obj(IMMultiblockLogic.SMALL_MINING_MACHINE.blockItem().get(), ImmersiveMechanical.id("block/metal_multiblock/small_mining_machine.obj")).transforms(ImmersiveMechanical.id("item/small_mining_machine"));


        this.obj(IMItems.LASER_TURRET.get(), ImmersiveMechanical.id("block/metal_device/turret_laser_inv.obj")).transforms(ImmersiveMechanical.id("item/turret"));

        this.addItemModels("", IMItems.ACSR.get());
        this.addItemModels("", IMItems.EHV_WIRE_COIL.get());
        this.addItemModels("", IMItems.ROW_CHROME.get());
        IMMetals.METALS.forEach((metals, metalTypesDeferredItemMap) -> {
            metalTypesDeferredItemMap.forEach((metalTypes, itemDeferredItem) -> {
                this.addItemModels("", itemDeferredItem);
            });
        });

    }
}
