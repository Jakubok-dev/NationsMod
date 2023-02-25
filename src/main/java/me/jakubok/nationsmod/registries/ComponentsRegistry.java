package me.jakubok.nationsmod.registries;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.collections.ChunkBinaryTree;
import net.minecraft.util.Identifier;

public class ComponentsRegistry implements LevelComponentInitializer, EntityComponentInitializer, WorldComponentInitializer {

    public static final ComponentKey<LegalOrganisationsRegistry> LEGAL_ORGANISATIONS_REGISTRY = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "legal_organisations_registry"), LegalOrganisationsRegistry.class);

    public static final ComponentKey<ChunkBinaryTree> CHUNK_BINARY_TREE = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "chunk_binary_tree"), ChunkBinaryTree.class);

    public static final ComponentKey<PlayerInfoRegistry> PLAYER_INFO = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "player_info"), PlayerInfoRegistry.class);

    public static final ComponentKey<BorderSlots> BORDER_SLOTS = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "border_slots"), BorderSlots.class);

    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        registry.register(LEGAL_ORGANISATIONS_REGISTRY, t -> new LegalOrganisationsRegistry(t));
        registry.register(PLAYER_INFO, t -> new PlayerInfoRegistry(t));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(BORDER_SLOTS, p -> new BorderSlots(), RespawnCopyStrategy.ALWAYS_COPY);  
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(CHUNK_BINARY_TREE, t -> new ChunkBinaryTree());
    }
}
