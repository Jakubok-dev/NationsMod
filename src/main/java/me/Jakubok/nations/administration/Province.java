package me.Jakubok.nations.administration;

import me.Jakubok.nations.terrain.ModChunkPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Province extends TerritoryClaimer {

    public String name;
    public Country belonging;
    public List<Town> towns = new ArrayList<>();
    public Province(String name, World world, Country belonging) {
        super(world);
        this.name = name;
        this.belonging = belonging;
    }

    @Override
    protected boolean expand(ModChunkPos chunk) {
        return false;
    }

    @Override
    protected boolean expand(BlockPos pos) {
        return false;
    }

    @Override
    protected void abandon() {
        for (int i = 0; i < towns.size(); i++) {
            towns.get(i).province = null;
        }
        towns.clear();
    }
}
