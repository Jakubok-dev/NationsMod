package me.jakubok.nationsmod.administration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

public class Nation extends AdministratingUnit<NationLawDescription> implements Joinable {

    public Nation(String name, World world, String provinceName, Town capital) {
        super(new NationLawDescription(), name, world.getLevelProperties());
        this.setCapitalsID(capital.getId());

        Province mainProvince = new Province(provinceName, capital, this, world);
        this.getTheListOfProvincesIDs().add(mainProvince.getId());

    }
    public Nation(NbtCompound tag, WorldProperties props) {
        super(new NationLawDescription(), props);
        readFromNbt(tag);
    }

    @Override
    public Set<PlayerAccount> getPlayerMembers() {
        Set<PlayerAccount> result = new HashSet<>();
        for (Province province : this.getProvinces()) {
            for (Town town : province.getTowns()) {
                result.addAll(town.getPlayerMembers());
            }
        }
        return result;
    }
    @Override
    public Set<UUID> getAIMembers() {
        Set<UUID> result = new HashSet<>();
        for (Province province : this.getProvinces()) {
            for (Town town : province.getTowns()) {
                result.addAll(town.getAIMembers());
            }
        }
        return result;
    }

    public UUID getCapitalsID() {
        return (UUID)this.law.getARule(NationLawDescription.capitalsIDLabel);
    }
    public boolean setCapitalsID(UUID id) {
        return this.law.putARule(NationLawDescription.capitalsIDLabel, id);
    }

    public Town getCapital() {
        return Town.fromUUID(getCapitalsID(), props);
    }

    public List<UUID> getTheListOfProvincesIDs() {
        @SuppressWarnings("unchecked")
        List<UUID> result = (List<UUID>)this.law.getARule(NationLawDescription.listOfProvincesIDsLabel);
        return result;
    }

    public List<Province> getProvinces() {
        return this.getTheListOfProvincesIDs().stream()
            .map(el -> Province.fromUUID(el, props))
            .toList();
    }
    
    public static Nation fromUUID(UUID id, WorldProperties props) {
        return (Nation)ComponentsRegistry.LEGAL_ORGANISATIONS_REGISTRY.get(props).get(id);
    }
    public static Nation fromUUID(UUID id, World world) {
        return fromUUID(id, world.getLevelProperties());
    }
}
