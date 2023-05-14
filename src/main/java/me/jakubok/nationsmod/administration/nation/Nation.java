package me.jakubok.nationsmod.administration.nation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.governmentElements.formsOfGovernment.AbsoluteMonarchy;
import me.jakubok.nationsmod.administration.law.Directive;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

public class Nation extends AdministratingUnit<NationLawDescription> {

    public Nation(String name, String provinceName, Town capital, MinecraftServer server) {
        super(new NationLawDescription(), name, server);
        this.setCapitalsID(capital.getId());

        Province mainProvince = new Province(provinceName, capital, this, server);
        this.getTheListOfProvincesIDs().add(mainProvince.getId());

    }
    public Nation(NbtCompound tag, MinecraftServer server) {
        super(new NationLawDescription());
        readFromNbt(tag, server);
    }

    @Override
    public Set<PlayerAccount> getPlayerMembers(MinecraftServer server) {
        Set<PlayerAccount> result = new HashSet<>();

        for (Province province : this.getProvinces(server)) {
            if (province == null)
                break;
            for (Town town : province.getTowns(server)) {
                result.addAll(town.getPlayerMembers());
            }
        }
        return result;
    }
    @Override
    public Set<UUID> getAIMembers(MinecraftServer server) {
        Set<UUID> result = new HashSet<>();
        for (Province province : this.getProvinces(server)) {
            if (province == null)
                break;
            for (Town town : province.getTowns(server)) {
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

    public Town getCapital(MinecraftServer server) {
        return Town.fromUUID(getCapitalsID(), server);
    }

    public List<UUID> getTheListOfProvincesIDs() {
        @SuppressWarnings("unchecked")
        List<UUID> result = (List<UUID>)this.law.getARule(NationLawDescription.listOfProvincesIDsLabel);
        return result;
    }

    public List<Province> getProvinces(MinecraftServer server) {
        return this.getTheListOfProvincesIDs().stream()
            .map(el -> Province.fromUUID(el, server))
            .toList();
    }

    @Override
    public void readTheFormOfGovernment(NbtCompound nbt, MinecraftServer server) {
        switch (nbt.getString("formOfGovernment")) {
            case "absolute_monarchy":
                this.formOfGovernment = new AbsoluteMonarchy<Nation, NationLawDescription>(this, () -> new Directive<>(this.description), server);
                break;
            default:
                throw new CrashException(CrashReport.create(new Throwable(), "Unknown form of government"));
        }
    }
    
    public static Nation fromUUID(UUID id, MinecraftServer server) {
        return (Nation)LegalOrganisationRegistry.getRegistry(server).get(id);
    }
}
