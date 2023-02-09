package me.jakubok.nationsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerMixin extends PlayerEntity {

    public ClientPlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    private BlockPos lastPosition;

    @Inject(at = @At("RETURN"), method = "tick")
    private void checkIfPositionHasChanged(CallbackInfo info) {
        if (this.lastPosition == null) {
            this.lastPosition = new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ());
            ClientPlayNetworking.send(Packets.CHECK_POSITION, PacketByteBufs.create());
            return;
        }

        if (this.lastPosition.getX() != this.getBlockX() || this.lastPosition.getZ() != this.getBlockZ()) {
            this.lastPosition = new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ());
            
            ClientPlayNetworking.send(Packets.CHECK_POSITION, PacketByteBufs.create());
            return;
        }
    }
}
