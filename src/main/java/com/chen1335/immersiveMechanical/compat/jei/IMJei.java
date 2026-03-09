package com.chen1335.immersiveMechanical.compat.jei;

import blusunrize.immersiveengineering.common.util.compat.jei.JEIRecipeTypes;
import com.chen1335.immersiveMechanical.API.objects.IMRecipe;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import com.chen1335.immersiveMechanical.compat.jei.categories.IndustrialFurnaceCategory;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class IMJei implements IModPlugin {
    private static final ResourceLocation ID = ImmersiveMechanical.id("jei");

    public static final RecipeType<RecipeHolder<IndustrialFurnaceRecipe>> INDUSTRIAL_FURNACE = RecipeType.createRecipeHolderType(IMRecipe.Types.INDUSTRIAL_FURNACE.type().getId());

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(new IndustrialFurnaceCategory(jeiHelpers));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(IMMultiblockLogic.GREEN_HOUSE.iconStack(), JEIRecipeTypes.CLOCHE);
        registration.addRecipeCatalyst(IMMultiblockLogic.GREEN_HOUSE.iconStack(), JEIRecipeTypes.CLOCHE_FERTILIZER);
        registration.addRecipeCatalyst(IMMultiblockLogic.INDUSTRIAL_FURNACES.iconStack(), INDUSTRIAL_FURNACE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
            registration.addRecipes(INDUSTRIAL_FURNACE, recipeManager.getAllRecipesFor(IMRecipe.Types.INDUSTRIAL_FURNACE.get()));
        }
    }
}
