package me.Jakubok.nations.administration;

import me.Jakubok.nations.regimes.Regime;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class Country {

    public String name;
    protected Town capital;
    public List<Province> provinces;
    public Regime regime;
    protected LivingEntity ruler;
    protected long wealth;

    public Country(String name, Town capital, List<Province> provinces, Regime regime, LivingEntity ruler) {
        this.name = name;
        this.capital = capital;
        this.provinces = provinces;
        this.regime = regime;
        this.ruler = ruler;
    }

    public Town getCapital() {
        return capital;
    }

    public boolean setCapital(Town newcapital) {
        if (!newcapital.belongsToProvince()) return false;
        if (newcapital.province.belonging != this) return false;
        capital = newcapital;
        return true;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public LivingEntity getRuler() {
        return ruler;
    }
}
