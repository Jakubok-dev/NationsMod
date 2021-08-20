package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.networking.client.TownCreationScreenPacketReceiver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworking {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_TOWN_CREATION_SCREEN_PACKET, TownCreationScreenPacketReceiver::handle);
    }
}
