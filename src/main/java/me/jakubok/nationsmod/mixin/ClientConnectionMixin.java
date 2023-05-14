package me.jakubok.nationsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.collection.BorderGroup;
import me.jakubok.nationsmod.collection.ClientBorderDrawer;
import me.jakubok.nationsmod.map.MapStorage;
import net.minecraft.network.ClientConnection;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(at = @At("HEAD"), method = "handleDisconnection")
    private void clearVariables(CallbackInfo info) {
        NationsClient.map = new MapStorage();
        NationsClient.selectedSlot = -1;
        NationsClient.borderSlot = new BorderGroup();
        NationsClient.drawer = new ClientBorderDrawer();
    }
}
