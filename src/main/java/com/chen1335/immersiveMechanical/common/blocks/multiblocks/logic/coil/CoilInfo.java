package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil;

import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public record CoilInfo(DoubleSupplier timeModify, DoubleSupplier energyModify) {
    public static final Map<Block, CoilInfo> COIL_INFO_MAP = new HashMap<>();

    public static final CoilInfo DEFAULT = new CoilInfo(() -> 1F, () -> 1F);


    public static void register(Supplier<? extends Block> blockSupplier, CoilInfo coilInfo) {
        COIL_INFO_MAP.put(blockSupplier.get(), coilInfo);
    }


}
