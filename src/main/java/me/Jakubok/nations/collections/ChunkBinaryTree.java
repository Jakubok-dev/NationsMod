package me.Jakubok.nations.collections;

import me.Jakubok.nations.terrain.ModChunkPos;
import org.jetbrains.annotations.Nullable;

public class ChunkBinaryTree {

    public Node<ModChunkPos> root;

    protected Node<ModChunkPos> addRecursive(Node<ModChunkPos> current, ModChunkPos value) {

        if (current == null) {
            current = new Node<>(value);
        }

        if (current.value.x > value.x) {
            current.right = addRecursive(current.right, value);
        }
        else if (current.value.x == value.x) {
            if (current.value.z > value.z) {
                current.right = addRecursive(current.right, value);
            }
            else if (current.value.z < value.z) {
                current.left = addRecursive(current.left, value);
            }
            return current;
        }
        current.left = addRecursive(current.left, value);

        return current;
    }

    public void add(ModChunkPos value) {
        root = addRecursive(root, value);
    }

    @Nullable
    protected Node<ModChunkPos> getRecursive(Node<ModChunkPos> current, int x, int z) {
        if (current == null) return null;

        if (current.value.x > x) {
            return getRecursive(current.right, x, z);
        }
        else if (current.value.x == x) {
            if (current.value.z > z) {
                return getRecursive(current.right, x, z);
            }
            if (current.value.z < z) {
                return getRecursive(current.left, x, z);
            }
            return current;
        }
        return getRecursive(current.left, x, z);
    }

    @Nullable
    public ModChunkPos get(int x, int z) {

        Node<ModChunkPos> res = getRecursive(root, x, z);
        if (res != null)
            return res.value;
        return null;
    }

    @Nullable
    public ModChunkPos get(ModChunkPos pos) {
        int x = pos.x;
        int z = pos.z;
        Node<ModChunkPos> res = getRecursive(root, x, z);
        if (res != null)
            return res.value;
        return null;
    }

    protected Node<ModChunkPos> getMin(Node<ModChunkPos> value) {
        Node<ModChunkPos> minimum = value;
        while (value != null) {
            if (value.value.x < minimum.value.x) minimum = value;
            else if (value.value.x == minimum.value.x && value.value.z < minimum.value.z) minimum = value;
            value = value.left;
        }
        return minimum;
    }

    @Nullable
    protected Node<ModChunkPos> removeRec(Node<ModChunkPos> node, ModChunkPos value) {
        if (node == null) return null;

        if (node.value.x > value.x) {
            node.right = removeRec(node.right, value);
        }
        else if (node.value.x == value.x) {
            if (node.value.z > value.z) {
                node.right = removeRec(node.right, value);
            }
            else if (node.value.z < value.z) {
                node.left = removeRec(node.left, value);
            }

            if (node.left == null) {
                return node.right;
            }
            else if (node.right == null) {
                return node.left;
            }

            Node<ModChunkPos> min = getMin(node.right);
            node.value = min.value;
            removeRec(node.right, min.value);
        }
        node.left = removeRec(node.left, value);

        return node;
    }

    public boolean remove(ModChunkPos pos) {
        return removeRec(root, pos) != null;
    }

    public boolean contains(int x, int z) {
        return get(x, z) == null ? false : true;
    }

    public boolean contains(ModChunkPos pos) {
        int x = pos.x;
        int z = pos.z;
        return get(x, z) == null ? false : true;
    }
}
