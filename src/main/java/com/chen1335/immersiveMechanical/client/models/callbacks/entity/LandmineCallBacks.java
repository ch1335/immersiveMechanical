package com.chen1335.immersiveMechanical.client.models.callbacks.entity;

import blusunrize.immersiveengineering.api.client.ieobj.BlockCallback;
import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallbacks;
import blusunrize.immersiveengineering.api.client.ieobj.ItemCallback;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.utils.ModelUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LandmineCallBacks implements BlockCallback<BlockState> , ItemCallback<BlockState> {
    public static final LandmineCallBacks INSTANCE = new LandmineCallBacks();

    public static ModelProperty<BlockState> getProperty() {
        return IEOBJCallbacks.getModelProperty(INSTANCE);
    }

    @Override
    public BlockState extractKey(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, BlockEntity blockEntity) {
        return getDefaultKey();
    }

    @Override
    public boolean dependsOnLayer() {
        return true;
    }

    @Override
    public BlockState getDefaultKey() {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public @Nullable TextureAtlasSprite getTextureReplacement(BlockState object, String group, String material) {
        if (!object.isAir()) {
            ResourceLocation sideTexture = ModelUtils.getSideTexture(object, Direction.UP);
            if (sideTexture != null) {
                return ClientUtils.getSprite(sideTexture);
            }
        }
        return null;
    }

    @Override
    public BlockState extractKey(ItemStack stack, LivingEntity owner) {
        return Blocks.AIR.defaultBlockState();
    }
}
