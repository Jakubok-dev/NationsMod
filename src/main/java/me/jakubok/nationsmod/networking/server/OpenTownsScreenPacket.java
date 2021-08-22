package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.gui.TownsScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class OpenTownsScreenPacket {

    protected static TownsScreenHandlerFactory factory = new TownsScreenHandlerFactory();

    public static void handle(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        playerEntity.openHandledScreen(factory);
    }

    protected static class TownsScreenHandlerFactory implements NamedScreenHandlerFactory {

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
            return new TownsScreenHandler(syncId, inv); 
        }

        @Override
        public Text getDisplayName() {
            return null;
        }
    }
}
