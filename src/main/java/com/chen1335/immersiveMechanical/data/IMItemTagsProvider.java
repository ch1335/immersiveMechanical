package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.API.objects.IMItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class IMItemTagsProvider extends ItemTagsProvider {
    public IMItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        IMItems.Metals.METALS.forEach((metals, metalTypesMap) -> {
            metalTypesMap.forEach((metalTypes, itemDeferredItem) -> {
                tag(metalTypes.getTag(metals.getName())).add(
                        itemDeferredItem.asItem()
                );
                tag(metalTypes.getTypTag()).add(
                        itemDeferredItem.asItem()
                );
            });
        });
    }
}
