package com.chen1335.immersiveMechanical.client;

import com.chen1335.immersiveMechanical.API.objects.IMBlockEntityTypes;
import com.chen1335.immersiveMechanical.API.objects.IMMenuTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.gui.GreenHouseScreen;
import com.chen1335.immersiveMechanical.client.gui.LaserTurretScreen;
import com.chen1335.immersiveMechanical.client.render.tile.GreenHouseRender;
import com.chen1335.immersiveMechanical.client.render.tile.LaserTurretRender;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientEventHandler {
    @EventBusSubscriber(value = {Dist.CLIENT}, modid = ImmersiveMechanical.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class MOD {
        @SubscribeEvent
        public static void RegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(IMMenuTypes.GREEN_HOUSE.getType(), GreenHouseScreen::new);
            event.register(IMMenuTypes.LASER_TURRET.getType(), LaserTurretScreen::new);
        }

        @SubscribeEvent
        public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(IMMultiblockLogic.GREEN_HOUSE.masterBE().get(), GreenHouseRender::new);
            event.registerBlockEntityRenderer(IMBlockEntityTypes.TURRET_LASER.master(), LaserTurretRender::new);
        }

        private static final List<ModelResourceLocation> MODELS = new ArrayList<>();

        @SubscribeEvent
        public static void registerModels(ModelEvent.RegisterAdditional event) {
            event.register(LaserTurretRender.MODEL_RESOURCE_LOCATION);
        }
    }
}
