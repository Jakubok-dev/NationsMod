package me.jakubok.nationsmod.registries.territory;

import me.jakubok.nationsmod.NationsMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

class TerritoryRegistry extends PersistentState {
    public final TreeMap<ChunkPos, UnitaryTerritoryRegistry> tree = new TreeMap<>((el1, el2) -> el1.x != el2.x ? Integer.compare(el1.x, el2.x) : Integer.compare(el1.z, el2.z));
    public final ServerWorld world;

    private TerritoryRegistry(ServerWorld world) {
        this.world = world;
    }
    private TerritoryRegistry(ServerWorld world, NbtCompound nbt) {
        this.world = world;
        for (int i = 0; i < nbt.getInt("size"); i++) {
            UnitaryTerritoryRegistry r = new UnitaryTerritoryRegistry(this.world, nbt.getCompound("registry" + i));
            tree.put(r.getPos(), r);
        }
    }

    public UnitaryTerritoryRegistry getFloor(ChunkPos pos) {
        ChunkPos key = this.tree.floorKey(pos);
        if (key == null)
            return null;
        return this.tree.get(key);
    }

    public UnitaryTerritoryRegistry getOrRegister(ChunkPos pos) {
        if (pos == null)
            return null;
        UnitaryTerritoryRegistry result = this.tree.get(pos);
        if (result == null)
            result = this.registerAUnit(new UnitaryTerritoryRegistry(this.world, pos));
        return result;
    }

    public UnitaryTerritoryRegistry registerAUnit(UnitaryTerritoryRegistry registry) {
        this.markDirty();
        return tree.put(registry.getPos(), registry);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        AtomicInteger size = new AtomicInteger(0);
        tree.forEach((k, v) -> {
            nbt.put("registry" + size.getAndIncrement(), v.writeToNbtAndReturn(new NbtCompound()));
        });
        nbt.putInt("size", size.get());
        return nbt;
    }

    public static TerritoryRegistry getRegistry(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        return manager.getOrCreate(
                nbt -> new TerritoryRegistry(world, nbt),
                () -> new TerritoryRegistry(world),
                NationsMod.MOD_ID + ":territory_registry"
        );
    }
}
