package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

public class Province extends TerritoryClaimer {

    private String name;
    private UUID nationsId;
    private List<UUID> townsIds = new ArrayList<>();
    private UUID capitalsId;

    public Province(String name, Town capital, Nation nation, World world) {
        super(world);
        this.name = name;
        this.nationsId = nation.getId();
        this.capitalsId = capital.getId();
        capital.setProvince(this);

        ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(this.props).registerClaimer(this);
    }
    public Province(NbtCompound tag, WorldProperties props) {
        super(props);
        readFromNbt(tag);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Town getCapital() {
        return Town.fromUUID(capitalsId, props);
    }
    
    public List<Town> getTowns() {
        return townsIds.stream()
        .map(el -> Town.fromUUID(el, props))
        .toList();
    }

    public Nation getNation() {
        return Nation.fromUUID(nationsId, this.props);
    }

    public static Province fromUUID(UUID id, WorldProperties props) {
        return (Province)ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(props).getClaimer(id);
    }
    public static Province fromUUID(UUID id, World world) {
        return fromUUID(id, world.getLevelProperties());
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        super.readFromNbt(tag);
        this.name = tag.getString("name");
        this.nationsId = tag.getUuid("nations_id");
        this.capitalsId = tag.getUuid("capitals_id");

        for (int i = 1; i <= tag.getInt("towns_ids_size"); i++)
            townsIds.add(tag.getUuid("towns_id" + i));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        super.writeToNbt(tag);
        tag.putString("name", this.name);
        tag.putUuid("nations_id", this.nationsId);
        tag.putUuid("capitals_id", this.capitalsId);
        tag.putBoolean("district", false);
        tag.putBoolean("province", true);

        AtomicInteger townsIdsSize = new AtomicInteger(0);

        for (UUID townsID : townsIds)
            tag.putUuid("towns_id" + townsIdsSize.incrementAndGet(), townsID);
        tag.putInt("towns_ids_size", townsIdsSize.get());
    }
    @Override
    protected void sendMapBlockInfo(ServerWorld world) {
        // TODO Implement later
        
    }
}
