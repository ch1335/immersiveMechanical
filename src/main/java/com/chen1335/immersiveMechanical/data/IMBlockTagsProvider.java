package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
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
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                IMBlocks.LARGE_BATTERY_CORE.value(),
                IMBlocks.CHROME_ORE.value(),
                IMBlocks.DEEPSLATE_CHROME_ORE.value()
        );

        tag(BlockTags.NEEDS_IRON_TOOL).add(
                IMBlocks.LARGE_BATTERY_CORE.value(),
                IMBlocks.CHROME_ORE.value(),
                IMBlocks.DEEPSLATE_CHROME_ORE.value()
        );
    }
}
