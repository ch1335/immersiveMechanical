package com.chen1335.immersiveMechanical.compat.jade;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public class CoilIconProvider implements IBlockComponentProvider {
    public static final ResourceLocation ID = ImmersiveEngineering.rl("coil_icon");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        iTooltip.add(Component.translatable("immersive_mechanical.Tooltip.coil_formed"));
    }

    @Override
    public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        BlockEntity blockEntity = accessor.getBlockEntity();
        if (blockEntity instanceof IMultiblockBE<?> be) {
            if (be.getHelper().getState() instanceof CoilLogic.State state) {
                return IElementHelper.get().item(new ItemStack(state.getCoilBlock()));
            }
        }
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }
}
