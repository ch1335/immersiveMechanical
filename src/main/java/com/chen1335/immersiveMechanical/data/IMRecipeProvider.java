package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.common.items.upgrades.ToolUpgrade;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.data.recipes.builder.ArcFurnaceRecipeBuilder;
import blusunrize.immersiveengineering.data.recipes.builder.CrusherRecipeBuilder;
import blusunrize.immersiveengineering.data.recipes.builder.MetalPressRecipeBuilder;
import com.chen1335.immersiveMechanical.API.tags.IMItemTags;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.data.recipeBuilders.IndustrialFurnaceRecipeBuilder;
import com.chen1335.immersiveMechanical.definitions.IMBlocks;
import com.chen1335.immersiveMechanical.definitions.IMItems;
import com.chen1335.immersiveMechanical.definitions.IMMetals;
import com.chen1335.registrate.MetalDefinition;
import com.chen1335.registrate.MetalTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class IMRecipeProvider extends RecipeProvider {
    public IMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput, HolderLookup.@NotNull Provider holderLookup) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMBlocks.LARGE_BATTERY_CORE.get())
                .define('A', IETags.getItemTag(IETags.getTagsFor(EnumMetals.STEEL).sheetmetal))
                .define('B', IEBlocks.MetalDevices.CAPACITOR_HV)
                .define('C', IEBlocks.MetalDecoration.ENGINEERING_LIGHT)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .unlockedBy("has_capacitor_hv", has(IEBlocks.MetalDevices.CAPACITOR_HV))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMBlocks.TURRET_LASER.get())
                .define('A', IEBlocks.MetalDevices.CAPACITOR_HV)
                .define('B', IEBlocks.WoodenDevices.TURNTABLE)
                .define('C', IEBlocks.MetalDecoration.ENGINEERING_RS)
                .define('D', IEItems.Ingredients.COMPONENT_ELECTRONIC_ADV)
                .define('E', IEBlocks.MetalDevices.TESLA_COIL)
                .define('F', IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.RAILGUN_SCOPE))
                .pattern(" F ")
                .pattern(" ED")
                .pattern("ABC")
                .unlockedBy("has_capacitor_hv", has(IEBlocks.MetalDevices.CAPACITOR_HV))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMItems.ACSR.get())
                .define('A', IETags.steelWire)
                .define('B', IETags.aluminumWire)
                .pattern("   ")
                .pattern("BAB")
                .pattern("   ")
                .unlockedBy("has_wire_steel", has(IEItems.Ingredients.WIRE_STEEL))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMItems.EHV_WIRE_COIL.get().getDefaultInstance().copyWithCount(4))
                .define('A', IETags.steelRod)
                .define('B', IMItems.ACSR.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .unlockedBy("has_acsr", has(IMItems.EHV_WIRE_COIL.get()))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMItems.NICHROME_WIRE_COIL.get().getDefaultInstance().copyWithCount(4))
                .define('A', IETags.steelRod)
                .define('B', IMItems.WIRE_NICHROME.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .unlockedBy("has_wire_nichrome", has(IMItems.WIRE_NICHROME.get()))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMBlocks.COIL_NICHROME.get())
                .define('A', IETags.getTagsFor(EnumMetals.STEEL).ingot)
                .define('B', IMItems.NICHROME_WIRE_COIL.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .unlockedBy("has_nichrome_wire_coil", has(IMItems.NICHROME_WIRE_COIL.get()))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMBlocks.CONNECTOR_EHV.get())
                .define('A', IETags.getTagsFor(EnumMetals.STEEL).ingot)
                .define('B', IETags.connectorInsulator)
                .pattern(" A ")
                .pattern("BAB")
                .pattern("BAB")
                .unlockedBy("has_steel_ingot", has(IETags.getTagsFor(EnumMetals.STEEL).ingot))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMBlocks.CONNECTOR_EHV_RELAY.get())
                .define('A', IETags.getTagsFor(EnumMetals.STEEL).ingot)
                .define('B', IEBlocks.StoneDecoration.INSULATING_GLASS)
                .pattern(" A ")
                .pattern("BAB")
                .pattern("BAB")
                .unlockedBy("has_steel_ingot", has(IETags.getTagsFor(EnumMetals.STEEL).ingot))
                .showNotification(false)
                .save(recipeOutput);

        oreCrusher(recipeOutput, IMItemTags.ORES_CHROME, IMMetals.CHROME);


        ArcFurnaceRecipeBuilder.builder()
                .output(IMMetals.NICHROME.getTag(MetalTypes.INGOTS), 5)
                .input(IETags.getTagsFor(EnumMetals.NICKEL).ingot, 4)
                .additive(IMMetals.CHROME.getTag(MetalTypes.INGOTS))
                .setTime(600)
                .setEnergy(307200)
                .build(recipeOutput, toRL("arcfurnace/alloy_nichrome"));

        ArcFurnaceRecipeBuilder.builder()
                .output(IMMetals.CHROME.getTag(MetalTypes.INGOTS), 2)
                .input(IMItemTags.ORES_CHROME, 1)
                .slag(IETags.slag, 1)
                .setTime(200)
                .setEnergy(102400)
                .build(recipeOutput, toRL("arcfurnace/ore_chrome"));


        ArcFurnaceRecipeBuilder.builder()
                .output(IMMetals.CHROME.getTag(MetalTypes.INGOTS), 1)
                .secondary(IMMetals.CHROME.getTag(MetalTypes.INGOTS), 0.5F)
                .input(IMItemTags.RAW_CHROME, 1)
                .setTime(100)
                .setEnergy(25600)
                .build(recipeOutput, toRL("arcfurnace/raw_ore_chrome"));

        IndustrialFurnaceRecipeBuilder.builder(
                Ingredient.of(IMItemTags.RAW_CHROME),
                new TagOutput(IMMetals.CHROME.getTag(MetalTypes.INGOTS))
                , 200,
                51200
        ).build(recipeOutput, toRL("industrial_furnace/raw_chrome"));

        IndustrialFurnaceRecipeBuilder.builder(
                Ingredient.of(IMMetals.CHROME.getTag(MetalTypes.DUSTS)),
                new TagOutput(IMMetals.CHROME.getTag(MetalTypes.INGOTS))
                , 100,
                25600
        ).build(recipeOutput, toRL("industrial_furnace/chrome_dust"));

        for (MetalDefinition metalDefinition : ImmersiveMechanical.REGISTRATE.getMetalDefinitions()) {
            ArcFurnaceRecipeBuilder.builder()
                    .output(metalDefinition.getTag(MetalTypes.INGOTS), 1)
                    .input(metalDefinition.getTag(MetalTypes.DUSTS), 1)
                    .setTime(100)
                    .setEnergy(25600)
                    .build(recipeOutput, toRL("arcfurnace/%s_dust".formatted(metalDefinition.getName())));
        }


        for (MetalDefinition metalDefinition : ImmersiveMechanical.REGISTRATE.getMetalDefinitions()) {
            ingotsToPlateByMetalPress(recipeOutput, metalDefinition);
            ingotsToPlateByHammer(recipeOutput, metalDefinition);

            ingotsToRodByMetalPress(recipeOutput, metalDefinition);

            ingotsToNuggets(recipeOutput, metalDefinition);
            ingotsToDustByCrusher(recipeOutput, metalDefinition);
            nuggetsToIngots(recipeOutput, metalDefinition);
        }


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IMItems.WIRE_NICHROME)
                .requires(IMMetals.NICHROME.getTag(MetalTypes.PLATES))
                .requires(IEItems.Tools.WIRECUTTER)
                .unlockedBy("has_nichrome_ingot", has(IMMetals.NICHROME.getTag(MetalTypes.INGOTS)))
                .save(recipeOutput, toRL(toPath(IMItems.WIRE_NICHROME)));

        press(recipeOutput, IEItems.Molds.MOLD_WIRE, IMMetals.NICHROME.getTag(MetalTypes.INGOTS), IMItems.WIRE_NICHROME, 2);
    }

    private static void press(RecipeOutput recipeOutput, ItemLike mold, TagKey<Item> input, ItemLike output, int count) {
        MetalPressRecipeBuilder.builder()
                .input(input)
                .mold(mold)
                .output(output, count)
                .setEnergy(2400)
                .build(recipeOutput, toRL("metalpress/%s".formatted(BuiltInRegistries.ITEM.getKey(IMItems.WIRE_NICHROME.asItem()).getPath())));
    }

    private static void ingotsToPlateByHammer(RecipeOutput recipeOutput, MetalDefinition metals) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, metals.getItem(MetalTypes.PLATES))
                .requires(metals.getTag(MetalTypes.INGOTS))
                .requires(IEItems.Tools.HAMMER)
                .unlockedBy("has_%s_ingot".formatted(metals.getName()), has(metals.getTag(MetalTypes.INGOTS)))
                .save(recipeOutput, toRL(toPath(metals.getItem(MetalTypes.PLATES))));
    }

    private static void ingotsToPlateByMetalPress(RecipeOutput recipeOutput, MetalDefinition metals) {
        MetalPressRecipeBuilder.builder()
                .input(metals.getTag(MetalTypes.INGOTS))
                .mold(IEItems.Molds.MOLD_PLATE)
                .output(metals.getTag(MetalTypes.PLATES), 1)
                .setEnergy(2400)
                .build(recipeOutput, toRL("metalpress/plate_" + metals.getName()));
    }

    private static void ingotsToRodByMetalPress(RecipeOutput recipeOutput, MetalDefinition metals) {
        MetalPressRecipeBuilder.builder()
                .input(metals.getTag(MetalTypes.INGOTS))
                .mold(IEItems.Molds.MOLD_ROD)
                .output(metals.getTag(MetalTypes.STICKS), 2)
                .setEnergy(2400)
                .build(recipeOutput, toRL("metalpress/rod_" + metals.getName()));
    }

    private static void nuggetsToIngots(RecipeOutput recipeOutput, MetalDefinition metals) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, metals.getItem(MetalTypes.INGOTS))
                .requires(Ingredient.of(metals.getTag(MetalTypes.NUGGETS)), 9)
                .unlockedBy("has_" + MetalTypes.NUGGETS.format(metals.getName()), has(metals.getItem(MetalTypes.NUGGETS)))
                .save(recipeOutput);
    }

    private static void ingotsToNuggets(RecipeOutput recipeOutput, MetalDefinition metals) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, metals.getItem(MetalTypes.NUGGETS), 9)
                .requires(Ingredient.of(metals.getTag(MetalTypes.INGOTS)))
                .unlockedBy("has_" + MetalTypes.INGOTS.format(metals.getName()), has(metals.getItem(MetalTypes.INGOTS)))
                .save(recipeOutput);
    }

    private static void oreCrusher(RecipeOutput recipeOutput, TagKey<Item> oreTag, MetalDefinition metals) {
        CrusherRecipeBuilder.builder()
                .input(Ingredient.of(oreTag))
                .output(new TagOutput(metals.getTag(MetalTypes.DUSTS), 2))
                .build(recipeOutput, toRL("crusher/ore_" + metals.getName()));
    }

    private static void ingotsToDustByCrusher(RecipeOutput recipeOutput, MetalDefinition metals) {
        CrusherRecipeBuilder.builder()
                .input(Ingredient.of(metals.getTag(MetalTypes.INGOTS)))
                .output(new TagOutput(metals.getTag(MetalTypes.DUSTS)))
                .build(recipeOutput, toRL("crusher/ingot_" + metals.getName()));
    }

    private static ResourceLocation toRL(String s) {
        return ImmersiveMechanical.id(s);
    }

    private static String toPath(ItemLike src) {
        return BuiltInRegistries.ITEM.getKey(src.asItem()).getPath();
    }
}
