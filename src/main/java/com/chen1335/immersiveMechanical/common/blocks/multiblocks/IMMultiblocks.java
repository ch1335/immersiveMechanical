package com.chen1335.immersiveMechanical.common.blocks.multiblocks;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.*;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMMultiblocks {
    public static final List<MultiblockHandler.IMultiblock> IM_MULTIBLOCKS = new ArrayList<>();
    public static IETemplateMultiblock LARGE_BATTERY;
    public static IETemplateMultiblock GREEN_HOUSE;
    public static IETemplateMultiblock COIL;
    public static IETemplateMultiblock INDUSTRIAL_FURNACES;
    public static IETemplateMultiblock SMALL_MINING_MACHINE;
    public static final Map<ResourceLocation, CoilTemplate> COILS = new HashMap<>();

    public static void init() {
        LARGE_BATTERY = register(new LargeBattery());
        GREEN_HOUSE = register(new GreenHouse());
        INDUSTRIAL_FURNACES = register(new IndustrialFurnaces());
        COIL = register(new CoilTemplate(ImmersiveMechanical.id("multiblocks/coil"), IMMultiblockLogic.COIL));
        SMALL_MINING_MACHINE = register(new SmallMiningMachine());
    }

    private static <T extends MultiblockHandler.IMultiblock> T register(T multiblock) {
        IM_MULTIBLOCKS.add(multiblock);
        MultiblockHandler.registerMultiblock(multiblock);
        return multiblock;
    }
}
