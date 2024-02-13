package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class UnselectAPolygon implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            PlayerInfo info = PlayerInfo.fromAccount(new PlayerAccount(player), server);
            if (info.polygonPlayerStorage.selectedSlot != -1)
                info.polygonPlayerStorage.getSelectedPolygon().unsubscribe("client" + player.getUuid().toString());
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeString(info.polygonPlayerStorage.getSelectedPolygon().name);
            ServerPlayNetworking.send(player, Packets.UNCACHE_A_POLYGON, buffer);
            info.polygonPlayerStorage.selectedSlot = -1;
        });
    }
}
