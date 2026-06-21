package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.pyrolyseOven;

import blusunrize.immersiveengineering.api.energy.AveragingEnergyStorage;
import blusunrize.immersiveengineering.api.fluid.FluidUtils;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.*;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler;
import blusunrize.immersiveengineering.common.util.CachedRecipe;
import blusunrize.immersiveengineering.common.util.inventory.WrappingItemHandler;
import com.chen1335.immersiveMechanical.API.ICoilModifiableMultiblockState;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilInfo;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes.PyrolyseOvenShape;
import com.chen1335.immersiveMechanical.recipe.PyrolyseOvenRecipe;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PyrolyseOvenLogic implements IMultiblockLogic<PyrolyseOvenLogic.State>, IServerTickableComponent<PyrolyseOvenLogic.State>, IClientTickableComponent<PyrolyseOvenLogic.State> {
    public static int DEFAULT_PARALLEL_MUL = 4;
    public static final int ENERGY_CAPACITY = 64000;
    public static final int OUT_SLOT_COUNT = 4;
    public static final int FIRST_OUT_SLOT = 4;
    public static final int IN_SLOT_COUNT = 4;
    public static final int FIRST_IN_SLOT = 0;
    public static final int TANK_CAPACITY = 48 * FluidType.BUCKET_VOLUME;
    private static final int[] OUTPUT_SLOTS = Util.make(new int[OUT_SLOT_COUNT], slots -> {
        for (int i = 0; i < OUT_SLOT_COUNT; ++i)
            slots[i] = FIRST_OUT_SLOT + i;
    });
    public static final int EMPTY_CONTAINER_SLOT = 8;
    public static final int FULL_CONTAINER_SLOT = 9;


    private static final CapabilityPosition ENERGY_INPUT = new CapabilityPosition(1, 3, 4, RelativeBlockFace.UP);
    private static final CapabilityPosition ITEM_INPUT = new CapabilityPosition(1, 2, 5, RelativeBlockFace.BACK);
    private static final CapabilityPosition ITEM_OUTPUT = new CapabilityPosition(1, 2, 0, RelativeBlockFace.FRONT);
    private static final CapabilityPosition FLUID_OUTPUT = new CapabilityPosition(4, 0, 0, RelativeBlockFace.FRONT);

    private static final MultiblockFace AUTO_ITEM_OUTPUT = new MultiblockFace(1, 2, -1, RelativeBlockFace.FRONT);
    private static final MultiblockFace AUTO_FLUID_OUTPUT = new MultiblockFace(4, 0, -1, RelativeBlockFace.FRONT);

    @Override
    public void tickClient(IMultiblockContext<State> context) {

    }

    @Override
    public void tickServer(IMultiblockContext<State> context) {
        State state = context.getState();

        IMultiblockLevel level = context.getLevel();
        if (level.shouldTickModulo(8) || !state.initialized) {
            state.coilInfo = null;
            Block coilBlock1 = null;
            Block coilBlock2 = null;
            BlockEntity blockEntity1 = context.getLevel().getBlockEntity(new BlockPos(0, 1, 2));
            if (blockEntity1 instanceof IMultiblockBE<?> be) {
                if (be.getHelper().getState() instanceof CoilLogic.State coilState) {
                    coilBlock1 = coilState.getCoilBlock();
                }
            }
            BlockEntity blockEntity2 = context.getLevel().getBlockEntity(new BlockPos(0, 1, 3));
            if (blockEntity2 instanceof IMultiblockBE<?> be) {
                if (be.getHelper().getState() instanceof CoilLogic.State coilState) {
                    coilBlock2 = coilState.getCoilBlock();
                }
            }
            if (coilBlock1 == coilBlock2 && coilBlock2 != null) {
                state.coilInfo = CoilInfo.COIL_INFO_MAP.getOrDefault(coilBlock1, CoilInfo.DEFAULT);
            }
            state.initialized = true;
        }

        if (level.shouldTickModulo(10)) {
            IItemHandler outputHandler = state.itemOutput.get();
            if (outputHandler != null) {
                for (int i = FIRST_OUT_SLOT; i < FIRST_OUT_SLOT + IN_SLOT_COUNT; i++) {
                    ItemStack slot = state.inventory.getStackInSlot(i);
                    if (!slot.isEmpty()) {
                        ItemStack itemStack = ItemHandlerHelper.insertItemStacked(outputHandler, slot, false);
                        state.inventory.setStackInSlot(i, itemStack);
                    }
                }
            }
        }

        IFluidHandler fluidHandler = state.fluidOutput.get();
        if (fluidHandler != null) {
            int fill = fluidHandler.fill(state.tank.getFluid(), IFluidHandler.FluidAction.EXECUTE);
            state.tank.drain(fill, IFluidHandler.FluidAction.EXECUTE);
        }


        if (state.energyStorage.getEnergyStored() <= 0) return;

        if (state.coilInfo == null) return;

        if (state.process > 0) {
            if (noInput(state)) {
                state.process = 0;
                state.processMax = 0;
                state.active = false;
            } else {
                RecipeHolder<PyrolyseOvenRecipe> recipe = getRecipe(context);
                FluidStack fluid = state.tank.getFluid();
                if (recipe == null || (!fluid.isEmpty() && !recipe.value().getFluidOutput().is(fluid.getFluidType())) || recipe.value().getBaseTime() != state.originalProcessMax) {
                    state.process = 0;
                    state.processMax = 0;
                    state.active = false;
                } else {
                    int energyCost = (int) (recipe.value().getBaseEnergy() * state.coilInfo.energyModify() / state.processMax);
                    if (state.energyStorage.getEnergyStored() >= energyCost) {
                        state.process--;
                        state.energyStorage.extractEnergy(energyCost, false);
                    }

                }

            }
            context.markMasterDirty();
        } else {
            RecipeHolder<PyrolyseOvenRecipe> recipe = getRecipe(context);
            if (recipe != null && state.active) {
                PyrolyseOvenRecipe value = recipe.value();
                int parallelPerRecipe = value.getInput().getCount();
                int maxParallel = parallelPerRecipe * DEFAULT_PARALLEL_MUL;
                int successAmount = 0;
                for (int i = maxParallel; i > 0; i--) {
                    int amount = value.getFluidOutput().getAmount();
                    int needFill = amount * i;
                    int fill = state.tank.fill(value.getFluidOutput().copyWithAmount(needFill), IFluidHandler.FluidAction.SIMULATE);
                    if (needFill == fill) {
                        successAmount = i;
                        break;
                    }

                }

                for (int i = successAmount; i > 0; i--) {
                    ItemStack copy = recipe.value().getOutput().get().copyWithCount(i);

                    ItemStack result = ItemHandlerHelper.insertItemStacked(state.recipeOutputHandler, copy, true);
                    if (result.isEmpty()) {
                        successAmount = i;
                        break;
                    }
                }
                if (successAmount > 0) {
                    int finalSuccess;
                    int j = successAmount;
                    for (int i = 0; i < IN_SLOT_COUNT; i++) {
                        ItemStack slot = state.inventory.getStackInSlot(i);
                        if (!slot.isEmpty()) {
                            while (j > 0) {
                                if (value.matches(slot) && slot.getCount() >= 1) {
                                    slot.shrink(1);
                                } else {
                                    break;
                                }
                                j--;
                            }
                        }
                    }
                    finalSuccess = successAmount - j;

                    if (finalSuccess > 0) {
                        FluidStack fluidStack = value.getFluidOutput().copyWithAmount(value.getFluidOutput().getAmount() * finalSuccess);
                        state.tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        ItemStack itemStack = value.getOutput().get().copyWithCount(value.getOutput().get().getCount() * finalSuccess);
                        ItemHandlerHelper.insertItemStacked(state.recipeOutputHandler, itemStack, false);
                    }
                    state.active = false;
                }

            } else {
                RecipeHolder<PyrolyseOvenRecipe> recipeHolder = getRecipe(context);
                if (recipeHolder != null) {
                    PyrolyseOvenRecipe value = recipeHolder.value();
                    state.originalProcessMax = value.getBaseTime();
                    state.processMax = (int) ((int) (state.originalProcessMax / state.coilInfo.timeModify()) * 0.75);
                    state.process = state.processMax;
                    state.active = true;
                }
            }
        }


        if (state.tank.getFluidAmount() > 0 && FluidUtils.fillFluidContainer(
                state.tank, EMPTY_CONTAINER_SLOT, FULL_CONTAINER_SLOT, state.inventory
        ))
            context.markMasterDirty();

    }

    @Nullable
    public RecipeHolder<PyrolyseOvenRecipe> getRecipe(IMultiblockContext<PyrolyseOvenLogic.State> context) {
        PyrolyseOvenLogic.State state = context.getState();
        RecipeHolder<PyrolyseOvenRecipe> apply = state.cachedRecipe.apply(context.getLevel().getRawLevel());
        if (apply == null) {
            return null;
        }
        PyrolyseOvenRecipe recipe = apply.value();

        if (!state.tank.isEmpty() && !state.tank.getFluid().is(recipe.getFluidOutput().getFluidType())) {
            return null;
        }

        if (!state.tank.isEmpty() && state.tank.getCapacity() - state.tank.getFluidAmount() < recipe.getFluidOutput().getAmount()) {
            return null;
        }
        for (int i = 4; i < OUT_SLOT_COUNT + 4; i++) {
            ItemStack slot = state.inventory.getStackInSlot(i);
            if (slot.isEmpty() || (slot.is(recipe.getOutput().get().getItem())) && slot.getMaxStackSize() - slot.getCount() > recipe.getOutput().get().getCount()) {
                return apply;
            }
        }
        return null;
    }

    public boolean noInput(State state) {
        for (int i = 0; i < IN_SLOT_COUNT; i++) {
            ItemStack slot = state.inventory.getStackInSlot(i);
            if (!slot.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void dropExtraItems(State state, Consumer<ItemStack> drop) {
        MBInventoryUtils.dropItems(state.inventory, drop);
    }

    @Override
    public State createInitialState(IInitialMultiblockContext<State> context) {
        return new State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return new PyrolyseOvenShape();
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<PyrolyseOvenLogic.State> register) {
        register.register(Capabilities.EnergyStorage.BLOCK, (state, position) -> {
            return position.side() != null && !ENERGY_INPUT.equals(position) ? null : state.energyStorage;
        });

        register.register(Capabilities.ItemHandler.BLOCK, (state, position) -> {
            return position.side() != null && !ITEM_INPUT.equals(position) ? null : state.inputHandler;
        });

        register.register(Capabilities.ItemHandler.BLOCK, (state, position) -> {
            return position.side() != null && !ITEM_OUTPUT.equals(position) ? null : state.outputHandler;
        });

        register.register(Capabilities.FluidHandler.BLOCK, (state, position) -> {
            return position.side() != null && !FLUID_OUTPUT.equals(position) ? null : state.fluidHandler;
        });
    }


    public static class State implements IMultiblockState, ICoilModifiableMultiblockState, ProcessContext.ProcessContextInMachine<PyrolyseOvenRecipe> {
        public ItemStackHandler inventory = new ItemStackHandler(10) {
            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }
        };
        public AveragingEnergyStorage energyStorage = new AveragingEnergyStorage(64000);
        public FluidTank tank = new FluidTank(TANK_CAPACITY);
        public final IFluidHandler fluidHandler;
        public final IItemHandler inputHandler = new WrappingItemHandler(inventory, true, false, new WrappingItemHandler.IntRange(0, 4));
        public final IItemHandler outputHandler = new WrappingItemHandler(inventory, false, true, new WrappingItemHandler.IntRange(4, 8));
        public final IItemHandler recipeOutputHandler = new WrappingItemHandler(inventory, true, false, new WrappingItemHandler.IntRange(4, 8));

        public CoilInfo coilInfo;
        public boolean initialized = false;

        private final Function<Level, RecipeHolder<PyrolyseOvenRecipe>> cachedRecipe;
        public int process = 0;
        public int processMax = 0;
        public int originalProcessMax = 0;
        public boolean active = false;
        public final Supplier<IItemHandler> itemOutput;
        public final Supplier<IFluidHandler> fluidOutput;

        public State(IInitialMultiblockContext<State> context) {
            fluidHandler = ArrayFluidHandler.drainOnly(tank, context.getMarkDirtyRunnable());
            cachedRecipe = CachedRecipe.cachedSkip1(PyrolyseOvenRecipe::findRecipe, this::getInput);
            itemOutput = context.getCapabilityAt(Capabilities.ItemHandler.BLOCK, AUTO_ITEM_OUTPUT);
            fluidOutput = context.getCapabilityAt(Capabilities.FluidHandler.BLOCK, AUTO_FLUID_OUTPUT);
        }


        public ItemStack getInput() {
            for (int i = 0; i < IN_SLOT_COUNT; i++) {
                ItemStack slot = inventory.getStackInSlot(i);
                if (!slot.isEmpty()) {
                    return slot;
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            nbt.put("tank", tank.writeToNBT(provider, new CompoundTag()));
            nbt.putInt("process", process);
            nbt.putInt("processMax", processMax);
            nbt.putInt("originalProcessMax", originalProcessMax);
            nbt.putBoolean("active", active);
            nbt.put("inventory", inventory.serializeNBT(provider));
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            tank.readFromNBT(provider, nbt.getCompound("tank"));
            process = nbt.getInt("process");
            processMax = nbt.getInt("processMax");
            originalProcessMax = nbt.getInt("originalProcessMax");
            active = nbt.getBoolean("active");
            inventory.deserializeNBT(provider, nbt.getCompound("inventory"));
        }

        @Override
        public AveragingEnergyStorage getEnergy() {
            return energyStorage;
        }

        @Override
        public CoilInfo getCoilInfo() {
            return coilInfo;
        }
    }
}
