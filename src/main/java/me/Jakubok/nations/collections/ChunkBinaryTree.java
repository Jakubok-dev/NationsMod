package me.Jakubok.nations.collections;

import me.Jakubok.nations.terrain.ModChunkPos;
import org.jetbrains.annotations.Nullable;

public class ChunkBinaryTree {

    public Leaf<ModChunkPos> root;

    protected Leaf<ModChunkPos> addRecursive(Leaf<ModChunkPos> current, ModChunkPos value) {

        if (current == null)
            return new Leaf<>(value);

        if (value.x < current.value.x)
            return addRecursive(current.left, value);
        else if (value.x == current.value.x) {

            if (value.z < current.value.z)
                return addRecursive(current.left, value);
            else if (value.z > current.value.z)
                return addRecursive(current.right, value);
            return current;
        }
        else if (value.x > current.value.x)
            return addRecursive(current.right, value);

        return current;
    }

    public void add(ModChunkPos value) {
        root = addRecursive(root, value);
    }

    @Nullable
    protected Leaf<ModChunkPos> getRecursive(Leaf<ModChunkPos> current, int x, int z) {
        if (current == null)
            return null;

        if (x < current.value.x)
            return getRecursive(current.left, x, z);
        else if (x == current.value.x) {

            if (z < current.value.z)
                return getRecursive(current.left, x, z);
            else if (z > current.value.z)
                return getRecursive(current.left, x, z);
            return current;
        }
        else if (x > current.value.x)
            return getRecursive(current.left, x, z);

        return current;
    }

    @Nullable
    public ModChunkPos get(int x, int z) {

        Leaf<ModChunkPos> res = getRecursive(root, x, z);
        if (res != null)
            return getRecursive(root, x, z).value;
        return null;
    }

    @Nullable
    public ModChunkPos get(ModChunkPos pos) {
        int x = pos.x;
        int z = pos.z;
        Leaf<ModChunkPos> res = getRecursive(root, x, z);
        if (res != null)
            return getRecursive(root, x, z).value;
        return null;
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
