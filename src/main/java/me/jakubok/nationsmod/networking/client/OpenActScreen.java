package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.nation.NationLawDescription;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.gui.ActScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class OpenActScreen implements ClientPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        assert nbt != null;
        String type = nbt.getString("type");
        NbtCompound actNbt = nbt.getCompound("act");
        switch (type) {
            case "town" -> {
                NbtCompound townNbt = nbt.getCompound("town");
                Town town = new Town(townNbt, null);
                Act<TownLawDescription> act = new Act<>(town.description, actNbt);
                client.execute(() -> client.setScreen(new ActScreen<>(act, town, null)));
            }
            case "nation" -> {
                NbtCompound nationNbt = nbt.getCompound("nation");
                Nation nation = new Nation(nationNbt, null);
                Act<NationLawDescription> act = new Act<>(nation.description, actNbt);
                client.execute(() -> client.setScreen(new ActScreen<>(act, nation, null)));
            }
        }
    }
}
