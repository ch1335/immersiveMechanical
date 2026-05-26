package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import com.chen1335.immersiveMechanical.definitions.IMRecipe;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin extends SimpleJsonResourceReloadListener {
    @Shadow
    @Final
    private HolderLookup.Provider registries;
    @Unique
    private static HolderLookup.Provider IM$REGISTRIES;

    @Unique
    private static final Map<Ingredient, Pair<RecipeHolder<IndustrialFurnaceRecipe>, Runnable>> IM$CAPTURED_RECIPE = new HashMap<>();

    public RecipeManagerMixin(Gson gson, String directory) {
        super(gson, directory);
    }

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
    private void applyHead(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        IM$REGISTRIES = registries;
    }

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;"))
    private void applyReturn(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        IM$CAPTURED_RECIPE.values().forEach(pair -> pair.getSecond().run());
        IM$CAPTURED_RECIPE.clear();
        IM$REGISTRIES = null;
    }

    @Inject(method = "lambda$apply$0", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;"))
    private static void lambda$apply$0(ResourceLocation resourcelocation, ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder1, WithConditions<Recipe<?>> r, CallbackInfo ci) {
        Recipe<?> carrier = r.carrier();
        if (carrier.getType() == RecipeType.SMELTING || carrier.getType() == RecipeType.BLASTING) {
            boolean isFood = carrier.getResultItem(IM$REGISTRIES).has(DataComponents.FOOD);
            if (isFood) {
                return;
            }
            IndustrialFurnaceRecipe industrialFurnaceRecipe = IndustrialFurnaceRecipe.fromSmeltingRecipe((AbstractCookingRecipe) carrier, IM$REGISTRIES);
            ResourceLocation resourceLocation = resourcelocation.withPrefix("industrial_furnace/");
            RecipeHolder<IndustrialFurnaceRecipe> recipeHolder = new RecipeHolder<>(resourceLocation, industrialFurnaceRecipe);

            Pair<RecipeHolder<IndustrialFurnaceRecipe>, Runnable> pair = IM$CAPTURED_RECIPE.get(industrialFurnaceRecipe.input());
            if (pair == null) {
                IM$CAPTURED_RECIPE.put(industrialFurnaceRecipe.input(), Pair.of(recipeHolder, () -> {
                    builder.put(IMRecipe.INDUSTRIAL_FURNACE.get(), recipeHolder);
                    builder1.put(resourceLocation, recipeHolder);
                }));
            } else if (pair.getFirst().value().getBaseTime() > industrialFurnaceRecipe.getBaseTime()) {
                IM$CAPTURED_RECIPE.put(industrialFurnaceRecipe.input(), Pair.of(recipeHolder, () -> {
                    builder.put(IMRecipe.INDUSTRIAL_FURNACE.get(), recipeHolder);
                    builder1.put(resourceLocation, recipeHolder);
                }));
            }

        }
    }
}
