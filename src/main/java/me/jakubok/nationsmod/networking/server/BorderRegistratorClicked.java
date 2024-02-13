package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.collection.PolygonPlayerStorage;
import me.jakubok.nationsmod.geometry.Point;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class BorderRegistratorClicked implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        boolean clickedABorderSign = buf.readBoolean();
        BlockPos pos = clickedABorderSign ? buf.readBlockPos() : null;

        server.execute(() -> {
            PlayerInfo info = PlayerInfo.fromAccount(new PlayerAccount(player), server);
            PolygonPlayerStorage slots = info.polygonPlayerStorage;
            if (!clickedABorderSign || slots.selectedSlot == -1) {
                PacketByteBuf buffer = PacketByteBufs.create();
                List<String> polygonNames = slots.slots.stream().map(el -> el.name).toList();
                NbtCompound nbt = new NbtCompound();

                for (int i = 0; i < polygonNames.size(); i++)
                    nbt.putString("polygon" + i, polygonNames.get(i));
                nbt.putInt("size", polygonNames.size());

                buffer.writeNbt(nbt);
                ServerPlayNetworking.send(player, Packets.OPEN_POLYGONS_STORAGE_SCREEN, buffer);

                return;
            }

            Polygon polygon = slots.getSelectedPolygon();

            if (player.isSneaking()) {
                if (polygon.delete(new Point(pos.getX(), pos.getZ()))) {
                    info.lastlyClickedBorderSign = null;
                    player.sendMessage(Text.of("Deletion successful"), true);
                } else player.sendMessage(Text.of("Deletion failed"), true);
                return;
            }

            if (info.lastlyClickedBorderSign == null) {
                info.lastlyClickedBorderSign = new Point(pos.getX(), pos.getZ());
                player.sendMessage(Text.of("The first position has been set at X=" + pos.getX() + " and Z=" + pos.getZ()), true);

                return;
            }

            if (info.lastlyClickedBorderSign.equals(new Point(pos.getX(), pos.getZ()))) {
                info.lastlyClickedBorderSign = null;
                player.sendMessage(Text.of("The first position has been cleared"), true);
                return;
            }

            if (polygon.add(new Point(pos.getX(), pos.getZ()), info.lastlyClickedBorderSign)) {
                player.sendMessage(Text.of("The edge added successfully X1=" + pos.getX() + " Z1=" + pos.getZ() + " X2=" + info.lastlyClickedBorderSign.key + " Z2=" + info.lastlyClickedBorderSign.value), true);
                info.lastlyClickedBorderSign = null;
                return;
            }
            player.sendMessage(Text.of("Couldn't add the edge"), true);
            player.sendMessage(Text.of("You can add an edge to a point, which has only one edge"), false);
        });
    }
}
