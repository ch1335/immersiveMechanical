package com.chen1335.immersiveMechanical.client.test;

import net.minecraft.client.DeltaTracker;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class TEST {
    public static void RightClickItem(PlayerInteractEvent.RightClickItem event) {
//            if (!event.getEntity().level().isClientSide) {
//                return;
//            }
//            Minecraft minecraft = Minecraft.getInstance();
//            ResourceLocation resourcelocation = ImmersiveMechanical.id("shaders/post/heat_burn.json");
//            if (entityEffect != null) {
//                entityEffect.close();
//            }
//            entityEffect = null;
//            if (event.getItemStack().is(Items.DIAMOND)) {
//                return;
//            }
//            try {
//                entityEffect = new PostChain(
//                        minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getMainRenderTarget(), resourcelocation
//                );
//                entityEffect.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
//
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

    }


    public static void RenderEffect(DeltaTracker deltaTracker) {
//            if (entityEffect != null) {
//                Minecraft minecraft = Minecraft.getInstance();
//                entityEffect.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
//                entityEffect.process(deltaTracker.getGameTimeDeltaTicks());
//            }

    }
}
