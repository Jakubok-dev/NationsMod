package me.jakubok.nationsmod;

import java.util.HashMap;
import java.util.Map;

import me.jakubok.nationsmod.collections.ClientBorderDrawer;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.registries.KeyBindingRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
public class NationsClient implements ClientModInitializer {

    public static ClientBorderDrawer drawer = new ClientBorderDrawer();
    public static Map<BlockPos, Colour> map = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ClientNetworking.register();
        KeyBindingRegistry.init();
    }

    public static void renderBlock(WorldAccess world, int blockx, int blockz) {
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
        NationsClient.map.put(new BlockPos(blockx, 64, blockz), colour);
    }
}
