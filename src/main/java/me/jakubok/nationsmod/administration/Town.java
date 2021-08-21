package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;


public class Town implements ComponentV3 {
    private String name;
    private UUID id = UUID.randomUUID();
    private List<UUID> districtsIDs = new ArrayList<>();
    public final WorldProperties props;

    public Town(String name, WorldProperties props) {
        this.name = name;
        this.props = props;
        ComponentsRegistry.TOWNS_REGISTRY.get(this.props).registerTown(this);
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
        tag.putString("name", this.name);
        tag.putUuid("id", this.id);
        
        AtomicInteger districtsSize = new AtomicInteger(0);
        for (UUID districtID : districtsIDs)
            tag.putUuid("districtId" + districtsSize.incrementAndGet(), districtID);
        tag.putInt("districtsSize", districtsSize.get());
    }
}
