package com.chen1335.immersiveMechanical.API.registrate.data;

import blusunrize.immersiveengineering.common.util.loot.BEDropLootEntry;
import blusunrize.immersiveengineering.common.util.loot.DropInventoryLootEntry;
import blusunrize.immersiveengineering.data.loot.LootUtils;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;

public class IMLootHelper {
    public static void dropTile(RegistrateBlockLootTables lootTables, Block block) {
        lootTables.add(block, LootTable.lootTable().withPool(dropTile()));
    }

    private static LootPool.Builder dropTile() {
        return LootPool.lootPool().when(ExplosionCondition.survivesExplosion()).add(BEDropLootEntry.builder());
    }


    public static LootTable.Builder dropOre(RegistrateBlockLootTables lootTables, Block block, Item item) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = lootTables.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
        return lootTables.createSilkTouchDispatchTable(
                block,
                lootTables.applyExplosionDecay(
                        block, LootItem.lootTableItem(item).apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }

    public static void registerMultiblock(RegistrateBlockLootTables lootTables, Block block) {
        register(lootTables, block, dropInv(), dropOriginalBlock());
    }


    private static void register(RegistrateBlockLootTables lootTables, Block b, LootPool.Builder... pools) {
        LootTable.Builder builder = LootTable.lootTable();
        for (LootPool.Builder pool : pools) {
            builder.withPool(pool);
        }
        lootTables.add(b, builder);
    }


    private static LootPool.Builder dropOriginalBlock() {
        return createPoolBuilder()
                .add(LootUtils.getMultiblockDropBuilder());
    }

    private static LootPool.Builder dropInv() {
        return createPoolBuilder()
                .add(DropInventoryLootEntry.builder());
    }

    private static LootPool.Builder createPoolBuilder() {
        return LootPool.lootPool().when(ExplosionCondition.survivesExplosion());
    }
}
