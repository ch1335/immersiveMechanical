package com.chen1335.immersiveMechanical.client.render.tile;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.client.ieobj.ItemCallback;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.client.models.obj.SpecificIEOBJModel;
import blusunrize.immersiveengineering.client.models.obj.callback.item.DrillCallbacks;
import blusunrize.immersiveengineering.client.render.tile.IEMultiblockRenderer;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.smallMiningMachine.SmallMiningMachineLogic;
import com.chen1335.immersiveMechanical.mixins.immersive_mechanical.client.IEOBJItemRendererInvoker;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.util.Cast;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public class SmallMiningMachineRender extends IEMultiblockRenderer<SmallMiningMachineLogic.State> {
    public static final ModelResourceLocation MODEL_RESOURCE_LOCATION = new ModelResourceLocation(IEApi.ieLoc("item/drill"), "inventory");

    private final IEOBJItemRendererInvoker itemRendererInvoker;

    public SmallMiningMachineRender(BlockEntityRendererProvider.Context context) {
        itemRendererInvoker = (IEOBJItemRendererInvoker) ItemCallback.DYNAMIC_IEOBJ_RENDERER.get();

    }

    @Override
    public void render(@NotNull IMultiblockContext<SmallMiningMachineLogic.State> ctx, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(ctx.getState().drill, null, null, 0);
        SpecificIEOBJModel<DrillCallbacks.Key> drillModel = Cast.cast(model);
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1, 0.5);


        SmallMiningMachineLogic.State state = ctx.getState();
        matrixStack.mulPose(Axis.ZP.rotationDegrees(-90));
        matrixStack.scale(1.8F, 1.8F, 1.8F);
        List<String> toRenderParts = List.of(
                "drill_head",
                "upgrade_damage0"
        );
        matrixStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, state.angleO, state.angle)));
        itemRendererInvoker.im$renderQuadsForGroups(toRenderParts, drillModel, (DrillCallbacks) drillModel.getCallback(), ctx.getState().drill, matrixStack, bufferIn, new HashSet<>(toRenderParts), combinedLightIn, combinedOverlayIn);
        matrixStack.popPose();
    }
}
