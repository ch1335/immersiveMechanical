package com.chen1335.immersiveMechanical.client.models.callbacks;

import blusunrize.immersiveengineering.api.client.ieobj.BlockCallback;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.client.ClientUtils;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.EndPointLogic;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EndPointCallBacks implements BlockCallback<EndPointLogic.ConnectionType> {
    public static final EndPointCallBacks INSTANCE = new EndPointCallBacks();

    @Override
    public EndPointLogic.ConnectionType extractKey(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState blockState, BlockEntity blockEntity) {
        if (level.getBlockEntity(pos) instanceof IMultiblockBE<?> be && be.getHelper().getState() instanceof EndPointLogic.State state) {
            return state.connectionType;
        }
        return getDefaultKey();
    }

    @Override
    public EndPointLogic.ConnectionType getDefaultKey() {
        return EndPointLogic.ConnectionType.INPUT;
    }


    @Override
    public boolean dependsOnLayer() {
        return true;
    }

    @Override
    public @Nullable TextureAtlasSprite getTextureReplacement(EndPointLogic.ConnectionType connectionType, String group, String material) {
        if (material.equals("connection")) {
            ResourceLocation sideTexture = null;
            if (connectionType == EndPointLogic.ConnectionType.INPUT) {
                sideTexture = ResourceLocation.parse("immersive_mechanical:block/metal_multiblock/flywheel/interface_input");
            } else if (connectionType == EndPointLogic.ConnectionType.OUTPUT) {
                sideTexture = ResourceLocation.parse("immersive_mechanical:block/metal_multiblock/flywheel/interface_output");
            }
            if (sideTexture != null) {
                return ClientUtils.getSprite(sideTexture);
            }
        }
        return null;
    }
}
