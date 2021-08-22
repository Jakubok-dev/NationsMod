package me.jakubok.nationsmod.mixin;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jakubok.nationsmod.administration.District;
import me.jakubok.nationsmod.administration.TerritoryClaimer;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity {

    public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    private boolean inAWilderness = true;

    @Inject(at = @At("RETURN"), method = "tick")
    private void claimCheck(CallbackInfo info) {
        ChunkClaimRegistry chunkClaimRegistry = ComponentsRegistry.CHUNK_BINARY_TREE.get(this.getEntityWorld().getLevelProperties()).get(this.getBlockPos());

        if (chunkClaimRegistry == null) {
            wilderness();
            return;
        }

        if (chunkClaimRegistry.claimBelonging(this.getBlockPos()) != null) {
            if (!inAWilderness)
                return;
            inAWilderness = false;

            UUID territoryClaimerID = chunkClaimRegistry.claimBelonging(this.getBlockPos());
            TerritoryClaimer territoryClaimer = ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(this.getEntityWorld().getLevelProperties()).getClaimer(territoryClaimerID);
            Text.of("" + territoryClaimer);

            if (territoryClaimer instanceof District) {
                District district = (District)territoryClaimer;
                this.sendMessage(Text.of(district.getTown().getName() + " | " + district.getName()), true);
            }
            return;
        }
        wilderness();
    }

    private void wilderness() {
        if (inAWilderness)
            return;
        inAWilderness = true;
        this.sendMessage(new TranslatableText("nationsmod.wilderness"), true);
    }
}
