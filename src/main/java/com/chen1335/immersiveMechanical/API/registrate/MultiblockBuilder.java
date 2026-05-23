package com.chen1335.immersiveMechanical.API.registrate;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.api.multiblocks.TemplateMultiblock;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistrationBuilder;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockItem;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.IEMultiblockBuilder;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import com.chen1335.immersiveMechanical.API.registrate.data.IMLootHelper;
import com.google.common.base.Preconditions;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public class MultiblockBuilder<S extends IMultiblockState, L extends IMultiblockLogic<S>> {
    private final List<Consumer<MultiblockDefinition<S, L>>> onRegister = new ArrayList<>();
    private final IERegistrate owner;
    private final String name;
    private final Supplier<L> logicSupplier;
    private final IMultiblockFactory multiblockFactory;

    private final MultiblockRegistrationBuilder.RegistrationMethod<Block> blockRegistrationMethod = new MultiblockRegistrationBuilder.RegistrationMethod<Block>() {
        @Override
        public <T extends Block> Supplier<T> register(String path, Supplier<T> makeInstance) {
            return owner.block(path, properties1 -> makeInstance.get())
                    .blockstate(NonNullBiConsumer.noop())
                    .loot(IMLootHelper::registerMultiblock)
                    .register();
        }
    };

    private final MultiblockRegistrationBuilder.RegistrationMethod<Item> itemRegistrationMethod = new MultiblockRegistrationBuilder.RegistrationMethod<Item>() {
        @Override
        public <T extends Item> Supplier<T> register(String path, Supplier<T> makeInstance) {
            return owner.item(path, properties1 -> makeInstance.get())
                    .model(NonNullBiConsumer.noop())
                    .register();
        }
    };

    private final DeferredRegister<BlockEntityType<?>> multiblockBes;
    private BlockPos masterFromOrigin;
    private BlockPos triggerFromOrigin;
    private List<BlockMatcher.MatcherPredicate> additionalPredicates = List.of();
    private BlockPos size;
    private float manualScale;
    private BlockBehaviour.Properties properties = IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get();
    private boolean mirrorable = true;
    private IEMenuTypes.MultiblockContainer<S, ?> menu;
    private Function<MultiblockRegistration<S>, ? extends MultiblockPartBlock<S>> makeBlock = reg -> {
        if (reg.mirrorable())
            return new MultiblockPartBlock.WithMirrorState<>(properties, reg);
        else
            return new MultiblockPartBlock<>(properties, reg);
    };

    private Function<Block, Item> makeItem = MultiblockItem::new;

    public MultiblockBuilder(IERegistrate owner,
                             String name,
                             Supplier<L> logicSupplier,
                             IMultiblockFactory multiblockFactory,
                             DeferredRegister<BlockEntityType<?>> multiblockBes
    ) {
        this.owner = owner;
        this.name = name;
        this.logicSupplier = logicSupplier;
        this.multiblockFactory = multiblockFactory;
        this.multiblockBes = multiblockBes;
    }


    public MultiblockBuilder<S, L> onRegister(Consumer<MultiblockDefinition<S, L>> consumer) {
        onRegister.add(consumer);
        return this;
    }

    public MultiblockBuilder<S, L> masterFromOrigin(BlockPos masterFromOrigin) {
        this.masterFromOrigin = masterFromOrigin;
        return this;
    }

    public MultiblockBuilder<S, L> triggerFromOrigin(BlockPos triggerFromOrigin) {
        this.triggerFromOrigin = triggerFromOrigin;

        return this;
    }

    public MultiblockBuilder<S, L> size(BlockPos size) {
        this.size = size;
        return this;
    }

    public MultiblockBuilder<S, L> additionalPredicates(List<BlockMatcher.MatcherPredicate> additionalPredicates) {
        this.additionalPredicates = additionalPredicates;
        return this;
    }

    public MultiblockBuilder<S, L> manualScale(float manualScale) {
        this.manualScale = manualScale;
        return this;
    }

    public MultiblockBuilder<S, L> properties(BlockBehaviour.Properties properties) {
        this.properties = properties;
        return this;
    }


    public MultiblockBuilder<S, L> notMirrored() {
        this.mirrorable = false;
        return this;
    }

    public MultiblockBuilder<S, L> gui(IEMenuTypes.MultiblockContainer<S, ?> menu) {
        this.menu = menu;
        return this;
    }

    public MultiblockBuilder<S, L> customBlock(Function<MultiblockRegistration<S>, ? extends MultiblockPartBlock<S>> makeBlock) {
        this.makeBlock = makeBlock;
        return this;
    }

    public MultiblockBuilder<S, L> customItem(Function<Block, Item> makeItem) {
        this.makeItem = makeItem;
        return this;
    }


    public MultiblockDefinition<S, L> register() {
        Objects.requireNonNull(masterFromOrigin);
        Objects.requireNonNull(triggerFromOrigin);
        Objects.requireNonNull(size);
        Preconditions.checkState(manualScale != 0);

        L logic = logicSupplier.get();
        Mutable<TemplateMultiblock> typeBox = new MutableObject<>();
        IEMultiblockBuilder<S> builder = new IEMultiblockBuilder<>(logic, name)
                .customBlock(blockRegistrationMethod, itemRegistrationMethod, makeBlock, makeItem)
                .defaultBEs(multiblockBes)
                .structure(typeBox::getValue);


        if (!mirrorable) {
            builder.notMirrored();
        }

        if (menu != null) {
            builder.gui(menu);
        }
        MultiblockRegistration<S> registration = builder.build();
        IETemplateMultiblock multiblock = multiblockFactory.create(
                ResourceLocation.fromNamespaceAndPath(owner.getModid(), "multiblocks/%s".formatted(name)),
                masterFromOrigin,
                triggerFromOrigin,
                size,
                registration,
                additionalPredicates,
                manualScale
        );

        typeBox.setValue(multiblock);
        MultiblockHandler.registerMultiblock(multiblock);
        MultiblockDefinition<S, L> definition = new MultiblockDefinition<>(logicSupplier.get(), multiblock, registration);
        onRegister.forEach(consumer -> consumer.accept(definition));
        return definition;
    }
}
