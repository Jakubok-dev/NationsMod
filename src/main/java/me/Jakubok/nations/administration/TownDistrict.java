package me.Jakubok.nations.administration;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TownDistrict extends TeritorryClaimer {

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
    protected boolean expand() {
        return false;
    }

    @Override
    protected void abandon() {

    }
}
