package com.chen1335.immersiveMechanical.common.blocks.multiblocks;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;

import java.util.ArrayList;
import java.util.List;

public class IMMultiblocks {
    public static final List<MultiblockHandler.IMultiblock> IM_MULTIBLOCKS = new ArrayList();
    public static IETemplateMultiblock LARGE_BATTERY;


    public static void init() {
        LARGE_BATTERY = register(new LargeBattery());
    }

    private static <T extends MultiblockHandler.IMultiblock> T register(T multiblock) {
        IM_MULTIBLOCKS.add(multiblock);
        MultiblockHandler.registerMultiblock(multiblock);
        return multiblock;
    }
}
