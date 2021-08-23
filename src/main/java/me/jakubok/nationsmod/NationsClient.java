package me.jakubok.nationsmod;

import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.registries.KeyBindingRegistry;
import net.fabricmc.api.ClientModInitializer;
public class NationsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientNetworking.register();
        KeyBindingRegistry.init();
    }
}
