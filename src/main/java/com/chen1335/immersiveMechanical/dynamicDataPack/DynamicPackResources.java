package com.chen1335.immersiveMechanical.dynamicDataPack;

import jdk.jfr.Name;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DynamicPackResources implements PackResources {
    private final String packId;
    private final PackLocationInfo locationInfo;
    private final DynamicPackContents contents;
    private final Set<String> namespaces = new HashSet<>();

    public DynamicPackResources(String packId, DynamicPackContents contents) {
        this.packId = packId;
        this.contents = contents;
        this.locationInfo = new PackLocationInfo(
                packId,
                Component.literal(packId),
                PackSource.BUILT_IN,
                Optional.empty()
        );
    }

    public void addNamespace(String namespace) {
        namespaces.add(namespace);
    }

    public DynamicPackContents getContents() {
        return contents;
    }

    @Override
    public PackLocationInfo location() {
        return locationInfo;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... elements) {
        return null;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
        return contents.getResource(location);
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput output) {
        contents.listResources(namespace, path, output);
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return Collections.unmodifiableSet(namespaces);
    }


    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getMetadataSection(MetadataSectionSerializer<T> deserializer) {
        if (deserializer == PackMetadataSection.TYPE) {
            return (T) new PackMetadataSection(
                    Component.literal("Immersive Mechanical Dynamic Data"),
                    SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA)
            );
        }
        return null;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public void close() {
    }
}
