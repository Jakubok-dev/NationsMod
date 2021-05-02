package me.Jakubok.nations.administration;

import me.Jakubok.nations.terrain.ModChunkPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Town {

    public String name;
    protected BlockPos center;
    public LivingEntity ruler;
    protected Province province;
    protected List<TownDistrict> districts = new ArrayList<>();
    protected long wealth;

    public Town(String name, BlockPos center, LivingEntity ruler, @Nullable Province province, World world) {
        this.name = name;
        this.center = center;
        this.ruler = ruler;
        this.province = province;

        TownDistrict dist = createDistrict("Center", this.center, world);
        ModChunkPos pos = new ModChunkPos(new ChunkPos(center));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(new BlockPos(pos.getEndX()+1,64, pos.getStartZ())));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(new BlockPos(pos.getStartX()-1,64, pos.getStartZ())));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(new BlockPos(pos.getStartX(),64,pos.getEndZ()+1)));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(new BlockPos(pos.getStartX(),64,pos.getStartZ()-1)));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(new BlockPos(pos.getStartX()-1,64,pos.getStartZ()-1)));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(new BlockPos(pos.getEndX()+1,64,pos.getStartZ()-1)));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(new BlockPos(pos.getStartX()-1,64,pos.getEndZ()+1)));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(new BlockPos(pos.getEndX()+1,64,pos.getEndZ()+1)));
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

    public List<TownDistrict> getDistricts() {
        return districts;
    }

    public TownDistrict createDistrict(String name, BlockPos center, World world) {
        TownDistrict temp = new TownDistrict(this, name, world, center);
        districts.add(temp);
        return temp;
    }

    public boolean removeDistrict(TownDistrict dist) {
        if (!districts.contains(dist)) return false;
        districts.remove(dist); return true;
    }
}
