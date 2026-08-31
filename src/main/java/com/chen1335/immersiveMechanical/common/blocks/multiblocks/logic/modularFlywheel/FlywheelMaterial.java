package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel;

import com.chen1335.immersiveMechanical.API.objects.IMRegistries;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public record FlywheelMaterial(Holder<Block> blockHolder, int maxEnergyStored) {
    public static final Codec<FlywheelMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("block").forGetter(FlywheelMaterial::blockHolder),
            Codec.INT.fieldOf("maxEnergyStored").forGetter(FlywheelMaterial::maxEnergyStored)
    ).apply(instance, FlywheelMaterial::new));


    public static void bootstrap(BootstrapContext<FlywheelMaterial> context) {
        register(context, Blocks.IRON_BLOCK, 16_000_000);
    }

    private static void register(BootstrapContext<FlywheelMaterial> context, Block ironBlock, int i) {
        context.register(ResourceKey.create(IMRegistries.FLYWHEEL_MATERIAL, ResourceLocation.parse(ironBlock.builtInRegistryHolder().getRegisteredName())), new FlywheelMaterial(BuiltInRegistries.BLOCK.wrapAsHolder(ironBlock), i));
    }

}
