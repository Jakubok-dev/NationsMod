package me.Jakubok.nations.network;

import me.Jakubok.nations.Nations;
import me.Jakubok.nations.administration.Town;
import me.Jakubok.nations.block.NationPillarEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CreateNationByPlayer {

    public CreateNationByPlayer() {
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(Nations.MOD_ID, "create_nation_by_player"), (server, player, networkHandler, buf, packetSender) -> {
            BlockPos center = buf.readBlockPos();
            World world = player.getEntityWorld();
            CompoundTag tag = buf.readCompoundTag();
            String title = tag.getString("title");

            server.execute(() -> {
                if (!(world.getBlockEntity(center) instanceof NationPillarEntity)) return;
                NationPillarEntity blockEntity = (NationPillarEntity) world.getBlockEntity(center);
                if (blockEntity.institutions.town != null) return;
                blockEntity.institutions.town = new Town(title, center, player, null, world);
            });
        });
    }
}
