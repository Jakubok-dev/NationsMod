package me.jakubok.nationsmod.administration;

import java.util.List;
import java.util.UUID;

import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.collections.PlayerInfo;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;


public class Town extends AdministratingUnit<TownLawDescription> {

    public Town(String name, String districtName, ChunkPos pos, World world, Province province, BorderGroup borderGroup) {
        super(new TownLawDescription(), name, world.getLevelProperties());
        if (province != null)
            this.setProvincesID(province.getId());

        District mainDistrict = new District(districtName, this, world, borderGroup);

        this.getTheListOfDistrictsIDs().add(mainDistrict.getId());
    }
    public Town(String name, String districtName, ChunkPos pos, World world, BorderGroup group) {
        this(name, districtName, pos, world, null, group);
    }
    public Town(NbtCompound tag, WorldProperties props) {
        super(new TownLawDescription(), props);
        this.readFromNbt(tag);
    }

    public List<PlayerAccount> getTheListOfPlayerAccounts() {
        @SuppressWarnings("unchecked")
        List<PlayerAccount> result = (List<PlayerAccount>)this.law.getARule(TownLawDescription.listOfPlayerAccountsLabel);
        return result;
    }

    public List<UUID> getTheListOfDistrictsIDs() {
        @SuppressWarnings("unchecked")
        List<UUID> result = (List<UUID>)this.law.getARule(TownLawDescription.listOfDistrictsIDsLabel);
        return result;
    }

    public boolean addACitizen(PlayerAccount account) {
        PlayerInfo info = PlayerInfo.fromAccount(account, this.props);

        if (info.getCitizenInfo().addCitizenship(this)) {
            this.getTheListOfPlayerAccounts().add(account);
            return true;
        }
        return false;
    }

    public boolean removeACitizen(PlayerAccount account) {
        PlayerInfo info = PlayerInfo.fromAccount(account, this.props);

        for (int i = 0; i < this.getTheListOfPlayerAccounts().size(); i++) {
            if (this.getTheListOfPlayerAccounts().get(i).equals(account)) {
                this.getTheListOfPlayerAccounts().remove(i);
                return info.getCitizenInfo().removeCitizenship(this);
            }
        }
        return false;
    }

    public List<PlayerInfo> getCitizens() {
        return this.getTheListOfPlayerAccounts().stream()
            .map(el -> PlayerInfo.fromAccount(el, this.props))
            .toList();
    }

    public List<District> getDistricts() {
        return this.getTheListOfDistrictsIDs().stream()
        .map(el -> (District)ComponentsRegistry.LEGAL_ORGANISATIONS_REGISTRY.get(props).get(el))
        .toList();
        
    }

    public UUID getProvincesID() {
        return (UUID)this.law.getARule(TownLawDescription.provincesIDLabel);
    }

    public boolean setProvincesID(UUID id) {
        return this.law.putARule(TownLawDescription.provincesIDLabel, id);
    }

    public Province getProvince() {
        return Province.fromUUID(this.getProvincesID(), props);
    }

    public void setProvince(Province province) {
        this.setProvincesID(province.getId());
        for (int i = 0; i < this.getTheListOfPlayerAccounts().size(); i++) {
            PlayerInfo info = PlayerInfo.fromAccount(this.getTheListOfPlayerAccounts().get(i), this.props);
            if (!info.getCitizenInfo().setNationality(province.getNation()))
                this.removeACitizen(this.getTheListOfPlayerAccounts().get(i));
        }
    }

    public void changeCitizensNationality() {
        for (int i = 0; i < this.getTheListOfPlayerAccounts().size(); i++) {
            PlayerInfo info = PlayerInfo.fromAccount(this.getTheListOfPlayerAccounts().get(i), this.props);
            if (!info.getCitizenInfo().setNationality(this.getProvince().getNation()))
                this.removeACitizen(this.getTheListOfPlayerAccounts().get(i));
        }
    }

    public boolean hasProvince() {
        return getProvince() != null;
    }

    public static Town fromUUID(UUID id, WorldProperties props) {
        return (Town)ComponentsRegistry.LEGAL_ORGANISATIONS_REGISTRY.get(props).get(id);
    }

    public static Town fromUUID(UUID id, World world) {
        return fromUUID(id, world.getLevelProperties());
    }
}
