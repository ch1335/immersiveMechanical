package com.chen1335.immersiveMechanical.client.render.tile;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.client.render.tile.IEMultiblockRenderer;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class CoilRender extends IEMultiblockRenderer<CoilLogic.State> {

    public CoilRender(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(@NotNull IMultiblockContext<CoilLogic.State> ctx, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

    }
}
