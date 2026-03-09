package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil;

import com.chen1335.immersiveMechanical.common.IMStructureSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public record CoilInfo(float timeModify, float energyModify) {
    public static final Map<Block, CoilInfo> COIL_INFO_MAP = new HashMap<>();

    public static final CoilInfo DEFAULT = new CoilInfo(1F, 1F);

    private static final List<Runnable> ADDERS = new ArrayList<>();

    public static void register(Supplier<? extends Block> blockSupplier, ResourceLocation id, CoilInfo coilInfo) {
        IMStructureSource.COILS.put(blockSupplier, id);
        ADDERS.add(() -> COIL_INFO_MAP.put(blockSupplier.get(), coilInfo));
    }

    public static void setup() {
        ADDERS.forEach(Runnable::run);
        ADDERS.clear();
    }
}
