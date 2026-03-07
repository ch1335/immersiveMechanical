package com.chen1335.immersiveMechanical.client.models.callbacks;

import blusunrize.immersiveengineering.api.client.ieobj.BlockCallback;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.utils.ModelUtils;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks.IMCoilBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoilCallbacks implements BlockCallback<Block> {
    public static final CoilCallbacks INSTANCE = new CoilCallbacks();

    @Override
    public Block extractKey(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, BlockEntity blockEntity) {
        if (state.getBlock() instanceof IMCoilBlock coilBlock) {
            return coilBlock.getMaterialBlock();
        }
        return getDefaultKey();
    }

    @Override
    public Block getDefaultKey() {
        return IEBlocks.MetalDecoration.LV_COIL.get();
    }

    @Override
    public boolean dependsOnLayer() {
        return true;
    }

    @Override
    public @Nullable TextureAtlasSprite getTextureReplacement(Block object, String group, String material) {
        if (material.equals("side")) {
            ResourceLocation sideTexture = ModelUtils.getSideTexture(object.asItem().getDefaultInstance(), Direction.NORTH);
            if (sideTexture != null) {
               return ClientUtils.getSprite(sideTexture);
            }
        }
        return null;
    }
}
