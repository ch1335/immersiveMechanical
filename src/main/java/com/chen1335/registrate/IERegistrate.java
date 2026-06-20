package com.chen1335.registrate;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.MultiblockBEType;
import blusunrize.immersiveengineering.common.blocks.metal.BasicConnectorBlock;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.mixins.immersive_mechanical.ArgContainerInvoker;
import com.chen1335.immersiveMechanical.mixins.immersive_mechanical.EnergyConnectorBlockEntityAccessor;
import com.chen1335.immersiveMechanical.mixins.immersive_mechanical.MultiblockContainerInvoker;
import com.chen1335.registrate.builder.MetalBuilder;
import com.chen1335.registrate.builder.MultiblockBuilder;
import com.chen1335.registrate.builder.RecipeBuilder;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IERegistrate extends AbstractRegistrate<IERegistrate> {
    private final DeferredRegister<BlockEntityType<?>> beRegister = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ImmersiveMechanical.MODID);

    public static final List<MultiblockHandler.IMultiblock> ALL_MULTIBLOCKS = new ArrayList<>();

    private final List<MultiblockHandler.IMultiblock> multiblocks = new ArrayList<>();

    private final List<MetalDefinition> metalDefinitions = new ArrayList<>();

    public static IERegistrate create(String modid) {
        return new IERegistrate(modid);
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
        beRegister.register(bus);
        return super.registerEventListeners(bus);
    }

    protected IERegistrate(String modid) {
        super(modid);
    }

    public <S extends IMultiblockState, L extends IMultiblockLogic<S>> MultiblockBuilder<S, L> multiblock(String name, Supplier<L> lSupplier) {
        return this.multiblock(name, lSupplier, SimpleMultiblock::new);
    }

    public <S extends IMultiblockState, L extends IMultiblockLogic<S>> MultiblockBuilder<S, L> multiblock(String name, Supplier<L> lSupplier, IMultiblockFactory multiblockFactory) {
        return new MultiblockBuilder<>(this, name, lSupplier, multiblockFactory, beRegister).onRegister(definition -> {
            multiblocks.add(definition.multiblock());
            ALL_MULTIBLOCKS.add(definition.multiblock());
        });
    }

    public BlockEntityEntry<EnergyConnectorBlockEntity> registerConnector(String name, String cat, boolean relay, float length, int maxTransfer, BlockEntry<BasicConnectorBlock<EnergyConnectorBlockEntity>> availableBlock) {
        Pair<String, Boolean> pair = Pair.of(cat, relay);
        BlockEntityEntry<EnergyConnectorBlockEntity> register = this.blockEntity(name, (BlockEntityBuilder.BlockEntityFactory<EnergyConnectorBlockEntity>) (type, pos, state) -> new EnergyConnectorBlockEntity(type, pos, state) {
                    @Override
                    public int getMaxInput() {
                        return maxTransfer;
                    }

                    @Override
                    public int getMaxOutput() {
                        return maxTransfer;
                    }
                })
                .validBlock(availableBlock)
                .register();
        EnergyConnectorBlockEntity.NAME_TO_SPEC.put(register.getId(), pair);
        EnergyConnectorBlockEntity.SPEC_TO_TYPE.put(pair, register);
        EnergyConnectorBlockEntityAccessor.IM$getLength().put(pair, length);
        return register;
    }

    public <T extends BlockEntity & IEBlockInterfaces.IGeneralMultiblock> MultiblockBEType<T> makeMultiblock(String name, MultiblockBEType.BEWithTypeConstructor<T> make, Supplier<? extends Block> block) {
        return new MultiblockBEType<>(
                name, beRegister, make, block, state -> state.hasProperty(IEProperties.MULTIBLOCKSLAVE) && !state.getValue(IEProperties.MULTIBLOCKSLAVE)
        );
    }

    public <S extends IMultiblockState, M extends IEContainerMenu, U extends Screen & MenuAccess<M>> IEMenuTypes.MultiblockContainer<S, M> multiblockMenu(
            String name,
            IEMenuTypes.ArgContainerConstructor<IEContainerMenu.MultiblockMenuContext<S>, M> container,
            IEMenuTypes.ClientContainerConstructor<M> client,
            NonNullSupplier<MenuBuilder.ScreenFactory<M, U>> supplier
    ) {

        MenuBuilder<M, U, IERegistrate> menu = this.menu(name, client::construct, supplier);
        return MultiblockContainerInvoker.IM$create(DeferredHolder.create(menu.getRegistryKey(), menu.register().getId()), container);
    }

    public <S, M extends IEContainerMenu, U extends Screen & MenuAccess<M>> IEMenuTypes.ArgContainer<S, M> argMenu(
            String name,
            IEMenuTypes.ArgContainerConstructor<S, M> container,
            IEMenuTypes.ClientContainerConstructor<M> client,
            NonNullSupplier<MenuBuilder.ScreenFactory<M, U>> supplier
    ) {

        MenuBuilder<M, U, IERegistrate> menu = this.menu(name, client::construct, supplier);
        return ArgContainerInvoker.IM$create(DeferredHolder.create(menu.getRegistryKey(), menu.register().getId()), container);
    }

    public <T extends Recipe<?>> RecipeBuilder<T> recipeType(String name, Class<T> clazz, Supplier<IERecipeSerializer<T>> supplier) {
        return entry(name, callBack -> new RecipeBuilder<>(this, this, name, callBack, clazz, supplier));
    }

    public List<MultiblockHandler.IMultiblock> getMultiblocks() {
        return multiblocks;
    }

    public MetalBuilder metal(String name) {
        return new MetalBuilder(this, name);
    }

    public List<MetalDefinition> getMetalDefinitions() {
        return metalDefinitions;
    }
}
