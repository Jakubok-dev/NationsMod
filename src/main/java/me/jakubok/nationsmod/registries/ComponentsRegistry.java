package me.jakubok.nationsmod.registries;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.ReadOnlyChunk;

public class ComponentsRegistry implements LevelComponentInitializer, ChunkComponentInitializer {

    public static final ComponentKey<TerritoryClaimersRegistry> TERRITORY_CLAIMERS_REGISTRY = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "territory_claimers_registry"), TerritoryClaimersRegistry.class);

    public static final ComponentKey<TownsRegistry> TOWNS_REGISTRY = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "towns_registry"), TownsRegistry.class);

    public static final ComponentKey<ChunkClaimRegistry> CHUNK_CLAIM_REGISTRY = ComponentRegistry.getOrCreate(new Identifier(NationsMod.MOD_ID, "chunk_claim_registry"), ChunkClaimRegistry.class);

    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        registry.register(TERRITORY_CLAIMERS_REGISTRY, t -> new TerritoryClaimersRegistry(t));
        registry.register(TOWNS_REGISTRY, t -> new TownsRegistry(t));
    }

    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
        registry.register(CHUNK_CLAIM_REGISTRY, t -> {
            if (t instanceof ReadOnlyChunk)
                return new ChunkClaimRegistry();
            return new ChunkClaimRegistry(t.getPos().x, t.getPos().z);
        });  
    }    
}
