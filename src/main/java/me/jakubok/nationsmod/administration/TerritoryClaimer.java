package me.jakubok.nationsmod.administration;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;

public abstract class TerritoryClaimer implements ComponentV3 {

    private UUID id = UUID.randomUUID();
    private long claimedBlocksCount = 0;
    public final WorldProperties props;

    public TerritoryClaimer(WorldProperties props) {
        this.props = props;
    }

    public UUID getId() {
        return id;
    }

    public long getClaimedBlocksCount() {
        return claimedBlocksCount;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        id = tag.getUuid("id");
        claimedBlocksCount = tag.getLong("claimedBlocksCount");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putUuid("id", id);
        tag.putLong("claimedBlocksCount", claimedBlocksCount);
    }
}