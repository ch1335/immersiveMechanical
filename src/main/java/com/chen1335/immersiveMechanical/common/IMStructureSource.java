package com.chen1335.immersiveMechanical.common;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.DataFixer;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.LevelStorageSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class IMStructureSource {
    private static final FileToIdConverter RESOURCE_LISTER = new FileToIdConverter("structure", ".nbt");
    private final ResourceManager resourceManager;
    private final LevelStorageSource.LevelStorageAccess levelStorageAccess;
    private final DataFixer fixerUpper;
    private final HolderGetter<Block> blockLookup;

    public static final HashBiMap<Supplier<? extends Block>, ResourceLocation> COILS = HashBiMap.create();

    public IMStructureSource(ResourceManager resourceManager, LevelStorageSource.LevelStorageAccess levelStorageAccess, DataFixer fixerUpper, HolderGetter<Block> blockLookup) {
        this.resourceManager = resourceManager;

        this.levelStorageAccess = levelStorageAccess;
        this.fixerUpper = fixerUpper;
        this.blockLookup = blockLookup;
    }

    public Optional<StructureTemplate> load(ResourceLocation resourceLocation) {
        BiMap<ResourceLocation, Supplier<? extends Block>> inverse = COILS.inverse();
        if (inverse.containsKey(resourceLocation)) {
            Optional<Resource> resource = resourceManager.getResource(RESOURCE_LISTER.idToFile(ImmersiveMechanical.id("multiblocks/coil_template")));
            if (resource.isPresent()) {
                Resource resource1 = resource.get();
                try (InputStream input = resource1.open()) {
                    CompoundTag nbt = NbtIo.readCompressed(input, NbtAccounter.unlimitedHeap());
                    nbt.getList("palette", Tag.TAG_COMPOUND).getCompound(0).putString("Name", BuiltInRegistries.BLOCK.getKey(inverse.get(resourceLocation).get()).toString());
                    StructureTemplate template = new StructureTemplate();
                    template.load(blockLookup, nbt);
                    return Optional.of(template);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return Optional.empty();
    }

    public Stream<ResourceLocation> list() {
        List<ResourceLocation> list = new ArrayList<>();
        list.addAll(COILS.values());
        return list.stream();
    }
}
