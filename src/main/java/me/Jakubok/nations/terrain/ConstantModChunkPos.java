package me.Jakubok.nations.terrain;

import me.Jakubok.nations.administration.TerritoryClaimer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ConstantModChunkPos {

    public final List<BlockPos> blocks = new ArrayList<>();
    public final int blocksCount;
    public final TerritoryClaimer claimer;
    public final int x;
    public final int z;

    public ConstantModChunkPos(ModChunkPos chunk, TerritoryClaimer claimer) {
        this.claimer = claimer;
        this.blocksCount = setBlocks(chunk);
        this.x = chunk.x;
        this.z = chunk.z;
    }

    public ConstantModChunkPos(NbtCompound tag, TerritoryClaimer claimer) {
        this.x = tag.getInt("chunkX");
        this.z = tag.getInt("chunkZ");
        this.claimer = claimer;
        this.blocksCount = tag.getInt("blocksCount");
        for (int i = 0; i < blocksCount; i++) {
            blocks.add(arrayToBlockPos(tag.getIntArray("block" + i)));
        }
    }

    protected int setBlocks(ModChunkPos chunk) {
        chunk.belongingBlocks.forEach(((blockPos, claimer1) -> {
            if (claimer1 == claimer) blocks.add(blockPos);
        }));
        return blocks.size();
    }

    protected int[] blockPosToArray(BlockPos pos) {
        int[] result = {pos.getX(), pos.getZ()};
        return result;
    }

    protected BlockPos arrayToBlockPos(int[] pos) {
        if (pos.length < 1) return null;
        return new BlockPos(pos[0], 64, pos[1]);
    }

    public NbtCompound saveToTag(NbtCompound tag) {
        tag.putInt("blocksCount", blocksCount);
        tag.putInt("chunkX", x);
        tag.putInt("chunkZ", z);
        for (int i = 0; i < blocksCount; i++)
            tag.putIntArray("block" + i, blockPosToArray(blocks.get(i)));

        return tag;
    }
}
