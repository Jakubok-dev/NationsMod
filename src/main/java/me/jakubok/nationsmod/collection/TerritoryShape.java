package me.jakubok.nationsmod.collection;

import me.jakubok.nationsmod.geometry.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TerritoryShape implements Serialisable {
    private UUID id;
    public UUID claimantsID;
    private Polygon polygon;
    private RegistryKey<World> worldRegistryKey;

    private TerritoryShape(UUID id, Polygon polygon, UUID claimantsID, RegistryKey<World> worldRegistryKey) {
        this.id = id;
        this.polygon = polygon;
        this.claimantsID = claimantsID;
        this.worldRegistryKey = worldRegistryKey;
    }
    public TerritoryShape(NbtCompound nbt) {
        this.readFromNbt(nbt);
    }

    public Set<BorderEdge> asBorderEdges() {
        PolygonNode<Point> node = this.polygon.root;
        while (node.value.value != this.polygon.getValueSet().from) {
            node = node.right;
            if (node == polygon.root)
                return null;
        }

        if (node.left.value.key > node.value.key && node.right.value.key > node.value.key) {
            double slopeLeft = ((double)(node.left.value.value - node.value.value)) / ((double)(node.left.value.key - node.value.key));
            double slopeRight = ((double)(node.right.value.value - node.value.value)) / ((double)(node.right.value.key - node.value.key));
            return Math.abs(slopeRight) <= Math.abs(slopeLeft) ? this.iterateToTheRight(node) : this.iterateToTheLeft(node);
        }
        if (node.left.value.key > node.value.key)
            return this.iterateToTheLeft(node);
        if (node.right.value.key > node.value.key)
            return this.iterateToTheRight(node);
        if (node.left.value.key.equals(node.value.key)) {
            node = node.right;
            return this.iterateToTheLeft(node);
        }
        if (node.right.value.key.equals(node.value.key)) {
            node = node.left;
            return this.iterateToTheRight(node);
        }

        double slopeLeft = ((double)(node.left.value.value - node.value.value)) / ((double)(node.left.value.key - node.value.key));
        double slopeRight = ((double)(node.right.value.value - node.value.value)) / ((double)(node.right.value.key - node.value.key));

        if (Math.abs(slopeLeft) <= Math.abs(slopeRight)) {
            node = node.left;
            return this.iterateToTheRight(node);
        }
        node = node.right;
        return this.iterateToTheLeft(node);
    }

    private Set<BorderEdge> iterateToTheRight(PolygonNode<Point> node) {
        Set<BorderEdge> edges = new HashSet<>();
        PolygonNode<Point> firstNode = node;

        do {
            if (node.value.key.equals(node.right.value.key)) {
                node = node.right;
                continue;
            }
            boolean startsTheShape = node.right.value.key > node.value.key;
            boolean doesTheNextStartTheShape = node.right.right.value.key > node.right.value.key;
            boolean isTheEndingClosed = startsTheShape == doesTheNextStartTheShape || node.right.value.key.equals(node.right.right.value.key);
            boolean isThePreviousLineALinearEquation = node.left.value.key.equals(node.value.key);

            edges.add(new BorderEdge(
                    (LinearFunction) MathEquation.fromTwoPoints(
                            new Range(node.value.key + .5d, node.right.value.key + .5d, !startsTheShape && isTheEndingClosed || startsTheShape && isThePreviousLineALinearEquation, startsTheShape && isTheEndingClosed || !startsTheShape && isThePreviousLineALinearEquation),
                            new Range(node.value.value + .5d, node.right.value.value + .5d, true, true),
                            node.value.key + .5d, node.value.value + .5d,
                            node.right.value.key + .5d, node.right.value.value + .5d
                    ),
                    this.id,
                    startsTheShape
            ));
            node = node.right;
        } while (node != firstNode);
        return edges;
    }

    private Set<BorderEdge> iterateToTheLeft(PolygonNode<Point> node) {
        Set<BorderEdge> edges = new HashSet<>();
        PolygonNode<Point> firstNode = node;

        do {
            if (node.value.key.equals(node.left.value.key)) {
                node = node.left;
                continue;
            }
            boolean startsTheShape = node.left.value.key > node.value.key;
            boolean doesTheNextStartTheShape = node.left.right.value.key > node.left.value.key;
            boolean isTheEndingClosed = startsTheShape == doesTheNextStartTheShape || node.left.value.key.equals(node.left.left.value.key);
            boolean isThePreviousLineALinearEquation = node.right.value.key.equals(node.value.key);

            edges.add(new BorderEdge(
                    (LinearFunction) MathEquation.fromTwoPoints(
                            new Range(node.value.key + .5d, node.left.value.key + .5d, !startsTheShape && isTheEndingClosed || startsTheShape && isThePreviousLineALinearEquation, startsTheShape && isTheEndingClosed || !startsTheShape && isThePreviousLineALinearEquation),
                            new Range(node.value.value + .5d, node.left.value.value + .5d, true, true),
                            node.value.key + .5d, node.value.value + .5d,
                            node.left.value.key + .5d, node.left.value.value + .5d
                    ),
                    this.id,
                    startsTheShape
            ));
            node = node.left;
        } while (node != firstNode);
        return edges;
    }

    public UUID getId() {
        return id;
    }

    public boolean setId(UUID id) {
        if (this.id == null) {
            this.id = id;
            return true;
        }
        return false;
    }

    public RegistryKey<World> getWorldRegistryKey() {
        return worldRegistryKey;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (!tag.getBoolean("is_id_null"))
            this.id = tag.getUuid("id");
        this.claimantsID = tag.getUuid("claimantsID");
        this.polygon = new Polygon(tag.getCompound("polygon"));
        this.worldRegistryKey = RegistryKey.of(RegistryKey.ofRegistry(new Identifier(tag.getString("world_registry_key_registry"))), new Identifier("world_registry_key_value"));
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        if (this.id != null)
            tag.putUuid("id", this.id);
        tag.putBoolean("is_id_null", this.id == null);
        tag.putUuid("claimantsID", this.claimantsID);
        tag.put("polygon", this.polygon.writeToNbtAndReturn(new NbtCompound()));
        tag.putString("world_registry_key_registry", this.worldRegistryKey.getRegistry().toString());
        tag.putString("world_registry_key_value", this.worldRegistryKey.getValue().toString());
        return tag;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public static TerritoryShape of(Polygon polygon, UUID claimantsID, RegistryKey<World> worldRegistryKey) {
        if (!polygon.isThePolygonClosed())
            return null;
        Polygon clone = new Polygon(polygon.name);
        PolygonNode<Point> node = polygon.root;
        while (node != null) {
            clone.addToTheRight(node.value);
            node = node.right;
            if (node == polygon.root) {
                clone.addToTheRight(polygon.root.value);
                break;
            }
        }
        return new TerritoryShape(null, clone, claimantsID, worldRegistryKey);
    }
}
