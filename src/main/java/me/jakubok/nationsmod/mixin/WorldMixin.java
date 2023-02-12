package me.jakubok.nationsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess {

    @Environment(EnvType.CLIENT)
    @Inject(at = @At("RETURN"), method = "onBlockChanged")
    private void updateTheMap(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo info) {
        if (!this.isClient())
            return;
        NationsClient.map.renderBlockLayer(this, pos.getX(), pos.getZ());
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeBlockPos(new BlockPos(pos.getX(), 64, pos.getZ()));
        ClientPlayNetworking.send(Packets.GET_BLOCKS_CLAIMANT_COLOUR, buffer);
    }
}
