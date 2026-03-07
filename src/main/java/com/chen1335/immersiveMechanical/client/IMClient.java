package com.chen1335.immersiveMechanical.client;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallbacks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.models.callbacks.CoilCallbacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.GreenHouseCallbacks;
import com.chen1335.immersiveMechanical.client.render.tile.GreenHouseRender;
import net.minecraft.resources.ResourceLocation;


public class IMClient {

    public static void init() {
        IEOBJCallbacks.register(rl("green_house"), GreenHouseCallbacks.INSTANCE);
        IEOBJCallbacks.register(rl("coil"), CoilCallbacks.INSTANCE);
    }


    static {
        IEApi.renderCacheClearers.add(GreenHouseRender::reset);
    }

    private static ResourceLocation rl(String s) {
        return ImmersiveMechanical.id(s);
    }

}
