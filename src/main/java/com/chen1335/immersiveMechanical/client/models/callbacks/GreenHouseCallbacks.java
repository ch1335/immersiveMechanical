package com.chen1335.immersiveMechanical.client.models.callbacks;

import blusunrize.immersiveengineering.api.client.ieobj.BlockCallback;
import com.mojang.datafixers.util.Unit;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class GreenHouseCallbacks implements BlockCallback<Unit> {
    public static final GreenHouseCallbacks INSTANCE = new GreenHouseCallbacks();

    @Override
    public Unit extractKey(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, BlockEntity blockEntity) {
        return getDefaultKey();
    }

    @Override
    public boolean dependsOnLayer() {
        return true;
    }

    @Override
    public Unit getDefaultKey() {
        return Unit.INSTANCE;
    }

    @Override
    public boolean shouldRenderGroup(Unit object, String group, RenderType layer) {
        return "glass".equals(group) == (layer == RenderType.translucent());
    }
}
