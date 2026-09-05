package com.chen1335.immersiveMechanical.common.items.dataComponents;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record Disguise(BlockState blockState) {
    public static final Disguise EMPTY = new Disguise(Blocks.AIR.defaultBlockState());
    public static final Codec<Disguise> CODEC = BlockState.CODEC.xmap(Disguise::new, Disguise::blockState);
}
