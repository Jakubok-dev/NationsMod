package me.jakubok.nationsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(at = @At("RETURN"), method = "loadChunk")
    private void renderMap(int x, int z, ChunkData chunkData, CallbackInfo info) {
        for (int blockx = ChunkSectionPos.getBlockCoord(x); blockx < ChunkSectionPos.getBlockCoord(x) + 16; blockx++) {
            for (int blockz = ChunkSectionPos.getBlockCoord(z); blockz < ChunkSectionPos.getBlockCoord(z) + 16; blockz++) {
                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeBlockPos(new BlockPos(blockx, 64, blockz));
                ClientPlayNetworking.send(Packets.GET_BLOCKS_CLAIMANT_COLOUR, buffer);
                NationsClient.map.renderBlockLayer(this.client.world, blockx, blockz);
            }
        }
    }
}
