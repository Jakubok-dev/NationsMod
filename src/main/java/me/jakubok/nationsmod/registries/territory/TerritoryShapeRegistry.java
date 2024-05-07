package me.jakubok.nationsmod.registries.territory;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.collection.TerritoryShape;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class TerritoryShapeRegistry extends PersistentState {
    private final Map<UUID, TerritoryShape> shapes = new HashMap<>();

    private TerritoryShapeRegistry() {}
    private TerritoryShapeRegistry(NbtCompound nbt) {
        for (int i = 0; i < nbt.getInt("size"); i++) {
            UUID key = nbt.getUuid("key" + i);
            TerritoryShape value = new TerritoryShape(nbt.getCompound("value" + i));
            shapes.put(key, value);
        }
    }

    public Map<UUID, TerritoryShape> getTheShapes() {
        return this.shapes;
    }
    public TerritoryShape getAShape(UUID id) {
        this.markDirty();
        return this.shapes.get(id);
    }
    public UUID registerAShape(TerritoryShape territoryShape) {
        UUID id = UUID.randomUUID();
        if (!territoryShape.setId(id))
            return null;
        this.shapes.put(id, territoryShape);
        this.markDirty();
        return id;
    }
    public TerritoryShape removeAShape(UUID id) {
        this.markDirty();
        return this.shapes.remove(id);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        int iteration = 0;
        for (Map.Entry<UUID, TerritoryShape> entry : this.shapes.entrySet()) {
            nbt.putUuid("key" + iteration, entry.getKey());
            nbt.put("value" + iteration, entry.getValue().writeToNbtAndReturn(new NbtCompound()));
            iteration++;
        }
        nbt.putInt("size", iteration);
        return nbt;
    }

    public static TerritoryShapeRegistry getRegistry(MinecraftServer server) {
        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        return manager.getOrCreate(
                TerritoryShapeRegistry::new,
                TerritoryShapeRegistry::new,
                NationsMod.MOD_ID + "-territory_shape_registry"
        );
    }
}
