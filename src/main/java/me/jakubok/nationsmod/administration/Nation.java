package me.jakubok.nationsmod.administration;

import java.util.List;
import java.util.UUID;

import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

public class Nation extends LegalOrganisation<NationLawDescription> {

    public final WorldProperties props;
    
    public Nation(String name, World world, String provinceName, Town capital) {
        super(new NationLawDescription());
        this.setName(name);
        this.props = world.getLevelProperties();
        this.setCapitalsID(capital.getId());

        Province mainProvince = new Province(provinceName, capital, this, world);
        this.getTheListOfProvincesIDs().add(mainProvince.getId());

        ComponentsRegistry.NATIONS_REGISTRY.get(this.props).registerNation(this);
    }
    public Nation(NbtCompound tag, WorldProperties props) {
        super(new NationLawDescription());
        this.props = props;
        readFromNbt(tag);
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
        return ComponentsRegistry.NATIONS_REGISTRY.get(props).getNation(id);
    }
    public static Nation fromUUID(UUID id, World world) {
        return fromUUID(id, world.getLevelProperties());
    }
}
