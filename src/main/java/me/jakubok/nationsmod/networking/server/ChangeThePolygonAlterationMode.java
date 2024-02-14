package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.collection.PolygonAlterationMode;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ChangeThePolygonAlterationMode implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PolygonAlterationMode mode = PolygonAlterationMode.values()[buf.readInt()];
        server.execute(() -> {
            PlayerInfo info = PlayerInfo.fromAccount(new PlayerAccount(player), server);
            info.polygonPlayerStorage.mode = mode;
        });
    }
}
