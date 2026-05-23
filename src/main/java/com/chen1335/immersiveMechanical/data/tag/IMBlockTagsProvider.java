package com.chen1335.immersiveMechanical.data.tag;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import com.chen1335.immersiveMechanical.definitions.IMBlocks;
import com.chen1335.immersiveMechanical.API.tags.IMBlockTags;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
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
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                IMBlocks.LARGE_BATTERY_CORE.value(),
                IMBlocks.CHROME_ORE.value(),
                IMBlocks.DEEPSLATE_CHROME_ORE.value(),
                IMBlocks.CONNECTOR_EHV.value(),
                IMBlocks.CONNECTOR_EHV_RELAY.value(),
                IMBlocks.TURRET_LASER.value(),
                IMBlocks.COIL_NICHROME.value()
        );

        tag(BlockTags.NEEDS_IRON_TOOL).add(
                IMBlocks.LARGE_BATTERY_CORE.value(),
                IMBlocks.CHROME_ORE.value(),
                IMBlocks.DEEPSLATE_CHROME_ORE.value(),
                IMBlocks.CONNECTOR_EHV.value(),
                IMBlocks.CONNECTOR_EHV_RELAY.value(),
                IMBlocks.TURRET_LASER.value(),
                IMBlocks.COIL_NICHROME.value()
        );

        tag(Tags.Blocks.ORES)
                .addTag(IMBlockTags.ORES_CHROME);


        tag(IMBlockTags.ORES_CHROME)
                .add(
                        IMBlocks.CHROME_ORE.value(),
                        IMBlocks.DEEPSLATE_CHROME_ORE.value()
                );

        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(
                IMBlocks.DEEPSLATE_CHROME_ORE.value()
        );

        Block[] array = ImmersiveMechanical.REGISTRATE.getMultiblocks().stream().map(MultiblockHandler.IMultiblock::getBlock).toList().toArray(new Block[0]);

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                array
        );
    }
}
