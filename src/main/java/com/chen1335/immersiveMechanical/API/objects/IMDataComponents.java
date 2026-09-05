package com.chen1335.immersiveMechanical.API.objects;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.items.dataComponents.Disguise;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPE = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ImmersiveMechanical.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Disguise>> DISGUISE = DATA_COMPONENT_TYPE.registerComponentType("disguise", builder -> builder.persistent(Disguise.CODEC));
}
