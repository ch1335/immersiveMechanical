package com.chen1335.immersiveMechanical.data;

import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.items.upgrades.ToolUpgrade;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.chen1335.immersiveMechanical.API.objects.IMItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
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
    }


}
