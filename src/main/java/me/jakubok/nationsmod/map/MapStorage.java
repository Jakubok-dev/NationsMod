package me.jakubok.nationsmod.map;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.collections.Pair;
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
    protected Map<BlockPos, MapBlockInfo> claimersInfoLayer = new HashMap<>();

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

    public void addATownsName(String name, UUID id, BlockPos pos) {
        if (this.claimersInfoLayer.get(pos) == null) {
            this.claimersInfoLayer.put(pos, new MapBlockInfo(null, new Pair<String, UUID>(name, id)));
            return;
        }
        MapBlockInfo info = this.claimersInfoLayer.get(pos);
        info.town = new Pair<String, UUID>(name, id);
    }
    public void addADistrictsName(String name, UUID id, BlockPos pos) {
        if (this.claimersInfoLayer.get(pos) == null) {
            this.claimersInfoLayer.put(pos, new MapBlockInfo(new Pair<String, UUID>(name, id), null));
            return;
        }
        MapBlockInfo info = this.claimersInfoLayer.get(pos);
        info.district = new Pair<String, UUID>(name, id);
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

    public void clearATownsName(BlockPos pos) {
        if (this.claimersInfoLayer.get(pos) == null)
            return;
        MapBlockInfo info = this.claimersInfoLayer.get(pos);
        info.town = null;
    }
    public void clearADistrictsName(BlockPos pos) {
        if (this.claimersInfoLayer.get(pos) == null)
            return;
        MapBlockInfo info = this.claimersInfoLayer.get(pos);
        info.district = null;
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

    public String claimersAtAsString(BlockPos pos) {
        String result = "";

        MapBlockInfo info = this.claimersInfoLayer.get(pos);

        if (info == null)
            return null;

        if (info.district != null)
            result += info.district.key;
        if (info.town != null)
            result += " | " + info.town.key;

        if (result == "")
            return null;
        return result;
    }

    public UUID districtsUUIDAt(BlockPos pos) {
        MapBlockInfo info = this.claimersInfoLayer.get(pos);
        if (info == null)
            return null;
        if (info.district == null)
            return null;
        return info.district.value;
    }
    public UUID townsUUIDAt(BlockPos pos) {
        MapBlockInfo info = this.claimersInfoLayer.get(pos);
        if (info == null)
            return null;
        if (info.town == null)
            return null;
        return info.town.value;
    }
}
