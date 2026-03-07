package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.common.gui.sync.GenericDataSerializers;
import com.chen1335.immersiveMechanical.common.gui.IndustrialFurnacesMenu;
import com.chen1335.immersiveMechanical.common.gui.sync.IMGenericDataSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenericDataSerializers.class)
public abstract class GenericDataSerializersMixin {
    @Shadow
    private static <T> GenericDataSerializers.DataSerializer<T> register(StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        return null;
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit(CallbackInfo ci) {
        IMGenericDataSerializers.INDUSTRIAL_FURNACES_PROCESS_SLOTS = register(IndustrialFurnacesMenu.SlotProgress.STREAM_CODEC.apply(ByteBufCodecs.list()));
    }
}
