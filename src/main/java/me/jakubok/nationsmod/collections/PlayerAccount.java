package me.jakubok.nationsmod.collections;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class PlayerAccount implements Serialisable {
    public UUID playersID, playersOfflineID;
    public String name;

    public PlayerAccount(UUID playerID, UUID playerOfflineID, String name) {
        this.playersID = playerID;
        this.playersOfflineID = playerOfflineID;
        this.name = name;
    }
    public PlayerAccount(PlayerEntity player) {
        this(player.getGameProfile().getId(), UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getGameProfile().getName()).getBytes(StandardCharsets.UTF_8)), player.getGameProfile().getName());
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

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof PlayerAccount))
            return false;
        
        if (
            ((PlayerAccount)obj).name.equals(this.name) &&
            ((PlayerAccount)obj).playersID.equals(this.playersID) &&
            ((PlayerAccount)obj).playersOfflineID.equals(this.playersOfflineID)
        )
            return true;

        return false;
    }
}
