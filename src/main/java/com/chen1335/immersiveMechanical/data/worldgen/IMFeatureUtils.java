package com.chen1335.immersiveMechanical.data.worldgen;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class IMFeatureUtils {
    public static BlockPredicate simplePatchPredicate(TagKey<Block> blocks) {
        BlockPredicate blockpredicate;
        blockpredicate = BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesTag(Direction.DOWN.getNormal(), blocks));
        return blockpredicate;
    }

    public static BlockPredicate notAirPredicate(Direction direction) {
        BlockPredicate blockpredicate;
        blockpredicate = BlockPredicate.not(BlockPredicate.matchesBlocks(direction.getNormal(), Blocks.AIR));
        return blockpredicate;
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        IMFeatures.bootstrap(context);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ImmersiveMechanical.id(name));
    }
}
