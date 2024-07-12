package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.items.ActAndQuill;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SealAnAct implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            ItemStack stack = player.getMainHandStack();
            if (!(stack.getItem() instanceof ActAndQuill))
                return;
            ItemStack newStack = new ItemStack(ItemRegistry.ACT);
            newStack.setSubNbt(NationsMod.MOD_ID, stack.getSubNbt(NationsMod.MOD_ID));
            newStack.setCustomName(Text.literal(stack.getName().getString()).formatted(Formatting.BOLD));
            player.setStackInHand(player.getActiveHand(), newStack);
        });
    }
}
