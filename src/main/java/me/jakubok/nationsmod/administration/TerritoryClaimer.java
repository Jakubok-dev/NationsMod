package me.jakubok.nationsmod.administration;

import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

public abstract class TerritoryClaimer<D extends TerritoryClaimerLawDescription> extends LegalOrganisation<D> {

    public TerritoryClaimer(D description,String name, World world) {
        super(description, name, world.getLevelProperties());
    }
    public TerritoryClaimer(D description, WorldProperties props, NbtCompound nbt) {
        super(description, props);
        this.readFromNbt(nbt);
    }

    public abstract Colour getTheMapColour();
    public abstract void sendMapBlockInfo(ServerWorld world, BlockPos pos);

    public long getClaimedBlocksCount() {
        return (Long)this.law.getARule(TerritoryClaimerLawDescription.claimedBlocksCountLabel);
    }
    public boolean setClaimedBLocksCount(Long value) {
        return this.law.putARule(TerritoryClaimerLawDescription.claimedBlocksCountLabel, value);
    }

    public int claim(BlockPos pos, World world) {
        
        ChunkClaimRegistry chunkClaimRegistry = ComponentsRegistry.CHUNK_BINARY_TREE.get(world).getOrCreate(pos);

        if(chunkClaimRegistry.isBelonging(pos))
            return 0;

        chunkClaimRegistry.addClaim(pos, this, (ServerWorld)world);
        this.setClaimedBLocksCount(this.getClaimedBlocksCount()+1);
        
        this.setMinX(Math.min(this.getMinX(), pos.getX()));
        this.setMaxX(Math.min(this.getMaxX(), pos.getX()));
        this.setMinZ(Math.min(this.getMinZ(), pos.getX()));
        this.setMaxZ(Math.min(this.getMaxZ(), pos.getX()));

        this.sendMapBlockInfo((ServerWorld)world, pos);

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
        if (chunkClaimRegistry.claimBelonging(pos).toString() != this.getId().toString())
            return 0;

        chunkClaimRegistry.removeClaim(pos, (ServerWorld)world);
        this.setClaimedBLocksCount(this.getClaimedBlocksCount()-1);
        
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

    public int getMaxX() {
        return (Integer)this.law.getARule(TerritoryClaimerLawDescription.maxXLabel);
    }
    public boolean setMaxX(int value) {
        return this.law.putARule(TerritoryClaimerLawDescription.maxXLabel, value);
    }

    public int getMaxZ() {
        return (Integer)this.law.getARule(TerritoryClaimerLawDescription.maxZLabel);
    }
    public boolean setMaxZ(int value) {
        return this.law.putARule(TerritoryClaimerLawDescription.maxZLabel, value);
    }

    public int getMinX() {
        return (Integer)this.law.getARule(TerritoryClaimerLawDescription.minXLabel);
    }
    public boolean setMinX(int value) {
        return this.law.putARule(TerritoryClaimerLawDescription.minXLabel, value);
    }

    public int getMinZ() {
        return (Integer)this.law.getARule(TerritoryClaimerLawDescription.minZLabel);
    }
    public boolean setMinZ(int value) {
        return this.law.putARule(TerritoryClaimerLawDescription.minZLabel, value);
    }
}