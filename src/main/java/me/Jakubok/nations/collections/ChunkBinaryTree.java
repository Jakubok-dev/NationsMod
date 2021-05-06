package me.Jakubok.nations.collections;

import me.Jakubok.nations.administration.TerritoryClaimer;
import me.Jakubok.nations.terrain.ConstantModChunkPos;
import me.Jakubok.nations.terrain.ModChunkPos;
import me.Jakubok.nations.util.GlobalChunkRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ChunkBinaryTree {

    public ChunkBinaryTree() {}
    public ChunkBinaryTree(CompoundTag tag, TerritoryClaimer claimer, boolean register, World world) {
        int count = tag.getInt("chunksCount");
        if (register) {
            for (int i = 0; i < count; i++) {
                CompoundTag subTag = (CompoundTag)tag.get("chunk" + i);
                ConstantModChunkPos constModChunkPos = new ConstantModChunkPos(subTag, claimer);

                ModChunkPos modChunkPos = new ModChunkPos(new ChunkPos(constModChunkPos.x, constModChunkPos.z));

                GlobalChunkRegistry.register(modChunkPos, world);
                GlobalChunkRegistry.get(world, modChunkPos).addConstant(constModChunkPos);
                modChunkPos = GlobalChunkRegistry.get(world, modChunkPos);

                add(modChunkPos);
            }
        }
        else {
            for (int i = 0; i < count; i++) {
                CompoundTag subTag = (CompoundTag)tag.get("chunk" + i);
                ConstantModChunkPos constModChunkPos = new ConstantModChunkPos(subTag, claimer);

                ModChunkPos modChunkPos = new ModChunkPos(new ChunkPos(constModChunkPos.x, constModChunkPos.z));
                modChunkPos.addConstant(constModChunkPos);

                add(modChunkPos);
            }
        }
    }

    public Node<ModChunkPos> root;

    protected Node<ModChunkPos> addRecursive(Node<ModChunkPos> current, ModChunkPos value) {

        if (current == null) {
            current = new Node<>(value);
        }

        if (value.x < current.value.x) {
            current.left = addRecursive(current.left, value);
        }
        else if (current.value.x == value.x) {
            if (value.z < current.value.z) {
                current.left = addRecursive(current.left, value);
            }
            else if (value.z > current.value.z) {
                current.right = addRecursive(current.right, value);
            }
            return current;
        }
        else
            current.right = addRecursive(current.right, value);

        return current;
    }

    public void add(ModChunkPos value) {
        root = addRecursive(root, value);
    }

    @Nullable
    protected Node<ModChunkPos> getRecursive(Node<ModChunkPos> current, int x, int z) {
        if (current == null) return null;

        if (x < current.value.x) {
            return getRecursive(current.left, x, z);
        }
        else if (current.value.x == x) {
            if (z < current.value.z) {
                return getRecursive(current.left, x, z);
            }
            if (z > current.value.z) {
                return getRecursive(current.right, x, z);
            }
            return current;
        }
        return getRecursive(current.right, x, z);
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

    @Nullable
    public Node<ModChunkPos> getNode(ModChunkPos pos) {
        int x = pos.x;
        int z = pos.z;
        Node<ModChunkPos> res = getRecursive(root, x, z);
        return res;
    }

    public boolean contains(int x, int z) {
        return get(x, z) != null;
    }

    public boolean contains(ModChunkPos pos) {
        int x = pos.x;
        int z = pos.z;
        return get(x, z) != null;
    }

    public List<ModChunkPos> treeToList() {
        List<ModChunkPos> arr = new ArrayList<>();
        Queue<Node<ModChunkPos>> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            Node<ModChunkPos> temp = q.remove();
            arr.add(temp.value);
            if (temp.left != null) q.add(temp.left);
            if (temp.right != null) q.add(temp.right);
        }

        return arr;
    }

    public void clear() {
        root = null;
    }

    public CompoundTag saveToTag(CompoundTag tag, TerritoryClaimer claimer) {
        List<ModChunkPos> temp = treeToList();
        List<ConstantModChunkPos> constTemp = new ArrayList<>();
        temp.forEach(chunkPos -> constTemp.add(new ConstantModChunkPos(chunkPos, claimer)));
        for (int i = 0; i < constTemp.size(); i++) {
            CompoundTag subTag = new CompoundTag();
            constTemp.get(i).saveToTag(subTag);
            tag.put("chunk" + i, subTag);
        }
        tag.putInt("chunksCount", constTemp.size());
        return tag;
    }
}