package com.chen1335.immersiveMechanical.definitions;

import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import com.chen1335.immersiveMechanical.client.gui.GreenHouseScreen;
import com.chen1335.immersiveMechanical.client.gui.IndustrialFurnacesScreen;
import com.chen1335.immersiveMechanical.client.gui.LaserTurretScreen;
import com.chen1335.immersiveMechanical.client.gui.SmallMiningMachineScreen;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace.IndustrialFurnacesLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.smallMiningMachine.SmallMiningMachineLogic;
import com.chen1335.immersiveMechanical.common.gui.GreenHouseMenu;
import com.chen1335.immersiveMechanical.common.gui.IndustrialFurnacesMenu;
import com.chen1335.immersiveMechanical.common.gui.LaserTurretMenu;
import com.chen1335.immersiveMechanical.common.gui.SmallMiningMachineMenu;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

public class IMMenuTypes {

    public static IEMenuTypes.MultiblockContainer<GreenHouseLogic.State, GreenHouseMenu> GREEN_HOUSE = REGISTRATE.multiblockMenu("green_house",
            GreenHouseMenu::makeServer,
            GreenHouseMenu::makeClient,
            () -> GreenHouseScreen::new
    );

    public static IEMenuTypes.ArgContainer<TurretLaserBlockEntity, LaserTurretMenu> LASER_TURRET = REGISTRATE.argMenu("laser_turret",
            LaserTurretMenu::makeServer,
            LaserTurretMenu::makeClient,
            () -> LaserTurretScreen::new
    );

    public static IEMenuTypes.MultiblockContainer<IndustrialFurnacesLogic.State, IndustrialFurnacesMenu> INDUSTRIAL_FURNACES = REGISTRATE.multiblockMenu("industrial_furnaces",
            IndustrialFurnacesMenu::makeServer,
            IndustrialFurnacesMenu::makeClient,
            () -> IndustrialFurnacesScreen::new
    );

    public static IEMenuTypes.MultiblockContainer<SmallMiningMachineLogic.State, SmallMiningMachineMenu> SMALL_MINING_MACHINE = REGISTRATE.multiblockMenu("small_mining_machine",
            SmallMiningMachineMenu::makeServer,
            SmallMiningMachineMenu::makeClient,
            () -> SmallMiningMachineScreen::new
    );

}
