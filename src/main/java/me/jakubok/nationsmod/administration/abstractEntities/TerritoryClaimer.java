package me.jakubok.nationsmod.administration.abstractEntities;

import me.jakubok.nationsmod.collection.TerritoryShape;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.registries.territory.GameTerritoryManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public abstract class TerritoryClaimer<D extends TerritoryClaimerLawDescription> extends LegalOrganisation<D> {

    public TerritoryClaimer(D description, String name, MinecraftServer server) {
        super(description, name, server);
    }
    public TerritoryClaimer(D description, NbtCompound nbt, MinecraftServer server) {
        super(description);
        this.readFromNbt(nbt, server);
    }

    public TerritoryShape getTheTerritoryShape(MinecraftServer server) {
        UUID shapesID = this.getTheTerritoryShapesID();
        if (shapesID == null)
            return  null;
        return GameTerritoryManager.get(shapesID, server);
    }

    public UUID getTheTerritoryShapesID() {
        return (UUID)this.law.getARule(TerritoryClaimerLawDescription.shapesIDLabel);
    }

    public boolean unclaimTerritory(MinecraftServer server) {
        UUID shapesID = this.getTheTerritoryShapesID();
        if (shapesID == null)
            return false;
        return GameTerritoryManager.deregister(shapesID, server) != null;
    }

    public abstract boolean claimTerritory(Polygon polygon, ServerWorld world);

    protected boolean setTheTerritoryShape(TerritoryShape shape) {
        if (shape.claimantsID != this.getId())
            return false;
        shape.claimantsID = this.getId();
        return this.law.putARule(TerritoryClaimerLawDescription.shapesIDLabel, shape.getId());
    }

    @Override
    public boolean deregister(MinecraftServer server) {
        this.unclaimTerritory(server);
        return super.deregister(server);
    }
}