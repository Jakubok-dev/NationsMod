package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.gui.TownsScreen;
import me.jakubok.nationsmod.gui.TownsScreenHandler;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ModScreenRegistry {
    static {
        ScreenRegistry.<TownsScreenHandler, TownsScreen>register(ModScreenHandlerRegistry.TOWNS_SCREEN_HANDLER, (handler, inv, text) -> new TownsScreen(handler, inv));
    }

    public static void init() {}
}
