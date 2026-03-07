package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import com.chen1335.immersiveMechanical.common.blocks.multiblocks.IMMultiblocks;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks.IMCoilBlock;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockStateModelLoader.class)
public class BlockStateModelLoaderMixin {

    @Inject(method = "loadBlockStateDefinitions", at = @At("HEAD"))
    private void loadBlockStateDefinitions(ResourceLocation blockStateId, StateDefinition<Block, BlockState> stateDefenition, CallbackInfo ci, @Local(argsOnly = true) LocalRef<ResourceLocation> locationLocalRef) {
        if (stateDefenition.getOwner().getClass() == IMCoilBlock.class) {
            locationLocalRef.set(IMMultiblocks.COIL_TEMPLATE.getBlock().builtInRegistryHolder().key().location());
        }
    }
}
