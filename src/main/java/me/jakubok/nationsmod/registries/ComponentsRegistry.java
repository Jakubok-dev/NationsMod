package me.jakubok.nationsmod.registries;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.collections.ChunkBinaryTree;
import net.minecraft.util.Identifier;

public class ComponentsRegistry implements LevelComponentInitializer {

    public static final ComponentKey<TerritoryClaimersRegistry> TERRITORY_CLAIMERS_REGISTRY = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "territory_claimers_registry"), TerritoryClaimersRegistry.class);

    public static final ComponentKey<TownsRegistry> TOWNS_REGISTRY = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "towns_registry"), TownsRegistry.class);

    public static final ComponentKey<ChunkBinaryTree> CHUNK_BINARY_TREE = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "chunk_binary_tree"), ChunkBinaryTree.class);

    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        registry.register(TERRITORY_CLAIMERS_REGISTRY, t -> new TerritoryClaimersRegistry(t));
        registry.register(TOWNS_REGISTRY, t -> new TownsRegistry(t));
        registry.register(CHUNK_BINARY_TREE, t -> new ChunkBinaryTree());
    }
}
