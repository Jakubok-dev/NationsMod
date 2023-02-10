package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BorderRegistrator extends Item {

    public BorderRegistrator() {
        super(new FabricItemSettings().group(NationsMod.ITEM_GROUP).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            return super.use(world, user, hand); 

        BorderSlots slots = ComponentsRegistry.BORDER_SLOTS.get(user);
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeNbt(slots.writeToNbtAndReturn(new NbtCompound()));
        ServerPlayNetworking.send((ServerPlayerEntity)user, Packets.OPEN_BORDER_REGISTRATOR_SCREEN, buffer);

        return TypedActionResult.success(user.getMainHandStack());
    }
}
