package com.chen1335.immersiveMechanical.compat.jei.categories;

import blusunrize.immersiveengineering.api.energy.GeneratorFuel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IEMultiblocks;
import com.chen1335.immersiveMechanical.compat.jei.IMJei;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GeneratorFuelCategory implements IRecipeCategory<RecipeHolder<GeneratorFuel>> {
    private final IDrawable icon;

    public GeneratorFuelCategory(IJeiHelpers jeiHelpers) {
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        this.icon = guiHelper.createDrawableItemStack(IEMultiblocks.DIESEL_GENERATOR.getBlock().asItem().getDefaultInstance());
    }

    @Override
    public RecipeType<RecipeHolder<GeneratorFuel>> getRecipeType() {
        return IMJei.GENERATOR_FUEL;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("immersive_mechanical.jei.title.generator_fuel");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<GeneratorFuel> holder, IFocusGroup iFocusGroup) {
        GeneratorFuel recipe = holder.value();
        List<FluidStack> list = recipe.getFluids().stream().map(fluid -> new FluidStack(fluid, 1000)).toList();
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 2).addIngredients(NeoForgeTypes.FLUID_STACK, list).addRichTooltipCallback((iRecipeSlotView, iTooltipBuilder) -> {
            iTooltipBuilder.add(Component.literal("1000 mB").withStyle(ChatFormatting.GRAY));
        });


    }

    @Override
    public void draw(RecipeHolder<GeneratorFuel> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        int y = 22;
        guiGraphics.drawString(font, Component.translatable("immersive_mechanical.jei.burm_time", String.format("%.1f", (float) recipe.value().getBurnTime() / 20)), 0, y, 0, false);
        guiGraphics.drawString(font, Component.translatable("immersive_mechanical.jei.expected_output", recipe.value().getBurnTime() * 4096), 0, y+10, 0, false);
    }

}
