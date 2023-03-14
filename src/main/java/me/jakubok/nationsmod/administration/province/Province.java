package me.jakubok.nationsmod.administration.province;

import java.util.List;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

public class Province extends TerritoryClaimer<ProvinceLawDescription> {

    public Province(String name, Town capital, Nation nation, World world) {
        super(new ProvinceLawDescription(), name, world);
        this.setNationsUUID(nation.getId());
        this.setCapitalsUUID(capital.getId());
        capital.setProvince(this);
    }
    public Province(NbtCompound tag, WorldProperties props) {
        super(new ProvinceLawDescription(), props, tag);
    }

    public Town getCapital() {
        return Town.fromUUID(getCapitalsUUID(), props);
    }
    public UUID getCapitalsUUID() {
        return (UUID)this.law.getARule(ProvinceLawDescription.capitalsIDLabel);
    }
    public boolean setCapitalsUUID(UUID id) {
        return this.law.putARule(ProvinceLawDescription.capitalsIDLabel, id);
    }
    
    public List<Town> getTowns() {
        return this.getTownsIDs().stream()
        .map(el -> Town.fromUUID(el, props))
        .toList();
    }
    public List<UUID> getTownsIDs() {
        @SuppressWarnings("unchecked")
        List<UUID> result =  (List<UUID>)this.law.getARule(ProvinceLawDescription.townsIDsLabel);
        return result;
    }

    public Nation getNation() {
        return Nation.fromUUID(this.getNationsUUID(), this.props);
    }
    public UUID getNationsUUID() {
        return (UUID)this.law.getARule(ProvinceLawDescription.nationsIDLabel);
    }
    public boolean setNationsUUID(UUID id) {
        return this.law.putARule(ProvinceLawDescription.nationsIDLabel, id);
    }

    public static Province fromUUID(UUID id, WorldProperties props) {
        return (Province)ComponentsRegistry.LEGAL_ORGANISATIONS_REGISTRY.get(props).get(id);
    }
    public static Province fromUUID(UUID id, World world) {
        return fromUUID(id, world.getLevelProperties());
    }
    @Override
    public Colour getTheMapColour() {
        return this.getNation().getTheMapColour();
    }
    @Override
    public void sendMapBlockInfo(ServerWorld world, BlockPos pos) {
    }
}
