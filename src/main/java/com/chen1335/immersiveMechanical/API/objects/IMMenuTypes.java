package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import com.chen1335.immersiveMechanical.common.gui.GreenHouseMenu;
import com.chen1335.immersiveMechanical.common.gui.LaserTurretMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE_DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, ImmersiveMechanical.MODID);

    public static IEMenuTypes.MultiblockContainer<GreenHouseLogic.State, GreenHouseMenu> GREEN_HOUSE = IEMenuTypes.registerMultiblock("im_green_house", GreenHouseMenu::makeServer, GreenHouseMenu::makeClient);

    public static IEMenuTypes.ArgContainer<TurretLaserBlockEntity, LaserTurretMenu> LASER_TURRET = IEMenuTypes.registerArg("im_laser_turret", LaserTurretMenu::makeServer, LaserTurretMenu::makeClient);


}
