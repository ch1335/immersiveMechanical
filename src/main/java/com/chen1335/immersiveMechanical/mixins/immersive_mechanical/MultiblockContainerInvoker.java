package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = IEMenuTypes.MultiblockContainer.class)
public interface MultiblockContainerInvoker {
    @Invoker("<init>")
    static <S extends IMultiblockState, C extends IEContainerMenu> IEMenuTypes.MultiblockContainer<S, C> IM$create(DeferredHolder<MenuType<?>, MenuType<C>> type, IEMenuTypes.ArgContainerConstructor<IEContainerMenu.MultiblockMenuContext<S>, C> factory) {
        throw new RuntimeException("accessor not applied");
    }
}
