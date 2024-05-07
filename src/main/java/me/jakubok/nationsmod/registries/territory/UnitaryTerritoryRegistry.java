package me.jakubok.nationsmod.registries.territory;

import me.jakubok.nationsmod.collection.BorderEdge;
import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.collection.Serialisable;
import me.jakubok.nationsmod.exception.BorderNotFoundException;
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

    public UUID getLocalShapesID(double x, double y) throws BorderNotFoundException {
        PriorityQueue<Pair<Double, BorderEdge>> q = this.getValuesAt(x);
        while (!q.isEmpty() && q.peek().key > y)
            q.poll();
        if (q.isEmpty()) {
            throw new BorderNotFoundException();
        }
        BorderEdge edge1 = q.poll().value;
        BorderEdge edge2 = !q.isEmpty() ? q.poll().value : null;
        if (!edge1.startsTheShape && edge2 != null) {
            if (edge1.fun.equals(edge2.fun)) {
                BorderEdge temp = edge1;
                edge1 = edge2;
                edge2 = temp;
            }
        }
        return edge1.startsTheShape ? edge1.shapesID : null;
    }

    public boolean isColliding(BorderEdge edge) {
        for (BorderEdge registeredEdge : this.borderEdges) {
            if (registeredEdge.collides(edge))
                return true;
        }
        double centre = (edge.fun.domain.to + edge.fun.domain.from) / 2;
        //return this.getLocalShapesID(centre, edge.fun.apply(centre)) != null;
        return false;
    }

    public Set<UUID> getCollidingShapesIDs(BorderEdge edge) {
        Set<UUID> res = new HashSet<>();
        for (BorderEdge registeredEdge : this.borderEdges) {
            if (registeredEdge.collides(edge))
                res.add(registeredEdge.shapesID);
        }
        if (res.isEmpty()) {
            double centre = (edge.fun.domain.to - edge.fun.domain.from) / 2;
            UUID id = null;
            try {
                id = this.getLocalShapesID(centre, edge.fun.apply(centre));
            } catch (BorderNotFoundException ignored) {}
            if (id != null)
                res.add(id);
        }
        return res;
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
