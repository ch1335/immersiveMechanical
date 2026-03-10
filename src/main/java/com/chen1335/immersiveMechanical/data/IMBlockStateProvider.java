package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.data.blockstates.BlockStates;
import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IMBlockStateProvider extends BlockStates {
    public IMBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper);
    }


    @Override
    protected void registerStatesAndModels() {
        simpleBlock(IMBlocks.LARGE_BATTERY_CORE.value());
        simpleBlock(IMBlocks.CHROME_ORE.value());
        simpleBlock(IMBlocks.DEEPSLATE_CHROME_ORE.value());
        turret(IMBlocks.TURRET_LASER, "block/metal_device/turret_laser.obj.ie");

        this.cubeSideVertical(IMBlocks.COIL_NICHROME, ImmersiveMechanical.id("block/metal_decoration/coil_nichrome_side"), ImmersiveMechanical.id("block/metal_decoration/coil_nichrome_top"));

    }
}
