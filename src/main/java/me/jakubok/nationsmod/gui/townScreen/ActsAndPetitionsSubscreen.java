package me.jakubok.nationsmod.gui.townScreen;

import me.jakubok.nationsmod.administration.law.Petition;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.gui.miscellaneous.PetitionEntry;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyListWidget;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ActsAndPetitionsSubscreen {

    public final Subscreen<TabWindow> subscreen;
    public final TownScreen inst;
    public PropertyListWidget listWidget;

    public ActsAndPetitionsSubscreen(TownScreen inst) {
        this.subscreen = new Subscreen<>(Text.of("Acts & Petitions"), new ItemStack(ItemRegistry.ACT), this::render, this::init);
        this.inst = inst;
    }

    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) {
        
    }

    protected void init(TabWindow instance) {
        List<PropertyEntry> entries = new ArrayList<>();
        for (Petition<TownLawDescription> petition : inst.town.petitions.values())
            entries.add(new PetitionEntry(inst.getClient(), petition));

        this.listWidget = new PropertyListWidget(
                instance.getClient(),
                entries,
                instance.getWindowLeft(),
                instance.getWindowWidth() - 5,
                instance.getWindowHeight() - 30,
                instance.getWindowTop() + 25,
                instance.getWindowBottom() - 5,
                25
        );
        this.inst.addDrawableChild(this.listWidget);
    }
}
