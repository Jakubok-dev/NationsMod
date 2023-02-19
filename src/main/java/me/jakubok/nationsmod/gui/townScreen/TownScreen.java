package me.jakubok.nationsmod.gui.townScreen;

import java.util.ArrayList;
import java.util.List;

import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class TownScreen extends TabWindow {

    protected final Town town;
    public final GeneralInfoSubscreen generalInfo;
    public final PetitionsAndDirectivesSubscreen petitionsAndDirectives;
    public final ButtonWidget petitionSubmit;

    public TownScreen(Town town, Screen previousScreen) {
        super(Text.of(town.getName()), previousScreen);
        this.town = town;
        this.generalInfo = new GeneralInfoSubscreen(this);
        this.petitionsAndDirectives = new PetitionsAndDirectivesSubscreen(this);

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
        tabs.add(this.generalInfo.subscreen);

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

        tabs.add(this.petitionsAndDirectives.subscreen);
        
        return tabs;
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(this.petitionSubmit);
    }

    private void handlePetitionSubmit(ButtonWidget t) {
    }
}
