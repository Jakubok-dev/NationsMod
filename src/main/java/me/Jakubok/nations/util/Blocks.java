package me.Jakubok.nations.util;

import me.Jakubok.nations.Nations;
import me.Jakubok.nations.block.NationPhillarBase;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks {

    public static final Block NATION_PHILLAR = new NationPhillarBase();

    public static void init() {
        Registry.register(Registry.BLOCK, new Identifier(Nations.MOD_ID, "nation_phillar"), NATION_PHILLAR);
    }
}
