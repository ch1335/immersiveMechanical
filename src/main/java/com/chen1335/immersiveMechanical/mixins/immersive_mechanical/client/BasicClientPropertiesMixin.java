package com.chen1335.immersiveMechanical.mixins.immersive_mechanical.client;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.client.render.tile.DynamicModel;
import blusunrize.immersiveengineering.client.utils.BasicClientProperties;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.chen1335.registrate.IERegistrate;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BasicClientProperties.class)
public class BasicClientPropertiesMixin {
    @Shadow
    @Final
    private static Map<ResourceLocation, DynamicModel> MODELS;

    @Inject(method = "initModels", at = @At("RETURN"))
    private static void initModels(CallbackInfo ci) {
        for (MultiblockHandler.IMultiblock mb : IERegistrate.ALL_MULTIBLOCKS)
            if (mb instanceof IETemplateMultiblock ieMB) {
                MODELS.put(mb.getUniqueName(), new DynamicModel(ieMB.getBlockName().toString()));
            }

    }
}
