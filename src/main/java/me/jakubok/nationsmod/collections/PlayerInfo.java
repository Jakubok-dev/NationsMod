package me.jakubok.nationsmod.collections;

import java.util.UUID;

import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.registries.PlayerInfoRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PlayerInfo implements Serialisable {

    public boolean inAWilderness = true;
    public UUID currentDistrict;
    public UUID currentTown;
    public UUID currentProvince;
    public UUID currentNation;

    protected UUID citizenship;

    protected PlayerAccount account;
    public BorderSlots slots = new BorderSlots();

    public boolean online = false;

    public PlayerInfo(NbtCompound compound) {
        this.readFromNbt(compound);
    }
    public PlayerInfo(PlayerEntity entity) {
        this.account = new PlayerAccount(entity);
    }
    public PlayerInfo(PlayerAccount account) {
        this.account = account;
    }

    public PlayerAccount getPlayerAccount() {
        return this.account;
    }

    public void setPlayerAccount(PlayerAccount account) {
        if (!account.isAnOnlineAccount() && this.account.isAnOnlineAccount())
            return;
        
        this.account = account;
    }

    public UUID getCitizenship() {
        return citizenship;
    }
    public boolean setCitizenship(UUID citizenship, MinecraftServer server) {
        if (Town.fromUUID(citizenship, server) == null)
            return false;
        this.citizenship = citizenship;
        return true;
    }
    public void removeCitizenship() {
        this.citizenship = null;
    }

    public Text getToolBarText(ServerPlayerEntity player, MinecraftServer server) {

        ChunkClaimRegistry registry = ChunkBinaryTree.getRegistry(player.getWorld()).get(player.getBlockPos());

        if (registry == null)
            return wilderness();
        if (!registry.isBelonging(player.getBlockPos()))
            return wilderness();

        inAWilderness = false;

        District district = District.fromUUID(registry.claimBelonging(player.getBlockPos()), server);
        Town town = district.getTown(server);

        if (!town.hasProvince(server)) {
            if (!town.getId().equals(this.currentTown)) {
                this.currentTown = town.getId();
                this.currentDistrict = district.getId();
                return Text.of(district.getName() + " | " + town.getName());
            }

            if (!district.getId().equals(this.currentDistrict)) {
                this.currentDistrict = district.getId();
                return Text.of(district.getName());
            }

            return null;
        }

        Province province = town.getProvince(server);
        Nation nation = province.getNation(server);

        if (!nation.getId().equals(this.currentNation)) {
            this.currentNation = nation.getId();
            this.currentProvince = province.getId();
            this.currentTown = town.getId();
            this.currentDistrict = district.getId();

            return Text.of(district.getName() + " | " + town.getName() + " | " + province.getName() + " | " + nation.getName());
        }

        if (!province.getId().equals(this.currentProvince)) {
            this.currentProvince = province.getId();
            this.currentTown = town.getId();
            this.currentDistrict = district.getId();

            return Text.of(district.getName() + " | " + town.getName() + " | " + province.getName());
        }

        if (!town.getId().equals(this.currentTown)) {
            this.currentTown = town.getId();
            this.currentDistrict = district.getId();

            return Text.of(district.getName() + " | " + town.getName());
        }

        if (!district.getId().equals(this.currentDistrict)) {
            this.currentDistrict = district.getId();

            return Text.of(district.getName());
        }

        return null;
    }

    private Text wilderness() {
        if (!inAWilderness) {
            inAWilderness = true;
            this.currentDistrict = null;
            this.currentTown = null;
            this.currentProvince = null;
            this.currentNation = null;
            return new TranslatableText("nationsmod.wilderness");
        }
        return null;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.inAWilderness = tag.getBoolean("in_a_wilderness");

        if (!tag.getBoolean("is_current_district_null"))
            this.currentDistrict = tag.getUuid("current_district");
        
        if (!tag.getBoolean("is_current_town_null"))
            this.currentTown = tag.getUuid("current_town");

        if (!tag.getBoolean("is_current_province_null"))
            this.currentProvince = tag.getUuid("current_province");

        if (!tag.getBoolean("is_current_nation_null"))
            this.currentNation = tag.getUuid("current_nation");

        this.account = new PlayerAccount(tag.getCompound("account"));

        if (!tag.getBoolean("is_citizenship_null"))
            this.citizenship = tag.getUuid("citizenship");
        
        if (!tag.getBoolean("are_slots_null"))
            this.slots.readFromNbt(tag.getCompound("slots"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("in_a_wilderness", this.inAWilderness);

        if (this.currentDistrict != null)
            tag.putUuid("current_district", this.currentDistrict);
        tag.putBoolean("is_current_district_null", this.currentDistrict == null);

        if (this.currentTown != null)
            tag.putUuid("current_town", this.currentTown);
        tag.putBoolean("is_current_town_null", this.currentTown == null);

        if (this.currentProvince != null)
            tag.putUuid("current_province", this.currentProvince);
        tag.putBoolean("is_current_province_null", this.currentProvince == null);

        if (this.currentNation != null)
            tag.putUuid("current_nation", this.currentNation);
        tag.putBoolean("is_current_nation_null", this.currentNation == null);

        tag.put("account", this.account.writeToNbtAndReturn(new NbtCompound()));

        if (this.citizenship != null)
            tag.putUuid("citizenship", this.citizenship);
        tag.putBoolean("is_citizenship_null", this.citizenship == null);

        if (this.slots != null)
            tag.put("slots", this.slots.writeToNbtAndReturn(new NbtCompound(), true));
        tag.putBoolean("are_slots_null", this.slots == null);
    }
    
    public static PlayerInfo fromAccount(PlayerAccount account, MinecraftServer server) {
        return PlayerInfoRegistry.getRegistry(server).getAPlayer(account);
    }
}
