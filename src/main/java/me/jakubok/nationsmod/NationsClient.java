package me.jakubok.nationsmod;

import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.ClientBorderDrawer;
import me.jakubok.nationsmod.map.MapStorage;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.registries.KeyBindingRegistry;
import net.fabricmc.api.ClientModInitializer;
public class NationsClient implements ClientModInitializer {

    public static ClientBorderDrawer drawer = new ClientBorderDrawer();
    public static MapStorage map = new MapStorage();
    public static int selectedSlot = -1;
    public static BorderGroup borderSlot = new BorderGroup();

    @Override
    public void onInitializeClient() {
        ClientNetworking.register();
        KeyBindingRegistry.init();
    }
}
