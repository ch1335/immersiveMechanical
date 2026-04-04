package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace.IndustrialFurnacesLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.smallMiningMachine.SmallMiningMachineLogic;
import com.chen1335.immersiveMechanical.common.gui.GreenHouseMenu;
import com.chen1335.immersiveMechanical.common.gui.IndustrialFurnacesMenu;
import com.chen1335.immersiveMechanical.common.gui.LaserTurretMenu;
import com.chen1335.immersiveMechanical.common.gui.SmallMiningMachineMenu;

public class IMMenuTypes {

    public static IEMenuTypes.MultiblockContainer<GreenHouseLogic.State, GreenHouseMenu> GREEN_HOUSE = IEMenuTypes.registerMultiblock("im_green_house", GreenHouseMenu::makeServer, GreenHouseMenu::makeClient);

    public static IEMenuTypes.ArgContainer<TurretLaserBlockEntity, LaserTurretMenu> LASER_TURRET = IEMenuTypes.registerArg("im_laser_turret", LaserTurretMenu::makeServer, LaserTurretMenu::makeClient);

    public static IEMenuTypes.MultiblockContainer<IndustrialFurnacesLogic.State, IndustrialFurnacesMenu> INDUSTRIAL_FURNACES = IEMenuTypes.registerMultiblock("im_industrial_furnaces", IndustrialFurnacesMenu::makeServer, IndustrialFurnacesMenu::makeClient);

    public static IEMenuTypes.MultiblockContainer<SmallMiningMachineLogic.State, SmallMiningMachineMenu> SMALL_MINING_MACHINE = IEMenuTypes.registerMultiblock("im_small_mining_machine", SmallMiningMachineMenu::makeServer, SmallMiningMachineMenu::makeClient);

}
