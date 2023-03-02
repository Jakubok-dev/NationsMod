package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.entity.human.HumanEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {

    public static final EntityType<HumanEntity> HUMAN = Registry.register(
        Registry.ENTITY_TYPE,
        new Identifier(NationsMod.MOD_ID, "human"),
        FabricEntityTypeBuilder.<HumanEntity>create(SpawnGroup.CREATURE, HumanEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );

    static {
        FabricDefaultAttributeRegistry.register(HUMAN, HumanEntity.getHumanAttributes());
    }

    public static void init() {}
}
