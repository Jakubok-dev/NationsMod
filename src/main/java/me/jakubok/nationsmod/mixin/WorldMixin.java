package me.jakubok.nationsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jakubok.nationsmod.NationsClient;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess {

    @Inject(at = @At("RETURN"), method = "onBlockChanged", cancellable = true)
    private void updateTheMap(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo info) {
        if (this.isClient()) {
            info.cancel();
        }
        NationsClient.renderBlock(this, pos.getX(), pos.getZ());
    }
}
