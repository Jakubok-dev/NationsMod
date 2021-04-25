package me.Jakubok.nations.administration;

import me.Jakubok.nations.collections.ChunkBinaryTree;
import me.Jakubok.nations.terrain.ModChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Province extends TeritorryClaimer {

    public String name;
    public Country belonging;
    public ChunkBinaryTree chunks = new ChunkBinaryTree();
    public List<Town> towns = new ArrayList<>();
    public Province(String name, World world, Country belonging) {
        super(world);
        this.name = name;
        this.belonging = belonging;
    }
}
