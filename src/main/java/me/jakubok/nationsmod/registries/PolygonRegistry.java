package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.geometry.Polygon;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PolygonRegistry extends PersistentState {
    private final Map<UUID, Polygon> polygons = new HashMap<>();

    private  PolygonRegistry() {}
    private PolygonRegistry(NbtCompound nbt) {
        for (int i = 0; i < nbt.getInt("size"); i++) {
            UUID key = nbt.getUuid("key" + i);
            Polygon value = new Polygon(nbt.getCompound("value" + i));
            polygons.put(key, value);
        }
    }

    public Map<UUID, Polygon> getThePolygons() {
        return this.polygons;
    }
    public Polygon getAPolygon(UUID id) {
        this.markDirty();
        return this.polygons.get(id);
    }
    public UUID registerAPolygon(Polygon polygon) {
        UUID id = UUID.randomUUID();
        this.polygons.put(id, polygon);
        this.markDirty();
        return id;
    }
    public Polygon removeAPolygon(UUID id) {
        this.markDirty();
        return this.polygons.remove(id);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        int iteration = 0;
        for (Map.Entry<UUID, Polygon> entry : this.polygons.entrySet()) {
            nbt.putUuid("key" + iteration, entry.getKey());
            nbt.put("value" + iteration, entry.getValue().writeToNbtAndReturn(new NbtCompound()));
            iteration++;
        }
        nbt.putInt("size", iteration);
        return nbt;
    }

    public static PolygonRegistry getRegistry(MinecraftServer server) {
        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        return manager.getOrCreate(
                PolygonRegistry::new,
                PolygonRegistry::new,
                NationsMod.MOD_ID + ":polygon_registry"
        );
    }
}
