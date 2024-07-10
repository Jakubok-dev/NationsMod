package me.jakubok.nationsmod.gui.townScreen;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.gui.ChangeLawApprovementScreen;
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
                                this.inst.town,
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
                MutableTextProperty.of(
                        this.inst.getClient(),
                        new Pair<>(TownLawDescription.petitionSupportLabel, inst.town.getThePetitionSupport()),
                        o -> Text.of("Petition support:"),
                        o -> Text.of(o + "%"),
                        (property, client) -> new ChangeATextPropertyScreen(
                                Text.of("Changing the petition support"),
                                265,
                                85,
                                4,
                                this.inst,
                                Text.of("New percentage:"),
                                Text.literal("0..100"),
                                this.inst.act,
                                this.inst.town,
                                TownLawDescription.petitionSupportLabel,
                                o -> {
                                    Text res = BasicValidations.BASIC_PERCENTAGE_VALIDATION(o);
                                    if (!res.getString().equals(""))
                                        return res;
                                    Pattern pattern = Pattern.compile("^(-?[1-9]\\d*)%?$|^(-?\\d)%?$");
                                    Matcher matcher = pattern.matcher((String)o);
                                    matcher.find();
                                    String capture = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                                    int intCapture = Integer.parseInt(capture);
                                    if (intCapture < 0 || intCapture > 100)
                                        return Text.literal("Illegal range!").formatted(Formatting.RED);;
                                    return Text.of("");
                                },
                                l -> {
                                    Pattern pattern = Pattern.compile("^(-?[1-9]\\d*)%?$|^(-?\\d)%?$");
                                    Matcher matcher = pattern.matcher((String)l.get(0));
                                    matcher.find();
                                    String capture = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                                    Integer intCapture = Integer.parseInt(capture);
                                    if (this.inst.town.law.getARule(TownLawDescription.petitionSupportLabel) != null) {
                                        if (this.inst.town.law.getARule(TownLawDescription.petitionSupportLabel).equals(intCapture)) {
                                            this.inst.act.resetARule(TownLawDescription.petitionSupportLabel);
                                            assert client.currentScreen != null;
                                            client.currentScreen.close();
                                            return;
                                        }
                                    }
                                    this.inst.act.putARule(TownLawDescription.petitionSupportLabel, intCapture);
                                    assert client.currentScreen != null;
                                    client.currentScreen.close();
                                }
                        ),
                        this.inst.act
                ),
                MutableTextProperty.of(
                        this.inst.getClient(),
                        new Pair<>(TownLawDescription.citizenshipApprovementLabel, inst.town.getTheCitizenshipApprovement()),
                        o -> Text.of("Citizenship:"),
                        o -> o.displayText,
                        (property, client) -> new ChangeLawApprovementScreen(
                                Text.of("Change the citizenship approvement"),
                                this.inst,
                                this.inst.act,
                                this.inst.town,
                                TownLawDescription.citizenshipApprovementLabel
                        ),
                        this.inst.act
                )
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
