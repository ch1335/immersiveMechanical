package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.common.util.loot.BEDropLootEntry;
import blusunrize.immersiveengineering.common.util.loot.DropInventoryLootEntry;
import blusunrize.immersiveengineering.data.loot.LootUtils;
import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import com.chen1335.immersiveMechanical.API.objects.IMItems;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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

            this.registerMultiblock(IMMultiblockLogic.COIL);
            this.registerMultiblock(IMMultiblockLogic.GREEN_HOUSE);
            this.registerMultiblock(IMMultiblockLogic.LARGE_BATTERY);
            this.registerMultiblock(IMMultiblockLogic.INDUSTRIAL_FURNACES);
            this.registerMultiblock(IMMultiblockLogic.SMALL_MINING_MACHINE);
        }

        private void dropTile(Block holder) {
            add(holder, LootTable.lootTable().withPool(dropTile()));
        }

        private LootPool.Builder dropTile() {
            return LootPool.lootPool().when(ExplosionCondition.survivesExplosion()).add(BEDropLootEntry.builder());
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            ArrayList<Block> blocks = new ArrayList<>();
            IMBlocks.BLOCKS.getEntries().stream().map(Holder::value).forEach(blocks::add);
            IMMultiblockLogic.BLOCK_REGISTER.getEntries().stream().map(Holder::value).forEach(blocks::add);


            return blocks;
        }

        private void registerMultiblock(MultiblockRegistration<?> registration) {
            registerMultiblock(registration.block());
        }

        private void registerMultiblock(Supplier<? extends Block> b) {
            register(b, dropInv(), dropOriginalBlock());
        }

        private LootPool.Builder dropOriginalBlock() {
            return createPoolBuilder()
                    .add(LootUtils.getMultiblockDropBuilder());
        }

        private LootPool.Builder dropInv() {
            return createPoolBuilder()
                    .add(DropInventoryLootEntry.builder());
        }

        private LootPool.Builder createPoolBuilder() {
            return LootPool.lootPool().when(ExplosionCondition.survivesExplosion());
        }

        private void register(Supplier<? extends Block> b, LootPool.Builder... pools) {
            LootTable.Builder builder = LootTable.lootTable();
            for (LootPool.Builder pool : pools)
                builder.withPool(pool);
            register(b, builder);
        }

        private void register(Supplier<? extends Block> b, LootTable.Builder table) {
            add(b.get(), table);
        }


    }
}
