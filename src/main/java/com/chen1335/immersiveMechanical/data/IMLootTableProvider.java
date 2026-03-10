package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.common.util.loot.BEDropLootEntry;
import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import com.chen1335.immersiveMechanical.API.objects.IMItems;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class IMLootTableProvider extends LootTableProvider {
    public IMLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)), registries);
    }

    public static class BlockLoot extends BlockLootSubProvider {

        protected BlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }


        @Override
        protected void generate() {
            dropTile(IMBlocks.LARGE_BATTERY_CORE.get());
            dropSelf(IMBlocks.CONNECTOR_EHV.get());
            dropSelf(IMBlocks.CONNECTOR_EHV_RELAY.get());
            dropSelf(IMBlocks.TURRET_LASER.get());
            dropSelf(IMBlocks.COIL_NICHROME.get());

            this.add(IMBlocks.CHROME_ORE.value(), block -> this.createOreDrop(block, IMItems.ROW_CHROME.asItem()));
            this.add(IMBlocks.DEEPSLATE_CHROME_ORE.value(), block -> this.createOreDrop(block, IMItems.ROW_CHROME.asItem()));

        }

        private void dropTile(Block holder) {
            add(holder, LootTable.lootTable().withPool(dropTile()));
        }

        private LootPool.Builder dropTile() {
            return LootPool.lootPool().when(ExplosionCondition.survivesExplosion()).add(BEDropLootEntry.builder());
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return IMBlocks.BLOCKS.getEntries().stream().map(Holder::value).toList();
        }
    }
}
