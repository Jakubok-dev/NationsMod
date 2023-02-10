package me.jakubok.nationsmod.map;

import java.util.HashMap;
import java.util.Map;

import me.jakubok.nationsmod.collections.Colour;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;

public class MapStorage {
    protected Map<BlockPos, Colour> blockLayer = new HashMap<>();
    protected Map<BlockPos, Colour> townLayer = new HashMap<>();
    protected Map<BlockPos, Colour> borderRegistratorLayer = new HashMap<>();

    public void renderBlockLayer(WorldAccess world, int blockx, int blockz) {
        final int blockHeight = world.getTopY(Heightmap.Type.MOTION_BLOCKING, blockx, blockz) - 1;
        Biome biome = world.getBiome(new BlockPos(blockx, blockHeight, blockz));
        Block block = world.getBlockState(new BlockPos(blockx, blockHeight, blockz)).getBlock();

        Colour colour;
        if (block == Blocks.GRASS_BLOCK) {
            colour = new Colour(biome.getGrassColorAt((double)blockx + 0.5d, (double)blockz + 0.5d));
        }
        else if (block == Blocks.WATER) {
            colour = new Colour(biome.getWaterColor());
        } else {
            colour = new Colour(block.getDefaultMapColor().color);
        }
        double shadeFactor = (((double)blockHeight + 64d) / (double)(62 + 64));
        colour.changeTheShade(shadeFactor);
        this.blockLayer.put(new BlockPos(blockx, 64, blockz), colour);
    }

    public void renderTheTownLayer(int bytemask, BlockPos pos) {
        Colour claimersColour = new Colour(bytemask);
        this.townLayer.put(pos, claimersColour);
    }

    public void clearTheTownLayer(BlockPos pos) {
        this.townLayer.remove(pos);
    }

    public void renderTheBorderRegistratorLayer(BlockPos pos) {
        this.borderRegistratorLayer.put(pos, new Colour(255, 255, 255));
    }

    public void clearTheBorderRegistratorLayer(BlockPos pos) {
        this.borderRegistratorLayer.remove(pos);
    }

    public Colour theColourAt(BlockPos pos) {
        Colour colour = this.blockLayer.getOrDefault(pos, new Colour(0));
        Colour townColour = this.townLayer.get(pos);
        if (townColour != null)
            colour = Colour.MIX(colour, 1.0d, townColour, 1.5d);
        Colour borderRegistratorColour = this.borderRegistratorLayer.get(pos);
        if (borderRegistratorColour != null)
            colour = Colour.MIX(colour, 1.0d, borderRegistratorColour, 1.0d);
        return colour;
    }
}
