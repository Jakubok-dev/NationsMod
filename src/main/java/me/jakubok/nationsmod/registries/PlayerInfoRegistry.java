package me.jakubok.nationsmod.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class PlayerInfoRegistry extends PersistentState {
    private List<PlayerInfo> players = new ArrayList<>(); 

    public List<PlayerInfo> getPlayers() {
        this.markDirty();
        return this.players;
    }

    public PlayerInfo getAPlayer(PlayerAccount account) {
        this.markDirty();
        if (account == null)
            return null;
        for (PlayerInfo player : this.players) {
            if (account.isAnOnlineAccount()) {
                if (player.getPlayerAccount().playersID.equals(account.playersID))
                    return player;
            } else {
                if (player.getPlayerAccount().name.equals(account.name))
                    return player;
            }
        }
        
        PlayerInfo player = new PlayerInfo(account);
        this.registerAPlayer(player);
        return player;
    }

    protected void registerAPlayer(PlayerInfo info) {
        this.players.add(info);
        this.markDirty();
    }

    public boolean removeAPlayer(PlayerAccount account) {
        if (this.getAPlayer(account) == null)
            return false;
        this.markDirty();
        return this.players.remove(this.getAPlayer(account));
    }

    public void readFromNbt(NbtCompound tag) {
        for (int i = 1; i <= tag.getInt("size"); i++) {
            NbtCompound compound = (NbtCompound)tag.get("player_info" + i);
            this.players.add(new PlayerInfo(compound));
        } 
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        AtomicInteger size = new AtomicInteger(0);
        this.players.forEach(el -> {
            NbtCompound compound = new NbtCompound();
            el.writeToNbt(compound);
            tag.put("player_info" + size.incrementAndGet(), compound);
        });
        tag.putInt("size", size.get());
        return tag;
    }

    public static PlayerInfoRegistry getRegistry(MinecraftServer server) {

        Function<NbtCompound, PlayerInfoRegistry> createFromNbt = nbt -> {
            PlayerInfoRegistry registry = new PlayerInfoRegistry();
            registry.readFromNbt(nbt);
            return registry;
        };

        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        PlayerInfoRegistry registry = manager.getOrCreate(
            createFromNbt,
            PlayerInfoRegistry::new, 
            NationsMod.MOD_ID + ":player_info_registry"
        );
        return registry;
    }
}
