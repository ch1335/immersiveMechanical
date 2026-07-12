package com.chen1335.immersiveMechanical.compat.jei;

import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.energy.GeneratorFuel;
import blusunrize.immersiveengineering.api.excavator.MineralMix;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IEMultiblocks;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIRecipeTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.compat.jei.categories.GeneratorFuelCategory;
import com.chen1335.immersiveMechanical.compat.jei.categories.IndustrialFurnaceCategory;
import com.chen1335.immersiveMechanical.compat.jei.categories.MineralMixCategory;
import com.chen1335.immersiveMechanical.compat.jei.categories.PyrolyseOvenCategory;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import com.chen1335.immersiveMechanical.definitions.IMRecipe;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import com.chen1335.immersiveMechanical.recipe.PyrolyseOvenRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class IMJei implements IModPlugin {
    private static final ResourceLocation ID = ImmersiveMechanical.id("jei");

    public static final RecipeType<RecipeHolder<IndustrialFurnaceRecipe>> INDUSTRIAL_FURNACE = RecipeType.createRecipeHolderType(IMRecipe.INDUSTRIAL_FURNACE.getId());

    public static final RecipeType<RecipeHolder<PyrolyseOvenRecipe>> PYROLYSE_OVEN = RecipeType.createRecipeHolderType(IMRecipe.PYROLYSE_OVEN.getId());

    public static final RecipeType<RecipeHolder<GeneratorFuel>> GENERATOR_FUEL = RecipeType.createRecipeHolderType(IERecipeTypes.GENERATOR_FUEL.type().getId());

    public static final RecipeType<RecipeHolder<MineralMix>> MINERAL_MIX = RecipeType.createRecipeHolderType(IERecipeTypes.MINERAL_MIX.type().getId());

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(new IndustrialFurnaceCategory(jeiHelpers));
        registration.addRecipeCategories(new PyrolyseOvenCategory(jeiHelpers));
        registration.addRecipeCategories(new GeneratorFuelCategory(jeiHelpers));
        registration.addRecipeCategories(new MineralMixCategory(jeiHelpers));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        ItemStack greenHouseIconStack = IMMultiblocks.GREEN_HOUSE.registration().iconStack();
        registration.addRecipeCatalyst(greenHouseIconStack, JEIRecipeTypes.CLOCHE);
        registration.addRecipeCatalyst(greenHouseIconStack, JEIRecipeTypes.CLOCHE_FERTILIZER);
        registration.addRecipeCatalyst(IMMultiblocks.INDUSTRIAL_FURNACES.registration().iconStack(), INDUSTRIAL_FURNACE);
        registration.addRecipeCatalyst(IMMultiblocks.PYROLYSE_OVEN_DEMO.getBlock().asItem(), PYROLYSE_OVEN);
        registration.addRecipeCatalyst(IEMultiblocks.DIESEL_GENERATOR.getBlock().asItem(), GENERATOR_FUEL);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
            registration.addRecipes(INDUSTRIAL_FURNACE, recipeManager.getAllRecipesFor(IMRecipe.INDUSTRIAL_FURNACE.get()));
            registration.addRecipes(PYROLYSE_OVEN, recipeManager.getAllRecipesFor(IMRecipe.PYROLYSE_OVEN.get()));
            registration.addRecipes(GENERATOR_FUEL, recipeManager.getAllRecipesFor(IERecipeTypes.GENERATOR_FUEL.get()));
            registration.addRecipes(MINERAL_MIX, recipeManager.getAllRecipesFor(IERecipeTypes.MINERAL_MIX.get()));
        }
    }
}
