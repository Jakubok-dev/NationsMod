package me.jakubok.nationsmod.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.administration.TerritoryClaimer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class ChunkClaimRegistry extends ChunkPos {

    private Map<BlockPos, UUID> claims = new HashMap<>();

    public ChunkClaimRegistry(BlockPos pos) {
        super(pos);
    }
    public ChunkClaimRegistry(int x, int z) {
        super(x, z);
    }

    public Map<BlockPos, UUID> getClaims() {
        return claims;
    }

    public boolean addClaim(int x, int z, TerritoryClaimer claimer) {
        
        BlockPos position = new BlockPos(x, 64, z);

        if (claims.get(position) != null)
            return false;

        claims.put(position, claimer.getId());
        
        return true;
    }
    public boolean addClaim(BlockPos pos, TerritoryClaimer claimer) {
        return addClaim(pos.getX(), pos.getZ(), claimer);
    }

    public boolean removeClaim(int x, int z) {
        BlockPos position = new BlockPos(x, 64, z);

        if (claims.get(position) == null)
            return false;

        claims.remove(position);
        
        return true;
    }
    public boolean removeClaim(BlockPos pos) {
        return removeClaim(pos.getX(), pos.getZ());
    }

    public boolean removeClaims(TerritoryClaimer claimer) {
        boolean result = false;
        for (BlockPos pos : claims.keySet()) {
            if (claims.get(pos).toString() == claimer.toString()) {
                result = true;
                claims.remove(pos);
            }
        }
        return result;
    }

    public boolean changeClaim(int x, int z, TerritoryClaimer claimer) {
        
        BlockPos position = new BlockPos(x, 64, z);

        if (claims.get(position) == null)
            return false;
        if (claims.get(position).toString() == claimer.getId().toString())
            return false;

        claims.put(position, claimer.getId());
        
        return true;
    }
    public boolean changeClaim(BlockPos pos, TerritoryClaimer claimer) {
        return changeClaim(pos.getX(), pos.getZ(), claimer);
    }

    public UUID claimBelonging(int x, int z) {
        BlockPos position = new BlockPos(x, 64, z);
        return claims.get(position);
    }
    public UUID claimBelonging(BlockPos pos) {
        return claimBelonging(pos.getX(), pos.getZ());
    }

    public boolean isBelonging(int x, int z) {
        return claimBelonging(x, z) != null;
    }
    public boolean isBelonging(BlockPos pos) {
        return isBelonging(pos.getX(), pos.getZ());
    }
}
