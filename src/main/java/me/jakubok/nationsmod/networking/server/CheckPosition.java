package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.collections.ChunkBinaryTree;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.collections.PlayerInfo;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.PlayerInfoRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CheckPosition implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        // Render the visual border
        ServerWorld world = player.getWorld();
        ChunkBinaryTree tree = ChunkBinaryTree.getRegistry(world);

        for (int i = player.getBlockX() - 7; i <= player.getBlockX() + 7; i++) {
            for (int j = player.getBlockZ() - 7; j <= player.getBlockZ() + 7; j++) {
                ChunkClaimRegistry registry = tree.get(new BlockPos(i, 64, j));
                if (registry == null)
                    continue;
                if (registry.claimBelonging(i, j) == null)
                    continue;
                District district = District.fromUUID(registry.claimBelonging(i, j), server);
                PacketByteBuf responseBuffer = PacketByteBufs.create();
                responseBuffer.writeBlockPos(new BlockPos(i, 64, j));
                Colour colour = new Colour(district.getTheMapColour(server).getBitmask());
                colour.changeTheShade(2);
                responseBuffer.writeInt(colour.getBitmask());
                responseBuffer.writeBoolean(false);
                ServerPlayNetworking.send(player, Packets.UNHIGHLIGHT_A_BLOCK_CLIENT, responseBuffer);
            }
        }

        for (int i = player.getBlockX() - 3; i <= player.getBlockX() + 3; i++) {
            for (int j = player.getBlockZ() - 3; j <= player.getBlockZ() + 3; j++) {
                ChunkClaimRegistry registry = tree.get(new BlockPos(i, 64, j));
                if (registry == null)
                    continue;
                if (registry.claimBelonging(i, j) == null)
                    continue;
                District district = District.fromUUID(registry.claimBelonging(i, j), server);
                if (
                    registry.claimBelonging(i, j).equals(ChunkClaimRegistry.GET_CLAIMANT(i - 1, j, world)) &&
                    registry.claimBelonging(i, j).equals(ChunkClaimRegistry.GET_CLAIMANT(i + 1, j, world)) &&
                    registry.claimBelonging(i, j).equals(ChunkClaimRegistry.GET_CLAIMANT(i, j - 1, world)) &&
                    registry.claimBelonging(i, j).equals(ChunkClaimRegistry.GET_CLAIMANT(i, j + 1, world)) &&
                    registry.claimBelonging(i, j).equals(ChunkClaimRegistry.GET_CLAIMANT(i - 1, j - 1, world)) &&
                    registry.claimBelonging(i, j).equals(ChunkClaimRegistry.GET_CLAIMANT(i + 1, j - 1, world)) &&
                    registry.claimBelonging(i, j).equals(ChunkClaimRegistry.GET_CLAIMANT(i - 1, j + 1, world)) &&
                    registry.claimBelonging(i, j).equals(ChunkClaimRegistry.GET_CLAIMANT(i + 1, j + 1, world))
                )
                    continue;
                PacketByteBuf responseBuffer = PacketByteBufs.create();
                responseBuffer.writeBlockPos(new BlockPos(i, 64, j));
                Colour colour = new Colour(district.getTheMapColour(server).getBitmask());
                colour.changeTheShade(2);
                responseBuffer.writeInt(colour.getBitmask());
                responseBuffer.writeBoolean(false);
                ServerPlayNetworking.send(player, Packets.HIGHLIGHT_A_BLOCK_CLIENT, responseBuffer);
            }
        }

        // Generate the tooltip
        PlayerInfo info = 
            PlayerInfoRegistry
            .getRegistry(server)
            .getAPlayer(new PlayerAccount(player));
        
        Text generatedText = info.getToolBarText(player, server);
        if (generatedText == null)
                return;
        player.sendMessage(generatedText, true);
    }
}
