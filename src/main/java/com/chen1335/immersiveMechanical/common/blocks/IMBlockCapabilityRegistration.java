package com.chen1335.immersiveMechanical.common.blocks;

import blusunrize.immersiveengineering.common.blocks.BlockCapabilityRegistration;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.MultiblockBEType;
import com.chen1335.immersiveMechanical.API.objects.IMBlockEntityTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = ImmersiveMechanical.MODID)
public class IMBlockCapabilityRegistration {
    @SubscribeEvent
    public static void registerBlockCapabilities(RegisterCapabilitiesEvent event) {
        TurretLaserBlockEntity.registerCapabilities(forType(event, IMBlockEntityTypes.TURRET_LASER));
    }


    private static <BE extends BlockEntity & IEBlockInterfaces.IGeneralMultiblock> BlockCapabilityRegistration.BECapabilityRegistrar<BE> forType(
            RegisterCapabilitiesEvent ev, MultiblockBEType<BE> type
    ) {
        return new BlockCapabilityRegistration.BECapabilityRegistrar<>() {
            @Override
            public <C, T> void register(BlockCapability<T, C> capability, ICapabilityProvider<? super BE, C, T> provider) {
                ev.registerBlockEntity(capability, type.dummy(), provider);
                ev.registerBlockEntity(capability, type.master(), provider);
            }
        };
    }
}
