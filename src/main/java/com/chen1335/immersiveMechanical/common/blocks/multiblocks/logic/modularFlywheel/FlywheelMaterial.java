package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel;

import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.API.objects.IMRegistries;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.Optional;

public record FlywheelMaterial(Holder<Block> manualBlock, HolderSet<Block> holderSet, int maxEnergyStored) {
    public static final Codec<FlywheelMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("manualBlock").forGetter(FlywheelMaterial::manualBlock),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(FlywheelMaterial::holderSet),
            Codec.INT.fieldOf("maxEnergyStored").forGetter(FlywheelMaterial::maxEnergyStored)
    ).apply(instance, FlywheelMaterial::new));


    public static Optional<Holder.Reference<FlywheelMaterial>> get(Level level, Block block) {
        return level.holderLookup(IMRegistries.FLYWHEEL_MATERIAL).listElements().filter(holder -> holder.value().holderSet().contains(block.builtInRegistryHolder())).findAny();
    }

    public static void bootstrap(BootstrapContext<FlywheelMaterial> context) {
        register(context, "copper", Blocks.COPPER_BLOCK, Tags.Blocks.STORAGE_BLOCKS_COPPER, 22_750_000);
        register(context, "iron", Blocks.IRON_BLOCK, Tags.Blocks.STORAGE_BLOCKS_IRON, 20_000_000);
        register(context, "gold", Blocks.GOLD_BLOCK, Tags.Blocks.STORAGE_BLOCKS_GOLD, 49_125_000);
        register(context, "steel", IEBlocks.Metals.STORAGE.get(EnumMetals.STEEL).get(), IETags.getTagsFor(EnumMetals.STEEL).storage, 20_000_000);
        register(context, "aluminum", IEBlocks.Metals.STORAGE.get(EnumMetals.ALUMINUM).get(), IETags.getTagsFor(EnumMetals.ALUMINUM).storage, 6_875_000);
        register(context, "nickel", IEBlocks.Metals.STORAGE.get(EnumMetals.NICKEL).get(), IETags.getTagsFor(EnumMetals.NICKEL).storage, 22_625_000);
        register(context, "uranium", IEBlocks.Metals.STORAGE.get(EnumMetals.URANIUM).get(), IETags.getTagsFor(EnumMetals.URANIUM).storage, 48_375_000);
        register(context, "silver", IEBlocks.Metals.STORAGE.get(EnumMetals.SILVER).get(), IETags.getTagsFor(EnumMetals.SILVER).storage, 26_625_000);
        register(context, "lead", IEBlocks.Metals.STORAGE.get(EnumMetals.LEAD).get(), IETags.getTagsFor(EnumMetals.LEAD).storage, 28_875_000);
        register(context, "constantan", IEBlocks.Metals.STORAGE.get(EnumMetals.CONSTANTAN).get(), IETags.getTagsFor(EnumMetals.CONSTANTAN).storage, 22_625_000);
        register(context, "electrum", IEBlocks.Metals.STORAGE.get(EnumMetals.ELECTRUM).get(), IETags.getTagsFor(EnumMetals.ELECTRUM).storage, 36_875_000);

        //?!滚木!?
        register(context, "logs", Blocks.OAK_LOG, BlockTags.LOGS, 1_000_000);
    }


    private static void register(BootstrapContext<FlywheelMaterial> context, String name, Block manualBlock, Block block, int maxEnergyStored) {
        HolderSet.Direct<Block> direct = HolderSet.direct(block.builtInRegistryHolder());
        context.register(ResourceKey.create(IMRegistries.FLYWHEEL_MATERIAL, ImmersiveMechanical.id(name)), new FlywheelMaterial(manualBlock.builtInRegistryHolder(), direct, maxEnergyStored));
    }

    private static void register(BootstrapContext<FlywheelMaterial> context, String name, Block manualBlock, TagKey<Block> tagKey, int maxEnergyStored) {
        context.register(ResourceKey.create(IMRegistries.FLYWHEEL_MATERIAL, ImmersiveMechanical.id(name)), new FlywheelMaterial(manualBlock.builtInRegistryHolder(), context.lookup(Registries.BLOCK).getOrThrow(tagKey), maxEnergyStored));
    }
}
