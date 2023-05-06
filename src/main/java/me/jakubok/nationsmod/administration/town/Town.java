package me.jakubok.nationsmod.administration.town;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.administration.governmentElements.formsOfGovernment.AbsoluteMonarchy;
import me.jakubok.nationsmod.administration.law.Directive;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.collections.PlayerInfo;
import me.jakubok.nationsmod.entity.human.HumanData;
import me.jakubok.nationsmod.entity.human.HumanEntity;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
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
    
    @Override
    public Set<PlayerAccount> getPlayerMembers() {
        @SuppressWarnings("unchecked")
        Set<PlayerAccount> result = (Set<PlayerAccount>)this.law.getARule(TownLawDescription.setOfPlayerMembersLabel);
        return result;
    }
    @Override
    public Set<UUID> getAIMembers() {
        @SuppressWarnings("unchecked")
        Set<UUID> result = (Set<UUID>)this.law.getARule(TownLawDescription.setOfAIMembersLabel);
        return result;
    }

    public boolean addAMember(PlayerEntity entity) {
        if (this.getPlayerMembers().contains(new PlayerAccount(entity)))
            return false;
        PlayerInfo info = ComponentsRegistry.PLAYER_INFO.get(this.props).getAPlayer(new PlayerAccount(entity));
        if (info.getCitizenship() != null)
            Town.fromUUID(info.getCitizenship(), this.props).removeAMember(entity);
        
        info.setCitizenship(this.getId());
        this.getPlayerMembers().add(new PlayerAccount(entity));

        return true;
    }
    public boolean removeAMember(PlayerEntity entity) {
        return this.getPlayerMembers().remove(new PlayerAccount(entity));
    }

    public boolean addAMember(HumanEntity entity) {
        if (this.getAIMembers().contains(entity.getUuid()))
            return false;
        HumanData data = entity.getHumanData();
        if (data.getCitizenship() != null)
            Town.fromUUID(data.getCitizenship(), this.props).removeAMember(entity);
        
        data.setCitizenship(this.getId(), this.props);
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
    }

    public boolean hasProvince() {
        return getProvince() != null;
    }

    @Override
    public void readTheFormOfGovernment(NbtCompound nbt) {
        switch (nbt.getString("formOfGovernment")) {
            case "absolute_monarchy":
                this.formOfGovernment = new AbsoluteMonarchy<Town, TownLawDescription>(this, () -> new Directive<>(this.description));
                break;
            default:
                throw new CrashException(CrashReport.create(new Throwable(), "Unknown form of government"));
        }
    }

    public static Town fromUUID(UUID id, WorldProperties props) {
        return (Town)ComponentsRegistry.LEGAL_ORGANISATIONS_REGISTRY.get(props).get(id);
    }

    public static Town fromUUID(UUID id, World world) {
        return fromUUID(id, world.getLevelProperties());
    }
}
