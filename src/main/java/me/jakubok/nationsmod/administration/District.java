package me.jakubok.nationsmod.administration;

import java.util.UUID;

import me.jakubok.nationsmod.collections.Border;
import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.Colour;
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

public class District extends TerritoryClaimer<DistrictLawDescription> {

    public District(String name, Town town, World world, BorderGroup group) {
        super(new DistrictLawDescription(), name, world);
        this.setTownsID(town.getId());
        ComponentsRegistry.LEGAL_ORGANISATIONS_REGISTRY.get(props).register(this);

        if (group == null)
            return;
        
        BorderGroup field = group.getField();
        if (field == null)
            return;

        for (Border elem : field.toList()) {
            this.claim(elem.position, world);
        }
    }
    public District(NbtCompound tag, WorldProperties props) {
        super(new DistrictLawDescription(), props, tag);
    }

    public Town getTown() {
        return (Town)ComponentsRegistry.LEGAL_ORGANISATIONS_REGISTRY.get(props).get(this.getTownsID());
    }

    public UUID getTownsID() {
        return (UUID)this.law.getARule(DistrictLawDescription.townIDLabel);
    }
    public boolean setTownsID(UUID id) {
        return this.law.putARule(DistrictLawDescription.townIDLabel, id);
    }

    public static District fromUUID(UUID id, World world) {
        return (District)ComponentsRegistry.LEGAL_ORGANISATIONS_REGISTRY.get(world.getLevelProperties()).get(id);
    }
    @Override
    public Colour getTheMapColour() {
        return this.getTown().getTheMapColour();
    }
    @Override
    public void sendMapBlockInfo(ServerWorld world, BlockPos pos) {
        PacketByteBuf buffer = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();

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
