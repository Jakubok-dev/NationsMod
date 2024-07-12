package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.gui.act.ActCreationScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class OpenActCreationScreen implements ClientPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        assert nbt != null;
        Pair<UUID, String> town = new Pair<>(nbt.getUuid("townID"), nbt.getString("townName"));
        Pair<UUID, String> nation;
        if (!nbt.getBoolean("is_nation_null"))
            nation = new Pair<>(nbt.getUuid("nationID"), nbt.getString("nationName"));
        else
            nation = null;
        client.execute(() -> client.setScreen(new ActCreationScreen(town, nation, null)));
    }
}
