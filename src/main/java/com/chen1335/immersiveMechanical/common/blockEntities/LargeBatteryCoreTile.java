package com.chen1335.immersiveMechanical.common.blockEntities;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.register.IEDataComponents;
import com.chen1335.immersiveMechanical.API.objects.IMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class LargeBatteryCoreTile extends IEBaseBlockEntity implements IEBlockInterfaces.IBlockEntityDrop {
    public MutableEnergyStorage energy = new MutableEnergyStorage(64000000);

    public LargeBatteryCoreTile(BlockPos pos, BlockState blockState) {
        super(IMBlockEntityTypes.LARGE_BATTERY_CORE_TILE.get(), pos, blockState);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag, boolean b, HolderLookup.@NotNull Provider provider) {
        compoundTag.putInt("energyStorage", energy.getEnergyStored());
    }


    @Override
    public void readCustomNBT(CompoundTag compoundTag, boolean b, HolderLookup.@NotNull Provider provider) {
        energy.setStoredEnergy(compoundTag.getInt("energyStorage"));
    }

    @Override
    public void getBlockEntityDrop(@NotNull LootContext lootContext, Consumer<ItemStack> consumer) {
        ItemStack stack = new ItemStack(getBlockState().getBlock(), 1);
        stack.set(IEDataComponents.GENERIC_ENERGY, this.energy.getEnergyStored());
        consumer.accept(stack);
    }

    @Override
    public void onBEPlaced(BlockPlaceContext blockPlaceContext) {
        ItemStack itemStack = blockPlaceContext.getItemInHand();
        this.energy.setStoredEnergy(itemStack.getOrDefault(IEDataComponents.GENERIC_ENERGY, 0));
    }
}
