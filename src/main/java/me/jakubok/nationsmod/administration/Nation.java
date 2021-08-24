package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

public class Nation implements ComponentV3 {

    private String name;
    private UUID id = UUID.randomUUID();
    public final WorldProperties props;
    private UUID capitalsId;
    private List<UUID> provincesIDs = new ArrayList<>();
    
    public Nation(String name, WorldProperties props, String provinceName, Town capital) {
        this.name = name;
        this.props = props;
        this.capitalsId = capital.getId();

        Province mainProvince = new Province(provinceName, capital, this, props);
        provincesIDs.add(mainProvince.getId());

        ComponentsRegistry.NATIONS_REGISTRY.get(this.props).registerNation(this);
    }
    public Nation(NbtCompound tag, WorldProperties props) {
        this.props = props;
        readFromNbt(tag);
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

    public Town getCapital() {
        return Town.fromUUID(capitalsId, props);
    }

    public List<Province> getProvinces() {
        return provincesIDs.stream()
            .map(el -> Province.fromUUID(el, props))
            .toList();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.id = tag.getUuid("id");
        this.name = tag.getString("name");
        this.capitalsId = tag.getUuid("capitals_id");
        for (int i = 1; i <= tag.getInt("provinces_ids_size"); i++)
            provincesIDs.add(tag.getUuid("provinces_id" + i));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putUuid("id", this.id);
        tag.putString("name", this.name);
        tag.putUuid("capitals_id", this.capitalsId);

        AtomicInteger provincesIdsSize = new AtomicInteger(0);
        for (UUID provincesID : provincesIDs)
            tag.putUuid("provinces_id" + provincesIdsSize.incrementAndGet(), provincesID);
        tag.putInt("provinces_ids_size", provincesIdsSize.get());
    }
    
    public static Nation fromUUID(UUID id, WorldProperties props) {
        return ComponentsRegistry.NATIONS_REGISTRY.get(props).getNation(id);
    }
    public static Nation fromUUID(UUID id, World world) {
        return fromUUID(id, world.getLevelProperties());
    }
}
