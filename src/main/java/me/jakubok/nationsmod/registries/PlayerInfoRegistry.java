package me.jakubok.nationsmod.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.collections.PlayerInfo;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;

public class PlayerInfoRegistry implements ComponentV3 {
    private List<PlayerInfo> players = new ArrayList<>(); 
    public final WorldProperties props;

    public PlayerInfoRegistry(WorldProperties props) {
        this.props = props;
    }

    public List<PlayerInfo> getNations() {
        return this.players;
    }

    public PlayerInfo getAPlayer(PlayerAccount account) {
        for (PlayerInfo player : this.players) {
            if (account.isAnOnlineAccount()) {
                if (player.getPlayerAccount().playersID.equals(account.playersID))
                    return player;
            } else {
                if (player.getPlayerAccount().name.equals(account.name))
                    return player;
            }
        }
        
        PlayerInfo player = new PlayerInfo(account, this.props);
        this.registerAPlayer(player);
        return player;
    }

    protected void registerAPlayer(PlayerInfo info) {
        this.players.add(info);
    }

    public boolean removeAPlayer(PlayerAccount account) {
        if (this.getAPlayer(account) == null)
            return false;

        return this.players.remove(this.getAPlayer(account));
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (int i = 1; i <= tag.getInt("size"); i++) {
            NbtCompound compound = (NbtCompound)tag.get("player_info" + i);
            this.players.add(new PlayerInfo(compound, this.props));
        } 
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        AtomicInteger size = new AtomicInteger(0);
        this.players.forEach(el -> {
            NbtCompound compound = new NbtCompound();
            el.writeToNbt(compound);
            tag.put("player_info" + size.incrementAndGet(), compound);
        });
        tag.putInt("size", size.get());
    }
}
