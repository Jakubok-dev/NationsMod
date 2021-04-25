package me.Jakubok.nations.terrain;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModChunkPos extends ChunkPos {

    protected List<BlockPos> belongingBlocks = new ArrayList<>();

    public ModChunkPos(ChunkPos chunkPos) {
        super(chunkPos.x, chunkPos.z);
    }

    public boolean isBelonging(BlockPos pos) {
        BlockPos fixedPos = new BlockPos(pos.getX(), 64, pos.getZ());
        return belongingBlocks.contains(fixedPos);
    }

    public boolean isBelonging(int x, int z) {
        BlockPos pos = new BlockPos(x, 64, z);
        return belongingBlocks.contains(pos);
    }

    public boolean markAsBelonging(BlockPos pos) {
        return belongingBlocks.add(new BlockPos(pos.getX(), 64, pos.getZ()));
    }

    public boolean markAsBelonging(int x, int z) {
        return belongingBlocks.add(new BlockPos(x, 64, z));
    }

    public boolean markAsNotBelonging(BlockPos pos) {
        return belongingBlocks.remove(new BlockPos(pos.getX(), 64, pos.getZ()));
    }

    public boolean markAsNotBelonging(int x, int z) {
        return belongingBlocks.remove(new BlockPos(x, 64, z));
    }
}
