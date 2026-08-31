package com.chen1335.immersiveMechanical.API.objects;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlywheelMaterial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = ImmersiveMechanical.MODID)
public class IMRegistries {
    public static final ResourceKey<Registry<FlywheelMaterial>> FLYWHEEL_MATERIAL = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ImmersiveMechanical.MODID, "flywheel_material"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                FLYWHEEL_MATERIAL,
                FlywheelMaterial.CODEC,
                FlywheelMaterial.CODEC
        );
    }
}
