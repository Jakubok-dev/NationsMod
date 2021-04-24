package me.Jakubok.nations.administration;

import me.Jakubok.nations.regimes.Regime;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class Country {

    protected String name;
    protected Town capital;
    protected List<Town> towns;
    protected Regime regime;
    protected LivingEntity ruler;
    protected long wealth;

    public Country(String name, Town capital, List<Town> towns, Regime regime, LivingEntity ruler) {
        this.name = name;
        this.capital = capital;
        this.towns = towns;
        this.regime = regime;
        this.ruler = ruler;
    }

    public String getName() {
        return name;
    }

    public Town getCapital() {
        return capital;
    }

    public List<Town> getTowns() {
        return towns;
    }

    public Regime getRegime() {
        return regime;
    }

    public LivingEntity getRuler() {
        return ruler;
    }
}
