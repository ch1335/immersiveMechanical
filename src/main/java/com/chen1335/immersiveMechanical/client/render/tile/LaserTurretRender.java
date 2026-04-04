package com.chen1335.immersiveMechanical.client.render.tile;

import blusunrize.immersiveengineering.client.render.tile.IEBlockEntityRenderer;
import blusunrize.immersiveengineering.client.render.tile.TurretRenderer;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.awt.*;

public class LaserTurretRender extends IEBlockEntityRenderer<TurretLaserBlockEntity> {
    public static final ModelResourceLocation MODEL_RESOURCE_LOCATION = new ModelResourceLocation(ImmersiveMechanical.id("dynamic/laser_turret"), "standalone");

    public LaserTurretRender(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(TurretLaserBlockEntity tile, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!tile.getLevelNonnull().hasChunkAt(tile.getBlockPos()))
            return;

        BlockState state = tile.getBlockState();
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getModelManager().getModel(MODEL_RESOURCE_LOCATION);

        poseStack.pushPose();
        poseStack.translate(.5, .5, .5);

        float defaultYaw = 180 - tile.getFacing().toYRot();
        poseStack.mulPose(new Quaternionf()
                .rotateY((Mth.lerp(partialTick, tile.rotationYawOld, tile.rotationYaw) + defaultYaw) * Mth.DEG_TO_RAD)
                .rotateX(-Mth.lerp(partialTick, tile.rotationPitchOld, tile.rotationPitch) * Mth.DEG_TO_RAD)
        );


        RenderSystem.setShaderTexture(0, ImmersiveMechanical.id("textures/misc/laser.png"));

        TurretRenderer.renderModelPart(bufferSource, poseStack, tile.getLevelNonnull(), state, model, tile.getBlockPos(), true, packedLight, "gun");

        ResourceLocation laser = ImmersiveMechanical.id("textures/misc/laser.png");
        if (tile.isActive) {
            poseStack.mulPose(new Quaternionf().rotateX(-90 * Mth.DEG_TO_RAD));
            poseStack.translate(-.5, .75, .35);
            renderBeam(poseStack, bufferSource, laser, partialTick, 0.7F, tile.getLevel().getGameTime(), 0, tile.beamLength-0.3f, Color.WHITE.getRGB(), 0.2F, 0.1F);

        }

        poseStack.popPose();
    }

    public static void renderBeam(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            ResourceLocation beamLocation,
            float partialTick,
            float textureScale,
            long gameTime,
            int yOffset,
            float height,
            int color,
            float beamRadius,
            float glowRadius
    ) {
        float i = yOffset + height;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        float f = (float) Math.floorMod(gameTime, 40) + partialTick;
        float f1 = height < 0 ? f : -f;
        float f2 = Mth.frac(f1 * 0.2F - (float) Mth.floor(f1 * 0.1F));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
        float f3 = 0.0F;
        float f5 = 0.0F;
        float f6 = -beamRadius;
        float f9 = -beamRadius;
        float f12 = -1.0F + f2;
        float f13 = height * textureScale * (0.5F / beamRadius) + f12;
        renderPart(
                poseStack,
                bufferSource.getBuffer(RenderType.beaconBeam(beamLocation, false)),
                color,
                yOffset,
                i,
                0.0F,
                beamRadius,
                beamRadius,
                0.0F,
                f6,
                0.0F,
                0.0F,
                f9,
                0.0F,
                1.0F,
                f13,
                f12
        );
        poseStack.popPose();
        f3 = -glowRadius;
        float f4 = -glowRadius;
        f5 = -glowRadius;
        f6 = -glowRadius;
        f12 = -1.0F + f2;
        f13 = height * textureScale + f12;
        renderPart(
                poseStack,
                bufferSource.getBuffer(RenderType.beaconBeam(beamLocation, true)),
                FastColor.ARGB32.color(32, color),
                yOffset,
                i,
                f3,
                f4,
                glowRadius,
                f5,
                f6,
                glowRadius,
                glowRadius,
                glowRadius,
                0.0F,
                1.0F,
                f13,
                f12
        );
        poseStack.popPose();
    }

    public static void renderPart(
            PoseStack poseStack,
            VertexConsumer consumer,
            int color,
            int minY,
            float maxY,
            float x1,
            float z1,
            float x2,
            float z2,
            float x3,
            float z3,
            float x4,
            float z4,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        PoseStack.Pose posestack$pose = poseStack.last();
        renderQuad(
                posestack$pose, consumer, color, minY, maxY, x1, z1, x2, z2, minU, maxU, minV, maxV
        );
        renderQuad(
                posestack$pose, consumer, color, minY, maxY, x4, z4, x3, z3, minU, maxU, minV, maxV
        );
        renderQuad(
                posestack$pose, consumer, color, minY, maxY, x2, z2, x4, z4, minU, maxU, minV, maxV
        );
        renderQuad(
                posestack$pose, consumer, color, minY, maxY, x3, z3, x1, z1, minU, maxU, minV, maxV
        );
    }

    private static void renderQuad(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            int color,
            int minY,
            float maxY,
            float minX,
            float minZ,
            float maxX,
            float maxZ,
            float minU,
            float maxU,
            float minV,
            float maxV
    ) {
        addVertex(pose, consumer, color, maxY, minX, minZ, maxU, minV);
        addVertex(pose, consumer, color, minY, minX, minZ, maxU, maxV);
        addVertex(pose, consumer, color, minY, maxX, maxZ, minU, maxV);
        addVertex(pose, consumer, color, maxY, maxX, maxZ, minU, minV);
    }

    private static void addVertex(
            PoseStack.Pose pose, VertexConsumer consumer, int color, float y, float x, float z, float u, float v
    ) {
        consumer.addVertex(pose, x, (float)y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(TurretLaserBlockEntity blockEntity) {
        if (blockEntity.isActive) {
            return AABB.INFINITE;
        }
        return super.getRenderBoundingBox(blockEntity);
    }
}
