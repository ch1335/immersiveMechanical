package com.chen1335.immersiveMechanical.client;

import com.chen1335.immersiveMechanical.API.objects.IMBlockEntityTypes;
import com.chen1335.immersiveMechanical.API.objects.IMMenuTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.gui.GreenHouseScreen;
import com.chen1335.immersiveMechanical.client.gui.IndustrialFurnacesScreen;
import com.chen1335.immersiveMechanical.client.gui.LaserTurretScreen;
import com.chen1335.immersiveMechanical.client.gui.SmallMiningMachineScreen;
import com.chen1335.immersiveMechanical.client.render.tile.GreenHouseRender;
import com.chen1335.immersiveMechanical.client.render.tile.LaserTurretRender;
import com.chen1335.immersiveMechanical.client.render.tile.SmallMiningMachineRender;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilInfo;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ClientEventHandler {

    @EventBusSubscriber(value = {Dist.CLIENT}, modid = ImmersiveMechanical.MODID)
    public static class MOD {
        @SubscribeEvent
        public static void RegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(IMMenuTypes.GREEN_HOUSE.getType(), GreenHouseScreen::new);
            event.register(IMMenuTypes.LASER_TURRET.getType(), LaserTurretScreen::new);
            event.register(IMMenuTypes.INDUSTRIAL_FURNACES.getType(), IndustrialFurnacesScreen::new);
            event.register(IMMenuTypes.SMALL_MINING_MACHINE.getType(), SmallMiningMachineScreen::new);
        }

        @SubscribeEvent
        public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(IMMultiblockLogic.GREEN_HOUSE.masterBE().get(), GreenHouseRender::new);
            event.registerBlockEntityRenderer(IMBlockEntityTypes.TURRET_LASER.master(), LaserTurretRender::new);
            event.registerBlockEntityRenderer(IMMultiblockLogic.SMALL_MINING_MACHINE.masterBE().get(), SmallMiningMachineRender::new);
        }

        @SubscribeEvent
        public static void registerModels(ModelEvent.RegisterAdditional event) {
            event.register(LaserTurretRender.MODEL_RESOURCE_LOCATION);
        }

        @SubscribeEvent
        public static void ItemTooltipEvent(ItemTooltipEvent event) {
            Block block = Block.byItem(event.getItemStack().getItem());
            CoilInfo coilInfo = CoilInfo.COIL_INFO_MAP.get(block);
            if (coilInfo != null) {
                event.getToolTip().add(Component.translatable("immersive_mechanical.tooltip.coil_info_for_industrial_furnaces"));
                event.getToolTip().add(Component.translatable("immersive_mechanical.tooltip.timeModify", "x" + coilInfo.timeModify()));
                event.getToolTip().add(Component.translatable("immersive_mechanical.tooltip.energyModify", "x" + coilInfo.energyModify()));
            }
        }

        @SubscribeEvent
        public static void ClientTickEvent(ClientTickEvent.Post event) {
            IMClient.TICKED++;
        }
    }
}
