package me.Jakubok.nations.terrain;

import me.Jakubok.nations.administration.TeritorryClaimer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class ModChunkPos extends ChunkPos {

    protected List<BlockPos> belongingBlocks = new ArrayList<>();
    public TeritorryClaimer chunkBelongsTo;

    public ModChunkPos(ChunkPos chunkPos, TeritorryClaimer chunkBelongsTo) {
        super(chunkPos.x, chunkPos.z);
        this.chunkBelongsTo = chunkBelongsTo;
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

    public void abandon() {
        belongingBlocks.clear();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ModChunkPos) {
            ModChunkPos chunkPos = (ModChunkPos) o;
            if (belongingBlocks.equals(chunkPos.belongingBlocks) && super.equals(chunkPos)) {
                return true;
            }
        }
        return false;
    }
}
