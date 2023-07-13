package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collection.BorderGroup;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.geometry.Polygon;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CreateAPolygon implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String name = buf.readString();
        server.execute(() -> {
            PlayerInfo info = PlayerInfo.fromAccount(new PlayerAccount(player), server);
            for (Polygon polygon : info.polygonPlayerStorage.slots) {
                if (
                        polygon.name.toLowerCase().equals(name.toLowerCase())
                                ||
                                name.equals("+")
                ) {
                    player.sendMessage(Text.translatable("gui.nationsmod.border_slot_creator_screen.name_not_unique"), false);
                    return;
                }
            }
            info.polygonPlayerStorage.slots.add(new Polygon(name));
        });
    }
}
