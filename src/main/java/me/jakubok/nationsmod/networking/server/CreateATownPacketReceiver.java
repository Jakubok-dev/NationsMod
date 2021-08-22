package me.jakubok.nationsmod.networking.server;

import java.util.concurrent.atomic.AtomicBoolean;

import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;

public class CreateATownPacketReceiver {
    public static void handle(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        NbtCompound compound = packetByteBuf.readNbt();
        String townName = compound.getString("town_name");
        String districtName = compound.getString("district_name");

        minecraftServer.execute(() -> {
            // Check if the town name is unique
            AtomicBoolean unique = new AtomicBoolean(true);
            ComponentsRegistry.TOWNS_REGISTRY.get(minecraftServer.getOverworld().getLevelProperties()).getTowns().forEach(el -> {
                if (townName.toLowerCase().equals(el.getName().toLowerCase())) {
                    playerEntity.sendMessage(new TranslatableText("gui.nationsmod.town_creation_screen.town_name_not_unique"), false);
                    unique.set(false);
                    return;
                }
            });

            if (!unique.get())
                return;

            if (!playerEntity.isCreative()) playerEntity.getMainHandStack().setCount(playerEntity.getMainHandStack().getCount()-1);

            new Town(townName, districtName, playerEntity.getChunkPos(), minecraftServer.getOverworld());
        });
    }
}
