package me.jakubok.nationsmod.collections;

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

    private Node<Border> insertRecursive(Node<Border> current, Border value) {
        if (current == null) {
            current = new Node<>(value);
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
        else
            current.right = this.insertRecursive(current.right, value);

        return current;
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
