package com.chen1335.immersiveMechanical.common.blocks.multiblocks;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.GreenHouse;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.LargeBattery;

import java.util.ArrayList;
import java.util.List;

public class IMMultiblocks {
    public static final List<MultiblockHandler.IMultiblock> IM_MULTIBLOCKS = new ArrayList<>();
    public static IETemplateMultiblock LARGE_BATTERY;
    public static IETemplateMultiblock GREEN_HOUSE;

    public static void init() {
        LARGE_BATTERY = register(new LargeBattery());
        GREEN_HOUSE = register(new GreenHouse());
    }

    private static <T extends MultiblockHandler.IMultiblock> T register(T multiblock) {
        IM_MULTIBLOCKS.add(multiblock);
        MultiblockHandler.registerMultiblock(multiblock);
        return multiblock;
    }
}
