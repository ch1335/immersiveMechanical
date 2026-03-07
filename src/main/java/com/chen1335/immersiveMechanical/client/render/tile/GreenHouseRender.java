package com.chen1335.immersiveMechanical.client.render.tile;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.render.tile.IEMultiblockRenderer;
import blusunrize.immersiveengineering.client.utils.ModelUtils;
import blusunrize.immersiveengineering.client.utils.RenderUtils;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.util.Utils;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class GreenHouseRender extends IEMultiblockRenderer<GreenHouseLogic.State> {
    private static final Map<Item, TextureAtlasSprite> ATLAS_SPRITE_MAP = new HashMap<>();
    private static final Map<BlockState, List<BakedQuad>> plantQuads = new HashMap<>();

    public GreenHouseRender(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(@NotNull IMultiblockContext<GreenHouseLogic.State> ctx, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        GreenHouseLogic.State state = ctx.getState();
        final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BlockState blockState = IEBlocks.MetalDevices.ELECTRIC_LANTERN.defaultBlockState();
        if (state.energyStorage.getEnergyStored() > 0) {
            blockState = blockState.setValue(IEProperties.ACTIVE, true);
        }
        matrixStack.pushPose();

        matrixStack.translate(0.5, 0, 0.5);
        Direction facing = ctx.getLevel().getOrientation().front();
        float dir = facing == Direction.SOUTH ? Mth.PI : facing == Direction.NORTH ? 0 : facing == Direction.EAST ? -Mth.HALF_PI : Mth.HALF_PI;
        matrixStack.mulPose(new Quaternionf().rotateY(dir));
        matrixStack.translate(-0.5, 0, -0.5);

        matrixStack.pushPose();
        matrixStack.translate(0, 3.98, 0);
        blockRenderer.renderSingleBlock(blockState, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn, ModelData.EMPTY, RenderType.solid());

        matrixStack.popPose();

        for (int i = 0; i < state.processUnits.size(); i++) {
            GreenHouseLogic.ProcessUnit processUnit = state.processUnits.get(i);
            renderSoilAndCrop(ctx, processUnit, matrixStack, bufferIn, blockRenderer, combinedLightIn, combinedOverlayIn);
        }
        matrixStack.popPose();
    }

    private static void renderSoilAndCrop(IMultiblockContext<GreenHouseLogic.State> ctx, GreenHouseLogic.ProcessUnit processUnit, PoseStack matrixStack, MultiBufferSource bufferIn, BlockRenderDispatcher blockRenderer, int combinedLightIn, int combinedOverlayIn) {
        GreenHouseLogic.State state = ctx.getState();
        float x = ((float) (processUnit.id / 4)) - 1.5f;
        float z = (processUnit.id % 4) - 1.5F;
        ItemStack soil = processUnit.soil;
        ItemStack seed = processUnit.seed;

        if (!soil.isEmpty()) {
            TextureAtlasSprite sprite = ATLAS_SPRITE_MAP.computeIfAbsent(soil.getItem(), item -> {
                ResourceLocation rl = getSoilTexture(soil);
                return ClientUtils.getSprite(Objects.requireNonNullElseGet(rl, MissingTextureAtlasSprite::getLocation));
            });

            ResourceLocation resourceLocation = sprite.atlasLocation();


            matrixStack.pushPose();
            matrixStack.translate(x, 1.01, z);

            VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.translucent());
            RenderSystem.setShaderTexture(0, resourceLocation);
            RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
            vertexConsumer.addVertex(matrixStack.last(), 0, 0, 1).setColor(1F, 1F, 1F, 1F).setUv(sprite.getU0(), sprite.getV1()).setOverlay(combinedOverlayIn).setNormal(0, 1, 0).setLight(combinedLightIn);
            vertexConsumer.addVertex(matrixStack.last(), 1, 0, 1).setColor(1F, 1F, 1F, 1F).setUv(sprite.getU1(), sprite.getV1()).setOverlay(combinedOverlayIn).setNormal(0, 1, 0).setLight(combinedLightIn);
            vertexConsumer.addVertex(matrixStack.last(), 1, 0, 0).setColor(1F, 1F, 1F, 1F).setUv(sprite.getU1(), sprite.getV0()).setOverlay(combinedOverlayIn).setNormal(0, 1, 0).setLight(combinedLightIn);
            vertexConsumer.addVertex(matrixStack.last(), 0, 0, 0).setColor(1F, 1F, 1F, 1F).setUv(sprite.getU0(), sprite.getV0()).setOverlay(combinedOverlayIn).setNormal(0, 1, 0).setLight(combinedLightIn);
            matrixStack.popPose();

        }

        ClocheRecipe recipe = processUnit.cachedRecipe.get();
        if (recipe != null) {
            RenderType type = Sheets.cutoutBlockSheet();
            VertexConsumer baseBuilder = bufferIn.getBuffer(type);
            matrixStack.pushPose();
            matrixStack.translate(x, 1.0625, z);

            float growth = Mth.clamp(processUnit.growth / recipe.getTime(seed, soil), 0, 1);
            float scale = recipe.renderFunction.getScale(seed, growth);
            matrixStack.translate((1 - scale) / 2, 0, (1 - scale) / 2);
            matrixStack.scale(scale, scale, scale);
            Collection<Pair<BlockState, Transformation>> blocks = recipe.renderFunction.getBlocks(seed, growth);

            for (Pair<BlockState, Transformation> block : blocks) {
                BlockState blockState = block.getFirst();
                List<BakedQuad> plantQuadList = plantQuads.get(blockState);
                if (plantQuadList == null) {
                    BakedModel plantModel = blockRenderer.getBlockModel(blockState);
                    plantQuadList = new ArrayList<>(plantModel.getQuads(blockState, null, ApiUtils.RANDOM_SOURCE, ModelData.EMPTY, null));
                    for (Direction f : Direction.values())
                        plantQuadList.addAll(plantModel.getQuads(blockState, f, ApiUtils.RANDOM_SOURCE, ModelData.EMPTY, null));
                    plantQuads.put(blockState, plantQuadList);
                }


                int col = ClientUtils.mc().getBlockColors().getColor(blockState, null, state.masterBlockPose, -1);
                matrixStack.pushTransformation(block.getSecond());
                RenderUtils.renderModelTESRFancy(
                        plantQuadList, baseBuilder, matrixStack, ctx.getLevel().getRawLevel(), state.masterBlockPose, false, col, combinedLightIn
                );
                matrixStack.popPose();
            }

            List<BakedQuad> injectedQuadList = new ArrayList<>();
            Consumer<?> quadInjector = (object) -> {
                if (object instanceof BakedQuad) injectedQuadList.add((BakedQuad) object);
            };
            recipe.renderFunction.injectQuads(seed, growth, quadInjector);
            if (!injectedQuadList.isEmpty())
                RenderUtils.renderModelTESRFancy(
                        injectedQuadList, baseBuilder, matrixStack, ctx.getLevel().getRawLevel(), state.masterBlockPose, false, -1, combinedLightIn
                );

            matrixStack.popPose();
        }
    }

    @Nullable
    private static ResourceLocation getSoilTexture(ItemStack soil) {
        ResourceLocation rl = ClocheRecipe.getSoilTexture(soil);
        if (rl == null) {
            try {
                BlockState state = Utils.getStateFromItemStack(soil);
                if (state != null)
                    rl = ModelUtils.getSideTexture(state, Direction.UP);
            } catch (Exception e) {
                rl = ModelUtils.getSideTexture(soil, Direction.UP);
            }
        }
        if (rl == null && !soil.isEmpty() && Utils.isFluidRelatedItemStack(soil))
            rl = FluidUtil.getFluidContained(soil)
                    .map(fs -> IClientFluidTypeExtensions.of(fs.getFluid()).getStillTexture(fs))
                    .orElse(null);
        return rl;
    }

    public static void reset() {
        ATLAS_SPRITE_MAP.clear();
        plantQuads.clear();
    }
}
