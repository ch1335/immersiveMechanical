package com.chen1335.immersiveMechanical.client.models.callbacks;

import blusunrize.immersiveengineering.api.client.ieobj.BlockCallback;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.client.ClientUtils;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.EndPointLogic;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EndPointCallBacks implements BlockCallback<EndPointCallBacks.Key> {
    public static final EndPointCallBacks INSTANCE = new EndPointCallBacks();

    @Override
    public Key extractKey(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState blockState, BlockEntity blockEntity) {
        if (level.getBlockEntity(pos) instanceof IMultiblockBE<?> be && be.getHelper().getState() instanceof EndPointLogic.State state) {
            return new Key(false, state.connectionType);
        }
        return getDefaultKey();
    }

    @Override
    public Key getDefaultKey() {
        return new Key(true, EndPointLogic.ConnectionType.INPUT);
    }


    @Override
    public boolean dependsOnLayer() {
        return true;
    }

    @Override
    public boolean shouldRenderGroup(Key key, String group, RenderType layer) {
        if ("coil".equals(group)) {
            return key.renderCoil;
        }
        return true;
    }

    @Override
    public @Nullable TextureAtlasSprite getTextureReplacement(Key key, String group, String material) {
        if (material.equals("connection")) {
            ResourceLocation sideTexture = null;
            if (key.connectionType == EndPointLogic.ConnectionType.INPUT) {
                sideTexture = ResourceLocation.parse("immersive_mechanical:block/metal_multiblock/flywheel/interface_input");
            } else if (key.connectionType == EndPointLogic.ConnectionType.OUTPUT) {
                sideTexture = ResourceLocation.parse("immersive_mechanical:block/metal_multiblock/flywheel/interface_output");
            }
            if (sideTexture != null) {
                return ClientUtils.getSprite(sideTexture);
            }
        }
        return null;
    }

    public record Key(boolean renderCoil, EndPointLogic.ConnectionType connectionType) {

    }
}
