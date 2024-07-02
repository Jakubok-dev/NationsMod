package me.jakubok.nationsmod.administration.nation;

import java.util.*;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.governmentElements.formsOfGovernment.AbsoluteMonarchy;
import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

public class Nation extends AdministratingUnit<NationLawDescription> {

    public Nation(String name, Town capital, MinecraftServer server) {
        super(new NationLawDescription(), name, server);
        this.setCapitalsID(capital.getId());
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

    public List<UUID> getProvincesIDs() {
        @SuppressWarnings("unchecked")
        List<UUID> result = (List<UUID>)this.law.getARule(NationLawDescription.listOfProvincesIDsLabel);
        return result;
    }

    public List<Province> getProvinces(MinecraftServer server) {
        return this.getProvincesIDs().stream()
            .map(el -> Province.fromUUID(el, server))
            .toList();
    }

    public Map<UUID, UUID> getTownProvinceRegistry() {
        @SuppressWarnings("unchecked")
        Map<UUID, UUID> result = (Map<UUID, UUID>)this.law.getARule(NationLawDescription.townProvinceRegistryLabel);
        return result;
    }

    public boolean addATown(Town town) {
        List<UUID> towns = this.getTownsIDs();
        if (towns.contains(town.getId()))
            return false;
        return towns.add(town.getId());
    }

    public boolean removeATown(UUID townID) {
        return this.getTownsIDs().remove(townID);
    }

    public List<UUID> getTownsIDs() {
        @SuppressWarnings("unchecked")
        List<UUID> result = (List<UUID>)this.law.getARule(NationLawDescription.listOfTownsIDsLabel);
        return result;
    }

    public List<Town> getTowns(MinecraftServer server) {
        return this.getTownsIDs().stream()
                .map(el -> Town.fromUUID(el, server))
                .toList();
    }

    public Map<UUID, UUID> getProvincesCapitalRegistry() {
        @SuppressWarnings("unchecked")
        Map<UUID, UUID> result = (Map<UUID, UUID>)this.law.getARule(NationLawDescription.provincesCapitalRegistryLabel);
        return result;
    }

    @Override
    public void readTheFormOfGovernment(NbtCompound nbt, MinecraftServer server) {
        switch (nbt.getString("formOfGovernment")) {
            case "absolute_monarchy":
                this.formOfGovernment = new AbsoluteMonarchy<Nation, NationLawDescription>(this, () -> new Act<>(this.description), server);
                break;
            default:
                throw new CrashException(CrashReport.create(new Throwable(), "Unknown form of government"));
        }
    }

    @Override
    public boolean deregister(MinecraftServer server) {
        this.getTowns(server).forEach(el -> el.leaveANation(server));
        this.getProvinces(server).forEach(el -> el.deregister(server));
        return super.deregister(server);
    }

    public static Nation fromUUID(UUID id, MinecraftServer server) {
        return (Nation)LegalOrganisationRegistry.getRegistry(server).get(id);
    }
}
