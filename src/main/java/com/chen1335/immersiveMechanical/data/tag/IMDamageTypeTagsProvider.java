package com.chen1335.immersiveMechanical.data.tag;

import com.chen1335.immersiveMechanical.API.objects.IMDamageTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class IMDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public IMDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ImmersiveMechanical.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(DamageTypeTags.BYPASSES_COOLDOWN).add(
                IMDamageTypes.LASER
        );

        tag(DamageTypeTags.NO_KNOCKBACK).add(
                IMDamageTypes.LASER
        );
    }
}
