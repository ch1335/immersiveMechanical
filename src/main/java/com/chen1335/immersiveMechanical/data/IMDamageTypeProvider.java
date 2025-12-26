package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.API.objects.IMDamageTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

public class IMDamageTypeProvider {
    public static void bootstrap(BootstrapContext<DamageType> ctx) {
        ctx.register(IMDamageTypes.LASER, new DamageType("laser", 0.1F));
    }
}
