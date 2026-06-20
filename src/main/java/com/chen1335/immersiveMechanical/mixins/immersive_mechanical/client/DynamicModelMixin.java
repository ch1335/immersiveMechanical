package com.chen1335.immersiveMechanical.mixins.immersive_mechanical.client;

import blusunrize.immersiveengineering.client.render.tile.DynamicModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(DynamicModel.class)
//FUCK HARD CODE NAMESPACE
public class DynamicModelMixin {
    @WrapOperation(method = "<init>",at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/IEApi;ieLoc(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation modifyId(String path, Operation<ResourceLocation> original){
        if (path.contains(":")) {
            return null;
        }
        return original.call(path);
    }


    @WrapOperation(method = "<init>", at = @At(value = "NEW", target = "(Lnet/minecraft/resources/ResourceLocation;Ljava/lang/String;)Lnet/minecraft/client/resources/model/ModelResourceLocation;"))
    private ModelResourceLocation init(ResourceLocation id, String variant, Operation<ModelResourceLocation> original, @Local(argsOnly = true) String desc) {
        if (desc.contains(":")) {
            return new ModelResourceLocation(ResourceLocation.parse(desc).withPrefix("dynamic/"), "standalone");
        }
        return original.call(id, variant);
    }
}
