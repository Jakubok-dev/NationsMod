package me.jakubok.nationsmod.administration;

import java.util.UUID;

import me.jakubok.nationsmod.collections.BorderGroup;
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
import net.minecraft.world.WorldProperties;

public class District extends TerritoryClaimer {

    private String name;
    private UUID townId;

    public District(String name, Town town, World world, BorderGroup group) {
        super(world, group);
        this.name = name;
        this.townId = town.getId();
        ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(props).registerClaimer(this);
        this.sendMapBlockInfo((ServerWorld)world);
    }
    public District(NbtCompound tag, WorldProperties props) {
        super(props);
        readFromNbt(tag);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Town getTown() {
        return ComponentsRegistry.TOWNS_REGISTRY.get(props).getTown(townId);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        super.readFromNbt(tag);
        name = tag.getString("name");
        townId = tag.getUuid("town_id");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        super.writeToNbt(tag);
        tag.putString("name", name);
        tag.putUuid("town_id", townId);
        tag.putBoolean("district", true);
        tag.putBoolean("province", false);
    }

    public static District fromUUID(UUID id, World world) {
        return (District)ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(world.getLevelProperties()).getClaimer(id);
    }
    @Override
    protected void sendMapBlockInfo(ServerWorld world) {
        while (!this.sendMapBlockInfoQ.isEmpty()) {
            PacketByteBuf buffer = PacketByteBufs.create();
            NbtCompound nbt = new NbtCompound();

            BlockPos pos = this.sendMapBlockInfoQ.poll();
            nbt.putString("townsName", this.getTown().getName());
            nbt.putString("districtsName", this.getName());
            nbt.putUuid("townsUUID", this.getTown().getId());
            nbt.putUuid("districtsUUID", this.getId());

            buffer.writeBlockPos(pos);
            buffer.writeNbt(nbt);
            for (ServerPlayerEntity playerEntity : PlayerLookup.tracking(world, pos)) {
                ServerPlayNetworking.send(playerEntity, Packets.PULL_MAP_BLOCK_INFO, buffer);
            }
        }
    }
}
