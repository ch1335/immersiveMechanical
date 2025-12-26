package com.chen1335.immersiveMechanical.common.compat.jei;

import blusunrize.immersiveengineering.common.util.compat.jei.JEIRecipeTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class IMJei implements IModPlugin {
    private static final ResourceLocation UID = ImmersiveMechanical.id("main");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        registration.addRecipeCatalyst(IMMultiblockLogic.GREEN_HOUSE.iconStack(), JEIRecipeTypes.CLOCHE);
        registration.addRecipeCatalyst(IMMultiblockLogic.GREEN_HOUSE.iconStack(), JEIRecipeTypes.CLOCHE_FERTILIZER);
    }
}
