package me.jakubok.nationsmod.collection;

import me.jakubok.nationsmod.geometry.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Territory {
    public final UUID id;
    public UUID claimantsID;
    private Polygon polygon;

    private Territory(UUID id, Polygon polygon, UUID claimantsID) {
        this.id = id;
        this.polygon = polygon;
        this.claimantsID = claimantsID;
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
                    claimantsID,
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
                    claimantsID,
                    startsTheShape
            ));
            node = node.left;
        } while (node != firstNode);
        return edges;
    }

    public static Territory of(UUID id, Polygon polygon, UUID claimantsID) {
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
        return new Territory(id, clone, claimantsID);
    }
}
