package me.jakubok.nationsmod.registries.territory;

import me.jakubok.nationsmod.exception.BorderNotFoundException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

class TerritoryRegistry {
    public final TreeMap<ChunkPos, UnitaryTerritoryRegistry> tree = new TreeMap<>((el1, el2) -> el1.x != el2.x ? Integer.compare(el1.x, el2.x) : Integer.compare(el1.z, el2.z));
    public final ServerWorld world;

    public TerritoryRegistry(ServerWorld world) {
        this.world = world;
    }
    public TerritoryRegistry(ServerWorld world, NbtCompound nbt) {
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
        if (key.x != pos.x)
            return null;
        return this.tree.get(key);
    }

    public UnitaryTerritoryRegistry getOrRegister(ChunkPos pos) {
        if (pos == null)
            return null;
        UnitaryTerritoryRegistry result = this.tree.get(pos);
        if (result == null) {
            result = new UnitaryTerritoryRegistry(this.world, pos);
            this.registerAUnit(result);
        }
        return result;
    }

    public UUID getShapesID(double x, double z) {
        ChunkPos pos = new ChunkPos(new BlockPos((int)Math.floor(x), 64, (int)Math.floor(z)));
        return this.getShapesID(x, z, pos);
    }

    protected UUID getShapesID(double x, double z, ChunkPos pos) {
        UnitaryTerritoryRegistry unitRegistry = this.getFloor(pos);
        if (unitRegistry == null)
            return null;
        UUID id;
        try {
            id = unitRegistry.getLocalShapesID(x, z);
        } catch (BorderNotFoundException e) {
            ChunkPos unitPos = unitRegistry.getPos();
            ChunkPos newPos = new ChunkPos(unitPos.x, unitPos.z - 1);
            return this.getShapesID(x, z, newPos);
        }
        return id;
    }


    public UnitaryTerritoryRegistry registerAUnit(UnitaryTerritoryRegistry registry) {
        return tree.put(registry.getPos(), registry);
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        AtomicInteger size = new AtomicInteger(0);
        tree.forEach((k, v) -> {
            nbt.put("registry" + size.getAndIncrement(), v.writeToNbtAndReturn(new NbtCompound()));
        });
        nbt.putInt("size", size.get());
        return nbt;
    }
}
