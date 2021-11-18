package me.jakubok.nationsmod.collections;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class PlayerAccount implements ComponentV3 {
    public UUID playersID, playersOfflineID;
    public String name;

    public PlayerAccount(UUID playerID, UUID playerOfflineID, String name) {
        this.playersID = playerID;
        this.playersOfflineID = playerOfflineID;
        this.name = name;
    }
    public PlayerAccount(PlayerEntity player) {
        this(player.getGameProfile().getId(), PlayerEntity.getOfflinePlayerUuid(player.getGameProfile().getName()), player.getGameProfile().getName());
    }
    public PlayerAccount(NbtCompound tag) {
        this.readFromNbt(tag);
    }

    public boolean isAnOnlineAccount() {
        return !this.playersID.equals(this.playersOfflineID);
    }
    @Override
    public void readFromNbt(NbtCompound tag) {
        this.playersID = tag.getUuid("playersID");
        this.playersOfflineID = tag.getUuid("playersOfflineID");
        this.name = tag.getString("name");        
    }
    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);     
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putUuid("playersID", this.playersID); 
        tag.putUuid("playersOfflineID", this.playersOfflineID);
        tag.putString("name", this.name);
        return tag;
    }
}
