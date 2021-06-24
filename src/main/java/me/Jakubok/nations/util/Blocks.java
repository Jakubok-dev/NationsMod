package me.Jakubok.nations.util;

import me.Jakubok.nations.Nations;
import me.Jakubok.nations.block.NationPillarBase;
import me.Jakubok.nations.block.NationPillarEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks {

    public static final Block NATION_PILLAR = new NationPillarBase();

    // Block Entities
    public static BlockEntityType<NationPillarEntity> NATION_PILLAR_ENTITY;

    public static void init() {
        Registry.register(Registry.BLOCK, new Identifier(Nations.MOD_ID, "nation_pillar"), NATION_PILLAR);
        NATION_PILLAR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Nations.MOD_ID, "nation_pillar_entity"), FabricBlockEntityTypeBuilder.create(NationPillarEntity::new, NATION_PILLAR).build(null));
    }
}
