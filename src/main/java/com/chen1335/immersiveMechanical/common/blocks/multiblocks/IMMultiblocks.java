package com.chen1335.immersiveMechanical.common.blocks.multiblocks;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.IMStructureSource;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.CoilLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.CoilTemplate;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.GreenHouse;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.IndustrialFurnaces;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.LargeBattery;
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
    public static IETemplateMultiblock COIL_TEMPLATE;
    public static IETemplateMultiblock INDUSTRIAL_FURNACES;
    public static final Map<ResourceLocation, IETemplateMultiblock> COILS = new HashMap<>();

    public static void init() {
        LARGE_BATTERY = register(new LargeBattery());
        GREEN_HOUSE = register(new GreenHouse());
        INDUSTRIAL_FURNACES = register(new IndustrialFurnaces());
        COIL_TEMPLATE = register(new CoilTemplate(ImmersiveMechanical.id("multiblocks/coil_template"), IMMultiblockLogic.COIL_TEMPLATE));
        IMStructureSource.COILS.forEach((supplier, resourceLocation) -> {
            COILS.put(resourceLocation, register(new CoilTemplate(resourceLocation, IMMultiblockLogic.coil(new CoilLogic(), resourceLocation.getPath(), supplier).notMirrored().structure(() -> COILS.get(resourceLocation)).build())));
        });
    }

    private static <T extends MultiblockHandler.IMultiblock> T register(T multiblock) {
        IM_MULTIBLOCKS.add(multiblock);
        MultiblockHandler.registerMultiblock(multiblock);
        return multiblock;
    }
}
