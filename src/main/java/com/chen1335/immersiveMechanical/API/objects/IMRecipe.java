package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMRecipe {
    public static class Types {
        private static final DeferredRegister<RecipeType<?>> TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, ImmersiveMechanical.MODID);
        public static final IERecipeTypes.TypeWithClass<IndustrialFurnaceRecipe> INDUSTRIAL_FURNACE = register("industrial_furnace", IndustrialFurnaceRecipe.class);

        private static <T extends Recipe<?>> IERecipeTypes.TypeWithClass<T> register(String name, Class<T> type) {
            DeferredHolder<RecipeType<?>, RecipeType<T>> regObj = TYPE.register(name, () -> new RecipeType<>() {
            });
            return new IERecipeTypes.TypeWithClass<>(regObj, type);
        }
    }

    public static class Serializers {
        private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ImmersiveMechanical.MODID);
        public static final DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<IndustrialFurnaceRecipe>> INDUSTRIAL_FURNACE_SERIALIZER = RECIPE_SERIALIZER.register("industrial_furnace", IndustrialFurnaceRecipe.Serializer::new);

    }


    public static void register(IEventBus eventBus) {
        Types.TYPE.register(eventBus);
        Serializers.RECIPE_SERIALIZER.register(eventBus);
    }

}
