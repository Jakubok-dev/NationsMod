package me.jakubok.nationsmod.administration.governmentElements.decisiveEntities;

import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.governmentElements.DecisiveEntity;
import me.jakubok.nationsmod.administration.governmentElements.FormOfGovernment;
import me.jakubok.nationsmod.collection.PlayerAccount;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;

public class Monarch extends DecisiveEntity {

    private PlayerAccount monarch;
    private UUID AIMonarch;

    public Monarch(AdministratingUnit<?> administratedUnit, FormOfGovernment<?, ?, ?, ?> formOfGovernment, MinecraftServer server) {
        super(administratedUnit, formOfGovernment);

        if (server != null) {
            if (!this.administratedUnit.getPlayerMembers(server).isEmpty()) {
                this.monarch = this.administratedUnit.getPlayerMembers(server).stream().findAny().get();
            } else if (!this.administratedUnit.getAIMembers(server).isEmpty()) {
                this.AIMonarch = this.administratedUnit.getAIMembers(server).stream().findAny().get();
            }
        }
    }

    public void setMonarch(PlayerAccount monarch) {
        if (this.AIMonarch != null)
            this.AIMonarch = null;
        this.monarch = monarch;
    }

    public void setAIMonarch(UUID AIMonarch) {
        if (this.monarch != null)
            this.monarch = null;
        this.AIMonarch = AIMonarch;
    }

    public PlayerAccount getMonarch() {
        return monarch;
    }
    public UUID getAIMonarch() {
        return AIMonarch;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (!tag.getBoolean("is_monarch_null"))
            this.monarch = new PlayerAccount(tag.getCompound("monarch"));
        if (!tag.getBoolean("is_ai_monarch_null"))
            this.AIMonarch = tag.getUuid("ai_monarch");
        
        super.readFromNbt(tag);
    }

    @Override
    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putBoolean("is_monarch_null", this.monarch == null);
        if (this.monarch != null)
            tag.put("monarch", this.monarch.writeToNbtAndReturn(new NbtCompound()));
        tag.putBoolean("is_ai_monarch_null", this.AIMonarch == null);
        if (this.AIMonarch != null)
            tag.putUuid("ai_monarch", this.AIMonarch);
        return super.writeToNbtAndReturn(tag);
    }
}
