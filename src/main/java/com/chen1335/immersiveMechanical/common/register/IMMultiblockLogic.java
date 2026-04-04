package com.chen1335.immersiveMechanical.common.register;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockItem;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.IEMultiblockBuilder;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.API.objects.IMMenuTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.IMMultiblockItem;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.IMMultiblocks;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks.IMCoilPartBlock;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace.IndustrialFurnacesLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.LargeBatteryLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.smallMiningMachine.SmallMiningMachineLogic;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class IMMultiblockLogic {
    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, ImmersiveMechanical.MODID);
    private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, ImmersiveMechanical.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BE_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "immersiveengineering");

    public static final MultiblockRegistration<LargeBatteryLogic.State> LARGE_BATTERY = metal(new LargeBatteryLogic(), "large_battery")
            .notMirrored()
            .structure(() -> IMMultiblocks.LARGE_BATTERY)
            .build();

    public static final MultiblockRegistration<SmallMiningMachineLogic.State> SMALL_MINING_MACHINE = metal(new SmallMiningMachineLogic(), "small_mining_machine")
            .notMirrored()
            .structure(() -> IMMultiblocks.SMALL_MINING_MACHINE)
            .gui(IMMenuTypes.SMALL_MINING_MACHINE)
            .build();

    public static final MultiblockRegistration<IndustrialFurnacesLogic.State> INDUSTRIAL_FURNACES = metal(new IndustrialFurnacesLogic(), "industrial_furnaces")
            .notMirrored()
            .structure(() -> IMMultiblocks.INDUSTRIAL_FURNACES)
            .gui(IMMenuTypes.INDUSTRIAL_FURNACES)
            .build();

    public static final MultiblockRegistration<GreenHouseLogic.State> GREEN_HOUSE = metalNoDefault(new GreenHouseLogic(), "green_house")
            .notMirrored()
            .structure(() -> IMMultiblocks.GREEN_HOUSE)
            .gui(IMMenuTypes.GREEN_HOUSE)
            .customBlock(BLOCK_REGISTER, ITEM_REGISTER, reg -> {
                BlockBehaviour.Properties properties = IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get().lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0);

                return new MultiblockPartBlock<>(properties, reg) {

                    @Override
                    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
                        super.createBlockStateDefinition(builder);
                        builder.add(BlockStateProperties.LIT);
                    }

                    {
                        registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.LIT, false));
                    }
                };

            }, MultiblockItem::new)
            .build();

    public static final MultiblockRegistration<CoilLogic.State> COIL = coil(new CoilLogic(), "coil")
            .notMirrored()
            .structure(() -> IMMultiblocks.COIL)
            .build();

    public static void init(IEventBus bus) {
        BLOCK_REGISTER.register(bus);
        ITEM_REGISTER.register(bus);
        BE_REGISTER.register(bus);
    }


    public static <S extends IMultiblockState> IEMultiblockBuilder<S> metal(IMultiblockLogic<S> logic, String name) {
        return new IEMultiblockBuilder<>(logic, name)
                .defaultBEs(BE_REGISTER)
                .defaultBlock(BLOCK_REGISTER, ITEM_REGISTER, IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get());
    }

    public static IEMultiblockBuilder<CoilLogic.State> coil(IMultiblockLogic<CoilLogic.State> logic, String name) {
        return new IEMultiblockBuilder<>(logic, name)
                .defaultBEs(BE_REGISTER)
                .customBlock(BLOCK_REGISTER, ITEM_REGISTER, reg -> new IMCoilPartBlock(IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get(), reg), IMMultiblockItem::new);
    }

    public static <S extends IMultiblockState> IEMultiblockBuilder<S> metalNoDefault(IMultiblockLogic<S> logic, String name) {
        return new IEMultiblockBuilder<>(logic, name)
                .defaultBEs(BE_REGISTER);
    }

}
