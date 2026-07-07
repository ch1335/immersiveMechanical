package com.chen1335.immersiveMechanical.common.blockEntities;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.register.IEDataComponents;
import com.chen1335.immersiveMechanical.config.IMServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class LargeBatteryCoreTile extends IEBaseBlockEntity implements IEBlockInterfaces.IBlockEntityDrop {
    public MutableEnergyStorage energy;

    public LargeBatteryCoreTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        ModConfigSpec.IntValue capaConfig = IMServerConfig.MACHINES.large_battery_core_capa;
        int capa = IMServerConfig.CONFIG_SPEC.isLoaded() ? capaConfig.getAsInt() : capaConfig.getDefault();
        energy = new MutableEnergyStorage(capa);
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
