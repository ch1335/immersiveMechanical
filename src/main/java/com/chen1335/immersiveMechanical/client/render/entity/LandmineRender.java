package com.chen1335.immersiveMechanical.client.render.entity;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.utils.client.ModelDataUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.utils.RenderUtils;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.models.callbacks.LandmineCallBacks;
import com.chen1335.immersiveMechanical.common.entities.Landmine;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class LandmineRender extends EntityRenderer<Landmine> {
    public static final ModelResourceLocation MODEL_RESOURCE_LOCATION = new ModelResourceLocation(ImmersiveMechanical.id("dynamic/landmine"), "standalone");

    private static BakedModel MODEL = null;
    private final EntityRenderDispatcher entityRenderDispatcher;

    public LandmineRender(EntityRendererProvider.Context context) {
        super(context);
        entityRenderDispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(Landmine landmine, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (MODEL == null) {
            MODEL = Minecraft.getInstance().getModelManager().getModel(MODEL_RESOURCE_LOCATION);
        }

        List<BakedQuad> quads = MODEL.getQuads(null, null, ApiUtils.RANDOM_SOURCE, ModelDataUtils.single(LandmineCallBacks.getProperty(), landmine.getDisguise()), RenderType.CUTOUT);

        if (entityRenderDispatcher.shouldRenderHitBoxes()) {
            AABB aabb = landmine.getFuseAABB().move(-landmine.getX(), -landmine.getY(), -landmine.getZ());
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), aabb, 1F, 0F, 0F, 1.0F);
        }

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutout());
        int col = ClientUtils.mc().getBlockColors().getColor(landmine.getDisguise(), landmine.level(), landmine.blockPosition(), 0);
        RenderUtils.renderModelTESRFast(quads, buffer, poseStack, col, packedLight, OverlayTexture.NO_OVERLAY);
    }

    public static void reset() {
        MODEL = null;
    }

    @Override
    public ResourceLocation getTextureLocation(Landmine entity) {
        return MissingTextureAtlasSprite.getLocation();
    }
}
