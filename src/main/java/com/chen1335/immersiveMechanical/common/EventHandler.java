package com.chen1335.immersiveMechanical.common;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.network.GreenHouseGrowsPack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class EventHandler {
    @EventBusSubscriber(modid = ImmersiveMechanical.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class Mod {
        @SubscribeEvent
        public static void RegisterPayloadHandlersEvent(RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playToClient(GreenHouseGrowsPack.TYPE, GreenHouseGrowsPack.CODEC, GreenHouseGrowsPack::handle);
        }

    }
}
