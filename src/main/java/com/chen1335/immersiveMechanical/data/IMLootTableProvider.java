package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.common.util.loot.BEDropLootEntry;
import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
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

    public static class BlockLoot implements LootTableSubProvider {
        BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output;

        public BlockLoot(HolderLookup.Provider provider) {

        }

        @Override
        public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            this.output = output;
            dropTile(IMBlocks.LARGE_BATTERY_CORE);
            dropSelf(IMBlocks.CONNECTOR_EHV);
            dropSelf(IMBlocks.CONNECTOR_EHV_RELAY);
        }

        private void dropTile(Holder<Block> holder) {
            output.accept(registerBlock(holder.getKey().location().getPath()), LootTable.lootTable().withPool(dropTile()));
        }

        private LootPool.Builder dropTile() {
            return LootPool.lootPool().when(ExplosionCondition.survivesExplosion()).add(BEDropLootEntry.builder());
        }

        private void dropSelf(Holder<Block> holder) {
            output.accept(registerBlock(holder.getKey().location().getPath()), LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(holder.value()))));
        }

        private static ResourceKey<LootTable> registerBlock(String name) {
            return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(ImmersiveMechanical.MODID, "blocks/" + name));
        }
    }
}
