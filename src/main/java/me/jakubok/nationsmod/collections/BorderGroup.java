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
    private int borderSize = 0;

    public int getBorderSize() {
        return this.borderSize;
    }

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

    private Node<Border> getMaxRecursive(Node<Border> current) {
        if (current.right != null)
            return getMaxRecursive(current.right);
        return current;
    }

    private Node<Border> getMaxsParentRecursive(Node<Border> current, Node<Border> parent) {
        if (current.right == null)
            return parent;
        return getMaxsParentRecursive(current.right, current);
    }

    private Node<Border> deleteRecursive(Node<Border> current, Node<Border> parent, boolean fromLeft, int x, int z) {
        
        if (current == null) {
            return null;
        }

        if (x < current.value.position.getX()) {
            return this.deleteRecursive(current.left, current, true, x, z);
        }
        else if (current.value.position.getX() == x) {
            if (z < current.value.position.getZ()) {
                return this.deleteRecursive(current.left, current, true, x, z);
            }
            if (z > current.value.position.getZ()) {
                return this.deleteRecursive(current.right, current, false, x, z);
            }
            
            if (current.getChildsCount() == 0) {
                if (parent == null) {
                    this.root = null;
                    return null;
                }
                
                if (fromLeft) 
                    parent.left = null;
                else
                    parent.right = null;

                current.value = null;

                return null;
            }

            if (current.getChildsCount() == 1) {
                boolean hasLeftChild = current.left != null;

                if (hasLeftChild) {
                    current.value = current.left.value;

                    return this.deleteRecursive(current.left, current, true, current.left.value.position.getX(), current.left.value.position.getZ());

                } else {
                    current.value = current.right.value;

                    return this.deleteRecursive(current.right, current, false, current.right.value.position.getX(), current.right.value.position.getZ());
                }
            }

            if (current.getChildsCount() == 2) {
                Node<Border> inorderPredecessor = this.getMaxRecursive(current.left);
                Node<Border> inorderPredecessorsParent = this.getMaxsParentRecursive(current.left, current);

                current.value = inorderPredecessor.value;

                this.deleteRecursive(
                    inorderPredecessor, 
                    inorderPredecessorsParent, 
                    inorderPredecessorsParent.left == inorderPredecessor, inorderPredecessor.value.position.getX(),
                    inorderPredecessor.value.position.getZ()
                );
            }
        }
        return this.deleteRecursive(current.right, current, false, x, z);
    }

    public void delete(int x, int z) {
        if (this.contains(x, z))
            this.borderSize--;
        this.deleteRecursive(root, null, false, x, z);
    }

    public void delete(Border value) {
        this.delete(value.position.getX(), value.position.getZ());
    }

    public void insert(Border value) {
        if (!this.contains(value.position))
            this.borderSize++;
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
        this.borderSize = tag.getInt("border_size");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.root != null)
            this.root.writeToNbt(tag);
        tag.putBoolean("is_root_null", this.root == null);
        tag.putString("name", this.name);
        tag.putInt("border_size", this.borderSize);
    }
}
