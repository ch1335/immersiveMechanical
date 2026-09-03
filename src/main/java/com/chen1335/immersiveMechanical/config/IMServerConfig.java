package com.chen1335.immersiveMechanical.config;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilInfo;
import com.chen1335.immersiveMechanical.definitions.IMBlocks;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import com.chen1335.immersiveMechanical.recipe.PyrolyseOvenRecipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ImmersiveMechanical.MODID)
public class IMServerConfig {
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading ev) {
        if (CONFIG_SPEC == ev.getConfig().getSpec()) {
            CoilInfo.register(IEBlocks.MetalDecoration.LV_COIL, COILS.lv_coil);
            CoilInfo.register(IEBlocks.MetalDecoration.MV_COIL, COILS.mv_coil);
            CoilInfo.register(IEBlocks.MetalDecoration.HV_COIL, COILS.hv_coil);
            CoilInfo.register(IMBlocks.COIL_NICHROME, COILS.nichrome_coil);
        }
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading ev) {
        if (CONFIG_SPEC == ev.getConfig().getSpec()) {
            CoilInfo.register(IEBlocks.MetalDecoration.LV_COIL, COILS.lv_coil);
            CoilInfo.register(IEBlocks.MetalDecoration.MV_COIL, COILS.mv_coil);
            CoilInfo.register(IEBlocks.MetalDecoration.HV_COIL, COILS.hv_coil);
            CoilInfo.register(IMBlocks.COIL_NICHROME, COILS.nichrome_coil);
        }
    }

    public static final ModConfigSpec CONFIG_SPEC;
    public static final IMServerConfig.Machines MACHINES;
    public static final IMServerConfig.Coils COILS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        MACHINES = new Machines(builder);
        COILS = new Coils(builder);
        CONFIG_SPEC = builder.build();
    }


    public static class Machines {
        public final ModConfigSpec.IntValue green_house_consumption;
        public final ModConfigSpec.DoubleValue green_house_fertilizer_efficiency;
        public final ModConfigSpec.DoubleValue green_house_fluid_efficiency;
        public final ModConfigSpec.IntValue small_mining_machine_consumption;
        public final ModConfigSpec.IntValue small_mining_machine_default_speed;
        public final ModConfigSpec.IntValue small_mining_machine_addition_speed_per_augers;
        public final ModConfigSpec.IntValue large_battery_core_capa;
        public final MultiblockRecipe.RecipeMultiplier industrial_furnaces_recipe_multiplier;
        public final MultiblockRecipe.RecipeMultiplier pyrolyse_oven_recipe_multiplier;
        public final ModConfigSpec.IntValue pyrolyse_oven_parallel_multiplier;
        public final ModConfigSpec.IntValue flywheel_energy_storage_coefficient;
        public final ModConfigSpec.IntValue flywheel_maximum_length;
        public final ModConfigSpec.IntValue flywheel_maximum_energy_transfer;
        public final ModConfigSpec.IntValue flywheel_basic_energy_transfer;
        public final ModConfigSpec.DoubleValue flywheel_maximum_transfer_requirement;

        public Machines(ModConfigSpec.Builder builder) {
            builder.push("machines");
            {
                builder.push("large_battery_core_capa");
                large_battery_core_capa = addPositive(builder, "storage_capacity", 64000000, "The energy storage capacity of large battery core");
                builder.pop();
            }
            {
                builder.push("green_house");
                green_house_consumption = addPositive(builder, "consumption", 12, "The Flux per tick the green house consumes to grow plants per seed slot");
                green_house_fertilizer_efficiency = builder
                        .comment("Compared to Cloche's fertilizer efficiency")
                        .defineInRange("fertilizer_efficiency", 3D, 1D, 100D);
                green_house_fluid_efficiency = builder
                        .comment("Compared to Cloche's fluid efficiency")
                        .defineInRange("fluid_efficiency", 1.5D, 1D, 100D);
                builder.pop();
            }
            {
                builder.push("small_mining_machine");
                small_mining_machine_consumption = addPositive(builder, "consumption", 512, "The Flux per tick the small mining machine consumes when active");
                small_mining_machine_default_speed = addPositive(builder, "default_speed", 5, "Number of scanned blocks per tick without augers");
                small_mining_machine_addition_speed_per_augers = addPositive(builder, "addition_speed_per_augers", 5, "Additional scanning speed provided by each auger");
                builder.pop();
            }

            industrial_furnaces_recipe_multiplier = addMachineEnergyTimeModifiers(builder, "industrial furnaces");
            {
                pyrolyse_oven_recipe_multiplier = addMachineEnergyTimeModifiers(builder, "pyrolyse oven", false);
                pyrolyse_oven_parallel_multiplier = addPositive(builder, "parallel_multiplier", 4, "Compared to the parallel multiples of coke ovens");
                builder.pop();
            }

            {
                builder.push("fly_wheel");
                flywheel_energy_storage_coefficient = addPositive(builder, "storage_coefficient", 1, "Maximum energy storage multiplier of flywheel");
                flywheel_maximum_length = addPositive(builder, "maximum_length", 10, "Maximum length of flywheel");
                flywheel_maximum_energy_transfer = addPositive(builder, "maximum_energy_transfer", 16384, "Maximum energy transfer of flywheel");
                flywheel_maximum_transfer_requirement = builder.comment("Maximum transfer speed requirement")
                        .defineInRange("flywheel_maximum_transfer_requirement", 0.5D, 0D, 1D);
                flywheel_basic_energy_transfer = addPositive(builder, "basic_energy_transfer", 4096, "Basic energy transfer of flywheel");

                builder.pop();
            }

            builder.pop();
        }


        private MultiblockRecipe.RecipeMultiplier addMachineEnergyTimeModifiers(ModConfigSpec.Builder builder, String machine) {
            return addMachineEnergyTimeModifiers(builder, machine, true);
        }

        private MultiblockRecipe.RecipeMultiplier addMachineEnergyTimeModifiers(ModConfigSpec.Builder builder, String machine, boolean popCategory) {
            builder.push(machine.replace(' ', '_'));
            ModConfigSpec.DoubleValue energy = builder
                    .comment("A modifier to apply to the energy costs of every " + machine + " recipe")
                    .defineInRange("energyModifier", 1, 1e-3, 1e3);
            ModConfigSpec.DoubleValue time = builder
                    .comment("A modifier to apply to the time of every " + machine + " recipe")
                    .defineInRange("timeModifier", 1, 1e-3, 1e3);
            if (popCategory)
                builder.pop();
            return new MultiblockRecipe.RecipeMultiplier(time::get, energy::get);
        }

        public void setUpConfig() {
            IndustrialFurnaceRecipe.MULTIPLIERS.setValue(industrial_furnaces_recipe_multiplier);
            PyrolyseOvenRecipe.MULTIPLIERS.setValue(pyrolyse_oven_recipe_multiplier);
        }
    }

    public static class Coils {
        public final CoilInfo lv_coil;
        public final CoilInfo mv_coil;
        public final CoilInfo hv_coil;
        public final CoilInfo nichrome_coil;

        public Coils(ModConfigSpec.Builder builder) {
            builder.push("coils");
            lv_coil = coil(builder, "lv coil", new CoilInfo(() -> 1D, () -> 1D));
            mv_coil = coil(builder, "mv coil", new CoilInfo(() -> 1.3D, () -> 1.15D));
            hv_coil = coil(builder, "hv coil", new CoilInfo(() -> 1.7D, () -> 1.25D));
            nichrome_coil = coil(builder, "nichrome coil", new CoilInfo(() -> 2.2D, () -> 1.45D));
            builder.pop();
        }

        private CoilInfo coil(ModConfigSpec.Builder builder, String name, CoilInfo defaultInfo) {
            builder.push(name.replace(' ', '_'));
            ModConfigSpec.DoubleValue energy = builder
                    .comment("The energy modifier of" + name)
                    .defineInRange("energyModifier", defaultInfo.energyModify().getAsDouble(), 1e-3, 1e3);
            ModConfigSpec.DoubleValue time = builder
                    .comment("The time modifier of" + name)
                    .defineInRange("timeModifier", defaultInfo.timeModify().getAsDouble(), 1e-3, 1e3);
            builder.pop();
            return new CoilInfo(time::get, energy::get);
        }
    }

    private static ModConfigSpec.IntValue addPositive(ModConfigSpec.Builder builder, String name, int defaultVal, String... desc) {
        return builder
                .comment(desc)
                .defineInRange(name, defaultVal, 1, Integer.MAX_VALUE);
    }
}
