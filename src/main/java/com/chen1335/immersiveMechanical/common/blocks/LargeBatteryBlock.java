package com.chen1335.immersiveMechanical.common.blocks;

import blusunrize.immersiveengineering.common.blocks.IEEntityBlock;
import com.chen1335.immersiveMechanical.API.objects.IMBlockEntityTypes;
import com.chen1335.immersiveMechanical.common.blockEntities.LargeBatteryCoreTile;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LargeBatteryBlock extends IEEntityBlock<LargeBatteryCoreTile> {
    public static final MapCodec<LargeBatteryBlock> CODEC = simpleCodec(LargeBatteryBlock::new);

    public LargeBatteryBlock(Properties properties) {
        super(IMBlockEntityTypes.LARGE_BATTERY_CORE_TILE, properties);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new LargeBatteryCoreTile(pos, state);
    }

}
