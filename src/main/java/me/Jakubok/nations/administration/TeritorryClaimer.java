package me.Jakubok.nations.administration;

import me.Jakubok.nations.collections.ChunkBinaryTree;
import me.Jakubok.nations.terrain.ModChunkPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TeritorryClaimer {
    protected ChunkBinaryTree chunks = new ChunkBinaryTree();
    protected World world;

    public TeritorryClaimer(World world) {
        this.world = world;
    }

    public boolean isBlockBelonging(BlockPos pos) {
        Chunk chunkpos = world.getChunk(pos);
        ModChunkPos modChunkPos = new ModChunkPos(chunkpos.getPos(), this);
        modChunkPos = chunks.get(modChunkPos);
        if (modChunkPos == null) return false;

        return modChunkPos.isBelonging(pos);
    }

    public boolean isBlockBelonging(int x, int z) {
        BlockPos pos = new BlockPos(x, 64, z);
        Chunk chunkpos = world.getChunk(pos);
        ModChunkPos modChunkPos = new ModChunkPos(chunkpos.getPos(), this);
        modChunkPos = chunks.get(modChunkPos);
        if (modChunkPos == null) return false;

        return modChunkPos.isBelonging(pos) && modChunkPos.chunkBelongsTo == this;
    }
}
