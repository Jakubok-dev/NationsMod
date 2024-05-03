package me.jakubok.nationsmod.registries.territory;

import me.jakubok.nationsmod.collection.BorderEdge;
import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.collection.Serialisable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import java.util.*;

class UnitaryTerritoryRegistry implements Serialisable {
    private ChunkPos pos;
    public Set<BorderEdge> borderEdges = new HashSet<>();
    public final ServerWorld world;

    public UnitaryTerritoryRegistry(ServerWorld world, ChunkPos pos) {
        this.world = world;
        this.pos = pos;
    }
    public UnitaryTerritoryRegistry(ServerWorld world, NbtCompound nbt) {
        this.world = world;
        this.readFromNbt(nbt);
    }

    public PriorityQueue<Pair<Double, BorderEdge>> getValuesAt(double x) {
        PriorityQueue<Pair<Double, BorderEdge>> q = new PriorityQueue<>(Collections.reverseOrder(Comparator.comparingDouble(p -> p.key)));
        for (BorderEdge e : this.borderEdges) {
            Double y = e.fun.apply(x);
            if (y != null)
                q.add(new Pair<>(y, e));
        }
        return q;
    }

    public UUID getShapesID(double x, double y) {
        PriorityQueue<Pair<Double, BorderEdge>> q = this.getValuesAt(x);
        while (!q.isEmpty() && q.peek().key > y)
            q.poll();
        if (q.isEmpty()) {
            UnitaryTerritoryRegistry registryBellow = TerritoryRegistry.getRegistry(this.world).tree.floorEntry(new ChunkPos(this.pos.x, this.pos.z - 1)).getValue();
            if (registryBellow == null)
                return null;
            if (registryBellow.pos.x != this.pos.x)
                return null;
            return registryBellow.getShapesID(x, y);
        }
        BorderEdge edge1 = q.poll().value;
        BorderEdge edge2 = !q.isEmpty() ? q.poll().value : null;
        if (!edge1.startsTheShape && edge2 != null) {
            BorderEdge temp = edge1;
            edge1 = edge2;
            edge2 = temp;
        }
        return edge1.startsTheShape ? edge1.shapesID : edge2 != null ? edge2.shapesID : null;
    }

    public boolean doesCollide(BorderEdge edge) {
        for (BorderEdge registeredEdge : this.borderEdges) {
            if (registeredEdge.collides(edge))
                return true;
        }
        double centre = (edge.fun.domain.to - edge.fun.domain.from) / 2;
        return this.getShapesID(centre, edge.fun.apply(centre)) != null;
    }

    public ChunkPos getPos() {
        return pos;
    }

    public int getX() {
        return this.pos.x << 4;
    }

    public int getZ() {
        return this.pos.z << 4;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        int x = tag.getInt("x");
        int z = tag.getInt("z");
        this.pos = new ChunkPos(x, z);
        this.borderEdges.clear();
        for (int i = 0; i < tag.getInt("size"); i++)
            this.borderEdges.add(new BorderEdge(tag.getCompound("edge" + i)));
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putInt("x", this.pos.x);
        tag.putInt("z", this.pos.z);
        int i = 0;
        for (BorderEdge element : this.borderEdges) {
            tag.put("edge" + i, element.writeToNbtAndReturn(new NbtCompound()));
            i++;
        }
        tag.putInt("size", i);
        return tag;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }
}
