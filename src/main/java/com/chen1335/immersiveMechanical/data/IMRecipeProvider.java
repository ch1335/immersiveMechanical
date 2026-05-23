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
import com.chen1335.immersiveMechanical.definitions.IMItems;
import com.chen1335.immersiveMechanical.API.objects.metal.IMMetalTypes;
import com.chen1335.immersiveMechanical.API.objects.metal.IMMetals;
import com.chen1335.immersiveMechanical.API.tags.IMItemTags;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.data.recipeBuilders.IndustrialFurnaceRecipeBuilder;
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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMItems.LARGE_BATTERY_CORE.get())
                .define('A', IETags.getItemTag(IETags.getTagsFor(EnumMetals.STEEL).sheetmetal))
                .define('B', IEBlocks.MetalDevices.CAPACITOR_HV)
                .define('C', IEBlocks.MetalDecoration.ENGINEERING_LIGHT)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .unlockedBy("has_capacitor_hv", has(IEBlocks.MetalDevices.CAPACITOR_HV))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMItems.LASER_TURRET.get())
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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMItems.COIL_NICHROME.get().getDefaultInstance())
                .define('A', IETags.getTagsFor(EnumMetals.STEEL).ingot)
                .define('B', IMItems.NICHROME_WIRE_COIL.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .unlockedBy("has_nichrome_wire_coil", has(IMItems.NICHROME_WIRE_COIL.get()))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMItems.CONNECTOR_EHV.get())
                .define('A', IETags.getTagsFor(EnumMetals.STEEL).ingot)
                .define('B', IETags.connectorInsulator)
                .pattern(" A ")
                .pattern("BAB")
                .pattern("BAB")
                .unlockedBy("has_steel_ingot", has(IETags.getTagsFor(EnumMetals.STEEL).ingot))
                .showNotification(false)
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMItems.CONNECTOR_EHV_RELAY.get())
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
                .output(IMMetals.NICHROME.getTag(IMMetalTypes.INGOTS), 5)
                .input(IETags.getTagsFor(EnumMetals.NICKEL).ingot, 4)
                .additive(IMMetals.CHROME.getTag(IMMetalTypes.INGOTS))
                .setTime(600)
                .setEnergy(307200)
                .build(recipeOutput, toRL("arcfurnace/alloy_nichrome"));

        ArcFurnaceRecipeBuilder.builder()
                .output(IMMetals.CHROME.getTag(IMMetalTypes.INGOTS), 2)
                .input(IMItemTags.ORES_CHROME, 1)
                .slag(IETags.slag, 1)
                .setTime(200)
                .setEnergy(102400)
                .build(recipeOutput, toRL("arcfurnace/ore_chrome"));


        ArcFurnaceRecipeBuilder.builder()
                .output(IMMetals.CHROME.getTag(IMMetalTypes.INGOTS), 1)
                .secondary(IMMetals.CHROME.getTag(IMMetalTypes.INGOTS), 0.5F)
                .input(IMItemTags.RAW_CHROME, 1)
                .setTime(100)
                .setEnergy(25600)
                .build(recipeOutput, toRL("arcfurnace/raw_ore_chrome"));

        IndustrialFurnaceRecipeBuilder.builder(
                Ingredient.of(IMItemTags.RAW_CHROME),
                new TagOutput(IMMetals.CHROME.getTag(IMMetalTypes.INGOTS))
                , 200,
                51200
        ).build(recipeOutput, toRL("industrial_furnace/raw_chrome"));

        IndustrialFurnaceRecipeBuilder.builder(
                Ingredient.of(IMMetals.CHROME.getTag(IMMetalTypes.DUSTS)),
                new TagOutput(IMMetals.CHROME.getTag(IMMetalTypes.INGOTS))
                , 100,
                25600
        ).build(recipeOutput, toRL("industrial_furnace/chrome_dust"));


        IMMetals.METALS.forEach((metals, itemMap) -> {
            ArcFurnaceRecipeBuilder.builder()
                    .output(metals.getTag(IMMetalTypes.INGOTS), 1)
                    .input(itemMap.get(IMMetalTypes.DUSTS), 1)
                    .setTime(100)
                    .setEnergy(25600)
                    .build(recipeOutput, toRL("arcfurnace/%s_dust".formatted(metals.getName())));
        });


        for (IMMetals metals : IMMetals.METAL_TAGS.keySet()) {
            ingotsToPlateByMetalPress(recipeOutput, metals);
            ingotsToPlateByHammer(recipeOutput, metals);

            ingotsToRodByMetalPress(recipeOutput, metals);

            ingotsToNuggets(recipeOutput, metals);
            ingotsToDustByCrusher(recipeOutput, metals);
            nuggetsToIngots(recipeOutput, metals);
        }

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IMItems.WIRE_NICHROME)
                .requires(IMMetals.NICHROME.getTag(IMMetalTypes.PLATES))
                .requires(IEItems.Tools.WIRECUTTER)
                .unlockedBy("has_nichrome_ingot", has(IMMetals.NICHROME.getTag(IMMetalTypes.INGOTS)))
                .save(recipeOutput, toRL(toPath(IMItems.WIRE_NICHROME)));

        press(recipeOutput, IEItems.Molds.MOLD_WIRE, IMMetals.NICHROME.getMetal(IMMetalTypes.INGOTS), IMItems.WIRE_NICHROME, 2);
    }

    private static void press(RecipeOutput recipeOutput, ItemLike mold, ItemLike input, ItemLike output, int count) {
        MetalPressRecipeBuilder.builder()
                .input(input)
                .mold(mold)
                .output(output, count)
                .setEnergy(2400)
                .build(recipeOutput, toRL("metalpress/%s".formatted(BuiltInRegistries.ITEM.getKey(input.asItem()).getPath())));
    }

    private static void ingotsToPlateByHammer(RecipeOutput recipeOutput, IMMetals metals) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, metals.getMetal(IMMetalTypes.PLATES))
                .requires(metals.getTag(IMMetalTypes.INGOTS))
                .requires(IEItems.Tools.HAMMER)
                .unlockedBy("has_%s_ingot".formatted(metals.getName()), has(metals.getTag(IMMetalTypes.INGOTS)))
                .save(recipeOutput, toRL(toPath(metals.getMetal(IMMetalTypes.PLATES))));
    }

    private static void ingotsToPlateByMetalPress(RecipeOutput recipeOutput, IMMetals metals) {
        MetalPressRecipeBuilder.builder()
                .input(metals.getTag(IMMetalTypes.INGOTS))
                .mold(IEItems.Molds.MOLD_PLATE)
                .output(metals.getMetal(IMMetalTypes.PLATES), 1)
                .setEnergy(2400)
                .build(recipeOutput, toRL("metalpress/plate_" + metals.getName()));
    }

    private static void ingotsToRodByMetalPress(RecipeOutput recipeOutput, IMMetals metals) {
        MetalPressRecipeBuilder.builder()
                .input(metals.getTag(IMMetalTypes.INGOTS))
                .mold(IEItems.Molds.MOLD_ROD)
                .output(metals.getMetal(IMMetalTypes.STICKS), 2)
                .setEnergy(2400)
                .build(recipeOutput, toRL("metalpress/rod_" + metals.getName()));
    }

    private static void nuggetsToIngots(RecipeOutput recipeOutput, IMMetals metals) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, metals.getMetal(IMMetalTypes.INGOTS))
                .requires(Ingredient.of(metals.getTag(IMMetalTypes.NUGGETS)), 9)
                .unlockedBy("has_" + IMMetalTypes.NUGGETS.format(metals.getName()), has(metals.getMetal(IMMetalTypes.NUGGETS)))
                .save(recipeOutput);
    }

    private static void ingotsToNuggets(RecipeOutput recipeOutput, IMMetals metals) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, metals.getMetal(IMMetalTypes.NUGGETS), 9)
                .requires(Ingredient.of(metals.getTag(IMMetalTypes.INGOTS)))
                .unlockedBy("has_" + IMMetalTypes.INGOTS.format(metals.getName()), has(metals.getMetal(IMMetalTypes.INGOTS)))
                .save(recipeOutput);
    }

    private static void oreCrusher(RecipeOutput recipeOutput, TagKey<Item> oreTag, IMMetals metals) {
        CrusherRecipeBuilder.builder()
                .input(Ingredient.of(oreTag))
                .output(new TagOutput(metals.getTag(IMMetalTypes.DUSTS), 2))
                .build(recipeOutput, toRL("crusher/ore_" + metals.getName()));
    }

    private static void ingotsToDustByCrusher(RecipeOutput recipeOutput, IMMetals metals) {
        CrusherRecipeBuilder.builder()
                .input(Ingredient.of(metals.getTag(IMMetalTypes.INGOTS)))
                .output(new TagOutput(metals.getTag(IMMetalTypes.DUSTS)))
                .build(recipeOutput, toRL("crusher/ingot_" + metals.getName()));
    }

    private static ResourceLocation toRL(String s) {
        return ImmersiveMechanical.id(s);
    }

    private static String toPath(ItemLike src) {
        return BuiltInRegistries.ITEM.getKey(src.asItem()).getPath();
    }
}
