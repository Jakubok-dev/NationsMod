package me.jakubok.nationsmod.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.TerritoryClaimer;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    public boolean addClaim(int x, int z, TerritoryClaimer<?> claimer, ServerWorld world) {
        
        BlockPos position = new BlockPos(x, 64, z);

        if (claims.get(position) != null)
            return false;

        claims.put(position, claimer.getId());
        
        this.addToPlayersMaps(world, position, claimer);

        return true;
    }
    public boolean addClaim(BlockPos pos, TerritoryClaimer<?> claimer, ServerWorld world) {
        return addClaim(pos.getX(), pos.getZ(), claimer, world);
    }

    public boolean removeClaim(int x, int z, ServerWorld world) {
        BlockPos position = new BlockPos(x, 64, z);

        if (claims.get(position) == null)
            return false;

        claims.remove(position);

        this.removeFromPlayersMaps(world, position);
        
        return true;
    }
    public boolean removeClaim(BlockPos pos, ServerWorld world) {
        return removeClaim(pos.getX(), pos.getZ(), world);
    }

    public boolean removeClaims(TerritoryClaimer<?> claimer, ServerWorld world) {
        boolean result = false;
        for (BlockPos pos : claims.keySet()) {
            if (claims.get(pos).equals(claimer.getId())) {
                result = true;
                claims.remove(pos);
                this.removeFromPlayersMaps(world, pos);
            }
        }
        return result;
    }

    public boolean changeClaim(int x, int z, TerritoryClaimer<?> claimer, ServerWorld world) {
        
        BlockPos position = new BlockPos(x, 64, z);

        if (claims.get(position) == null)
            return false;
        if (claims.get(position).toString() == claimer.getId().toString())
            return false;

        this.removeFromPlayersMaps(world, position);
        claims.put(position, claimer.getId());
        this.addToPlayersMaps(world, position, claimer);
        
        return true;
    }
    public boolean changeClaim(BlockPos pos, TerritoryClaimer<?> claimer, ServerWorld world) {
        return changeClaim(pos.getX(), pos.getZ(), claimer, world);
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

    public void addToPlayersMaps(ServerWorld world, BlockPos pos, TerritoryClaimer<?> claimer) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeBlockPos(pos);
        buffer.writeInt(claimer.getTheMapColour().getBitmask());
        for (ServerPlayerEntity playerEntity : PlayerLookup.tracking(world, pos)) {
            ServerPlayNetworking.send(playerEntity, Packets.RENDER_CLAIMANTS_COLOUR, buffer);
        }
    }
    public void removeFromPlayersMaps(ServerWorld world, BlockPos pos) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeBlockPos(pos);
        for (ServerPlayerEntity playerEntity : PlayerLookup.tracking(world, pos)) {
            ServerPlayNetworking.send(playerEntity, Packets.CLEAR_CLAIMANT_ON_THE_MAP, buffer);
        }
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
        writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
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

        return tag;
    }

    public static UUID GET_CLAIMANT(int x, int z, World world) {
        return GET_CLAIMANT(new BlockPos(x, 64, z), world);
    }
    public static UUID GET_CLAIMANT(BlockPos pos, World world) {
        ChunkClaimRegistry registry = ComponentsRegistry.CHUNK_BINARY_TREE.get(world).get(pos);
        if (registry == null)
            return null;
        return registry.claimBelonging(pos);
    }
}
