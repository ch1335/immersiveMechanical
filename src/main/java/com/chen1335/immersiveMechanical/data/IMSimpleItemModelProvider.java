package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IMSimpleItemModelProvider extends ItemModelProvider {
    public IMSimpleItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ImmersiveMechanical.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleBlockItem(IMBlocks.LARGE_BATTERY_CORE.value());
        simpleBlockItem(IMBlocks.CHROME_ORE.value());
        simpleBlockItem(IMBlocks.DEEPSLATE_CHROME_ORE.value());
        simpleBlockItem(IMBlocks.COIL_NICHROME.value());
    }
}
