package com.chen1335.immersiveMechanical.compat.jei.categories;

import blusunrize.immersiveengineering.common.util.Utils;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.compat.jei.IMJei;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

public class IndustrialFurnaceCategory implements IRecipeCategory<RecipeHolder<IndustrialFurnaceRecipe>> {

    private final IDrawable icon;

    private static final Component TITLE = Component.translatable("gui.jei.category.smelting");
    public static final ResourceLocation ICON = ImmersiveMechanical.id("textures/gui/container/industrial_furnaces_jei.png");
    private final IDrawableStatic background;


    public IndustrialFurnaceCategory(IJeiHelpers jeiHelpers) {
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        this.background = guiHelper.drawableBuilder(ICON, 0, 0, 58, 20).setTextureSize(58, 20).build();
        this.icon = guiHelper.createDrawableItemStack(IMMultiblocks.INDUSTRIAL_FURNACES.registration().iconStack());
    }

    @Override
    public RecipeType<RecipeHolder<IndustrialFurnaceRecipe>> getRecipeType() {
        return IMJei.INDUSTRIAL_FURNACE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<IndustrialFurnaceRecipe> holder, IFocusGroup iFocusGroup) {
        IndustrialFurnaceRecipe recipe = holder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 2).addIngredients(recipe.input());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 40, 2).addItemStack(recipe.output().get());
    }

    @Override
    public int getWidth() {
        return 58;
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public void draw(RecipeHolder<IndustrialFurnaceRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<IndustrialFurnaceRecipe> holder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        IndustrialFurnaceRecipe recipe = holder.value();
        if (mouseX >= 21 && mouseX <= 36 && mouseY >= 2 && mouseY <= 17) {
            float time = recipe.getTotalProcessTime();
            float energy = recipe.getTotalProcessEnergy() / time;
            Utils.formatDouble(energy, "#.##");
            tooltip.add(Component.translatable("desc.immersiveengineering.info.ift", Utils.formatDouble(energy, "#.##")));
            tooltip.add(Component.translatable("desc.immersiveengineering.info.seconds", Utils.formatDouble(time / 20, "#.##")));
        }
    }
}
