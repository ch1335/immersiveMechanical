package com.chen1335.immersiveMechanical.data.worldgen;

import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class IMFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> CHROME_ORE_LARGE = IMFeatureUtils.createKey("chrome_ore_large");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest ruletest1 = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest ruletest2 = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> list2 = List.of(
                OreConfiguration.target(ruletest1, IMBlocks.CHROME_ORE.value().defaultBlockState()),
                OreConfiguration.target(ruletest2, IMBlocks.DEEPSLATE_CHROME_ORE.value().defaultBlockState())
        );
        FeatureUtils.register(context, CHROME_ORE_LARGE, Feature.ORE, new OreConfiguration(list2, 12));

    }
}
