package me.jakubok.nationsmod.networking.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.gui.TownsScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class OpenTownsScreenPacketReceiver {
    public static void handle(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        NbtCompound compound = packetByteBuf.readNbt();

        Map<String, UUID> towns = new HashMap<>();
        for (int i = 1; i <= compound.getInt("size"); i++)
            towns.put(compound.getString("town_name" + i), compound.getUuid("town_id" + i));

        minecraftClient.execute(() -> {
            minecraftClient.setScreen(new TownsScreen(towns));
        });
    }
}
