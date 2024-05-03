package me.jakubok.nationsmod.registries.territory;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.collection.BorderEdge;
import me.jakubok.nationsmod.collection.TerritoryShape;
import me.jakubok.nationsmod.geometry.Polygon;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.List;
import java.util.UUID;

public class GameTerritoryManager {

    public static TerritoryShape get(UUID id, MinecraftServer server) {
        return TerritoryShapeRegistry.getRegistry(server).getAShape(id);
    }

    public static TerritoryShape at(double x, double z, ServerWorld world) {
        ChunkPos pos = new ChunkPos(new BlockPos((int)x, 64, (int)z));
        UnitaryTerritoryRegistry unitRegistry = TerritoryRegistry.getRegistry(world).getFloor(pos);
        UUID shapesID = unitRegistry.getShapesID(x, z);
        if (shapesID == null)
            return null;
        return TerritoryShapeRegistry.getRegistry(world.getServer()).getAShape(shapesID);
    }

    public static UUID register(Polygon polygon, TerritoryClaimer<?> claimer, ServerWorld world) {
        TerritoryShape shape = TerritoryShape.of(polygon, claimer.getId(), world.getRegistryKey());
        if (shape == null)
            return null;
        for (BorderEdge e : shape.asBorderEdges()) {
            for (ChunkPos p : e.fun.getOccupiedChunks()) {
                UnitaryTerritoryRegistry unitRegistry = TerritoryRegistry.getRegistry(world).getFloor(p);
                if (unitRegistry.doesCollide(e))
                    return null;
            }
        }
        UUID id = TerritoryShapeRegistry.getRegistry(world.getServer()).registerAShape(shape);
        if (id == null)
            return null;
        for (BorderEdge e : shape.asBorderEdges()) {
            for (ChunkPos p : e.fun.getOccupiedChunks()) {
                UnitaryTerritoryRegistry unitRegistry = TerritoryRegistry.getRegistry(world).getOrRegister(p);
                unitRegistry.borderEdges.add(e);
            }
        }
        return id;
    }

    public static TerritoryShape deregister(UUID shapesID, MinecraftServer server) {
        TerritoryShape shape = TerritoryShapeRegistry.getRegistry(server).getAShape(shapesID);
        if (shape == null)
            return null;

        ServerWorld world = server.getWorld(shape.getWorldRegistryKey());
        assert world != null;

        for (BorderEdge e : shape.asBorderEdges()) {
            for (ChunkPos p : e.fun.getOccupiedChunks()) {
                UnitaryTerritoryRegistry unitRegistry = TerritoryRegistry.getRegistry(world).getOrRegister(p);
                List<BorderEdge> edges = unitRegistry.borderEdges.stream().filter(el -> el.shapesID.equals(shape.getId())).toList();
                if (edges.isEmpty())
                    continue;
                for (BorderEdge se : edges)
                    unitRegistry.borderEdges.remove(se);
            }
        }

        return TerritoryShapeRegistry.getRegistry(server).removeAShape(shapesID);
    }
}
