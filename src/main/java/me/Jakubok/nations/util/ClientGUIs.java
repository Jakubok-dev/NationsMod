package me.Jakubok.nations.util;

import me.Jakubok.nations.gui.TownCreationDescription;
import me.Jakubok.nations.gui.TownCreationScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ClientGUIs {

    public static void init() {
        ScreenRegistry.<TownCreationDescription, TownCreationScreen>register(GUIs.TOWN_CREATION, (gui, inventory, title) -> new TownCreationScreen(gui, inventory.player));
    }
}
