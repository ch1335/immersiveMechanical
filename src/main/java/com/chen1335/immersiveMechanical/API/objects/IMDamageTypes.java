package com.chen1335.immersiveMechanical.API.objects;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class IMDamageTypes {
    public static final ResourceKey<DamageType> LASER = type("laser");

    private static ResourceKey<DamageType> type(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ImmersiveMechanical.id(path));
    }
}
