package me.jakubok.nationsmod.gui.townScreen;

import java.util.Arrays;
import java.util.List;

import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.gui.miscellaneous.*;
import me.jakubok.nationsmod.gui.miscellaneous.form.BasicValidations;
import me.jakubok.nationsmod.gui.miscellaneous.property.*;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class GeneralInfoSubscreen {
    public final Subscreen<TabWindow> subscreen;

    public List<PropertyEntry> propertyEntries;
    public PropertyListWidget list;
    public final TownScreen inst;

    public GeneralInfoSubscreen(TownScreen inst) {
        this.subscreen = new Subscreen<>(Text.of("General info"), new ItemStack(ItemRegistry.TOWN_INDEPENDENCE_DECLARATION), this::render, this::init);
        this.inst = inst;
    }

    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) {}

    protected void init(TabWindow instance) {
        this.propertyEntries = Arrays.asList(
                MutableTextProperty.of(
                        this.inst.getClient(),
                        new Pair<>(TownLawDescription.NameLabel, this.inst.town.getName()),
                        o -> Text.of("Name:"),
                        Text::of,
                        (property, client) -> new ChangeATextPropertyScreen(
                                Text.of("Changing town's name"),
                                230,
                                85,
                                4,
                                this.inst,
                                Text.of("New name:"),
                                Text.literal("Write..."),
                                this.inst.act,
                                TownLawDescription.NameLabel,
                                BasicValidations::BASIC_STRING_VALIDATION
                        ),
                        this.inst.act
                ),
                new TextProperty(inst.getClient(), Text.of("Government:"), inst.town.formOfGovernment.getDisplayName()),
                new TextProperty(inst.getClient(), Text.of("Citizens:"), Text.of(inst.town.getAIMembers().size() + inst.town.getPlayerMembers().size() + "")),
                new TextProperty(inst.getClient(), Text.of("Districts:"), Text.of(inst.town.getTheListOfDistrictsIDs().size() + "")),
                new TextProperty(inst.getClient(), Text.of("Province:"), Text.of("-")),
                new TextProperty(inst.getClient(), Text.of("Nation:"), Text.of("-")),
                new TextProperty(inst.getClient(), Text.of("Petition support:"), Text.of(inst.town.getThePetitionSupport() + "%")),
                new TextProperty(inst.getClient(), Text.of("Citizenship:"), inst.town.getTheCitizenshipApprovement().displayText)
        );
        this.list = new PropertyListWidget(
                instance.getClient(),
                this.propertyEntries,
                instance.getWindowLeft(),
                instance.getWindowWidth() - 5,
                this.inst.act == null ? instance.getWindowHeight() - 30 : instance.getWindowHeight() - 55,
                instance.getWindowTop() + 25,
                this.inst.act == null ? instance.getWindowBottom() - 5 : instance.getWindowBottom() - 30,
                this.inst.act == null ? 20 : 25
        );
        instance.addDrawableChild(this.list);
    }
}
