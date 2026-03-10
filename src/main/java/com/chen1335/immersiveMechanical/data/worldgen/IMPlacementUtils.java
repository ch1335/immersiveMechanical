package com.chen1335.immersiveMechanical.data.worldgen;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class IMPlacementUtils {

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        IMPlacements.bootstrap(context);
    }
    public static ResourceKey<PlacedFeature> createKey(String key) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ImmersiveMechanical.id(key));
    }
}
