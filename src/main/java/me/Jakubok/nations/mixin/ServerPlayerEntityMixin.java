package me.Jakubok.nations.mixin;

import me.Jakubok.nations.administration.Province;
import me.Jakubok.nations.administration.TerritoryClaimer;
import me.Jakubok.nations.administration.TownDistrict;
import me.Jakubok.nations.terrain.ModChunkPos;
import me.Jakubok.nations.util.GlobalChunkRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity {

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);

    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private TerritoryClaimer currentTerritory;

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        ChunkPos temp = new ChunkPos(getBlockPos());
        ModChunkPos pos = GlobalChunkRegistry.get(world, temp.x, temp.z);

        TerritoryClaimer claimer;
        if (pos == null)
            claimer = null;
        else
            claimer = pos.getOwner(getBlockPos());

        if (claimer == currentTerritory) return;
        currentTerritory = claimer;

        if (claimer instanceof TownDistrict) {
            TownDistrict townDistrict = (TownDistrict) claimer;
            sendMessage(Text.of(townDistrict.name + " | " + townDistrict.town.name), true);
        }
        else if (claimer instanceof Province) {
        }
        else if (claimer == null) {
            sendMessage(new TranslatableText("nationsmod.terrain.wilderness"), true);
        }
    }
}
