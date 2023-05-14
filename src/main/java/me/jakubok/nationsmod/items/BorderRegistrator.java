package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.collection.BorderSlots;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
import net.minecraft.world.World;

public class BorderRegistrator extends Item {

    public BorderRegistrator() {
        super(new FabricItemSettings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            return super.use(world, user, hand); 

        MinecraftServer server = ((ServerWorld)world).getServer();
        BorderSlots slots = PlayerInfo.fromAccount(new PlayerAccount(user), server).slots;
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeNbt(slots.writeToNbtAndReturn(new NbtCompound(), true));
        ServerPlayNetworking.send((ServerPlayerEntity)user, Packets.OPEN_BORDER_REGISTRATOR_SCREEN, buffer);

        return TypedActionResult.success(user.getMainHandStack());
    }
}
