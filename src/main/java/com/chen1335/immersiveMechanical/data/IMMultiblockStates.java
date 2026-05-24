package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.client.models.callbacks.CoilCallbacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.GreenHouseCallbacks;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import com.chen1335.registrate.devData.IEMultiblockStatesProvider;

import static net.minecraft.client.renderer.RenderType.solid;
import static net.minecraft.client.renderer.RenderType.translucent;

public class IMMultiblockStates {
    public static void init(IEMultiblockStatesProvider provider) {
        provider.createMultiblock(provider.innerObj("block/metal_multiblock/large_battery.obj"), IMMultiblocks.LARGE_BATTERY.multiblock());
        provider.createMultiblock(provider.innerObj("block/metal_multiblock/industrial_furnaces.obj"), IMMultiblocks.INDUSTRIAL_FURNACES.multiblock());
        provider.createMultiblock(provider.innerObj("block/metal_multiblock/small_mining_machine.obj"), IMMultiblocks.SMALL_MINING_MACHINE.multiblock());


        provider.createDynamicMultiblock(
                provider.ieObjBuilder("block/metal_multiblock/green_house.obj.ie", provider.innerModels)
                        .callback(GreenHouseCallbacks.INSTANCE)
                        .layer(solid(), translucent())
                        .end(),
                IMMultiblocks.GREEN_HOUSE.multiblock()
        );

        provider.createDynamicMultiblock(
                provider.ieObjBuilder("block/metal_multiblock/coil.obj.ie", provider.innerModels)
                        .callback(CoilCallbacks.INSTANCE)
                        .layer(solid())
                        .end(),
                IMMultiblocks.COIL.multiblock()
        );
    }

}
