package me.Jakubok.nations;

import me.Jakubok.nations.util.ClientGUIs;
import net.fabricmc.api.ClientModInitializer;

public class NationsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientGUIs.init();
    }
}
