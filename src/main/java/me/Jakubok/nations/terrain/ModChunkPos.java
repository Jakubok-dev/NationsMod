package me.Jakubok.nations.terrain;

import me.Jakubok.nations.administration.TerritoryClaimer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModChunkPos extends ChunkPos {

    protected Map<BlockPos, TerritoryClaimer> belongingBlocks = new HashMap<>();

    public ModChunkPos(ChunkPos chunkPos) {
        super(chunkPos.x, chunkPos.z);
    }

    public boolean isBelonging(BlockPos pos, TerritoryClaimer claimer) {
        BlockPos fixedPos = new BlockPos(pos.getX(), 64, pos.getZ());

        if (!belongingBlocks.containsKey(fixedPos)) return false;
        return belongingBlocks.get(fixedPos) == claimer;
    }

    public boolean isBelonging(int x, int z, TerritoryClaimer claimer) {
        BlockPos pos = new BlockPos(x, 64, z);

        if (!belongingBlocks.containsKey(pos)) return false;
        return belongingBlocks.get(pos) == claimer;
    }

    public void markAsBelonging(BlockPos pos, TerritoryClaimer claimer) {
        ChunkPos chpos = new ChunkPos(pos);
        if (chpos.x != x || chpos.z != z) return;
        belongingBlocks.put(new BlockPos(pos.getX(), 64, pos.getZ()), claimer);
    }

    public void markAsBelonging(int x, int z, TerritoryClaimer claimer) {
        ChunkPos chpos = new ChunkPos(x, z);
        if (chpos.x != x || chpos.z != z) return;
        belongingBlocks.put(new BlockPos(x, 64, z), claimer);
    }

    public void markAsNotBelonging(BlockPos pos) {
        ChunkPos chpos = new ChunkPos(pos);
        if (chpos.x != x || chpos.z != z) return;
        belongingBlocks.remove(new BlockPos(pos.getX(), 64, pos.getZ()));
    }

    public void markAsNotBelonging(int x, int z) {
        ChunkPos chpos = new ChunkPos(x, z);
        if (chpos.x != x || chpos.z != z) return;
        belongingBlocks.remove(new BlockPos(x, 64, z));
    }

    public List<BlockPos> getAllChunkBlocks() {
        List<BlockPos> blocks = new ArrayList<>();

        for (int i = getStartX(); i < getEndX()+1; i++)
            for (int j = getStartZ(); j < getEndZ()+1; j++)
                blocks.add(new BlockPos(i, 64, j));

        return blocks;
    }

    @Nullable
    public TerritoryClaimer getOwner(BlockPos pos) {
        BlockPos fixedPos = new BlockPos(pos.getX(), 64, pos.getZ());
        return belongingBlocks.get(fixedPos);
    }

    @Nullable
    public TerritoryClaimer getOwner(int x, int z) {
        BlockPos pos = new BlockPos(x, 64, z);
        return belongingBlocks.get(pos);
    }

    public void abandon() {
        belongingBlocks.clear();
    }

    public void abandon(TerritoryClaimer claimer) {
        belongingBlocks.forEach((b, c) -> {
            if (c == claimer) belongingBlocks.remove(b);
        });
    }
}
