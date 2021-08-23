package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.networking.client.OpenTownScreenPacketReceiver;
import me.jakubok.nationsmod.networking.client.OpenTownsScreenPacketReceiver;
import me.jakubok.nationsmod.networking.client.TownCreationScreenPacketReceiver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworking {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_TOWN_CREATION_SCREEN_PACKET, new TownCreationScreenPacketReceiver());
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_TOWNS_SCREEN_PACKET, new OpenTownsScreenPacketReceiver());
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_TOWN_SCREEN_PACKET, new OpenTownScreenPacketReceiver());
    }
}
