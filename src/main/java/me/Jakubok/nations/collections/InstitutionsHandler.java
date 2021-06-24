package me.Jakubok.nations.collections;

import me.Jakubok.nations.administration.Country;
import me.Jakubok.nations.administration.Province;
import me.Jakubok.nations.administration.Town;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class InstitutionsHandler {
    public Town town;
    public Country country;
    public Province province;

    public InstitutionsHandler() {}
    public InstitutionsHandler(NbtCompound tag, World world) {
        if (!tag.getBoolean("isCountryNull"));
        if (!tag.getBoolean("isProvinceNull"));
        if (!tag.getBoolean("isTownNull")) town = new Town((NbtCompound)tag.get("town"), world);
    }

    public NbtCompound saveToTag(NbtCompound tag) {
        if (town != null) tag.put("town", town.saveToTag(new NbtCompound()));
        tag.putBoolean("isTownNull", town == null);
        tag.putBoolean("isCountryNull", country == null);
        tag.putBoolean("isProvinceNull", province == null);
        return tag;
    }
}
