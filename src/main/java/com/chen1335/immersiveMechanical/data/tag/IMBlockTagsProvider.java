package com.chen1335.immersiveMechanical.data.tag;

import com.chen1335.immersiveMechanical.API.tags.IMBlockTags;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;


public class IMBlockTagsProvider extends BlockTagsProvider {
    public IMBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ImmersiveMechanical.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(Tags.Blocks.ORES)
                .addTag(IMBlockTags.ORES_CHROME);

    }
}
