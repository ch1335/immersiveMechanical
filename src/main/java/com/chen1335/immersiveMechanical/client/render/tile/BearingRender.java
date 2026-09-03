package com.chen1335.immersiveMechanical.client.render.tile;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import blusunrize.immersiveengineering.api.utils.client.ModelDataUtils;
import blusunrize.immersiveengineering.client.render.tile.IEMultiblockRenderer;
import blusunrize.immersiveengineering.client.utils.RenderUtils;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.models.callbacks.FlyWheelCallBacks;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.bearing.BearingLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel.FlyWheelLogic;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.List;

public class BearingRender extends IEMultiblockRenderer<BearingLogic.State> {
    public static final ModelResourceLocation MODEL_RESOURCE_LOCATION = new ModelResourceLocation(ImmersiveMechanical.id("dynamic/bearing"), "standalone");

    private static BakedModel MODEL = null;

    public BearingRender(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(@NotNull IMultiblockContext<BearingLogic.State> ctx, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferIn, int packedLight, int combinedOverlayIn) {
        if (MODEL == null) {
            MODEL = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getModelManager().getModel(MODEL_RESOURCE_LOCATION);
        }

        List<BakedQuad> quads = MODEL.getQuads(null, null, ApiUtils.RANDOM_SOURCE, ModelData.EMPTY, RenderType.SOLID);
        poseStack.pushPose();
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.translucent());
        poseStack.translate(0.5F, .5F, 0.5F);

        Direction facing = ctx.getLevel().getOrientation().front();
        float dir = facing == Direction.SOUTH ? Mth.PI : facing == Direction.NORTH ? 0 : facing == Direction.EAST ? -Mth.HALF_PI : Mth.HALF_PI;
        poseStack.mulPose(new Quaternionf().rotateY(dir));

        BearingLogic.State state = ctx.getState();
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, state.getAngleOld(), state.getAngle())));

        RenderUtils.renderModelTESRFast(quads, buffer, poseStack, packedLight, combinedOverlayIn);
        poseStack.popPose();
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull MultiblockBlockEntityMaster<BearingLogic.State> blockEntity) {
        return blockEntity.getHelper().getState().renderBoundingBox.apply(blockEntity);
    }

    public static void reset() {
        MODEL = null;
    }
}
