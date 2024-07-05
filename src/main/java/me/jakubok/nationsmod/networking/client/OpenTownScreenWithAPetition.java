package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.gui.townScreen.TownScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class OpenTownScreenWithAPetition implements ClientPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        assert nbt != null;
        NbtCompound townNbt = nbt.getCompound("town");
        NbtCompound actNbt = nbt.getCompound("act");
        Town town = new Town(townNbt, null);
        Act<TownLawDescription> act = new Act<>(town.description, actNbt);
        client.execute(() -> client.setScreen(new TownScreen(town, null, act)));
    }
}
