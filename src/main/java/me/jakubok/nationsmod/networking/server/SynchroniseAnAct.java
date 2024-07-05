package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.NationsMod;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SynchroniseAnAct implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbtCompound = buf.readNbt();
        if (nbtCompound == null) {
            player.sendMessage(Text.of("ERROR! Packet has been lost"));
            return;
        }
        server.execute(() -> {
            ItemStack stack = player.getStackInHand(player.preferredHand);
            NbtCompound stackNbt = stack.getOrCreateSubNbt(NationsMod.MOD_ID);
            stackNbt.put("act", nbtCompound);
        });
    }
}
