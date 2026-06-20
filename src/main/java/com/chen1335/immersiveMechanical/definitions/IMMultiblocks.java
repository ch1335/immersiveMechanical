package com.chen1335.immersiveMechanical.definitions;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.multiblocks.UnionMultiblock;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.render.tile.GreenHouseRender;
import com.chen1335.immersiveMechanical.client.render.tile.SmallMiningMachineRender;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks.GreenHousePartBlock;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks.IMCoilPartBlock;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.LargeBatteryLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace.IndustrialFurnacesLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.pyrolyseOven.PyrolyseOvenLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.smallMiningMachine.SmallMiningMachineLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.RecordOriginalBlockMultiblock;
import com.chen1335.registrate.IERegistrate;
import com.chen1335.registrate.MultiblockDefinition;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

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
            .setProperties(IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get().lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0))
            .customBlock(GreenHousePartBlock::new)
            .masterFromOrigin(new BlockPos(2, 0, 2))
            .triggerFromOrigin(new BlockPos(2, 0, 4))
            .size(new BlockPos(5, 5, 5))
            .manualScale(6)
            .render(() -> GreenHouseRender::new)
            .register();

    public static final MultiblockDefinition<SmallMiningMachineLogic.State, SmallMiningMachineLogic> SMALL_MINING_MACHINE = REGISTRATE.multiblock("small_mining_machine", SmallMiningMachineLogic::new)
            .notMirrored()
            .gui(IMMenuTypes.SMALL_MINING_MACHINE)
            .masterFromOrigin(new BlockPos(1, 1, 1))
            .triggerFromOrigin(new BlockPos(1, 1, 2))
            .size(new BlockPos(3, 4, 3))
            .manualScale(9)
            .render(() -> SmallMiningMachineRender::new)
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
            .customBlock(IMCoilPartBlock::new)
            .additionalPredicates(List.of(MatcherPredicates::coil))
            .masterFromOrigin(new BlockPos(1, 0, 2))
            .triggerFromOrigin(new BlockPos(1, 0, 2))
            .size(new BlockPos(3, 1, 3))
            .manualScale(9)
            .register();

    public static final MultiblockDefinition<CoilLogic.State, CoilLogic> COIL_VERTICAL = REGISTRATE.multiblock("coil_vertical", CoilLogic::new, RecordOriginalBlockMultiblock::new)
            .notMirrored()
            .customBlock(IMCoilPartBlock::new)
            .additionalPredicates(List.of(MatcherPredicates::coil))
            .masterFromOrigin(new BlockPos(1, 2, 0))
            .triggerFromOrigin(new BlockPos(1, 2, 0))
            .size(new BlockPos(3, 3, 1))
            .manualScale(9)
            .register();

    public static final MultiblockDefinition<PyrolyseOvenLogic.State, PyrolyseOvenLogic> PYROLYSE_OVEN = REGISTRATE.multiblock("pyrolyse_oven", PyrolyseOvenLogic::new)
            .notMirrored()
            .gui(IMMenuTypes.PYROLYSE_OVEN)
            .masterFromOrigin(new BlockPos(2, 0, 3))
            .triggerFromOrigin(new BlockPos(1, 2, 5))
            .size(new BlockPos(5, 4, 6))
            .manualScale(9)
            .register();


    public static final MultiblockHandler.IMultiblock INDUSTRIAL_FURNACES_DEMO = register((new UnionMultiblock(ImmersiveMechanical.id("industrial_furnaces_demo"),
            ImmutableList.of(
                    new UnionMultiblock.TransformedMultiblock(INDUSTRIAL_FURNACES.multiblock(), Vec3i.ZERO, Rotation.NONE),
                    new UnionMultiblock.TransformedMultiblock(COIL.multiblock(), new Vec3i(0, 1, 0), Rotation.NONE)
            )) {
        @Override
        public @NotNull BlockPos getTriggerOffset() {
            return new BlockPos(-1, -1, -1);
        }
    }));

    public static final MultiblockHandler.IMultiblock PYROLYSE_OVEN_DEMO = register((new UnionMultiblock(ImmersiveMechanical.id("pyrolyse_oven_demo"),
            ImmutableList.of(
                    new UnionMultiblock.TransformedMultiblock(PYROLYSE_OVEN.multiblock(), Vec3i.ZERO, Rotation.NONE),
                    new UnionMultiblock.TransformedMultiblock(COIL_VERTICAL.multiblock(), new Vec3i(0, 1, 2), Rotation.NONE),
                    new UnionMultiblock.TransformedMultiblock(COIL_VERTICAL.multiblock(), new Vec3i(0, 1, 3), Rotation.NONE)

            )) {
        @Override
        public @NotNull BlockPos getTriggerOffset() {
            return new BlockPos(-1, -1, -1);
        }
    }));

    private static <T extends MultiblockHandler.IMultiblock> T register(T multiblock) {
        REGISTRATE.getMultiblocks().add(multiblock);
        IERegistrate.ALL_MULTIBLOCKS.add(multiblock);
        MultiblockHandler.registerMultiblock(multiblock);
        return multiblock;
    }

    public static void init() {

    }
}
