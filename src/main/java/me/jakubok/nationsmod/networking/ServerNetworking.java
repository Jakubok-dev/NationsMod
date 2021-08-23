package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.networking.server.CreateATownPacketReceiver;
import me.jakubok.nationsmod.networking.server.PrepareTownsScreenPacketReceiver;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNetworking {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_TOWN_PACKET, CreateATownPacketReceiver::handle);
        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_TOWNS_SCREEN_PACKET, PrepareTownsScreenPacketReceiver::handle);
    }
}
