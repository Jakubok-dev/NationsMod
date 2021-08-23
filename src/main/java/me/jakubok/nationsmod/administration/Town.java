package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;


public class Town implements ComponentV3 {
    private String name;
    private UUID id = UUID.randomUUID();
    private List<UUID> districtsIDs = new ArrayList<>();
    public final WorldProperties props;

    public Town(String name, String districtName, ChunkPos pos, World world) {
        this.name = name;
        this.props = world.getLevelProperties();
        ComponentsRegistry.TOWNS_REGISTRY.get(this.props).registerTown(this);

        District mainDistrict = new District(districtName, this, props);

        mainDistrict.claim(pos, world);
        mainDistrict.claim(new ChunkPos(pos.x-1, pos.z), world);
        mainDistrict.claim(new ChunkPos(pos.x-1, pos.z-1), world);
        mainDistrict.claim(new ChunkPos(pos.x+1, pos.z), world);
        mainDistrict.claim(new ChunkPos(pos.x+1, pos.z+1), world);
        mainDistrict.claim(new ChunkPos(pos.x, pos.z-1), world);
        mainDistrict.claim(new ChunkPos(pos.x, pos.z+1), world);
        mainDistrict.claim(new ChunkPos(pos.x-1, pos.z+1), world);
        mainDistrict.claim(new ChunkPos(pos.x+1, pos.z-1), world);

        districtsIDs.add(mainDistrict.getId());
    }
    public Town(NbtCompound tag, WorldProperties props) {
        readFromNbt(tag);
        this.props = props;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public List<District> getDistricts() {
        return districtsIDs.stream()
        .map(el -> (District)ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(props).getClaimer(el))
        .toList();
        
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        name = tag.getString("name");
        id = tag.getUuid("id");

        for (int i = 1; i <= tag.getInt("districtsSize"); i++)
            districtsIDs.add(tag.getUuid("districtId" + i));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putString("name", this.name);
        tag.putUuid("id", this.id);
        
        AtomicInteger districtsSize = new AtomicInteger(0);
        for (UUID districtID : districtsIDs)
            tag.putUuid("districtId" + districtsSize.incrementAndGet(), districtID);
        tag.putInt("districtsSize", districtsSize.get());
        return tag;
    }

    public static Town fromUUID(UUID id, World world) {
        return ComponentsRegistry.TOWNS_REGISTRY.get(world.getLevelProperties()).getTown(id);
    }
}
