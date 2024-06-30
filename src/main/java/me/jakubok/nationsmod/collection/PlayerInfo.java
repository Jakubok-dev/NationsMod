package me.jakubok.nationsmod.collection;

import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.geometry.Point;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import me.jakubok.nationsmod.registries.PlayerInfoRegistry;
import me.jakubok.nationsmod.registries.territory.GameTerritoryManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class PlayerInfo implements Serialisable {

    public boolean inAWilderness = true;
    public UUID currentDistrict;
    public UUID currentTown;
    public UUID currentProvince;
    public UUID currentNation;
    protected UUID citizenship;
    protected PlayerAccount account;
    public boolean online = false;
    public Point lastlyClickedBorderSign;
    public PolygonPlayerStorage polygonPlayerStorage = new PolygonPlayerStorage();

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

        String res = "";
        TerritoryShape shape = GameTerritoryManager.at(player.getBlockX(), player.getBlockZ(), player.getWorld());
        if (shape == null)
            return this.wilderness();
        if (inAWilderness)
            inAWilderness = false;
        TerritoryClaimer<?> claimer = (TerritoryClaimer<?>) LegalOrganisationRegistry.getRegistry(server).get(shape.claimantsID);
        if (claimer instanceof District district) {
            if (!district.getId().equals(currentDistrict)) {
                res += district.getName();
                currentDistrict = district.getId();
            }
            Town town = district.getTown(server);
            if (!town.getId().equals(currentTown)) {
                res += " | " + town.getName();
                currentTown = town.getId();
            }

            if (town.hasProvince(server)) {
                Province province = town.getProvince(server);
                if (!province.getId().equals(currentProvince)) {
                    res += " | " + province.getName();
                    currentProvince = province.getId();
                }
            }

            if (town.hasNation(server)) {
                Nation nation = town.getNation(server);
                if (!nation.getId().equals(currentNation)) {
                    res += " | " + nation.getName();
                    currentNation = nation.getId();
                }
            }
        } else if (claimer instanceof Province province) {
            if (!province.getId().equals(currentProvince)) {
                res += province.getName();
                currentProvince = province.getId();
            }

            Nation nation = province.getNation(server);
            if (!nation.getId().equals(currentNation)) {
                res += " | " + nation.getName();
                currentNation = nation.getId();
            }
        }

        return Text.of(res);
    }

    private Text wilderness() {
        if (!inAWilderness) {
            inAWilderness = true;
            this.currentDistrict = null;
            this.currentTown = null;
            this.currentProvince = null;
            this.currentNation = null;
            return Text.translatable("nationsmod.wilderness");
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
        
        if (!tag.getBoolean("is_lastly_clicked_border_sign_null")) {
            int x = tag.getInt("lastly_clicked_border_sign_x");
            int z = tag.getInt("lastly_clicked_border_sign_z");
            this.lastlyClickedBorderSign = new Point(x, z);
        }

        this.polygonPlayerStorage = new PolygonPlayerStorage(tag.getCompound("polygons"));
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

        if (this.lastlyClickedBorderSign != null) {
            tag.putInt("lastly_clicked_border_sign_x", this.lastlyClickedBorderSign.key);
            tag.putInt("lastly_clicked_border_sign_z", this.lastlyClickedBorderSign.value);
        }
        tag.putBoolean("is_lastly_clicked_border_sign_null", this.lastlyClickedBorderSign == null);

        tag.put("polygons", this.polygonPlayerStorage.writeToNbtAndReturn(new NbtCompound(), true));
    }
    
    public static PlayerInfo fromAccount(PlayerAccount account, MinecraftServer server) {
        return PlayerInfoRegistry.getRegistry(server).getAPlayer(account);
    }
}
