package com.chen1335.immersiveMechanical.client;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallbacks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.models.callbacks.CoilCallbacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.EndPointCallBacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.FlyWheelCallBacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.GreenHouseCallbacks;
import com.chen1335.immersiveMechanical.client.render.tile.FlyWheelCoilRender;
import com.chen1335.immersiveMechanical.client.render.tile.FlyWheelRender;
import com.chen1335.immersiveMechanical.client.render.tile.GreenHouseRender;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.EndPointLogic;
import net.minecraft.resources.ResourceLocation;


public class IMClient {
    public static int TICKED = 0;

    public static void init() {
        IEOBJCallbacks.register(rl("green_house"), GreenHouseCallbacks.INSTANCE);
        IEOBJCallbacks.register(rl("coil"), CoilCallbacks.INSTANCE);
        IEOBJCallbacks.register(rl("flywheel"), FlyWheelCallBacks.INSTANCE);
        IEOBJCallbacks.register(rl("end_point"), EndPointCallBacks.INSTANCE);
    }

    static {
        IEApi.renderCacheClearers.add(GreenHouseRender::reset);
        IEApi.renderCacheClearers.add(FlyWheelCoilRender::reset);
        IEApi.renderCacheClearers.add(FlyWheelRender::reset);
    }

    private static ResourceLocation rl(String s) {
        return ImmersiveMechanical.id(s);
    }

}
