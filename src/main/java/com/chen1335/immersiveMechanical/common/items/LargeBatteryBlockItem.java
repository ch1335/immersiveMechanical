package com.chen1335.immersiveMechanical.common.items;

import blusunrize.immersiveengineering.common.blocks.BlockItemIE;
import blusunrize.immersiveengineering.common.register.IEDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LargeBatteryBlockItem extends BlockItemIE {
    public LargeBatteryBlockItem(Block b, Properties props) {
        super(b, props);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext ctx, @NotNull List<Component> tooltip, @NotNull TooltipFlag advanced) {
        super.appendHoverText(stack, ctx, tooltip, advanced);
        tooltip.add(Component.translatable("desc.immersive_mechanical.info.LargeBattery").withStyle(ChatFormatting.GRAY));
    }
}
