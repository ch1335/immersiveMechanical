package com.chen1335.immersiveMechanical.compat.jei.categories;

import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import blusunrize.immersiveengineering.api.excavator.MineralMix;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.chen1335.immersiveMechanical.compat.jei.IMJei;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class MineralMixCategory implements IRecipeCategory<RecipeHolder<MineralMix>> {
    private final IDrawable icon;

    public MineralMixCategory(IJeiHelpers jeiHelpers) {
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        this.icon = guiHelper.createDrawableItemStack(IEItems.Tools.SURVEY_TOOLS.asItem().getDefaultInstance());
    }

    @Override
    public RecipeType<RecipeHolder<MineralMix>> getRecipeType() {
        return IMJei.MINERAL_MIX;
    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 200;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("immersive_mechanical.jei.title.mineral_mix");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<MineralMix> holder, IFocusGroup iFocusGroup) {
        MineralMix value = holder.value();
        List<StackWithChance> outputs = value.outputs;
        for (int i = 0; i < outputs.size(); i++) {
            ItemStack itemStack = outputs.get(i).stack().get();
            builder.addSlot(RecipeIngredientRole.OUTPUT, 2, 90 + 18 * i).addItemStack(itemStack);
        }

        List<StackWithChance> spoils = value.spoils;
        for (int i = 0; i < spoils.size(); i++) {
            ItemStack itemStack = spoils.get(i).stack().get();
            builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 90 + 18 * i).addItemStack(itemStack);
        }
    }

    @Override
    public void draw(RecipeHolder<MineralMix> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        String translationKey = MineralMix.getTranslationKey(recipe.id());
        MineralMix mineralMix = recipe.value();
        Component name = Component.translatable(translationKey);
        if (!I18n.exists(translationKey)) {
            name = Component.literal(recipe.id().getPath());
        }

        int i = 0;
        guiGraphics.drawString(font, name, 0, i, 0, false);
        i += 10;
        guiGraphics.drawString(font, Component.translatable("immersive_mechanical.jei.weight", mineralMix.weight), 0, i, 0, false);
        i += 10;
        guiGraphics.drawString(font, Component.translatable("immersive_mechanical.jei.fail_chance", (int) (mineralMix.failChance * 100)), 0, i, 0, false);
        i += 10;
        String biomeText = mineralMix.biomeTagPredicates.stream().map(
                biomeTagPredicate -> biomeTagPredicate.tags().stream().map(biomeTagKey -> {
                    String key = biomeTagKey.location().toLanguageKey("tag.biome").replaceAll("/", ".");
                    if (I18n.exists(key))
                        return I18n.get(key);
                    else
                        return biomeTagKey.location().getPath();
                }).reduce((s, s2) -> I18n.get("ie.manual.entry.minerals.biomes_or", s, s2)).orElse("")
        ).reduce((s, s2) -> I18n.get("ie.manual.entry.minerals.biomes_and", s, s2)).orElse("");

        Component require = Component.translatable("immersive_mechanical.jei.biome_require", biomeText);
        List<FormattedCharSequence> split = Minecraft.getInstance().font.split(require, 150);
        for (FormattedCharSequence formattedCharSequence : split) {
            guiGraphics.drawString(font, formattedCharSequence, 0, i, 0, false);
            i += 10;
        }

        guiGraphics.drawString(font, Component.translatable("immersive_mechanical.jei.expected_ore_output"), 0, 70, 0, false);

        guiGraphics.drawString(font, Component.translatable("immersive_mechanical.jei.main_ore"), 2, 80, 0, false);

        guiGraphics.drawString(font, Component.translatable("immersive_mechanical.jei.spoils"), 80, 80, 0, false);


        List<StackWithChance> outputs = mineralMix.outputs;
        for (int i1 = 0; i1 < outputs.size(); i1++) {
            int y = 95 + i1 * 18;
            StackWithChance stackWithChance = outputs.get(i1);
            int output = (int) (stackWithChance.chance() * 38400 * (1 - mineralMix.failChance));
            guiGraphics.drawString(font, Component.empty().append(":%s(%s)".formatted(output, stackWithChance.chance())), 18, y, 0, false);

        }

        List<StackWithChance> spoils = mineralMix.spoils;
        for (int i1 = 0; i1 < spoils.size(); i1++) {
            int y = 95 + i1 * 18;
            StackWithChance stackWithChance = spoils.get(i1);
            int output = (int) (stackWithChance.chance() * 38400 * mineralMix.failChance);
            guiGraphics.drawString(font, Component.empty().append(":%s(%s)".formatted(output, stackWithChance.chance())), 98, y, 0, false);

        }


        guiGraphics.drawString(font, Component.literal("[?]"), 138, 0, 0, false);

    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<MineralMix> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX > 138 && mouseX < 150 && mouseY > 0 && mouseY < 10) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                tooltip.add(Component.translatable("immersive_mechanical.jei.available_biome"));
                Set<Holder.Reference<Biome>> set = new HashSet<>();
                Stream<Holder.Reference<Biome>> holders = level.registryAccess().registryOrThrow(Registries.BIOME).holders();
                holders.forEach(biomeReference -> {
                    if (recipe.value().validBiome(biomeReference)) {
                        set.add(biomeReference);
                    }
                });

                for (Holder.Reference<Biome> biomeReference : set) {
                    ResourceLocation location = biomeReference.key().location();
                    String key = "biome." + location.getNamespace() + "." + location.getPath();
                    Component name;
                    if (I18n.exists(key)) {
                        name = Component.translatable(key);
                    }else {
                        name = Component.literal(location.toString());
                    }
                    tooltip.add(name);
                }

            }

        }
    }
}
