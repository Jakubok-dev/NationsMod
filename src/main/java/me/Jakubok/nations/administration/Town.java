package me.Jakubok.nations.administration;

import me.Jakubok.nations.terrain.ModChunkPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
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

    public Town(String name, BlockPos center, LivingEntity ruler, String districtName, @Nullable Province province, World world) {
        this.name = name;
        this.center = center;
        this.ruler = ruler;
        this.province = province;

        TownDistrict dist = createDistrict(districtName  , this.center, world);
        final ModChunkPos template = new ModChunkPos(new ChunkPos(center));
        ModChunkPos pos = new ModChunkPos(new ChunkPos(center));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(template.x-1, template.z));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(template.x+1, template.z));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(template.x, template.z-1));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(template.x, template.z+1));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(template.x-1, template.z-1));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(template.x+1, template.z+1));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(template.x-1, template.z+1));
        dist.expand(pos); pos = new ModChunkPos(new ChunkPos(template.x+1, template.z-1));
        dist.expand(pos);
    }

    public Town(CompoundTag tag, World world) {
        if (tag.getBoolean("belongsToProvince")) return;
        for (int i = 0; i < tag.getInt("districtsCount"); i++)
            districts.add(new TownDistrict((CompoundTag)tag.get("district" + i), this, world));
        name = tag.getString("name");
        int[] arr = tag.getIntArray("center");
        center = new BlockPos(arr[0], arr[1], arr[2]);
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

    public CompoundTag saveToTag(CompoundTag tag) {
        for (int i = 0; i < districts.size(); i++) {
            CompoundTag subTag = new CompoundTag();
            districts.get(i).saveToTag(subTag);
            tag.put("district" + i, subTag);
        }
        tag.putInt("districtsCount", districts.size());
        tag.putString("name", name);
        tag.putIntArray("center", new int[] {center.getX(), center.getY(), center.getZ()});
        tag.putBoolean("belongsToProvince", belongsToProvince());
        return tag;
    }
}
