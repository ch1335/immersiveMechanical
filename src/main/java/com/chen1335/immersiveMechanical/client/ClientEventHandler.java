package com.chen1335.immersiveMechanical.client;

import com.chen1335.immersiveMechanical.API.objects.IMBlockEntityTypes;
import com.chen1335.immersiveMechanical.API.objects.IMMenuTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.gui.GreenHouseScreen;
import com.chen1335.immersiveMechanical.client.gui.IndustrialFurnacesScreen;
import com.chen1335.immersiveMechanical.client.gui.LaserTurretScreen;
import com.chen1335.immersiveMechanical.client.render.tile.CoilRender;
import com.chen1335.immersiveMechanical.client.render.tile.GreenHouseRender;
import com.chen1335.immersiveMechanical.client.render.tile.LaserTurretRender;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.IMMultiblocks;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.CoilTemplate;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientEventHandler {

    @EventBusSubscriber(value = {Dist.CLIENT}, modid = ImmersiveMechanical.MODID)
    public static class MOD {
        @SubscribeEvent
        public static void RegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(IMMenuTypes.GREEN_HOUSE.getType(), GreenHouseScreen::new);
            event.register(IMMenuTypes.LASER_TURRET.getType(), LaserTurretScreen::new);
            event.register(IMMenuTypes.INDUSTRIAL_FURNACES.getType(), IndustrialFurnacesScreen::new);
        }

        @SubscribeEvent
        public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(IMMultiblockLogic.GREEN_HOUSE.masterBE().get(), GreenHouseRender::new);
            event.registerBlockEntityRenderer(IMBlockEntityTypes.TURRET_LASER.master(), LaserTurretRender::new);
//            for (CoilTemplate value : IMMultiblocks.COILS.values()) {
//                event.registerBlockEntityRenderer(value.getLogic().masterBE().get(), CoilRender::new);
//            }
        }

        @SubscribeEvent
        public static void registerModels(ModelEvent.RegisterAdditional event) {
            event.register(LaserTurretRender.MODEL_RESOURCE_LOCATION);
        }
    }
}
