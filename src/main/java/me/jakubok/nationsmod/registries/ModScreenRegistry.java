package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.gui.TownsScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ModScreenRegistry {
    static {
        ScreenRegistry.register(ModScreenHandlerRegistry.TOWNS_SCREEN_HANDLER, (screenhandler, inv, text) -> new TownsScreen(screenhandler, inv));
    }

    public static void init() {}
}
