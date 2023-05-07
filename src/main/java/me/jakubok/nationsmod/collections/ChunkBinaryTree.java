package me.jakubok.nationsmod.collections;

import java.util.function.Function;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class ChunkBinaryTree extends PersistentState {

    public ChunkBinaryTree(NbtCompound tag) {
        readFromNbt(tag);
    }
    public ChunkBinaryTree() {}

    public Node<ChunkClaimRegistry> root;

    private Node<ChunkClaimRegistry> getOrCreateRecursive(Node<ChunkClaimRegistry> current, int x, int z) {
        if (current == null) {
            root = insertRecursive(root, new ChunkClaimRegistry(x, z));
            return getOrCreateRecursive(root, x, z);
        }

        if (x < current.value.getX()) {
            return this.getOrCreateRecursive(current.left, x, z);
        }
        else if (current.value.getX() == x) {
            if (z < current.value.getZ()) {
                return this.getOrCreateRecursive(current.left, x, z);
            }
            if (z > current.value.getZ()) {
                return this.getOrCreateRecursive(current.right, x, z);
            }
            this.markDirty();
            return current;
        }
        return this.getOrCreateRecursive(current.right, x, z);
    }

    public ChunkClaimRegistry getOrCreate(int x, int z) {
        return getOrCreateRecursive(root, x, z).value;
    }

    public ChunkClaimRegistry getOrCreate(BlockPos pos) {
        return getOrCreateRecursive(root, pos.getX() >> 4, pos.getZ() >> 4).value;
    }

    private Node<ChunkClaimRegistry> insertRecursive(Node<ChunkClaimRegistry> current, ChunkClaimRegistry value) {
        if (current == null) {
            current = new Node<>(value);
        }

        if (value.getX() < current.value.getX()) {
            current.left = this.insertRecursive(current.left, value);
        }
        else if (current.value.getX() == value.getX()) {
            if (value.getZ() < current.value.getZ()) {
                current.left = this.insertRecursive(current.left, value);
            }
            else if (value.getZ() > current.value.getZ()) {
                current.right = this.insertRecursive(current.right, value);
            }
            this.markDirty();
            return current;
        }
        else
            current.right = this.insertRecursive(current.right, value);
        
        this.markDirty();
        return current;
    }

    public void insert(ChunkClaimRegistry value) {
        root = insertRecursive(root, value);
    }

    private Node<ChunkClaimRegistry> getRecursive(Node<ChunkClaimRegistry> current, int x, int z) {
        if (current == null) {
            return null;
        }

        if (x < current.value.getX()) {
            return this.getRecursive(current.left, x, z);
        }
        else if (current.value.getX() == x) {
            if (z < current.value.getZ()) {
                return this.getRecursive(current.left, x, z);
            }
            if (z > current.value.getZ()) {
                return this.getRecursive(current.right, x, z);
            }
            this.markDirty();
            return current;
        }
        return this.getRecursive(current.right, x, z);
    }

    public ChunkClaimRegistry get(int x, int z) {
        Node<ChunkClaimRegistry> result = getRecursive(root, x, z);
        if (result == null) return null;
        return result.value;
    }

    public ChunkClaimRegistry get(BlockPos pos) {
        return get(pos.getX() >> 4, pos.getZ() >> 4);
    }

    public void readFromNbt(NbtCompound tag) {
        if (!tag.getBoolean("is_root_null"))
            this.root = new Node<ChunkClaimRegistry>(tag, () -> new ChunkClaimRegistry());
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if (this.root != null)
            this.root.writeToNbt(tag);
        tag.putBoolean("is_root_null", this.root == null);
        return tag;
    }

    public static ChunkBinaryTree getRegistry(ServerWorld world) {

        Function<NbtCompound, ChunkBinaryTree> createFromNbt = nbt -> {
            ChunkBinaryTree registry = new ChunkBinaryTree();
            registry.readFromNbt(nbt);
            return registry;
        };

        PersistentStateManager manager = world.getPersistentStateManager();
        ChunkBinaryTree registry = manager.getOrCreate(
            createFromNbt,
            ChunkBinaryTree::new, 
            NationsMod.MOD_ID + ":chunk_binary_tree"
        );
        return registry;
    }
}
