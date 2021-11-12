package me.jakubok.nationsmod.collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class BorderGroup implements ComponentV3 {

    public BorderGroup(NbtCompound tag) {
        this.readFromNbt(tag);
    }
    public BorderGroup(String name) {
        this.name = name;
    }
    public BorderGroup() {}

    public Node<Border> root;
    public String name;

    public int getBorderSize() {
        return this.toList().size();
    }

    private Node<Border> insertRecursive(Node<Border> current, Border value) {
        if (current == null) {
            current = new Node<>(value);
            return current;
        }

        if (value.position.getX() < current.value.position.getX()) {
            current.left = this.insertRecursive(current.left, value);
        }
        else if (current.value.position.getX() == value.position.getX()) {
            if (value.position.getZ() < current.value.position.getZ()) {
                current.left = this.insertRecursive(current.left, value);
            }
            else if (value.position.getZ() > current.value.position.getZ()) {
                current.right = this.insertRecursive(current.right, value);
            }
            return current;
        }
        else if (value.position.getX() > current.value.position.getX())
            current.right = this.insertRecursive(current.right, value);

        return current;
    }

    private Node<Border> deleteRecursive(Node<Border> current, int x, int z) {
        
        if (current == null)
            return null;

        if (current.value.position.getX() > x) {
            current.left = deleteRecursive(current.left, x, z);
        } else if (current.value.position.getX() < x) {
            current.right = deleteRecursive(current.right, x, z);
        } else if (current.value.position.getX() == x) {

            if (current.value.position.getZ() > z) {
                current.left = deleteRecursive(current.left, x, z);
            } else if (current.value.position.getZ() < z) {
                current.right = deleteRecursive(current.right, x, z);
            } else if (current.value.position.getZ() == z) {

                if (current.left == null)
                    return current.right;
                else if (current.right == null)
                    return current.left;

                current.value = this.minValue(current.right);

                current.right = this.deleteRecursive(current.right, current.value.position.getX(), current.value.position.getZ());
            }
        }

        return current;
    }

    public Border minValue(Node<Border> current) {
        Border minimumValue = current.value;
        while(current.left != null) {
            minimumValue = current.left.value;
            current = current.left;
        }
        return minimumValue;
    }

    public void delete(int x, int z) {
        this.root = this.deleteRecursive(root, x, z);
    }

    public void delete(Border value) {
        this.delete(value.position.getX(), value.position.getZ());
    }

    public void insert(Border value) {
        root = insertRecursive(root, value);
    }

    private Node<Border> getRecursive(Node<Border> current, int x, int z) {
        if (current == null) {
            return null;
        }

        if (x < current.value.position.getX()) {
            return this.getRecursive(current.left, x, z);
        }
        else if (current.value.position.getX() == x) {
            if (z < current.value.position.getZ()) {
                return this.getRecursive(current.left, x, z);
            }
            if (z > current.value.position.getZ()) {
                return this.getRecursive(current.right, x, z);
            }
            return current;
        }
        return this.getRecursive(current.right, x, z);
    }

    public Border get(int x, int z) {
        Node<Border> result = this.getRecursive(root, x, z);
        if (result == null) return null;
        return result.value;
    }
    public Border get(BlockPos pos) {
        return this.get(pos.getX(), pos.getZ());
    }

    public boolean contains(int x, int z) {
        return this.get(x, z) != null;
    }
    public boolean contains(BlockPos pos) {
        return this.get(pos) != null;
    }

    public List<Border> toList() {
        List<Border> arr = new ArrayList<>();
        if (root == null) return arr;
        Queue<Node<Border>> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            Node<Border> temp = q.remove();
            arr.add(temp.value);
            if (temp.left != null) q.add(temp.left);
            if (temp.right != null) q.add(temp.right);
        }

        return arr;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (!tag.getBoolean("is_root_null"))
            this.root = new Node<Border>(tag, () -> new Border());
        this.name = tag.getString("name");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.root != null)
            this.root.writeToNbt(tag);
        tag.putBoolean("is_root_null", this.root == null);
        tag.putString("name", this.name);
    }
}
