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
import me.jakubok.nationsmod.registries.LegalOrganisationsRegistry;
import me.jakubok.nationsmod.registries.PlayerInfoRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.ChunkPos;


public class Town extends AdministratingUnit<TownLawDescription> {

    public Town(String name, String districtName, ChunkPos pos, ServerWorld world, Province province, BorderGroup borderGroup, MinecraftServer server) {
        super(new TownLawDescription(), name, server);
        if (province != null)
            this.setProvincesID(province.getId());

        District mainDistrict = new District(districtName, this, world, borderGroup, server);

        this.getTheListOfDistrictsIDs().add(mainDistrict.getId());
    }
    public Town(String name, String districtName, ChunkPos pos, ServerWorld world, BorderGroup group, MinecraftServer server) {
        this(name, districtName, pos, world, null, group, server);
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
        HumanData data = entity.getHumanData();
        if (data.getCitizenship() != null)
            Town.fromUUID(data.getCitizenship(), server).removeAMember(entity);
        
        data.setCitizenship(this.getId(), server);
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
        .map(el -> (District)LegalOrganisationsRegistry.getRegistry(server).get(el))
        .toList();
        
    }

    public UUID getProvincesID() {
        return (UUID)this.law.getARule(TownLawDescription.provincesIDLabel);
    }

    public boolean setProvincesID(UUID id) {
        return this.law.putARule(TownLawDescription.provincesIDLabel, id);
    }

    public Province getProvince(MinecraftServer server) {
        return Province.fromUUID(this.getProvincesID(), server);
    }

    public void setProvince(Province province) {
        this.setProvincesID(province.getId());
    }

    public boolean hasProvince(MinecraftServer server) {
        return this.getProvince(server) != null;
    }

    @Override
    public void readTheFormOfGovernment(NbtCompound nbt, MinecraftServer server) {
        switch (nbt.getString("formOfGovernment")) {
            case "absolute_monarchy":
                this.formOfGovernment = new AbsoluteMonarchy<Town, TownLawDescription>(this, () -> new Directive<>(this.description), server);
                break;
            default:
                throw new CrashException(CrashReport.create(new Throwable(), "Unknown form of government"));
        }
    }

    public static Town fromUUID(UUID id, MinecraftServer server) {
        return (Town)LegalOrganisationsRegistry.getRegistry(server).get(id);
    }
}
