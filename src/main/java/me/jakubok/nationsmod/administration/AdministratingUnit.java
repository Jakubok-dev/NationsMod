package me.jakubok.nationsmod.administration;

import java.util.Random;

import me.jakubok.nationsmod.collections.Colour;
import net.minecraft.world.WorldProperties;

public abstract class AdministratingUnit<D extends AdministratingUnitLawDescription> extends LegalOrganisation<D> {

    public AdministratingUnit(D description, String name, WorldProperties props) {
        super(description, name, props);
        Random rng = new Random();
        if (this.getTheMapColour().getR() <= 0)
            this.getTheMapColour().setR(rng.nextInt(255));
        if (this.getTheMapColour().getG() <= 0)
            this.getTheMapColour().setG(rng.nextInt(255));
        if (this.getTheMapColour().getB() <= 0)
            this.getTheMapColour().setB(rng.nextInt(255));
    }
    public AdministratingUnit(D description, WorldProperties props) {
        super(description, props);
    }

    public Colour getTheMapColour() {
        return (Colour)this.law.getARule(AdministratingUnitLawDescription.mapColourLabel);
    }
}
