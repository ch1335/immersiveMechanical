package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(IEMenuTypes.ArgContainer.class)
public interface ArgContainerInvoker {
    @Invoker("<init>")
    static <S, C extends IEContainerMenu> IEMenuTypes.ArgContainer<S, C> IM$create(DeferredHolder<MenuType<?>, MenuType<C>> type, IEMenuTypes.ArgContainerConstructor<S, C> factory) {
        throw new RuntimeException("accessor not applied");
    }
}
