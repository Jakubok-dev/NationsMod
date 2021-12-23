package me.jakubok.nationsmod.gui.townScreen;

import java.util.ArrayList;
import java.util.List;

import me.jakubok.nationsmod.administration.Directive;
import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class TownScreen extends TabWindow {

    protected final Town town;
    public final GeneralInfoSubscreen generalInfo;
    public final ButtonWidget petitionSubmit;
    public Directive<Town> draft;

    public TownScreen(Town town) {
        super(Text.of(town.getName()));
        this.town = town;
        generalInfo = new GeneralInfoSubscreen(this);

        this.petitionSubmit = new ButtonWidget(
            windowLeft + 30, 
            windowBottom - 25, 
            210,
            20, 
            Text.of("Create a petition"),
            t -> handlePetitionSubmit(t)
        );
    }

    @Override
    protected List<Subscreen<TabWindow>> getTabs() {
        
        List<Subscreen<TabWindow>> tabs = new ArrayList<>();
        tabs.add(generalInfo.subscreen);

        tabs.add(new Subscreen<TabWindow>(
            Text.of("Districts"),
            new ItemStack(ItemRegistry.DISTRICT_DECLARATION), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
            },
            null
        ));

        tabs.add(new Subscreen<TabWindow>(
            Text.of("Citizens"),
            new ItemStack(Items.PLAYER_HEAD), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
            },
            null
        ));

        tabs.add(new Subscreen<TabWindow>(
            Text.of("Petitions and articles"),
            new ItemStack(ItemRegistry.DOCUMENT_PAPER), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
            },
            null
        ));
        
        return tabs;
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(this.petitionSubmit);
        if (this.draft == null)
            this.draft = new Directive<>("Draft", new PlayerAccount(this.client.player), Town.class);
    }

    private void handlePetitionSubmit(ButtonWidget t) {
        this.client.setScreen(new TownDirectiveCreationScreen(this.draft));
    }
}
