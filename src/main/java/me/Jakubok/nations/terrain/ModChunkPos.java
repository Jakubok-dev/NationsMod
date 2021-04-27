package me.Jakubok.nations.terrain;

import me.Jakubok.nations.administration.TeritorryClaimer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.Map;

public class ModChunkPos extends ChunkPos {

    protected Map<BlockPos, TeritorryClaimer> belongingBlocks = new HashMap<>();

    public ModChunkPos(ChunkPos chunkPos, TeritorryClaimer claimer) {
        super(chunkPos.x, chunkPos.z);
    }

    public boolean isBelonging(BlockPos pos, TeritorryClaimer claimer) {
        BlockPos fixedPos = new BlockPos(pos.getX(), 64, pos.getZ());

        if (!belongingBlocks.containsKey(fixedPos)) return false;
        return belongingBlocks.get(fixedPos) == claimer;
    }

    public boolean isBelonging(int x, int z, TeritorryClaimer claimer) {
        BlockPos pos = new BlockPos(x, 64, z);

        if (!belongingBlocks.containsKey(pos)) return false;
        return belongingBlocks.get(pos) == claimer;
    }

    public void markAsBelonging(BlockPos pos, TeritorryClaimer claimer) {
        belongingBlocks.put(new BlockPos(pos.getX(), 64, pos.getZ()), claimer);
    }

    public void markAsBelonging(int x, int z, TeritorryClaimer claimer) {
        belongingBlocks.put(new BlockPos(x, 64, z), claimer);
    }

    public void markAsNotBelonging(BlockPos pos) {
        belongingBlocks.remove(new BlockPos(pos.getX(), 64, pos.getZ()));
    }

    public void markAsNotBelonging(int x, int z) {
        belongingBlocks.remove(new BlockPos(x, 64, z));
    }

    public void abandon() {
        belongingBlocks.clear();
    }
}
