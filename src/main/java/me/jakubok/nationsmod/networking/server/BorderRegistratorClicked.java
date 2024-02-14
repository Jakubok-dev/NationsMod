package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.collection.PolygonAlterationMode;
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

import java.util.ArrayList;
import java.util.List;

public class BorderRegistratorClicked implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        boolean clickedABorderSign = buf.readBoolean();
        BlockPos pos = clickedABorderSign ? buf.readBlockPos() : null;

        server.execute(() -> {
            PlayerInfo info = PlayerInfo.fromAccount(new PlayerAccount(player), server);
            PolygonPlayerStorage storage = info.polygonPlayerStorage;
            PolygonAlterationMode mode = storage.mode;

            if (player.isSneaking() && storage.tempPoints.size() > 0) {
                Point d = storage.tempPoints.get(storage.tempPoints.size() - 1);
                storage.tempPoints.remove(storage.tempPoints.size() - 1);
                player.sendMessage(Text.of("A point at (X=" + d.key + "; Z=" + d.value + ") has been unmarked"), true);
                return;
            }

            if (!clickedABorderSign || storage.selectedSlot == -1 || mode == PolygonAlterationMode.NULL) {
                PacketByteBuf buffer = PacketByteBufs.create();
                List<String> polygonNames = storage.slots.stream().map(el -> el.name).toList();
                NbtCompound nbt = new NbtCompound();

                for (int i = 0; i < polygonNames.size(); i++)
                    nbt.putString("polygon" + i, polygonNames.get(i));
                nbt.putInt("size", polygonNames.size());
                nbt.putInt("mode", storage.mode.ordinal());
                buffer.writeNbt(nbt);
                ServerPlayNetworking.send(player, Packets.OPEN_POLYGONS_STORAGE_SCREEN, buffer);
                return;
            }

            Polygon polygon = storage.getSelectedPolygon();

            if (mode == PolygonAlterationMode.ADDITION) {
                if (storage.tempPoints.size() >= 1) {
                    if (polygon.add(new Point(pos.getX(), pos.getZ()), storage.tempPoints.get(0))) {
                        player.sendMessage(Text.of("Edge added successfully"), true);
                    } else {
                        player.sendMessage(Text.of("Addition failed"), true);
                        player.sendMessage(Text.of("You can add an edge to a point, which has already been connected to not more than one edge and it mustn't interfere with any edge in the polygon. If all conditions are true, try again by firstly clicking a point in the polygon"));
                    }
                    storage.tempPoints = new ArrayList<>();
                    return;
                }
                this.cacheThePoint(pos, player, storage);

            } else if (mode == PolygonAlterationMode.DELETION) {
                Point p = new Point(pos.getX(), pos.getZ());
                if (polygon.delete(p)) {
                    player.sendMessage(Text.of("Successfully deleted a point at (X=" + p.key + "; Y=" + p.value + ") from the polygon."), true);
                    return;
                }
                player.sendMessage(Text.of("Deletion failed"), true);
            } else if (mode == PolygonAlterationMode.INSERTION) {
                if (storage.tempPoints.size() >= 2) {
                    if (polygon.insert(new Point(pos.getX(), pos.getZ()), storage.tempPoints.get(1), storage.tempPoints.get(0))) {
                        player.sendMessage(Text.of("Inserted successfully"), true);
                    } else {
                        player.sendMessage(Text.of("Insertion failed"), true);
                        player.sendMessage(Text.of("The edges connecting the inserted point mustn't interfere with any edge in the polygon and the first selected point must be neighbouring the third one."));
                    }
                    storage.tempPoints = new ArrayList<>();
                    return;
                }
                this.cacheThePoint(pos, player, storage);
            } else if (mode == PolygonAlterationMode.OPENING) {
                if (storage.tempPoints.size() >= 1) {
                    if (polygon.openTheShape(new Point(pos.getX(), pos.getZ()), storage.tempPoints.get(0))) {
                        player.sendMessage(Text.of("Successfully opened the shape between specified two points"), true);
                    } else {
                        player.sendMessage(Text.of("Opening failed"), true);
                        player.sendMessage(Text.of("You can open only a closed polygon and two selected points must be neighbouring each other"));
                    }
                    storage.tempPoints = new ArrayList<>();
                    return;
                }
                this.cacheThePoint(pos, player, storage);
            }
        });
    }

    protected void cacheThePoint(BlockPos pos, ServerPlayerEntity player, PolygonPlayerStorage storage) {
        Point p = new Point(pos.getX(), pos.getZ());
        storage.tempPoints.add(p);
        player.sendMessage(Text.of("A point at (X=" + p.key + "; Z=" + p.value + ") has been marked as the position number " + storage.tempPoints.size()), true);
    }
}
