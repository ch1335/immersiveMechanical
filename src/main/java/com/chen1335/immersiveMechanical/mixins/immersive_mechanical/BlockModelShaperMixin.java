package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.api.IEProperties;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.IMMultiblocks;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks.IMCoilBlock;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockModelShaper.class)
public class BlockModelShaperMixin {
    @Inject(method = "getBlockModel", at = @At("HEAD"))
    private void getBlockModel(BlockState state, CallbackInfoReturnable<BakedModel> cir, @Local(argsOnly = true) LocalRef<BlockState> blockStateLocalRef) {
        if (state.getBlock().getClass() == IMCoilBlock.class) {
            BlockState blockState = IMMultiblocks.COIL_TEMPLATE.getBlock().defaultBlockState();
            BlockState blockState1 = blockState.setValue(IEProperties.FACING_HORIZONTAL, state.getValue(IEProperties.FACING_HORIZONTAL)).setValue(IEProperties.MULTIBLOCKSLAVE, state.getValue(IEProperties.MULTIBLOCKSLAVE));
            blockStateLocalRef.set(blockState1);
        }
    }

    @Inject(method = "getParticleIcon", at = @At("HEAD"))
    private void getParticleIcon(BlockState state, CallbackInfoReturnable<TextureAtlasSprite> cir, @Local(argsOnly = true) LocalRef<BlockState> blockStateLocalRef) {
        if (state.getBlock().getClass() == IMCoilBlock.class) {
            blockStateLocalRef.set(((IMCoilBlock) state.getBlock()).getMaterialBlock().defaultBlockState());
        }
    }

    @Inject(method = "getTexture", at = @At("HEAD"))
    private void getTexture(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<TextureAtlasSprite> cir, @Local(argsOnly = true) LocalRef<BlockState> blockStateLocalRef) {
        if (state.getBlock().getClass() == IMCoilBlock.class) {
            blockStateLocalRef.set(((IMCoilBlock) state.getBlock()).getMaterialBlock().defaultBlockState());
        }
    }
}
