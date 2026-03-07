package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import blusunrize.immersiveengineering.data.blockstates.MultiblockStates;
import blusunrize.immersiveengineering.data.models.NongeneratedModels;
import com.chen1335.immersiveMechanical.client.models.callbacks.CoilCallbacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.GreenHouseCallbacks;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.IMMultiblocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static net.minecraft.client.renderer.RenderType.*;

public class IMMultiblockStates extends MultiblockStates {
    public IMMultiblockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.createMultiblock(this.innerObj("block/metal_multiblock/large_battery.obj"), IMMultiblocks.LARGE_BATTERY);
        this.createMultiblock(this.innerObj("block/metal_multiblock/industrial_furnaces.obj"), IMMultiblocks.INDUSTRIAL_FURNACES);
//        this.createMultiblock(this.innerObj("block/metal_multiblock/green_house.obj.ie").renderType(RenderType.TRANSLUCENT.name), IMMultiblocks.GREEN_HOUSE);

        this.im_createDynamicMultiblock(
                this.ieObjBuilder("block/metal_multiblock/green_house.obj.ie", innerModels)
                        .callback(GreenHouseCallbacks.INSTANCE)
                        .layer(solid(), translucent())
                        .end(),
                IMMultiblocks.GREEN_HOUSE
        );

        this.im_createDynamicMultiblock(
                this.ieObjBuilder("block/metal_multiblock/coil.obj.ie", innerModels)
                        .callback(CoilCallbacks.INSTANCE)
                        .layer(cutout())
                        .end(),
                IMMultiblocks.COIL_TEMPLATE
        );
    }

    private void im_createDynamicMultiblock(NongeneratedModels.NongeneratedModel unsplitModel, IETemplateMultiblock multiblock) {

        Method m;
        try {
            m = MultiblockStates.class.getDeclaredMethod("createDynamicMultiblock", NongeneratedModels.NongeneratedModel.class, IETemplateMultiblock.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            m.setAccessible(true);
            m.invoke(this, unsplitModel, multiblock);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
