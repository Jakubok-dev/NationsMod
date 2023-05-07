package me.jakubok.nationsmod.administration.district;

import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collections.Border;
import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.LegalOrganisationsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class District extends TerritoryClaimer<DistrictLawDescription> {

    public District(String name, Town town, ServerWorld world, BorderGroup group, MinecraftServer server) {
        super(new DistrictLawDescription(), name, server);
        this.setTownsID(town.getId());

        if (group == null)
            return;
        
        BorderGroup field = group.getField();
        if (field == null)
            return;

        for (Border elem : field.toList()) {
            this.claim(elem.position, world);
        }
    }
    public District(NbtCompound tag, MinecraftServer server) {
        super(new DistrictLawDescription(), tag, server);
    }

    public Town getTown(MinecraftServer server) {
        return (Town)LegalOrganisationsRegistry.getRegistry(server).get(this.getTownsID());
    }

    public UUID getTownsID() {
        return (UUID)this.law.getARule(DistrictLawDescription.townIDLabel);
    }
    public boolean setTownsID(UUID id) {
        return this.law.putARule(DistrictLawDescription.townIDLabel, id);
    }

    public static District fromUUID(UUID id, MinecraftServer server) {
        return (District)LegalOrganisationsRegistry.getRegistry(server).get(id);
    }
    @Override
    public Colour getTheMapColour(MinecraftServer server) {
        return this.getTown(server).getTheMapColour();
    }
    @Override
    public void sendMapBlockInfo(ServerWorld world, BlockPos pos) {
        PacketByteBuf buffer = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();

        nbt.putString("townsName", this.getTown(world.getServer()).getName());
        nbt.putString("districtsName", this.getName());
        nbt.putUuid("townsUUID", this.getTown(world.getServer()).getId());
        nbt.putUuid("districtsUUID", this.getId());

        buffer.writeBlockPos(pos);
        buffer.writeNbt(nbt);
        for (ServerPlayerEntity playerEntity : PlayerLookup.tracking(world, pos)) {
            ServerPlayNetworking.send(playerEntity, Packets.PULL_MAP_BLOCK_INFO, buffer);
        }
    }
}
