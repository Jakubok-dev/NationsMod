package me.Jakubok.nations.administration;

import me.Jakubok.nations.terrain.ModChunkPos;
import net.minecraft.nbt.CompoundTag;
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

    public TownDistrict(CompoundTag tag, Town town, World world) {
        super(tag, world);
        this.town = town;
        name = tag.getString("name");
        int[] arr = tag.getIntArray("center");
        center = new BlockPos(arr[0], arr[1], arr[2]);
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
            if (chunk.getOwner(element) == null)
                chunk.markAsBelonging(element, this);
        }
        return true;
    }

    @Override
    protected boolean expand(BlockPos pos) {
        return false;
    }

    @Override
    public void abandon() {
        town.removeDistrict(this);
        town = null;
        center = null;

        List<ModChunkPos> temp = chunks.treeToList();
        for (ModChunkPos element : temp)
            element.abandon(this);
        chunks.clear();
    }

    @Override
    public CompoundTag saveToTag(CompoundTag tag) {
        super.saveToTag(tag);
        tag.putString("name", name);
        tag.putIntArray("center", new int[] {center.getX(), center.getY(), center.getZ()});

        return tag;
    }
}
