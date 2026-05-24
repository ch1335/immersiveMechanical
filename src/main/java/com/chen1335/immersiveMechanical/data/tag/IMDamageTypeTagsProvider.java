package com.chen1335.immersiveMechanical.data.tag;

import com.chen1335.immersiveMechanical.API.objects.IMDamageTypes;
import com.chen1335.registrate.devData.IEDamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;

public class IMDamageTypeTagsProvider {
    public static void init(IEDamageTypeTagsProvider provider) {
        provider.tag(DamageTypeTags.BYPASSES_COOLDOWN).add(
                IMDamageTypes.LASER
        );

        provider.tag(DamageTypeTags.NO_KNOCKBACK).add(
                IMDamageTypes.LASER
        );
    }
}
