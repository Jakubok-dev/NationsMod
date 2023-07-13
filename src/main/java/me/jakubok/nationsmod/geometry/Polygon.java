package me.jakubok.nationsmod.geometry;

import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.collection.Serialisable;
import net.minecraft.nbt.NbtCompound;

public class Polygon implements Serialisable {

    public EdgeNode root;
    public String name;

    public Polygon(String name) {
        this.name = name;
    }
    public Polygon(String name, MathEquation<?> root) {
        this.root = new EdgeNode(root);
        this.name = name;
    }
    public Polygon(NbtCompound nbt) {
        this.readFromNbt(nbt);
    }

    public boolean insertLeft(double x, double y) {
        if (isClosed() || this.root == null)
            return false;

        MathEquation<?> value = MathEquation.fromTwoPoints(x, y, this.root.value.domain.from, this.root.value.valueSet.from);
        if (this.doesCollide(value))
            return false;

        EdgeNode newNode = new EdgeNode(value);
        this.root.left = newNode;
        newNode.right = this.root;
        this.root = newNode;
        return true;
    }
    public boolean insertRight(double x, double y) {
        if (isClosed() || this.root == null)
            return false;

        EdgeNode iterator = root;
        while (iterator.right != null)
            iterator = iterator.right;

        MathEquation<?> value = MathEquation.fromTwoPoints(x, y, iterator.value.domain.to, iterator.value.valueSet.to);
        if (this.doesCollide(value))
            return false;

        iterator.right = new EdgeNode(value);
        iterator.right.left = iterator;

        return true;
    }

    public boolean deleteByPoint(double x, double y) {
        if (this.root == null)
            return false;

        Pair<EdgeNode, EdgeNode> edges = findEdges(x, y);

        if (edges == null)
            return false;
        if (edges.value == null) {
            this.deleteEdge(edges.key);
            return true;
        }

        MathEquation<?> resultant = MathEquation.fromTwoPoints(edges.key.value.domain.from, edges.key.value.valueSet.from, edges.value.value.domain.to, edges.value.value.valueSet.to);
        EdgeNode resultantEdge = new EdgeNode(resultant);

        this.deleteEdge(edges.key);
        this.deleteEdge(edges.value);
        if (edges.key.left != null) {
            edges.key.left.right = resultantEdge;
            resultantEdge.left = edges.key.left;
        }
        if (edges.value.right != null) {
            edges.value.right.left = resultantEdge;
            resultantEdge.right = edges.value.right;
        }

        return true;
    }

    public boolean deleteEdge(EdgeNode edge) {
        if (edge == this.root) {
            if(edge.right != null) {
                this.root = edge.right;
            } else {
                this.root = null;
                return true;
            }
        }

        if (edge.right != null)
            edge.right.left = null;
        if (edge.left != null)
            edge.left.right = null;
        return true;
    }

    public Pair<EdgeNode, EdgeNode> findEdges(double x, double y) {
        if (this.root == null)
            return null;
        EdgeNode iterator = this.root;
        while (iterator != null) {
            if (iterator.value.domain.to == x && iterator.value.valueSet.to == y) {
                return new Pair<>(iterator, iterator.right != null ? iterator.right : null);
            }

            iterator = iterator.right;
            if (iterator == root)
                break;
        }
        return null;
    }

    public boolean doesCollide(MathEquation<?> eq) {
        if (this.root == null)
            return false;
        EdgeNode iterator = this.root;
        while (iterator != null) {
            if (iterator.value.getTheIntersectionX(eq) != null || iterator.value.doesOverlap(eq))
                return true;
            iterator = iterator.right;
            if (iterator == this.root)
                break;
        }
        return false;
    }

    public boolean isClosed() {
        if (this.root == null)
            return false;
        return this.root.left != null;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.name = tag.getString("name");
        if (!tag.getBoolean("is_root_null"))
            this.root = new EdgeNode(tag.getCompound("root"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }
    public NbtCompound writeToNbtAndReturn(NbtCompound nbt) {
        nbt.putString("name", this.name);
        if (root != null)
            nbt.put("root", this.root.saveToNbt(new NbtCompound()));
        nbt.putBoolean("is_root_null", root == null);
        return nbt;
    }

    public static class EdgeNode {
        public MathEquation<?> value;
        public EdgeNode left;
        public EdgeNode right;

        public EdgeNode(MathEquation<?> value) {
            this.value = value;
        }
        public EdgeNode(NbtCompound nbt) {
            this(nbt, null);
        }

        private EdgeNode(NbtCompound nbt, EdgeNode root) {
            if (root == null)
                root = this;
            if (nbt.getCompound("value").getString("type").equals("linearEquation"))
                this.value = LinearEquation.readFromNbt(nbt.getCompound("value"));
            else if (nbt.getCompound("value").getString("type").equals("linearFunction"))
                this.value = LinearFunction.readFromNbt(nbt.getCompound("value"));

            if (nbt.getBoolean("is_next_present")) {
                NbtCompound next = nbt.getCompound("next");
                this.right = new EdgeNode(next, root);
                this.right.left = this;
            } else {
                if (nbt.getBoolean("isClosed")) {
                    this.right = root;
                    root.left = this;
                }
            }
        }

        public NbtCompound saveToNbt(NbtCompound nbt) {
            this.saveToNbtRecursively(nbt, this);
            return nbt;
        }
        private NbtCompound saveToNbtRecursively(NbtCompound nbt, EdgeNode root) {
            if (this.value instanceof LinearEquation eq)
                nbt.put("value", eq.writeToNbt(new NbtCompound()));
            else if (this.value instanceof LinearFunction fun)
                nbt.put("value", fun.writeToNbt(new NbtCompound()));

            if (this.right != null && this.right != root)
                nbt.put("next", this.right.saveToNbtRecursively(new NbtCompound(), root));
            nbt.putBoolean("is_next_present", this.right != null && this.right != root);
            if (this.right == root)
                nbt.putBoolean("isClosed", true);
            else if (this.right == null)
                nbt.putBoolean("isClosed", false);
            return nbt;
        }
    }
}
