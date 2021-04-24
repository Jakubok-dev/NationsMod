package me.Jakubok.nations.administration;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class Town {

    protected String name;
    protected BlockPos center;
    protected LivingEntity ruler;
    protected Country country;
    protected long wealth;

    public Town(String name, BlockPos center, LivingEntity ruler, @Nullable Country country) {
        this.name = name;
        this.center = center;
        this.ruler = ruler;
        this.country = country;
    }

    public boolean belongsToCountry() {
        return !(country == null);
    }

    public String getName() {
        return name;
    }

    public BlockPos getCenter() {
        return center;
    }

    public LivingEntity getRuler() {
        return ruler;
    }

    @Nullable
    public Country getCountry() {
        return country;
    }
}
