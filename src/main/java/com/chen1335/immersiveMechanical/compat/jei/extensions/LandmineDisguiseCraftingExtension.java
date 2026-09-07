package com.chen1335.immersiveMechanical.compat.jei.extensions;

import com.chen1335.immersiveMechanical.API.objects.IMDataComponents;
import com.chen1335.immersiveMechanical.common.items.dataComponents.Disguise;
import com.chen1335.immersiveMechanical.definitions.IMItems;
import com.chen1335.immersiveMechanical.recipe.LandmineDisguiseRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class LandmineDisguiseCraftingExtension implements ICraftingCategoryExtension<LandmineDisguiseRecipe> {
    private final List<ItemStack> disguiseBlocks;
    private final List<ItemStack> disguisedLandmines;

    public LandmineDisguiseCraftingExtension(Iterable<ItemStack> itemStacks) {
        disguiseBlocks = new ArrayList<>();
        disguisedLandmines = new ArrayList<>();

        for (ItemStack stack : itemStacks) {
            if (!(stack.getItem() instanceof BlockItem blockItem) || blockItem.getBlock().defaultBlockState().isAir()) {
                continue;
            }

            ItemStack disguiseBlock = stack.copyWithCount(1);
            ItemStack disguisedLandmine = IMItems.LANDMINE.get().getDefaultInstance();
            disguisedLandmine.set(IMDataComponents.DISGUISE, new Disguise(blockItem.getBlock().defaultBlockState()));
            disguisedLandmine.setCount(8);
            disguiseBlocks.add(disguiseBlock);
            disguisedLandmines.add(disguisedLandmine);
        }
    }

    @Override
    public void setRecipe(RecipeHolder<LandmineDisguiseRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        List<List<ItemStack>> inputs = new ArrayList<>(9);
        ItemStack landmine = IMItems.LANDMINE.get().getDefaultInstance();
        for (int slot = 0; slot < 9; slot++) {
            inputs.add(slot == 4 ? disguiseBlocks : List.of(landmine));
        }

        List<IRecipeSlotBuilder> inputSlots = craftingGridHelper.createAndSetInputs(builder, inputs, 3, 3);
        IRecipeSlotBuilder outputSlot = craftingGridHelper.createAndSetOutputs(builder, disguisedLandmines);
        builder.createFocusLink(inputSlots.get(4), outputSlot);
    }

    @Override
    public int getWidth(RecipeHolder<LandmineDisguiseRecipe> recipeHolder) {
        return 3;
    }

    @Override
    public int getHeight(RecipeHolder<LandmineDisguiseRecipe> recipeHolder) {
        return 3;
    }
}
