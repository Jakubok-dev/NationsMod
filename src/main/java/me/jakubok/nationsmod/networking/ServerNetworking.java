package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.networking.server.CreateATownPacketReceiver;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNetworking {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_TOWN_PACKET, CreateATownPacketReceiver::handle);
    }
}
