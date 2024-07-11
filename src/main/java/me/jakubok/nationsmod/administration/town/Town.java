package me.jakubok.nationsmod.administration.town;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.administration.governmentElements.formsOfGovernment.AbsoluteMonarchy;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.entity.human.HumanEntity;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import me.jakubok.nationsmod.registries.PlayerInfoRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;


public class Town extends AdministratingUnit<TownLawDescription> {

    public Town(String name, String districtName, MinecraftServer server, Nation nation) {
        super(new TownLawDescription(), name, server);
        if (nation != null)
            this.setNation(nation, server);

        District mainDistrict = new District(districtName, this, server);

        this.getTheListOfDistrictsIDs().add(mainDistrict.getId());
    }
    public Town(String name, String districtName, MinecraftServer server) {
        this(name, districtName, server, null);
    }
    public Town(NbtCompound tag, MinecraftServer server) {
        super(new TownLawDescription());
        this.readFromNbt(tag, server);
    }
    
    @Override
    public Set<PlayerAccount> getPlayerMembers(MinecraftServer server) {
        return this.getPlayerMembers();
    }
    public Set<PlayerAccount> getPlayerMembers() {
        @SuppressWarnings("unchecked")
        Set<PlayerAccount> result = (Set<PlayerAccount>)this.law.getARule(TownLawDescription.setOfPlayerMembersLabel);
        return result;
    }

    @Override
    public Set<UUID> getAIMembers(MinecraftServer server) {
        return this.getAIMembers();
    }
    public Set<UUID> getAIMembers() {
        @SuppressWarnings("unchecked")
        Set<UUID> result = (Set<UUID>)this.law.getARule(TownLawDescription.setOfAIMembersLabel);
        return result;
    }

    public boolean addAMember(PlayerEntity entity, MinecraftServer server) {
        if (this.getPlayerMembers().contains(new PlayerAccount(entity)))
            return false;
        PlayerInfo info = PlayerInfoRegistry.getRegistry(server).getAPlayer(new PlayerAccount(entity));
        if (info.getCitizenship() != null)
            Town.fromUUID(info.getCitizenship(), server).removeAMember(entity);
        
        info.setCitizenship(this.getId(), server);
        this.getPlayerMembers().add(new PlayerAccount(entity));

        return true;
    }
    public boolean removeAMember(PlayerEntity entity) {
        return this.getPlayerMembers().remove(new PlayerAccount(entity));
    }

    public boolean addAMember(HumanEntity entity, MinecraftServer server) {
        if (this.getAIMembers().contains(entity.getUuid()))
            return false;
        if (entity.getTheCitizenship() != null)
            Town.fromUUID(entity.getTheCitizenship(), server).removeAMember(entity);
        
        entity.setTheCitizenship(this.getId(), server);
        this.getAIMembers().add(entity.getUuid());
        
        return true;
    }
    public boolean removeAMember(HumanEntity entity) {
        return this.getAIMembers().remove(entity.getUuid());
    }

    public List<UUID> getTheListOfDistrictsIDs() {
        @SuppressWarnings("unchecked")
        List<UUID> result = (List<UUID>)this.law.getARule(TownLawDescription.listOfDistrictsIDsLabel);
        return result;
    }

    public List<District> getDistricts(MinecraftServer server) {
        return this.getTheListOfDistrictsIDs().stream()
        .map(el -> (District)LegalOrganisationRegistry.getRegistry(server).get(el))
        .toList();

    }

    public UUID getNationsID() {
        return (UUID)this.law.getARule(TownLawDescription.nationsIDLabel);
    }

    public boolean leaveANation(MinecraftServer server) {
        Nation nation = this.getNation(server);
        if (nation != null)
            return nation.removeATown(this.getId());
        return false;
    }

    public boolean setNation(UUID id, MinecraftServer server) {
        Nation nation = Nation.fromUUID(id, server);
        if (nation == null)
            return false;
        return this.setNation(nation, server);
    }

    public Nation getNation(MinecraftServer server) {
        UUID id = this.getNationsID();
        if (id == null)
            return null;
        return Nation.fromUUID(id, server);
    }

    public boolean setNation(Nation nation, MinecraftServer server) {
        this.leaveANation(server);
        if (!nation.addATown(this))
            return false;
        return this.law.putARule(TownLawDescription.nationsIDLabel, nation.getId());
    }

    public boolean hasNation(MinecraftServer server) {
        return this.getNation(server) != null;
    }

    public Province getProvince(MinecraftServer server) {
        Nation nation = this.getNation(server);
        if (nation == null)
            return null;
        return Province.fromUUID(nation.getTownProvinceRegistry().get(this.getId()), server);
    }

    public boolean setProvince(Province province, MinecraftServer server) {
        return setProvince(province.getId(), server);
    }

    public boolean setProvince(UUID id, MinecraftServer server) {
        Nation nation = this.getNation(server);
        if (nation == null)
            return false;
        return nation.getTownProvinceRegistry().put(this.getId(), id) != null;
    }

    public boolean hasProvince(MinecraftServer server) {
        return this.getProvince(server) != null;
    }

    @Override
    public void readTheFormOfGovernment(NbtCompound nbt, MinecraftServer server) {
        switch (nbt.getString("formOfGovernment")) {
            case "absolute_monarchy":
                this.formOfGovernment = new AbsoluteMonarchy<>(this, server);
                break;
            default:
                throw new CrashException(CrashReport.create(new Throwable(), "Unknown form of government"));
        }
    }

    @Override
    public boolean deregister(MinecraftServer server) {
        this.getDistricts(server).forEach(el -> el.deregister(server));
        return super.deregister(server);
    }

    public static Town fromUUID(UUID id, MinecraftServer server) {
        return (Town)LegalOrganisationRegistry.getRegistry(server).get(id);
    }
}
