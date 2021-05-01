package me.Jakubok.nations.util;

import me.Jakubok.nations.collections.ChunkBinaryTree;
import me.Jakubok.nations.terrain.ModChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class GlobalChunkRegistry {
    protected static HashMap<World, ChunkBinaryTree> GLOBAL_CHUNKS = new HashMap<>();

    public static boolean register(ModChunkPos chunk, World world) {
        if (!GLOBAL_CHUNKS.containsKey(world))
            GLOBAL_CHUNKS.put(world, new ChunkBinaryTree());

        if (GLOBAL_CHUNKS.get(world).contains(chunk)) return false;
        GLOBAL_CHUNKS.get(world).add(chunk);
        return true;
    }

    public static boolean contains(ModChunkPos chunk, World world) {
        if (!GLOBAL_CHUNKS.containsKey(world))
            GLOBAL_CHUNKS.put(world, new ChunkBinaryTree());
        return GLOBAL_CHUNKS.get(world).contains(chunk);
    }

    public static boolean containsWorld(World world) {
        return GLOBAL_CHUNKS.containsKey(world);
    }

    @Nullable
    public static ModChunkPos get(World world, int x, int z) {
        if (!GLOBAL_CHUNKS.containsKey(world))
            GLOBAL_CHUNKS.put(world, new ChunkBinaryTree());
        return GLOBAL_CHUNKS.get(world).get(x, z);
    }

    @Nullable
    public static ModChunkPos get(World world, ModChunkPos chunk) {
        if (!GLOBAL_CHUNKS.containsKey(world))
            GLOBAL_CHUNKS.put(world, new ChunkBinaryTree());
        return GLOBAL_CHUNKS.get(world).get(chunk);
    }

    public static List<ModChunkPos> toList(World world) {
        return GLOBAL_CHUNKS.get(world).treeToList();
    }
}
