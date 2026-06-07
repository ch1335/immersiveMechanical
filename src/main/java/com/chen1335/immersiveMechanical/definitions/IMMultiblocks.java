package com.chen1335.immersiveMechanical.definitions;

import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.pyrolyseOven.PyrolyseOvenLogic;
import com.chen1335.registrate.MultiblockDefinition;
import com.chen1335.immersiveMechanical.client.render.tile.GreenHouseRender;
import com.chen1335.immersiveMechanical.client.render.tile.SmallMiningMachineRender;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks.GreenHousePartBlock;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.LargeBatteryLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace.IndustrialFurnacesLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.smallMiningMachine.SmallMiningMachineLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.RecordOriginalBlockMultiblock;
import net.minecraft.core.BlockPos;

import java.util.List;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

public class IMMultiblocks {
    public static final MultiblockDefinition<LargeBatteryLogic.State, LargeBatteryLogic> LARGE_BATTERY = REGISTRATE.multiblock("large_battery", LargeBatteryLogic::new)
            .notMirrored()
            .masterFromOrigin(new BlockPos(1, 0, 1))
            .triggerFromOrigin(new BlockPos(1, 0, 2))
            .size(new BlockPos(3, 5, 3))
            .manualScale(9)
            .register();

    public static final MultiblockDefinition<GreenHouseLogic.State, GreenHouseLogic> GREEN_HOUSE = REGISTRATE.multiblock("green_house", GreenHouseLogic::new)
            .notMirrored()
            .gui(IMMenuTypes.GREEN_HOUSE)
            .customBlock(GreenHousePartBlock::new)
            .masterFromOrigin(new BlockPos(2, 0, 2))
            .triggerFromOrigin(new BlockPos(2, 0, 4))
            .size(new BlockPos(5, 5, 5))
            .manualScale(6)
            .render(()-> GreenHouseRender::new)
            .register();

    public static final MultiblockDefinition<SmallMiningMachineLogic.State, SmallMiningMachineLogic> SMALL_MINING_MACHINE = REGISTRATE.multiblock("small_mining_machine", SmallMiningMachineLogic::new)
            .notMirrored()
            .gui(IMMenuTypes.SMALL_MINING_MACHINE)
            .masterFromOrigin(new BlockPos(1, 1, 1))
            .triggerFromOrigin(new BlockPos(1, 1, 2))
            .size(new BlockPos(3, 4, 3))
            .manualScale(9)
            .render(()-> SmallMiningMachineRender::new)
            .register();

    public static final MultiblockDefinition<IndustrialFurnacesLogic.State, IndustrialFurnacesLogic> INDUSTRIAL_FURNACES = REGISTRATE.multiblock("industrial_furnaces", IndustrialFurnacesLogic::new)
            .notMirrored()
            .gui(IMMenuTypes.INDUSTRIAL_FURNACES)
            .masterFromOrigin(new BlockPos(1, 0, 1))
            .triggerFromOrigin(new BlockPos(1, 0, 2))
            .size(new BlockPos(3, 3, 3))
            .manualScale(9)
            .register();

    public static final MultiblockDefinition<CoilLogic.State, CoilLogic> COIL = REGISTRATE.multiblock("coil", CoilLogic::new, RecordOriginalBlockMultiblock::new)
            .notMirrored()
            .additionalPredicates(List.of(MatcherPredicates::coil))
            .masterFromOrigin(new BlockPos(1, 0, 2))
            .triggerFromOrigin(new BlockPos(1, 0, 2))
            .size(new BlockPos(3, 1, 3))
            .manualScale(9)
            .register();

    public static final MultiblockDefinition<CoilLogic.State, CoilLogic> COIL_VERTICAL = REGISTRATE.multiblock("coil_vertical", CoilLogic::new, RecordOriginalBlockMultiblock::new)
            .notMirrored()
            .additionalPredicates(List.of(MatcherPredicates::coil))
            .masterFromOrigin(new BlockPos(1, 2, 0))
            .triggerFromOrigin(new BlockPos(1, 2, 0))
            .size(new BlockPos(3, 3, 1))
            .manualScale(9)
            .register();

    public static final MultiblockDefinition<PyrolyseOvenLogic.State, PyrolyseOvenLogic> PYROLYSE_OVEN = REGISTRATE.multiblock("pyrolyse_oven", PyrolyseOvenLogic::new)
            .notMirrored()
            .masterFromOrigin(new BlockPos(2, 0, 3))
            .triggerFromOrigin(new BlockPos(1, 2, 5))
            .size(new BlockPos(5, 4, 6))
            .manualScale(9)
            .register();

    public static void init() {

    }
}
