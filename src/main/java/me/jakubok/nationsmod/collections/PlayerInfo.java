package me.jakubok.nationsmod.collections;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.District;
import me.jakubok.nationsmod.administration.Nation;
import me.jakubok.nationsmod.administration.Province;
import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PlayerInfo implements ComponentV3 {

    public boolean inAWilderness = true;
    public UUID currentDistrict;
    public UUID currentTown;
    public UUID currentProvince;
    public UUID currentNation;

    public Text getToolBarText(PlayerEntity player) {

        ChunkClaimRegistry registry = ComponentsRegistry.CHUNK_BINARY_TREE.get(player.getEntityWorld().getLevelProperties()).get(player.getBlockPos());

        if (registry == null)
            return wilderness();
        if (!registry.isBelonging(player.getBlockPos()))
            return wilderness();

        inAWilderness = false;

        District district = District.fromUUID(registry.claimBelonging(player.getBlockPos()), player.getEntityWorld());
        Town town = district.getTown();

        if (!town.hasProvince()) {
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

        Province province = town.getProvince();
        Nation nation = province.getNation();

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
    }
    
}
