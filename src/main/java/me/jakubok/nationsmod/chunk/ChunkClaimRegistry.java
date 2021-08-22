package me.jakubok.nationsmod.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.TerritoryClaimer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ChunkClaimRegistry implements ComponentV3 {

    private Map<BlockPos, UUID> claims = new HashMap<>();
    private int x, z;

    public ChunkClaimRegistry(BlockPos pos) {
        this.x = pos.getX();
        this.z = pos.getZ();
    }
    public ChunkClaimRegistry(int x, int z) {
        this.x = x;
        this.z = z;
    }
    public ChunkClaimRegistry() {

    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
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
    @Override
    public void readFromNbt(NbtCompound tag) {
        claims.clear();
        this.x = tag.getInt("x");
        this.z = tag.getInt("z");

        for (int i = 1; i <= tag.getInt("size"); i++) {
            NbtCompound claimCompound = (NbtCompound)tag.get("claim" + i);
            claims.put(
                new BlockPos(claimCompound.getInt("x"), 64, claimCompound.getInt("z")),
                claimCompound.getUuid("claimer")
            );
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("x", this.x);
        tag.putInt("z", this.z);

        AtomicInteger size = new AtomicInteger(0);
        claims.forEach((pos, claimerid) -> {
            NbtCompound claim = new NbtCompound();
            claim.putInt("x", pos.getX());
            claim.putInt("z", pos.getZ());
            claim.putUuid("claimer", claimerid);
            tag.put("claim" + size.incrementAndGet(), claim);
        });
        tag.putInt("size", size.get());
    }
}
