package com.chen1335.immersiveMechanical.client;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallbacks;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import com.chen1335.immersiveMechanical.API.objects.IMRegistries;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.models.callbacks.CoilCallbacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.EndPointCallBacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.FlyWheelCallBacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.GreenHouseCallbacks;
import com.chen1335.immersiveMechanical.client.models.callbacks.entity.LandmineCallBacks;
import com.chen1335.immersiveMechanical.client.render.entity.LandmineRender;
import com.chen1335.immersiveMechanical.client.render.tile.BearingRender;
import com.chen1335.immersiveMechanical.client.render.tile.FlyWheelCoilRender;
import com.chen1335.immersiveMechanical.client.render.tile.FlyWheelRender;
import com.chen1335.immersiveMechanical.client.render.tile.GreenHouseRender;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlywheelMaterial;
import com.chen1335.immersiveMechanical.config.IMServerConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Comparator;


public class IMClient {
    public static int TICKED = 0;

    public static void init() {
        IEOBJCallbacks.register(rl("green_house"), GreenHouseCallbacks.INSTANCE);
        IEOBJCallbacks.register(rl("coil"), CoilCallbacks.INSTANCE);
        IEOBJCallbacks.register(rl("flywheel"), FlyWheelCallBacks.INSTANCE);
        IEOBJCallbacks.register(rl("end_point"), EndPointCallBacks.INSTANCE);
        IEOBJCallbacks.register(rl("landmine"), LandmineCallBacks.INSTANCE);
    }

    static {
        IEApi.renderCacheClearers.add(GreenHouseRender::reset);
        IEApi.renderCacheClearers.add(FlyWheelCoilRender::reset);
        IEApi.renderCacheClearers.add(FlyWheelRender::reset);
        IEApi.renderCacheClearers.add(BearingRender::reset);
        IEApi.renderCacheClearers.add(LandmineRender::reset);
    }

    private static ResourceLocation rl(String s) {
        return ImmersiveMechanical.id(s);
    }

    public static void setup() {
        if (ManualHelper.ADD_CONFIG_GETTER.isInitialized()) {
            ManualHelper.addConfigGetter(s -> {
                if (s.startsWith(ImmersiveMechanical.MODID)) {
                    s = s.replace("immersive_mechanical.", "");
                    UnmodifiableConfig actualCfg = IMServerConfig.CONFIG_SPEC.getValues();
                    if (actualCfg.get(s) instanceof ModConfigSpec.ConfigValue<?> value)
                        return value.get();
                    else
                        return null;
                }
                return null;
            });
        }

        ManualHelper.DYNAMIC_TABLES.put("flywheel_material", () -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                return level.registryAccess().lookupOrThrow(IMRegistries.FLYWHEEL_MATERIAL).listElements()
                        .sorted(Comparator.<Holder.Reference<FlywheelMaterial>>comparingInt(r -> r.value().maxEnergyStored()).reversed())
                        .map(r -> new Component[]{r.value().manualBlock().value().getName(), Component.literal(r.value().maxEnergyStored() + " IF")})
                        .toArray(Component[][]::new);
            }
            return new Component[][]{};
        });
    }
}
