package me.jakubok.nationsmod.administration;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

public abstract class TerritoryClaimer implements ComponentV3 {

    private UUID id = UUID.randomUUID();
    private long claimedBlocksCount = 0;
    public final WorldProperties props;

    public TerritoryClaimer(WorldProperties props) {
        this.props = props;
    }

    public UUID getId() {
        return id;
    }

    public long getClaimedBlocksCount() {
        return claimedBlocksCount;
    }

    public int claim(BlockPos pos, World world) {
        
        ChunkClaimRegistry chunkClaimRegistry = ComponentsRegistry.CHUNK_BINARY_TREE.get(world).getOrCreate(pos);

        if(chunkClaimRegistry.isBelonging(pos))
            return 0;

        chunkClaimRegistry.addClaim(pos, this);
        claimedBlocksCount++;
        
        return 1;
    }
    public int claim(ChunkPos pos, World world) {
        int result = 0;
        for (int i = pos.x << 4; i < (pos.x << 4) + 16; i++) {
            for (int j = pos.z << 4; j < (pos.z << 4) + 16; j++)
                result += claim(new BlockPos(i, 64, j), world);
        }
        return result;
    }

    public int unclaim(BlockPos pos, World world) {
        ChunkClaimRegistry chunkClaimRegistry = ComponentsRegistry.CHUNK_BINARY_TREE.get(world).getOrCreate(pos);

        if (!chunkClaimRegistry.isBelonging(pos))
            return 0;
        if (chunkClaimRegistry.claimBelonging(pos).toString() != this.id.toString())
            return 0;

        chunkClaimRegistry.removeClaim(pos);
        claimedBlocksCount--;
        
        return 1;
    }
    public int unclaim(ChunkPos pos, World world) {
        int result = 0;
        for (int i = pos.x << 4; i < (pos.x << 4) + 16; i++) {
            for (int j = pos.z << 4; j < (pos.z << 4) + 16; j++)
                result += unclaim(new BlockPos(i, 64, j), world);
        }
        return result;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        id = tag.getUuid("id");
        claimedBlocksCount = tag.getLong("claimedBlocksCount");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putUuid("id", id);
        tag.putLong("claimedBlocksCount", claimedBlocksCount);
    }
}