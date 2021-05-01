package me.Jakubok.nations.administration;

import me.Jakubok.nations.terrain.ModChunkPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class TownDistrict extends TerritoryClaimer {

    public Town town;
    public String name;
    protected BlockPos center;

    public TownDistrict(Town town, String name, World world, BlockPos center) {
        super(world);
        this.town = town;
        this.name = name;
        this.center = center;
    }

    public BlockPos getCenter() {
        return center;
    }

    public boolean setCenter(BlockPos center) {
        if (!isBlockBelonging(center)) return false;
        this.center = center;
        return true;
    }

    @Override
    protected boolean expand(ModChunkPos chunk) {
        createChunk(chunk);
        chunk = chunks.get(chunk);
        List<BlockPos> blocks = chunk.getAllChunkBlocks();
        for (BlockPos element : blocks) {
            if (chunk.getOwner(element) != null)
                chunk.markAsNotBelonging(element);
        }
        return true;
    }

    @Override
    protected boolean expand(BlockPos pos) {
        return false;
    }

    @Override
    protected void abandon() {
        town.removeDistrict(this);
        town = null;
        center = null;

        List<ModChunkPos> temp = chunks.treeToList();
        for (ModChunkPos element : temp)
            element.abandon(this);
        chunks.clear();
    }
}
