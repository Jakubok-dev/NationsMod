package me.Jakubok.nations.administration;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Town extends TeritorryClaimer {

    protected String name;
    protected BlockPos center;
    protected LivingEntity ruler;
    protected Province province;
    protected long wealth;

    public Town(String name, BlockPos center, World world, LivingEntity ruler, @Nullable Province province) {
        super(world);
        this.name = name;
        this.center = center;
        this.ruler = ruler;
        this.province = province;
    }

    public boolean belongsToProvince() {
        return !(province == null);
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
    public Province getProvince() {
        return province;
    }
}
