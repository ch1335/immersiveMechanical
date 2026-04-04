package com.chen1335.immersiveMechanical.mixins.immersive_mechanical.client;

import blusunrize.immersiveengineering.api.client.ieobj.ItemCallback;
import blusunrize.immersiveengineering.client.models.obj.SpecificIEOBJModel;
import blusunrize.immersiveengineering.client.render.IEOBJItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Set;

@Mixin(IEOBJItemRenderer.class)
public interface IEOBJItemRendererInvoker {
    @Invoker("renderQuadsForGroups")
    <T> void im$renderQuadsForGroups(List<String> groups, SpecificIEOBJModel<T> model, ItemCallback<T> callback,
                                     ItemStack stack, PoseStack matrix, MultiBufferSource buffer,
                                     Set<String> visible, int light, int overlay);
}
