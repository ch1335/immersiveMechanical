package com.chen1335.immersiveMechanical.common.gui.sync;

import blusunrize.immersiveengineering.common.gui.sync.GenericDataSerializers;
import com.chen1335.immersiveMechanical.common.gui.IndustrialFurnacesMenu;
import net.minecraft.core.BlockPos;

import java.util.List;

public class IMGenericDataSerializers {
    public static GenericDataSerializers.DataSerializer<BlockPos> BLOCK_POS = GenericDataSerializers.BLOCK_POS;

    public static GenericDataSerializers.DataSerializer<List<IndustrialFurnacesMenu.SlotProgress>> INDUSTRIAL_FURNACES_PROCESS_SLOTS;

}
