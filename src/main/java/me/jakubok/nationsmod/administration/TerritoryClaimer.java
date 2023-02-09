package me.jakubok.nationsmod.administration;

import java.util.Random;
import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.collections.Border;
import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.Colour;
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
    private int minX = 2147483647, maxX = -2147483648, minZ = 2147483647, maxZ = -2147483648;
    public Colour mapColour = new Colour(0);

    public TerritoryClaimer(World world) {
        this(world, null);
    }

    public TerritoryClaimer(World world, BorderGroup borderGroup) {
        this.props = world.getLevelProperties();
        Random rng = new Random();
        if (mapColour.getR() <= 0)
            mapColour.setR(rng.nextInt(255));
        if (mapColour.getG() <= 0)
            mapColour.setG(rng.nextInt(255));
        if (mapColour.getB() <= 0)
            mapColour.setB(rng.nextInt(255));

        if (borderGroup == null)
            return;
        
        BorderGroup field = borderGroup.getField();
        if (field == null)
            return;

        for (Border elem : field.toList()) {
            this.claim(elem.position, world);
        }
    }
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
        
        this.minX = Math.min(this.minX, pos.getX());
        this.maxX = Math.max(this.maxX, pos.getX());
        this.minZ = Math.min(this.minZ, pos.getZ());
        this.maxZ = Math.max(this.maxZ, pos.getZ());

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

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        id = tag.getUuid("id");
        claimedBlocksCount = tag.getLong("claimedBlocksCount");
        this.mapColour = new Colour(tag.getCompound("mapColour"));
        this.maxX = tag.getInt("maxX");
        this.maxZ = tag.getInt("maxZ");
        this.minX = tag.getInt("minX");
        this.minZ = tag.getInt("minZ");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putUuid("id", id);
        tag.putLong("claimedBlocksCount", claimedBlocksCount);
        tag.put("mapColour", this.mapColour.writeToNbtAndReturn(new NbtCompound()));
        tag.putInt("maxX", this.maxX);
        tag.putInt("minX", this.minX);
        tag.putInt("maxZ", this.maxZ);
        tag.putInt("minZ", this.minZ);
        return tag;
    }
}