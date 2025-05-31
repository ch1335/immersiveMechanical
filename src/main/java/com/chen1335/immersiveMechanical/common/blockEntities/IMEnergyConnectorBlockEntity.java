package com.chen1335.immersiveMechanical.common.blockEntities;

import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import com.chen1335.immersiveMechanical.mixins.EnergyConnectorBlockEntityAccessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class IMEnergyConnectorBlockEntity extends EnergyConnectorBlockEntity {
    public static final Map<String, Pair<Integer, Integer>> MAP = Map.of("EHV", new Pair<>(16384, 16384));
    private final int maxInput;
    private final int maxOutput;

    public IMEnergyConnectorBlockEntity(String voltage, boolean relay, BlockPos pos, BlockState state, int maxInput, int maxOutput) {
        super(voltage, relay, pos, state);
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
    }

    @Override
    public int getMaxInput() {
        return MAP.get(((EnergyConnectorBlockEntityAccessor) this).getVoltage()).getFirst();
    }

    @Override
    public int getMaxOutput() {
        return MAP.get(((EnergyConnectorBlockEntityAccessor) this).getVoltage()).getSecond();
    }
}
