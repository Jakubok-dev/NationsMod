package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collections.PlayerInfo;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CheckPositionPacketReceiver implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        PlayerInfo info = ComponentsRegistry.PLAYER_INFO.get(player);
        Text generatedText = info.getToolBarText(player);
        if (generatedText == null)
                return;
        player.sendMessage(generatedText, true);
    }
}
