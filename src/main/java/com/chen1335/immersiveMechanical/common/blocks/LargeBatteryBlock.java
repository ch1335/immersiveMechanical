package com.chen1335.immersiveMechanical.common.blocks;

import blusunrize.immersiveengineering.common.blocks.IEEntityBlock;
import com.chen1335.immersiveMechanical.common.blockEntities.LargeBatteryCoreTile;
import com.chen1335.immersiveMechanical.definitions.IMBlockEntityTypes;
import com.mojang.serialization.MapCodec;

public class LargeBatteryBlock extends IEEntityBlock<LargeBatteryCoreTile> {
    public static final MapCodec<LargeBatteryBlock> CODEC = simpleCodec(LargeBatteryBlock::new);

    public LargeBatteryBlock(Properties properties) {
        super(IMBlockEntityTypes.LARGE_BATTERY_CORE_TILE, properties);
    }
}
