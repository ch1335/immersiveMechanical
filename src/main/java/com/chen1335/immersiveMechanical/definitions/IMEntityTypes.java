package com.chen1335.immersiveMechanical.definitions;

import com.chen1335.immersiveMechanical.client.render.entity.LandmineRender;
import com.chen1335.immersiveMechanical.common.entities.Landmine;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

public class IMEntityTypes {
    public static final EntityEntry<Landmine> LANDMINE = REGISTRATE.entity("landmine", Landmine::new, MobCategory.MISC)
            .properties(builder -> builder.sized(0.75F, 0.1875F))
            .renderer(() -> LandmineRender::new)
            .register();

    public static void init() {

    }
}
