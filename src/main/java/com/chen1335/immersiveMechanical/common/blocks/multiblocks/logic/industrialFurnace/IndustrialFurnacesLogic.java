package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace;

import blusunrize.immersiveengineering.api.energy.AveragingEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInMachine;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.function.Function;

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

    @Override
    public void tickServer(IMultiblockContext<State> context) {

        IndustrialFurnacesLogic.State state = context.getState();
        IMultiblockLevel level = context.getLevel();
        state.energy.receiveEnergy(10000,false);
        boolean tickedAny = state.processor.tickServer(state, level, true);
        if (state.active != tickedAny) {
            state.active = tickedAny;
            context.requestMasterBESync();
        }
        if (state.energy.getEnergyStored() <= 0) return;

        if (state.processor.getQueueSize() < state.processor.getMaxQueueSize())
            enqueueProcesses(state, level.getRawLevel());
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
            MultiblockProcessInMachine<IndustrialFurnaceRecipe> process = new MultiblockProcessInMachine<>(recipe, slot);

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

    public static class State implements IMultiblockState, ProcessContext.ProcessContextInMachine<IndustrialFurnaceRecipe> {

        private final AveragingEnergyStorage energy = new AveragingEnergyStorage(ENERGY_CAPACITY);
        private final MultiblockProcessor.InMachineProcessor<IndustrialFurnaceRecipe> processor;
        public ItemStackHandler inventory = new ItemStackHandler(18) {
            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }
        };

        public boolean active;

        public State(IInitialMultiblockContext<State> context) {
            this.processor = new MultiblockProcessor.InMachineProcessor<>(
                    9, i -> 0, 9, context.getMarkDirtyRunnable(), context.getSyncRunnable(), IndustrialFurnaceRecipe.RECIPES::getById
            );
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
            processor.fromNBT(nbt.get("processor"), (getRecipe, data, p) -> new MultiblockProcessInMachine<>(getRecipe, data), provider);
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
