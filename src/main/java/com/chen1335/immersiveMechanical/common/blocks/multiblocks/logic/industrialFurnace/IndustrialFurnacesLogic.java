package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace;

import blusunrize.immersiveengineering.api.energy.AveragingEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.CapabilityPosition;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.MultiblockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.RelativeBlockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInMachine;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import blusunrize.immersiveengineering.common.util.inventory.WrappingItemHandler;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilInfo;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class IndustrialFurnacesLogic implements IMultiblockLogic<IndustrialFurnacesLogic.State>, IServerTickableComponent<IndustrialFurnacesLogic.State> {
    public static final int ENERGY_CAPACITY = 64000;
    public static final int OUT_SLOT_COUNT = 9;
    public static final int FIRST_OUT_SLOT = 9;
    public static final int IN_SLOT_COUNT = 9;
    public static final int FIRST_IN_SLOT = 0;
    private static final int[] OUTPUT_SLOTS = Util.make(new int[OUT_SLOT_COUNT], slots -> {
        for (int i = 0; i < OUT_SLOT_COUNT; ++i)
            slots[i] = FIRST_OUT_SLOT + i;
    });

    private static final Set<CapabilityPosition> ENERGY_INPUTS = Set.of(new CapabilityPosition(1, 2, 1, RelativeBlockFace.UP));

    private static final Set<CapabilityPosition> ITEM_INPUTS = Set.of(new CapabilityPosition(0, 0, 1, RelativeBlockFace.RIGHT));

    private static final Set<CapabilityPosition> ITEM_OUTPUTS = Set.of(new CapabilityPosition(2, 0, 1, RelativeBlockFace.RIGHT));

    private static final MultiblockFace MAIN_OUT_POS = new MultiblockFace(3, 0, 1, RelativeBlockFace.LEFT);

    @Override
    public void tickServer(IMultiblockContext<State> context) {
        IMultiblockLevel level = context.getLevel();
        IndustrialFurnacesLogic.State state = context.getState();
        if (level.shouldTickModulo(8) || !state.initialized) {
            state.coilInfo = null;
            outputItems(state);
            BlockEntity blockEntity = context.getLevel().getBlockEntity(new BlockPos(0, 1, 1));
            if (blockEntity instanceof IMultiblockBE<?> be) {
                if (be.getHelper().getState() instanceof CoilLogic.State coilState) {
                    state.coilInfo = CoilInfo.COIL_INFO_MAP.getOrDefault(coilState.getCoilBlock(), CoilInfo.DEFAULT);
                }
            }
            state.initialized = true;
        }

        if (state.coilInfo == null) {
            return;
        }


        boolean tickedAny = state.processor.tickServer(state, level, true);
        if (state.active != tickedAny) {
            state.active = tickedAny;
            context.requestMasterBESync();
        }
        if (state.energy.getEnergyStored() <= 0) return;

        if (state.processor.getQueueSize() < state.processor.getMaxQueueSize())
            enqueueProcesses(state, level.getRawLevel());

    }

    private void outputItems(State state) {
        IItemHandler outputHandler = state.output.get();
        if (outputHandler != null)
            for (int j : OUTPUT_SLOTS) {
                final ItemStack nextStack = state.inventory.getStackInSlot(j);
                if (nextStack.isEmpty())
                    continue;
                ItemStack stack = nextStack.copyWithCount(1);
                stack = ItemHandlerHelper.insertItem(outputHandler, stack, false);
                if (stack.isEmpty())
                    nextStack.shrink(1);
            }
    }

    private void enqueueProcesses(State state, Level level) {
        Int2IntOpenHashMap usedInvSlots = new Int2IntOpenHashMap();

        for (MultiblockProcess<IndustrialFurnaceRecipe, ProcessContext.ProcessContextInMachine<IndustrialFurnaceRecipe>> process : state.processor.getQueue())
            if (process instanceof MultiblockProcessInMachine<IndustrialFurnaceRecipe> arcProcess) {
                int[] inputSlots = arcProcess.getInputSlots();
                int[] inputAmounts = arcProcess.getInputAmounts();
                if (inputAmounts == null)
                    continue;
                for (int i = 0; i < inputSlots.length; i++)
                    if (inputAmounts[i] > 0)
                        usedInvSlots.addTo(inputSlots[i], inputAmounts[i]);
            }

        for (int slot = FIRST_IN_SLOT; slot < IN_SLOT_COUNT; slot++) {
            if (usedInvSlots.containsKey(slot))
                continue;
            ItemStack stack = state.inventory.getStackInSlot(slot);
            if (stack.isEmpty())
                continue;
            RecipeHolder<IndustrialFurnaceRecipe> recipe = IndustrialFurnaceRecipe.findRecipe(level, stack);
            if (recipe == null)
                continue;
            MultiblockProcessInMachine<IndustrialFurnaceRecipe> process = new IndustrialFurnaceProcess(recipe, state, slot);

            if (state.processor.addProcessToQueue(process, level, false)) {
                process.setInputAmounts(1);
            }
        }
    }

    @Override
    public State createInitialState(IInitialMultiblockContext<State> context) {
        return new State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return blockPos -> Shapes.block();
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<IndustrialFurnacesLogic.State> register) {
        register.register(Capabilities.EnergyStorage.BLOCK, (state, position) -> {
            return position.side() != null && !ENERGY_INPUTS.contains(position) ? null : state.energy;
        });

        register.register(Capabilities.ItemHandler.BLOCK, (state, position) -> {
            return position.side() != null && !ITEM_INPUTS.contains(position) ? null : state.inputHandler;
        });

        register.register(Capabilities.ItemHandler.BLOCK, (state, position) -> {
            return position.side() != null && !ITEM_OUTPUTS.contains(position) ? null : state.outputHandler;
        });
    }


    public static class State implements IMultiblockState, ProcessContext.ProcessContextInMachine<IndustrialFurnaceRecipe> {
        public boolean initialized = false;
        private final AveragingEnergyStorage energy = new AveragingEnergyStorage(ENERGY_CAPACITY);
        private final MultiblockProcessor.InMachineProcessor<IndustrialFurnaceRecipe> processor;
        public final WrappingItemHandler outputHandler;
        public final Supplier<IItemHandler> output;
        public ItemStackHandler inventory = new ItemStackHandler(18) {
            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }
        };

        public final IItemHandler inputHandler;
        public boolean active;
        public CoilInfo coilInfo;

        public State(IInitialMultiblockContext<State> context) {
            this.processor = new MultiblockProcessor.InMachineProcessor<>(
                    9, i -> 0, 9, context.getMarkDirtyRunnable(), context.getSyncRunnable(), IndustrialFurnaceRecipe.RECIPES::getById
            );
            inputHandler = new IndustrialFurnacesInputHandler(inventory, context.getMarkDirtyRunnable());

            this.outputHandler = new WrappingItemHandler(
                    inventory, false, true, new WrappingItemHandler.IntRange(FIRST_OUT_SLOT, FIRST_OUT_SLOT + OUT_SLOT_COUNT)
            );

            this.output = context.getCapabilityAt(Capabilities.ItemHandler.BLOCK, MAIN_OUT_POS);
        }

        public List<MultiblockProcess<IndustrialFurnaceRecipe, ProcessContextInMachine<IndustrialFurnaceRecipe>>> getProcessQueue() {
            return processor.getQueue();
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            nbt.put("energy", energy.serializeNBT(provider));
            nbt.put("inventory", inventory.serializeNBT(provider));
            nbt.put("processor", this.processor.toNBT(provider));
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            energy.deserializeNBT(provider, nbt.getCompound("energy"));
            inventory.deserializeNBT(provider, nbt.getCompound("inventory"));
            processor.fromNBT(nbt.get("processor"), (getRecipe, data, p) -> new IndustrialFurnaceProcess(getRecipe, this, data), provider);
        }

        @Override
        public ItemStackHandler getInventory() {
            return inventory;
        }

        @Override
        public int[] getOutputSlots() {
            return OUTPUT_SLOTS;
        }

        @Override
        public AveragingEnergyStorage getEnergy() {
            return energy;
        }
    }

}
