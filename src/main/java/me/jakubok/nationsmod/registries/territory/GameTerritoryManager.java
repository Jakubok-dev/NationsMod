package me.jakubok.nationsmod.registries.territory;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.collection.BorderEdge;
import me.jakubok.nationsmod.collection.TerritoryShape;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GameTerritoryManager {

    public static TerritoryShape get(UUID id, MinecraftServer server) {
        return TerritoryShapeRegistry.getRegistry(server).getAShape(id);
    }

    public static TerritoryShape at(int x, int z, ServerWorld world) {
        return GameTerritoryManager.at(x + .5f, z + .5f, world);
    }

    public static TerritoryShape at(double x, double z, ServerWorld world) {
        UUID shapesID = TerritoryRegistries.getRegistry(world).getDistricts().getShapesID(x, z);
        if (shapesID == null)
            return GameTerritoryManager.atProvince(x, z, world);
        return TerritoryShapeRegistry.getRegistry(world.getServer()).getAShape(shapesID);
    }

    protected static TerritoryShape atDistrict(double x, double z, ServerWorld world) {
        UUID shapesID = TerritoryRegistries.getRegistry(world).getDistricts().getShapesID(x, z);
        if (shapesID == null)
            return null;
        return TerritoryShapeRegistry.getRegistry(world.getServer()).getAShape(shapesID);
    }

    protected static TerritoryShape atProvince(double x, double z, ServerWorld world) {
        UUID shapesID = TerritoryRegistries.getRegistry(world).getProvinces().getShapesID(x, z);
        if (shapesID == null)
            return null;
        return TerritoryShapeRegistry.getRegistry(world.getServer()).getAShape(shapesID);
    }

    public static TerritoryShape register(Polygon polygon, District claimer, ServerWorld world) {
        TerritoryShape shape = TerritoryShape.of(polygon, claimer.getId(), world.getRegistryKey());
        if (shape == null)
            return null;
        for (BorderEdge e : shape.asBorderEdges()) {

            double centre = (e.fun.domain.to + e.fun.domain.from) / 2;
            if (GameTerritoryManager.atDistrict(centre, e.fun.apply(centre), world) != null)
                return null;

            TerritoryShape foreignShape = GameTerritoryManager.atProvince(centre, e.fun.apply(centre), world);
            if (foreignShape != null) {
                Province province = (Province) LegalOrganisationRegistry.getRegistry(world.getServer()).get(foreignShape.claimantsID);
                if (province != null) {
                    // If the territory belongs to a province of a foreign country, discard the request.
                    if (!province.getNationsUUID().equals(claimer.getTown(world.getServer()).getNationsID())) {
                        return null;
                    }
                }
            }

            for (ChunkPos p : e.fun.getOccupiedChunks()) {
                UnitaryTerritoryRegistry districtUnitRegistry = TerritoryRegistries.getRegistry(world).getDistricts().getFloor(p);
                UnitaryTerritoryRegistry provinceUnitRegistry = TerritoryRegistries.getRegistry(world).getProvinces().getFloor(p);
                if (districtUnitRegistry != null)
                    if (districtUnitRegistry.isColliding(e))
                        return null;

                if (provinceUnitRegistry == null)
                    continue;
                Set<UUID> provinceShapesIDs = provinceUnitRegistry.getCollidingShapesIDs(e);
                for (UUID id : provinceShapesIDs) {
                    foreignShape = TerritoryShapeRegistry.getRegistry(world.getServer()).getAShape(id);
                    if (foreignShape == null)
                        continue;
                    Province province = (Province) LegalOrganisationRegistry.getRegistry(world.getServer()).get(foreignShape.claimantsID);
                    if (province == null)
                        continue;
                    // If the territory belongs to a province of a foreign country, discard the request.
                    if (!province.getNationsUUID().equals(claimer.getTown(world.getServer()).getNationsID())) {
                        return null;
                    }
                }
            }
        }
        UUID id = TerritoryShapeRegistry.getRegistry(world.getServer()).registerAShape(shape);
        if (id == null)
            return null;
        for (BorderEdge e : shape.asBorderEdges()) {
            for (ChunkPos p : e.fun.getOccupiedChunks()) {
                UnitaryTerritoryRegistry unitRegistry = TerritoryRegistries.getRegistry(world).getDistricts().getOrRegister(p);
                unitRegistry.borderEdges.add(e);
            }
        }
        return shape;
    }

    public static TerritoryShape register(Polygon polygon, Province claimer, ServerWorld world) {
        TerritoryShape shape = TerritoryShape.of(polygon, claimer.getId(), world.getRegistryKey());
        if (shape == null)
            return null;
        for (BorderEdge e : shape.asBorderEdges()) {

            double centre = (e.fun.domain.to + e.fun.domain.from) / 2;
            if (GameTerritoryManager.atProvince(centre, e.fun.apply(centre), world) != null)
                return null;

            TerritoryShape foreignShape = GameTerritoryManager.atDistrict(centre, e.fun.apply(centre), world);
            if (foreignShape != null) {
                District district = (District) LegalOrganisationRegistry.getRegistry(world.getServer()).get(foreignShape.claimantsID);
                if (district != null) {
                    // If the territory belongs to a district of a foreign country, discard the request.
                    if (!claimer.getNationsUUID().equals(district.getTown(world.getServer()).getNationsID())) {
                        return null;
                    }
                }
            }

            for (ChunkPos p : e.fun.getOccupiedChunks()) {
                UnitaryTerritoryRegistry provinceUnitRegistry = TerritoryRegistries.getRegistry(world).getProvinces().getFloor(p);
                UnitaryTerritoryRegistry districtUnitRegistry = TerritoryRegistries.getRegistry(world).getDistricts().getFloor(p);
                if (provinceUnitRegistry != null)
                    if (provinceUnitRegistry.isColliding(e))
                        return null;


                if (districtUnitRegistry == null)
                    continue;
                Set<UUID> districtShapesIDs = districtUnitRegistry.getCollidingShapesIDs(e);
                for (UUID id : districtShapesIDs) {
                    foreignShape = TerritoryShapeRegistry.getRegistry(world.getServer()).getAShape(id);
                    if (foreignShape == null)
                        continue;
                    District district = (District) LegalOrganisationRegistry.getRegistry(world.getServer()).get(foreignShape.claimantsID);
                    if (district == null)
                        continue;
                    // If the territory belongs to a district of a foreign country, discard the request.
                    if (!claimer.getNationsUUID().equals(district.getTown(world.getServer()).getNationsID())) {
                        return null;
                    }
                }
            }
        }
        UUID id = TerritoryShapeRegistry.getRegistry(world.getServer()).registerAShape(shape);
        if (id == null)
            return null;
        for (BorderEdge e : shape.asBorderEdges()) {
            for (ChunkPos p : e.fun.getOccupiedChunks()) {
                UnitaryTerritoryRegistry unitRegistry = TerritoryRegistries.getRegistry(world).getProvinces().getOrRegister(p);
                unitRegistry.borderEdges.add(e);
            }
        }
        return shape;
    }

    public static TerritoryShape deregister(UUID shapesID, MinecraftServer server) {
        TerritoryShape shape = TerritoryShapeRegistry.getRegistry(server).getAShape(shapesID);
        if (shape == null)
            return null;

        TerritoryClaimer<?> claimer = (TerritoryClaimer<?>)LegalOrganisationRegistry.getRegistry(server).get(shape.claimantsID);
        if (claimer == null)
            return TerritoryShapeRegistry.getRegistry(server).removeAShape(shapesID);

        boolean isClaimerADistrict = claimer instanceof District;

        ServerWorld world = server.getWorld(shape.getWorldRegistryKey());
        assert world != null;

        for (BorderEdge e : shape.asBorderEdges()) {
            for (ChunkPos p : e.fun.getOccupiedChunks()) {
                UnitaryTerritoryRegistry unitRegistry = isClaimerADistrict ? TerritoryRegistries.getRegistry(world).getDistricts().getOrRegister(p) : TerritoryRegistries.getRegistry(world).getProvinces().getOrRegister(p);
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
