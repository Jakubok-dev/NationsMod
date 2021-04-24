package me.Jakubok.nations.block;

import me.Jakubok.nations.util.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

public class NationPillarEntity extends BlockEntity {

    public boolean activated = false;
    public boolean health_inserted = false;
    public int charge_level = 0;

    public NationPillarEntity() {
        super(Blocks.NATION_PILLAR_ENTITY);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putBoolean("activated", activated);
        tag.putBoolean("health_inserted", health_inserted);
        tag.putInt("charge_level", charge_level);
        super.toTag(tag);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        activated = tag.getBoolean("activated");
        health_inserted = tag.getBoolean("health_inserted");
        charge_level = tag.getInt("charge_level");
        super.fromTag(state, tag);
    }
}
