package me.jakubok.nationsmod.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.collections.Colour;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(at = @At("RETURN"), method = "onBreak", cancellable = true)
    private void deleteBlockFromTheMap(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo info) {
        if (!world.isClient())
            info.cancel();
        int height = world.getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ()) - 1;
        Colour colour = new Colour(world.getBlockState(new BlockPos(pos.getX(), height, pos.getZ())).getBlock().getDefaultMapColor().color);
        NationsClient.map.put(new BlockPos(pos.getX(), height, pos.getZ()), colour);
    }

    @Inject(at = @At("RETURN"), method = "onPlaced", cancellable = true)
    private void addBlockToTheMap(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack, CallbackInfo info) {
        if (!world.isClient())
            info.cancel();
        int height = world.getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ()) - 1;
        Colour colour = new Colour(world.getBlockState(new BlockPos(pos.getX(), height, pos.getZ())).getBlock().getDefaultMapColor().color);
        NationsClient.map.put(new BlockPos(pos.getX(), height, pos.getZ()), colour);
    }
}
