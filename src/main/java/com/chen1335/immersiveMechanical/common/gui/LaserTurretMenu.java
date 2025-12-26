package com.chen1335.immersiveMechanical.common.gui;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.gui.TurretMenu;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import java.util.List;

public class LaserTurretMenu extends TurretMenu {
    protected LaserTurretMenu(TurretContext ctx) {
        super(ctx);
    }

    public static LaserTurretMenu makeServer(MenuType<LaserTurretMenu> type, int id, Inventory invPlayer, TurretLaserBlockEntity be) {
        return new LaserTurretMenu(
                TurretContext.serverCtx(type, id, invPlayer, be)
        );
    }

    public static LaserTurretMenu makeClient(MenuType<LaserTurretMenu> type, int id, Inventory invPlayer) {
        return new LaserTurretMenu(
                new TurretContext(
                        IEContainerMenu.clientCtx(type, id),
                        invPlayer,
                        new MutableEnergyStorage(TurretLaserBlockEntity.ENERGY_CAPACITY),
                        GetterAndSetter.standalone(List.of()),
                        () -> {
                        },
                        GetterAndSetter.standalone(false),
                        GetterAndSetter.standalone(false),
                        GetterAndSetter.standalone(false),
                        GetterAndSetter.standalone(false)
                )
        );
    }
}
