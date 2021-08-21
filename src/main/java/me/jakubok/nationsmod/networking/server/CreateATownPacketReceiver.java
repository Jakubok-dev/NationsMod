package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.administration.District;
import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CreateATownPacketReceiver {
    public static void handle(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        NbtCompound compound = packetByteBuf.readNbt();
        String townName = compound.getString("town_name");
        String districtName = compound.getString("district_name");

        minecraftServer.execute(() -> {

            ComponentsRegistry.TOWNS_REGISTRY.get(minecraftServer.getOverworld().getLevelProperties()).getTowns().forEach(el -> {
                if (townName.toLowerCase().equals(el.getName().toLowerCase())) {
                    playerEntity.sendMessage(new TranslatableText("gui.nationsmod.town_creation_screen.town_name_not_unique"), false);
                    return;
                }
            });

            Town newTown = new Town(townName, districtName, playerEntity.getChunkPos(), minecraftServer.getOverworld());
            playerEntity.sendMessage(Text.of("The " + newTown.getName() + " town has been created!"), false);
        });
    }
}
