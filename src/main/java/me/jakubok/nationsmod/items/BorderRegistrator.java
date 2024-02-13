package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.block.BorderSign;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.collection.PolygonPlayerStorage;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BorderRegistrator extends Item {

    public BorderRegistrator() {
        super(new FabricItemSettings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient)
            return super.use(world, user, hand);

        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hit = client.crosshairTarget;

        boolean clickedABorderSign = false;
        BlockPos pos = null;
        if (hit.getType() == HitResult.Type.BLOCK) {
            pos = ((BlockHitResult)hit).getBlockPos();
            BlockState bs = world.getBlockState(pos);
            clickedABorderSign = bs.getBlock() instanceof BorderSign;
        }

        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeBoolean(clickedABorderSign);
        if (clickedABorderSign)
            buffer.writeBlockPos(pos);

        ClientPlayNetworking.send(Packets.BORDER_REGISTRATOR_CLICKED, buffer);

        return TypedActionResult.success(user.getMainHandStack());
    }
}
