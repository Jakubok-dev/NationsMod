package me.jakubok.nationsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.registries.PlayerInfoRegistry;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/PlayerManager;onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void markPlayerAsOnline(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        PlayerInfo playerinfo = PlayerInfoRegistry
        .getRegistry(player.getServer())
        .getAPlayer(new PlayerAccount(player));

        playerinfo.online = true;
        playerinfo.setPlayerAccount(new PlayerAccount(player));
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/PlayerManager;remove(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void markPlayerAsOffline(ServerPlayerEntity player, CallbackInfo info) {
        PlayerInfo playerinfo = PlayerInfoRegistry
        .getRegistry(player.getServer())
        .getAPlayer(new PlayerAccount(player));

        playerinfo.online = false;
    }
}
