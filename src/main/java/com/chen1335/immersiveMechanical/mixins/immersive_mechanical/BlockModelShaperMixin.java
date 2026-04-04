package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockModelShaper.class)
public class BlockModelShaperMixin {

    @Inject(method = "getTexture", at = @At("HEAD"))
    private void getTexture(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<TextureAtlasSprite> cir, @Local(argsOnly = true) LocalRef<BlockState> blockStateLocalRef) {
        if (level.getBlockEntity(pos) instanceof IMultiblockBE<?> be) {
            IMultiblockState multiblockState = be.getHelper().getState();
            if (multiblockState instanceof CoilLogic.State coilState) {
                blockStateLocalRef.set(coilState.getCoilBlock().defaultBlockState());
            }
        }

    }
}
