package me.Jakubok.nations.administration;

import me.Jakubok.nations.collections.ChunkBinaryTree;
import me.Jakubok.nations.terrain.ModChunkPos;
import me.Jakubok.nations.util.GlobalChunkRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public abstract class TeritorryClaimer {
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

        return modChunkPos.isBelonging(pos, this);
    }

    public boolean isBlockBelonging(int x, int z) {
        Chunk chunkpos = world.getChunk(x, z);
        ModChunkPos modChunkPos = new ModChunkPos(chunkpos.getPos(), this);

        modChunkPos = chunks.get(modChunkPos);
        if (modChunkPos == null) return false;

        return modChunkPos.isBelonging(x, z, this);
    }

    protected void markBlock(BlockPos pos) {
        Chunk chunkpos = world.getChunk(pos);
        ModChunkPos modChunkPos = new ModChunkPos(chunkpos.getPos(), this);
        if (!chunks.contains(modChunkPos)) createChunk(modChunkPos);
        modChunkPos = chunks.get(modChunkPos);

        modChunkPos.markAsBelonging(pos, this);
    }

    protected void markBlock(int x, int z) {
        Chunk chunkpos = world.getChunk(x, z);
        ModChunkPos modChunkPos = new ModChunkPos(chunkpos.getPos(), this);
        if (!chunks.contains(modChunkPos)) createChunk(modChunkPos);
        modChunkPos = chunks.get(modChunkPos);

        modChunkPos.markAsBelonging(x, z, this);
    }

    protected void unmarkBlock(BlockPos pos) {
        Chunk chunkpos = world.getChunk(pos);
        ModChunkPos modChunkPos = new ModChunkPos(chunkpos.getPos(), this);
        if (!chunks.contains(modChunkPos)) createChunk(modChunkPos);
        modChunkPos = chunks.get(modChunkPos);

        modChunkPos.markAsNotBelonging(pos);
    }

    protected void unmarkBlock(int x, int z) {
        Chunk chunkpos = world.getChunk(x, z);
        ModChunkPos modChunkPos = new ModChunkPos(chunkpos.getPos(), this);
        if (!chunks.contains(modChunkPos)) createChunk(modChunkPos);
        modChunkPos = chunks.get(modChunkPos);

        modChunkPos.markAsNotBelonging(x, z);
    }

    protected boolean createChunk(ModChunkPos chunk) {
        if (GlobalChunkRegistry.contains(chunk, world)) {
            if (chunks.contains(chunk)) return false;
            chunks.add(GlobalChunkRegistry.get(world, chunk));
        }
        GlobalChunkRegistry.register(chunk, world);
        if (chunks.contains(chunk)) return false;
        chunks.add(chunk);

        return true;
    }

    protected abstract boolean expand();
    protected abstract void abandon();
}