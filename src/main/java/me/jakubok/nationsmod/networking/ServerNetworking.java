package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.networking.server.CheckPositionPacketReceiver;
import me.jakubok.nationsmod.networking.server.CreateATownPacketReceiver;
import me.jakubok.nationsmod.networking.server.PrepareTownScreenPacketReceiver;
import me.jakubok.nationsmod.networking.server.PrepareTownsScreenPacketReceiver;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNetworking {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_TOWN_PACKET, new CreateATownPacketReceiver());
        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_TOWNS_SCREEN_PACKET, new PrepareTownsScreenPacketReceiver());
        ServerPlayNetworking.registerGlobalReceiver(Packets.CHECK_POSITION, new CheckPositionPacketReceiver());
        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_TOWN_SCREEN_PACKET, new PrepareTownScreenPacketReceiver());
    }
}
