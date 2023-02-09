package me.jakubok.nationsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jakubok.nationsmod.NationsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.util.math.ChunkSectionPos;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(at = @At("RETURN"), method = "loadChunk")
    private void renderMap(int x, int z, ChunkData chunkData, CallbackInfo info) {
        for (int blockx = ChunkSectionPos.getBlockCoord(x); blockx < ChunkSectionPos.getBlockCoord(x) + 16; blockx++) {
            for (int blockz = ChunkSectionPos.getBlockCoord(z); blockz < ChunkSectionPos.getBlockCoord(z) + 16; blockz++) {
                NationsClient.renderBlock(this.client.world, blockx, blockz);
            }
        }
    }
}
