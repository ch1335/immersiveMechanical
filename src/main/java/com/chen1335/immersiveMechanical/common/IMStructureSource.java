package com.chen1335.immersiveMechanical.common;

import com.mojang.datafixers.DataFixer;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.LevelStorageSource;

import java.util.Optional;
import java.util.stream.Stream;

public class IMStructureSource {
    public IMStructureSource(ResourceManager resourceManager, LevelStorageSource.LevelStorageAccess levelStorageAccess, DataFixer fixerUpper, HolderGetter<Block> blockLookup) {

    }

    public Optional<StructureTemplate> load(ResourceLocation resourceLocation) {
        return Optional.empty();
    }

    public Stream<ResourceLocation> list() {
        return Stream.empty();
    }
}
