package me.Jakubok.nations.administration;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Town {

    public String name;
    protected BlockPos center;
    public LivingEntity ruler;
    protected Province province;
    protected long wealth;

    public Town(String name, BlockPos center, LivingEntity ruler, @Nullable Province province) {
        this.name = name;
        this.center = center;
        this.ruler = ruler;
        this.province = province;
    }

    public boolean belongsToProvince() {
        return !(province == null);
    }

    public BlockPos getCenter() {
        return center;
    }

    @Nullable
    public Province getProvince() {
        return province;
    }
}
