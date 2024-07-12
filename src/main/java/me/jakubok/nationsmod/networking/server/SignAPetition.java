package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

public class SignAPetition implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        if (nbt == null) {
            server.execute(() -> player.sendMessage(Text.of("ERROR! A packet has been lost")));
            return;
        }
        UUID unitsID = nbt.getUuid("unit");
        UUID petitionsID = nbt.getUuid("petition");
        server.execute(() -> {
            AdministratingUnit<?> administratingUnit = (AdministratingUnit<?>) LegalOrganisationRegistry.getRegistry(server).get(unitsID);
            administratingUnit.signAPetition(server, new PlayerAccount(player), petitionsID);
        });
    }
}
