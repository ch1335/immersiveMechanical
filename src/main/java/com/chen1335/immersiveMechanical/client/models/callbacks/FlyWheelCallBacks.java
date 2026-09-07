package com.chen1335.immersiveMechanical.client.models.callbacks;

import blusunrize.immersiveengineering.api.client.ieobj.BlockCallback;
import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallbacks;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.utils.ModelUtils;
import com.chen1335.immersiveMechanical.API.client.IMOBJCallback;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlyWheelCallBacks implements BlockCallback<Block>, IMOBJCallback<Block> {
    public static final FlyWheelCallBacks INSTANCE = new FlyWheelCallBacks();


    public static ModelProperty<Block> getProperty() {
        return IEOBJCallbacks.getModelProperty(INSTANCE);
    }

    @Override
    public Block extractKey(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, BlockEntity blockEntity) {
        return Blocks.IRON_BLOCK;
    }

    @Override
    public boolean dependsOnLayer() {
        return true;
    }

    @Override
    public Block getDefaultKey() {
        return Blocks.GOLD_BLOCK;
    }

    @Override
    public @Nullable TextureAtlasSprite getTextureReplacement(Block object, String group, String material) {
        if (material.equals("material")) {
            ResourceLocation sideTexture = ModelUtils.getSideTexture(object.defaultBlockState(), Direction.NORTH);
            if (sideTexture != null) {
                return ClientUtils.getSprite(sideTexture);
            }
        }
        return null;
    }

    @Override
    public @Nullable TextureAtlasSprite getParticleReplacement(Block object) {
        if (object.defaultBlockState().isAir()) {
            return null;
        }
        return ClientUtils.mc().getBlockRenderer().getBlockModelShaper().getParticleIcon(object.defaultBlockState());
    }
}
