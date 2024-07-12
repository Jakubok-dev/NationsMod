package me.jakubok.nationsmod.gui.townScreen;

import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.law.Petition;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.gui.miscellaneous.ActEntry;
import me.jakubok.nationsmod.gui.miscellaneous.PetitionEntry;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyListWidget;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.*;

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
        assert this.inst.getClient() != null;
        assert this.inst.getClient().player != null;
        List<PropertyEntry> entries = new ArrayList<>();

        for (Act<TownLawDescription> act : inst.town.formOfGovernment.mapOfDirectives
                .values()
                .stream()
                .sorted((a, b) -> {
                    if (a.status.ordinal() == b.status.ordinal())
                        return Comparator.<String>naturalOrder().compare(a.getName(), b.getName());
                    if (a.status.ordinal() < b.status.ordinal())
                        return 1;
                    return -1;
                })
                .toList()
        )
            entries.add(new ActEntry(inst.getClient(), act));

        for (Petition<TownLawDescription> petition : inst.town.petitions
                .values()
                .stream()
                .sorted((a, b) -> Comparator.<String>naturalOrder().compare(a.act.getName(), b.act.getName()))
                .toList()
        ) {
            entries.add(new PetitionEntry<>(inst.getClient(), petition, inst.town, inst.town.isACitizen(new PlayerAccount(this.inst.getClient().player)), inst));
        }
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
