package me.jakubok.nationsmod.geometry;

import me.jakubok.nationsmod.collection.PolygonNode;
import me.jakubok.nationsmod.collection.Serialisable;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Polygon implements Serialisable {
    public PolygonNode<Point> root;
    public String name;
    Map<String, Consumer<Polygon>> subscribers = new HashMap<>();
    Range domain = null, valueSet = null;
    public Polygon(String name) {
        this.name = name;
    }
    public Polygon(NbtCompound nbt) {
        this.readFromNbt(nbt);
    }

    public boolean add(Point added, Point pointInThePolygon) {
        if (this.isEmpty())
            this.addToTheRight(pointInThePolygon);
        if (this.root.value.equals(pointInThePolygon))
            return this.addToTheLeft(added);
        if (this.getTheLastNode().value.equals(pointInThePolygon))
            return this.addToTheRight(added);
        return false;
    }

    public boolean addToTheRight(Point p) {
        PolygonNode<Point> newNode = new PolygonNode<>(p);
        if (this.isEmpty()) {
            this.root = newNode;
            this.checkRanges(p, false);
            this.markDirty();
            return true;
        }
        if (this.root.left == null && this.root.right == null) {
            this.root.right = newNode;
            newNode.left = this.root;
            this.checkRanges(p, false);
            this.markDirty();
            return true;
        }

        PolygonNode<Point> lastNode = this.getTheLastNode();
        if (this.getTheNode(p) == this.root)
            return this.closeTheShape();
        if (this.doesInterfereWithThePolygon(lastNode.value, newNode.value) || this.getTheNode(p) != null)
            return false;
        lastNode.right = newNode;
        newNode.left = lastNode;
        this.checkRanges(p, false);
        this.markDirty();
        return true;
    }

    public boolean addToTheLeft(Point p) {
        PolygonNode<Point> newNode = new PolygonNode<>(p);
        if (this.isEmpty()) {
            this.root = newNode;
            this.checkRanges(p, false);
            this.markDirty();
            return true;
        }
        if (this.root.left == null && this.root.right == null) {
            this.root.left = newNode;
            newNode.right = this.root;
            this.root = newNode;
            this.checkRanges(p, false);
            this.markDirty();
            return true;
        }

        if (this.getTheNode(p) == this.getTheLastNode())
            return this.closeTheShape();
        if (this.doesInterfereWithThePolygon(root.value, newNode.value) || this.getTheNode(p) != null)
            return false;
        this.root.left = newNode;
        newNode.right = this.root;
        this.root = newNode;
        this.checkRanges(p, false);
        this.markDirty();
        return true;
    }

    protected boolean closeTheShape() {
        PolygonNode<Point> lastNode = this.getTheLastNode();
        if (this.doesInterfereWithThePolygon(this.root.value, lastNode.value))
            return false;
        this.root.left = lastNode;
        lastNode.right = this.root;
        this.markDirty();
        return true;
    }

    public boolean openTheShape(Point p1, Point p2) {
        if (!this.isThePolygonClosed())
            return false;
        PolygonNode<Point> n1 = this.getTheNode(p1);
        PolygonNode<Point> n2 = this.getTheNode(p2);

        if (n1 == null || n2 == null)
            return false;

        if (n1.left == n2) {
            n1.left = null;
            n2.right = null;
            this.root = n1;

            this.markDirty();
            return true;
        }
        if (n1.right == n2) {
            n1.right = null;
            n2.left = null;
            this.root = n2;

            this.markDirty();
            return true;
        }
        return false;
    }

    public boolean delete(Point p) {
        if (this.isEmpty())
            return false;
        if (this.root.left == null && this.root.right == null) {
            if (this.root.value.equals(p)) {
                this.root = null;
                this.checkRanges(p, true);
                this.markDirty();
                return true;
            } else return false;
        }

        if (root.right == null)
            return false;
        if (root.right.right == null) {
            if (this.root.value.equals(p)) {
                PolygonNode<Point> temp = this.root;
                this.root.right.left = null;
                this.root = this.root.right;
                temp.right = null;
                this.checkRanges(p, true);
                this.markDirty();
                return true;
            } else if (this.root.right.value.equals(p)) {
                this.root.right.left = null;
                this.root.right = null;
                this.checkRanges(p, true);
                this.markDirty();
                return true;
            } else return false;
        }

        PolygonNode<Point> foundNode = this.getTheNode(p);
        if (foundNode == null)
            return false;

        if (foundNode.left == null) {
            foundNode.right.left = null;
            this.root = foundNode.right;
            foundNode.right = null;
            this.checkRanges(p, true);
            this.markDirty();
            return true;
        }
        if (foundNode.right == null) {
            foundNode.left.right = null;
            foundNode.left = null;
            this.checkRanges(p, true);
            this.markDirty();
            return true;
        }

        if (this.doesInterfereWithThePolygon(foundNode.left.value, foundNode.right.value, foundNode))
            return false;

        foundNode.left.right = foundNode.right;
        foundNode.right.left = foundNode.left;
        if (this.root.value.equals(foundNode.value))
            this.root = foundNode.right;
        foundNode.right = null;
        foundNode.left = null;
        this.checkRanges(p, true);
        this.markDirty();
        return true;
    }

    public boolean insert(Point insertedPoint, Point p1, Point p2) {
        PolygonNode<Point> node1 = this.getTheNode(p1);
        PolygonNode<Point> node2 = this.getTheNode(p2);
        if (node1 == null || node2 == null || this.getTheNode(insertedPoint) != null)
            return false;
        PolygonNode<Point> leftSide, rightSide;
        if (node1.right == node2) {
            leftSide = node1;
            rightSide = node2;
        } else if (node1.left == node2) {
            rightSide = node1;
            leftSide = node2;
        } else return false;

        if (this.doesInterfereWithThePolygon(leftSide.value, insertedPoint, leftSide.value, rightSide.value) || this.doesInterfereWithThePolygon(insertedPoint, rightSide.value, leftSide.value, rightSide.value))
            return false;

        PolygonNode<Point> insertedNode = new PolygonNode<>(insertedPoint);
        leftSide.right = insertedNode;
        rightSide.left = insertedNode;
        insertedNode.left = leftSide;
        insertedNode.right = rightSide;

        this.updateRanges();
        this.markDirty();
        return true;
    }

    public boolean doesIntersectWithThePolygon(Point a, Point b) {
        if (this.isEmpty())
            return false;
        PolygonNode<Point> t = root.right;
        while (t != null) {
            if (Polygon.doesIntersect(a, b, t.left.value, t.value))
                return true;
            t = t.right;
            if (t == root.right)
                break;
        }
        return false;
    }

    public boolean doesInterfereWithThePolygon(Point a, Point b) {
        if (this.isEmpty())
            return false;
        PolygonNode<Point> t = root.right;
        while (t != null) {
            if (Polygon.doesInterfere(a, b, t.left.value, t.value))
                return true;
            t = t.right;
            if (t == root.right)
                break;
        }
        return false;
    }
    public boolean doesInterfereWithThePolygon(Point a, Point b, Point ignoredA, Point ignoredB) {
        if (this.isEmpty())
            return false;
        PolygonNode<Point> t = root.right;
        while (t != null) {
            if (t.value.equals(ignoredA) && t.left.value.equals(ignoredB) || t.value.equals(ignoredB) && t.left.value.equals(ignoredA)) {
                t = t.right;
                if (t == root.right)
                    break;
                continue;
            }
            if (Polygon.doesInterfere(a, b, t.left.value, t.value))
                return true;
            t = t.right;
            if (t == root.right)
                break;
        }
        return false;
    }
    public boolean doesInterfereWithThePolygon(Point a, Point b, PolygonNode<Point> ignoredPoint) {
        if (this.isEmpty())
            return false;
        MathEquation<?> ignoredEdge1 = null, ignoredEdge2 = null;
        if (ignoredPoint != null) {
            if (ignoredPoint.left != null)
                ignoredEdge1 = MathEquation.fromTwoPoints(ignoredPoint.left.value.key, ignoredPoint.left.value.value, ignoredPoint.value.key, ignoredPoint.value.value);
            if (ignoredPoint.right != null)
                ignoredEdge2 = MathEquation.fromTwoPoints(ignoredPoint.value.key, ignoredPoint.value.value, ignoredPoint.right.value.key, ignoredPoint.right.value.value);
        }
        PolygonNode<Point> t = root.right;
        while (t != null) {
            if (ignoredEdge1 != null)
                if (MathEquation.fromTwoPoints(t.left.value.key, t.left.value.value, t.value.key, t.value.value).doesOverlap(ignoredEdge1)) {
                    t = t.right;
                    if (t == root.right)
                        break;
                    continue;
                }
            if (ignoredEdge2 != null)
                if (MathEquation.fromTwoPoints(t.left.value.key, t.left.value.value, t.value.key, t.value.value).doesOverlap(ignoredEdge2)) {
                    t = t.right;
                    if (t == root.right)
                        break;
                    continue;
                }
            if (Polygon.doesInterfere(a, b, t.left.value, t.value))
                return true;
            t = t.right;
            if (t == root.right)
                break;
        }
        return false;
    }

    public PolygonNode<Point> getTheLastNode() {
        if (this.isEmpty())
            return null;
        if (this.isThePolygonClosed())
            return root.left;
        PolygonNode<Point> t = root;
        while (t.right != null)
            t = t.right;
        return t;
    }

    public boolean isThePolygonClosed() {
        if (this.isEmpty())
            return false;
        return root.left != null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public PolygonNode<Point> getTheNode(Point p) {
        if (this.isEmpty())
            return null;
        PolygonNode<Point> t = this.root;
        while (t != null) {
            if (t.value.equals(p))
                return t;

            t = t.right;
            if (t == this.root)
                break;
        }
        return null;
    }

    public int size() {
        if (this.isEmpty())
            return 0;
        int result = 0;
        PolygonNode<Point> t = this.root;
        while (t != null) {
            result++;
            t = t.right;
            if (t == this.root)
                break;
        }
        return result;
    }

    public Range getDomain() {
        return domain;
    }

    public Range getValueSet() {
        return valueSet;
    }

    protected boolean checkRanges(Point a, boolean deleted) {
        if (deleted) {
            if (this.domain == null || this.valueSet == null)
                return false;
            if (this.domain.from == a.key || this.domain.to == a.key || this.valueSet.from == a.value || this.valueSet.to == a.value) {
                return this.updateRanges();
            }
            return false;
        }

        if (this.domain == null || this.valueSet == null) {
            if (this.domain == null)
                this.domain = new Range(a.key, a.key);
            if (this.valueSet == null)
                this.valueSet = new Range(a.value, a.value);
            return true;
        }
        boolean changed = false;
        if (this.domain.from > a.key) {
            changed = true;
            this.domain = new Range(a.key, this.domain.to);
        }
        if (this.domain.to < a.key) {
            changed = true;
            this.domain = new Range(this.domain.from, a.key);
        }
        if (this.valueSet.from > a.value) {
            changed = true;
            this.valueSet = new Range(a.value, this.valueSet.to);
        }
        if (this.valueSet.to < a.value) {
            changed = true;
            this.valueSet = new Range(this.valueSet.from, a.value);
        }
        return changed;
    }

    protected boolean updateRanges() {
        if (root == null) {
            this.domain = null; this.valueSet = null;
            return true;
        }

        int xmin = this.root.value.key, xmax = this.root.value.key, ymin = this.root.value.value, ymax = this.root.value.value;
        PolygonNode<Point> n = this.root.right;
        while (n != null) {
            xmin = Math.min(n.value.key, xmin);
            xmax = Math.max(n.value.key, xmax);
            ymin = Math.min(n.value.value, ymin);
            ymax = Math.max(n.value.value, ymax);
            n = n.right;
            if (n == this.root)
                break;
        }
        Range oldDomain = this.domain, oldValueSet = this.valueSet;
        this.domain = new Range(xmin, xmax);
        this.valueSet = new Range(ymin, ymax);
        return !(this.domain.equals(oldDomain) && this.valueSet.equals(oldValueSet));
    }

    public Consumer<Polygon> subscribe(String key, Consumer<Polygon> subscriber) {
        return this.subscribers.put(key, subscriber);
    }

    public Consumer<Polygon> unsubscribe(String key) {
        return this.subscribers.remove(key);
    }

    protected void markDirty() {
        this.subscribers.forEach((k, v) -> {
            v.accept(this);
        });
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.getString("name") != null)
            this.name = tag.getString("name");
        if (!tag.getBoolean("is_empty")) {
            for (int i = 0; i < tag.getInt("size"); i++) {
                NbtCompound subtag = tag.getCompound("node" + i);
                Point p = new Point(0, 0);
                p.readFromNbt(subtag);
                this.addToTheRight(p);
            }
            if (tag.getBoolean("is_closed"))
                this.closeTheShape();
        }
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {

        if (!this.isEmpty()) {
            PolygonNode<Point> node = this.root;
            int i = 0;
            for (; node != null; i++) {
                tag.put("node" + i, node.value.writeToNbtAndReturn(new NbtCompound()));

                node = node.right;
                if (node == this.root) {
                    i++;
                    break;
                }
            }
            tag.putInt("size", i);
        }
        tag.putBoolean("is_empty", this.isEmpty());
        tag.putBoolean("is_closed", this.isThePolygonClosed());

        tag.putString("name", this.name);
        return tag;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    /**
     *
     * @param a The 1st point of the 1st line
     * @param b The 2nd point of the 1st line
     * @param ax The 1st point of the 2nd line
     * @param bx The 2nd point of the 2nd line
     */
    public static boolean doesIntersect(Point a, Point b, Point ax, Point bx) {
        MathEquation<?> eq = MathEquation.fromTwoPoints(a.key, a.value, b.key, b.value);
        MathEquation<?> eqx = MathEquation.fromTwoPoints(ax.key, ax.value, bx.key, bx.value);
        return eq.getTheIntersectionX(eqx) != null;
    }

    public static boolean doesOverlap(Point a, Point b, Point ax, Point bx) {
        MathEquation<?> eq = MathEquation.fromTwoPoints(a.key, a.value, b.key, b.value);
        MathEquation<?> eqx = MathEquation.fromTwoPoints(ax.key, ax.value, bx.key, bx.value);
        return eq.doesOverlap(eqx);
    }

    public static boolean doesInterfere(Point a, Point b, Point ax, Point bx) {
        return doesIntersect(a, b, ax, bx) || doesOverlap(a, b, ax, bx);
    }

    public static MathEquation<?> makeAnEdge(Point a, Point b) {
        return MathEquation.fromTwoPoints(a.key, a.value, b.key, b.value);
    }
}
