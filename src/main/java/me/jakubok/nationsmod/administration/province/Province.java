package me.jakubok.nationsmod.administration.province;

import java.util.List;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.registries.LegalOrganisationsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class Province extends TerritoryClaimer<ProvinceLawDescription> {

    public Province(String name, Town capital, Nation nation, MinecraftServer server) {
        super(new ProvinceLawDescription(), name, server);
        this.setNationsUUID(nation.getId());
        this.setCapitalsUUID(capital.getId());
        capital.setProvince(this);
    }
    public Province(NbtCompound tag, MinecraftServer server) {
        super(new ProvinceLawDescription(), tag, server);
    }

    public Town getCapital(MinecraftServer server) {
        return Town.fromUUID(getCapitalsUUID(), server);
    }
    public UUID getCapitalsUUID() {
        return (UUID)this.law.getARule(ProvinceLawDescription.capitalsIDLabel);
    }
    public boolean setCapitalsUUID(UUID id) {
        return this.law.putARule(ProvinceLawDescription.capitalsIDLabel, id);
    }
    
    public List<Town> getTowns(MinecraftServer server) {
        return this.getTownsIDs().stream()
        .map(el -> Town.fromUUID(el, server))
        .toList();
    }
    public List<UUID> getTownsIDs() {
        @SuppressWarnings("unchecked")
        List<UUID> result =  (List<UUID>)this.law.getARule(ProvinceLawDescription.townsIDsLabel);
        return result;
    }

    public Nation getNation(MinecraftServer server) {
        return Nation.fromUUID(this.getNationsUUID(), server);
    }
    public UUID getNationsUUID() {
        return (UUID)this.law.getARule(ProvinceLawDescription.nationsIDLabel);
    }
    public boolean setNationsUUID(UUID id) {
        return this.law.putARule(ProvinceLawDescription.nationsIDLabel, id);
    }

    public static Province fromUUID(UUID id, MinecraftServer server) {
        return (Province)LegalOrganisationsRegistry.getRegistry(server).get(id);
    }

    @Override
    public Colour getTheMapColour(MinecraftServer server) {
        return this.getNation(server).getTheMapColour();
    }
    @Override
    public void sendMapBlockInfo(ServerWorld world, BlockPos pos) {
    }
}
