package com.chen1335.immersiveMechanical.compat.jei.categories;

import blusunrize.immersiveengineering.common.util.compat.jei.JEIHelper;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.compat.jei.IMJei;
import com.chen1335.immersiveMechanical.config.IMServerConfig;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import com.chen1335.immersiveMechanical.recipe.PyrolyseOvenRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PyrolyseOvenCategory implements IRecipeCategory<RecipeHolder<PyrolyseOvenRecipe>> {
    public static final ResourceLocation ICON = ImmersiveMechanical.id("textures/gui/jei/pyrolyse_oven_jei.png");
    private static final Component TITLE = Component.translatable("block.immersive_mechanical.pyrolyse_oven");

    private final IDrawable icon;
    private final IDrawableStatic background;

    public PyrolyseOvenCategory(IJeiHelpers jeiHelpers) {
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        this.background = guiHelper.drawableBuilder(ICON, 0, 0, 123, 60).setTextureSize(123, 60).build();
        this.icon = guiHelper.createDrawableItemStack(IMMultiblocks.PYROLYSE_OVEN.registration().iconStack());
    }

    @Override
    public RecipeType<RecipeHolder<PyrolyseOvenRecipe>> getRecipeType() {
        return IMJei.PYROLYSE_OVEN;
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
    public int getWidth() {
        return 128;
    }

    @Override
    public int getHeight() {
        return 64;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<PyrolyseOvenRecipe> holder, IFocusGroup focuses) {
        PyrolyseOvenRecipe recipe = holder.value();
        int batchSize = recipe.getInput().getCount() * IMServerConfig.MACHINES.pyrolyse_oven_parallel_multiplier.get();

        builder.addSlot(RecipeIngredientRole.INPUT, 4, 15)
                .addItemStacks(recipe.getInput().getMatchingStackList())
                .addRichTooltipCallback((slot, tooltip) -> tooltip.add(
                        Component.translatable("desc.immersiveengineering.info.batched", batchSize).withStyle(ChatFormatting.GOLD)
                ));

        if (!recipe.getOutput().get().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 15).addItemStack(recipe.getOutput().get());
        }

        FluidStack fluidOutput = recipe.getFluidOutput();
        if (!fluidOutput.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 7)
                    .setFluidRenderer(48 * FluidType.BUCKET_VOLUME, false, 16, 47)
                    .addIngredient(NeoForgeTypes.FLUID_STACK, fluidOutput)
                    .addRichTooltipCallback(JEIHelper.fluidTooltipCallback);
        }
    }

    @Override
    public void onDisplayedIngredientsUpdate(RecipeHolder<PyrolyseOvenRecipe> recipe, List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
        long now = System.currentTimeMillis();
        long index = now / 1000L % 100000L;
        int batchSize = recipe.value().getInput().getCount() * IMServerConfig.MACHINES.pyrolyse_oven_parallel_multiplier.get();
        int qty = 1 + (Math.toIntExact(index) % batchSize);

        IRecipeSlotDrawable inputSlot = recipeSlots.getFirst();
        inputSlot.createDisplayOverrides().addItemStacks(inputSlot.getItemStacks().map(stack -> stack.copyWithCount(qty)).toList());

        IRecipeSlotDrawable outputSlot = recipeSlots.get(1);
        outputSlot.createDisplayOverrides().addItemStacks(outputSlot.getItemStacks().map(stack -> stack.copyWithCount(stack.getCount() * qty)).toList());

        if (recipeSlots.size() > 2) {
            IRecipeSlotDrawable tank = recipeSlots.get(2);
            tank.createDisplayOverrides().addFluidStack(recipe.value().getFluidOutput().getFluid(), (long) recipe.value().getFluidOutput().getAmount() * qty);
        }
    }

    @Override
    public void draw(RecipeHolder<PyrolyseOvenRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }
}
