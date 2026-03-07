package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import com.chen1335.immersiveMechanical.common.IMStructureSource;
import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DataFixer;
import net.minecraft.core.HolderGetter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureTemplateManager.class)
public class StructureTemplateManagerMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;build()Lcom/google/common/collect/ImmutableList;"))
    private void addNweSource(ResourceManager resourceManager, LevelStorageSource.LevelStorageAccess levelStorageAccess, DataFixer fixerUpper, HolderGetter<Block> blockLookup, CallbackInfo ci, @Local ImmutableList.Builder<StructureTemplateManager.Source> builder) {
        IMStructureSource source = new IMStructureSource(resourceManager,levelStorageAccess,fixerUpper,blockLookup);
        builder.add(new StructureTemplateManager.Source(source::load,source::list));
    }
}
