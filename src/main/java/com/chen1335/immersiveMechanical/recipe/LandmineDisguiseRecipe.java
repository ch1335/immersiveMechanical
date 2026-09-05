package com.chen1335.immersiveMechanical.recipe;

import com.chen1335.immersiveMechanical.API.objects.IMDataComponents;
import com.chen1335.immersiveMechanical.common.items.dataComponents.Disguise;
import com.chen1335.immersiveMechanical.definitions.IMItems;
import com.chen1335.immersiveMechanical.definitions.IMRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LandmineDisguiseRecipe extends CustomRecipe {
    public LandmineDisguiseRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != 3 || input.height() != 3) {
            return false;
        }

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (slot == 4) {
                if (!(stack.getItem() instanceof BlockItem)) {
                    return false;
                }
            } else if (!stack.is(IMItems.LANDMINE.get())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack blockStack = input.getItem(4);
        if (!(blockStack.getItem() instanceof BlockItem blockItem)) {
            return ItemStack.EMPTY;
        }

        BlockState disguise = blockItem.getBlock().defaultBlockState();
        ItemStack result = IMItems.LANDMINE.get().getDefaultInstance();
        result.set(IMDataComponents.DISGUISE, new Disguise(disguise));
        result.setCount(8);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return IMRecipe.LANDMINE_DISGUISE.getSerializer();
    }
}
