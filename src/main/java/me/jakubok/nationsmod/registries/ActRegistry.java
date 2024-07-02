package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.nation.NationLawDescription;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class ActRegistry extends PersistentState {
    public HashMap<UUID, Act<?>> acts = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        AtomicInteger size = new AtomicInteger();
        acts.forEach((k, v) -> {
            nbt.putUuid("key" + size.get(), k);
            nbt.put("value" + size.get(), v.writeToNbtAndReturn(new NbtCompound()));
            String type = "";
            if (v.description instanceof TownLawDescription)
                type = "town";
            if (v.description instanceof NationLawDescription)
                type = "nation";
            nbt.putString("type" + size.getAndIncrement(), type);
        });
        nbt.putInt("size", size.get());
        return nbt;
    }

    public void readFromNbt(NbtCompound tag) {
        acts.clear();
        for (int i = 0; i < tag.getInt("size"); i++) {
            switch (tag.getString("type" + i)) {
                case "town" -> acts.put(tag.getUuid("key" + i), new Act<>(new TownLawDescription(), tag.getCompound("value" + i)));
                case "nation" -> acts.put(tag.getUuid("key" + i), new Act<>(new NationLawDescription(), tag.getCompound("value" + i)));
                default -> {}
            }
        }
    }

    public static ActRegistry getRegistry(MinecraftServer server) {

        Function<NbtCompound, ActRegistry> createFromNbt = nbt -> {
            ActRegistry registry = new ActRegistry();
            registry.readFromNbt(nbt);
            return registry;
        };

        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        return manager.getOrCreate(
                createFromNbt,
                ActRegistry::new,
                NationsMod.MOD_ID + "-act_registry"
        );
    }
}
