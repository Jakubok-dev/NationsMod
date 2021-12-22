package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.collections.PlayerInfo;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;


public class Town implements ComponentV3 {
    private String name;
    private UUID id = UUID.randomUUID();
    private List<UUID> districtsIDs = new ArrayList<>();
    public final WorldProperties props;
    private UUID provincesID;
    private List<PlayerAccount> citizens = new ArrayList<>();
    private PlayerAccount ruler;

    public Town(String name, String districtName, ChunkPos pos, World world, Province province, BorderGroup borderGroup, PlayerAccount ruler) {
        this.name = name;
        this.props = world.getLevelProperties();
        this.citizens.add(ruler);
        this.ruler = ruler;
        if (province != null)
            this.provincesID = province.getId();
        ComponentsRegistry.TOWNS_REGISTRY.get(this.props).registerTown(this);

        District mainDistrict = new District(districtName, this, world, borderGroup);

        districtsIDs.add(mainDistrict.getId());
    }
    public Town(String name, String districtName, ChunkPos pos, World world, BorderGroup group, PlayerAccount ruler) {
        this(name, districtName, pos, world, null, group, ruler);
    }
    public Town(NbtCompound tag, WorldProperties props) {
        readFromNbt(tag);
        this.props = props;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public boolean setARuler(PlayerAccount ruler) {

        for (PlayerAccount account : this.citizens) {
            if (account.equals(ruler)) {
                this.ruler = ruler;
                return true;
            }
        }

        this.ruler = ruler;
        return false;
    }

    public PlayerAccount getARuler() {
        return ruler;
    }

    public boolean addACitizen(PlayerAccount account) {
        PlayerInfo info = PlayerInfo.fromAccount(account, this.props);

        if (info.getCitizenInfo().addCitizenship(this)) {
            this.citizens.add(account);
            return true;
        }
        return false;
    }

    public boolean removeACitizen(PlayerAccount account) {
        PlayerInfo info = PlayerInfo.fromAccount(account, this.props);

        for (int i = 0; i < this.citizens.size(); i++) {
            if (this.citizens.get(i).equals(account)) {
                this.citizens.remove(i);
                return info.getCitizenInfo().removeCitizenship(this);
            }
        }
        return false;
    }

    public List<PlayerInfo> getCitizens() {
        return this.citizens.stream()
            .map(el -> PlayerInfo.fromAccount(el, this.props))
            .toList();
    }

    public List<District> getDistricts() {
        return districtsIDs.stream()
        .map(el -> (District)ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(props).getClaimer(el))
        .toList();
        
    }

    public Province getProvince() {
        return Province.fromUUID(provincesID, props);
    }

    public void setProvince(Province province) {
        this.provincesID = province.getId();
        for (int i = 0; i < this.citizens.size(); i++) {
            PlayerInfo info = PlayerInfo.fromAccount(this.citizens.get(i), this.props);
            if (!info.getCitizenInfo().setNationality(province.getNation()))
                this.removeACitizen(this.citizens.get(i));
        }
    }

    public void changeCitizensNationality() {
        for (int i = 0; i < this.citizens.size(); i++) {
            PlayerInfo info = PlayerInfo.fromAccount(this.citizens.get(i), this.props);
            if (!info.getCitizenInfo().setNationality(this.getProvince().getNation()))
                this.removeACitizen(this.citizens.get(i));
        }
    }

    public boolean hasProvince() {
        return provincesID != null;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        name = tag.getString("name");
        id = tag.getUuid("id");
        if (!tag.getBoolean("is_province_null"))
            provincesID = tag.getUuid("provinces_id");

        for (int i = 1; i <= tag.getInt("districtsSize"); i++)
            districtsIDs.add(tag.getUuid("districtId" + i));

        for (int i = 1; i <= tag.getInt("citizensSize"); i++)
            this.citizens.add(new PlayerAccount(tag.getCompound("citizen" + i)));

        this.ruler = new PlayerAccount(tag.getCompound("ruler"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putString("name", this.name);
        tag.putUuid("id", this.id);
        if (this.provincesID != null)
            tag.putUuid("provinces_id", this.provincesID);
        tag.putBoolean("is_province_null", this.provincesID == null);
        
        AtomicInteger districtsSize = new AtomicInteger(0);
        for (UUID districtID : districtsIDs)
            tag.putUuid("districtId" + districtsSize.incrementAndGet(), districtID);
        tag.putInt("districtsSize", districtsSize.get());

        AtomicInteger citizensSize = new AtomicInteger(0);
        for (PlayerAccount account : this.citizens)
            tag.put("citizen" + citizensSize.incrementAndGet(), account.writeToNbtAndReturn(new NbtCompound()));
        tag.putInt("citizensSize", citizensSize.get());

        tag.put("ruler", this.ruler.writeToNbtAndReturn(new NbtCompound()));
        return tag;
    }

    public static Town fromUUID(UUID id, WorldProperties props) {
        return ComponentsRegistry.TOWNS_REGISTRY.get(props).getTown(id);
    }

    public static Town fromUUID(UUID id, World world) {
        return fromUUID(id, world.getLevelProperties());
    }
}
