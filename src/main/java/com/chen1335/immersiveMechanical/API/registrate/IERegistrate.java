package com.chen1335.immersiveMechanical.API.registrate;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IERegistrate extends AbstractRegistrate<IERegistrate> {
    private final DeferredRegister<Block> multiblockBlocks = DeferredRegister.create(BuiltInRegistries.BLOCK, ImmersiveMechanical.MODID);
    private final DeferredRegister<Item> multiblockItems = DeferredRegister.create(BuiltInRegistries.ITEM, ImmersiveMechanical.MODID);
    private final DeferredRegister<BlockEntityType<?>> multiblockBes = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ImmersiveMechanical.MODID);

    private final List<MultiblockHandler.IMultiblock> multiblocks = new ArrayList<>();

    public static IERegistrate create(String modid) {
        return new IERegistrate(modid);
    }

    public DeferredRegister<Block> getMultiblockBlocks() {
        return multiblockBlocks;
    }

    @Override
    public <T extends RegistrateProvider> void genData(ProviderType<? extends T> type, T gen) {
        if (type == ProviderType.LANG) {
            return;
        }
        super.genData(type, gen);
    }

    @Override
    public IERegistrate registerEventListeners(IEventBus bus) {
        multiblockBlocks.register(bus);
        multiblockItems.register(bus);
        multiblockBes.register(bus);
        return super.registerEventListeners(bus);
    }

    protected IERegistrate(String modid) {
        super(modid);
    }

    public <S extends IMultiblockState, L extends IMultiblockLogic<S>> MultiblockBuilder<S, L> multiblock(String name, Supplier<L> lSupplier) {
        return this.multiblock(name, lSupplier, SimpleMultiblock::new);
    }

    public <S extends IMultiblockState, L extends IMultiblockLogic<S>> MultiblockBuilder<S, L> multiblock(String name, Supplier<L> lSupplier, IMultiblockFactory multiblockFactory) {
        return new MultiblockBuilder<>(this, name, lSupplier, multiblockFactory, multiblockBes).onRegister(definition -> multiblocks.add(definition.multiblock()));
    }

    public List<MultiblockHandler.IMultiblock> getMultiblocks() {
        return multiblocks;
    }
}
