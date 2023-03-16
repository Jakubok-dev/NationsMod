package me.jakubok.nationsmod.administration.governmentElements;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.law.Directive;
import net.minecraft.nbt.NbtCompound;

public abstract class FormOfGovernment<L extends DecisiveEntity, E extends DecisiveEntity, U extends AdministratingUnit<?>> implements ComponentV3 {
    public final L legislative;
    public final E executive;
    public final U administratedUnit;
    public final Map<UUID, Directive<?>> mapOfDirectives = new HashMap<>();

    public FormOfGovernment(L legislative, E executive, U administratedUnit) {
        this.legislative = legislative;
        this.executive = executive;
        this.administratedUnit = administratedUnit;
    }

    public abstract void putUnderDeliberation(Directive<?> directive);

    public abstract String getName();
    public abstract String getDescription();

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.mapOfDirectives.clear();
        for (int i = 0; i < nbt.getInt("Size"); i++) {
            //String type = nbt.getString("type" + i);
        }
    }
    @Override
    public void writeToNbt(NbtCompound nbt) {
        this.writeToNbtAndReturn(nbt);
    }
    public NbtCompound writeToNbtAndReturn(NbtCompound nbt) {
        Directive<?>[] directives = mapOfDirectives.keySet().toArray(new Directive<?>[]{});
        nbt.putInt("Size", directives.length);
        for (int i = 0; i < directives.length; i++) {
            nbt.put("directive" + i, directives[i].writeToNbtAndReturn(new NbtCompound()));
            nbt.putString("type" + i, directives[i].description.getClass().getName());
        }
        return nbt;
    }
}
