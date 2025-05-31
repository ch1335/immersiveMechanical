package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.register.IEMultiblockLogic;
import blusunrize.immersiveengineering.data.ItemModels;
import blusunrize.immersiveengineering.data.blockstates.MultiblockStates;
import com.chen1335.immersiveMechanical.API.objects.IMItems;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IMItemModelProvider extends ItemModels {
    public IMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper, MultiblockStates blockStates) {
        super(output, existingFileHelper, blockStates);
    }

    @Override
    protected void registerModels() {
        this.obj(IMItems.CONNECTOR_EHV.get(), ImmersiveMechanical.id("block/connector/connector_ehv.obj")).texture("texture", this.modLoc("block/connector/connector_ehv")).transforms(ImmersiveEngineering.rl("item/connector"));

        this.obj(IMMultiblockLogic.LARGE_BATTERY.blockItem().get(), ImmersiveMechanical.id("block/metal_multiblock/large_battery.obj")).transforms(ImmersiveMechanical.id("item/large_battery"));
    }

}
