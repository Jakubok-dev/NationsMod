package me.jakubok.nationsmod.gui.townScreen;

import java.util.ArrayList;
import java.util.List;

import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.gui.TabWindow;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class TownScreen extends TabWindow {

    protected final Town town;
    public final Constitution constitution;

    public TownScreen(Town town) {
        super(Text.of(town.getName()));
        this.town = town;
        constitution = new Constitution(this);
    }

    @Override
    protected List<Subscreen<TabWindow>> getTabs() {
        
        List<Subscreen<TabWindow>> tabs = new ArrayList<>();
        tabs.add(constitution.subscreen);

        tabs.add(new Subscreen<TabWindow>(
            Text.of("Districts"),
            new ItemStack(ItemRegistry.TOWN_INDEPENDENCE_DECLARATION), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
            },
            null, 
            null
        ));

        tabs.add(new Subscreen<TabWindow>(
            Text.of("Citizens"),
            new ItemStack(Items.PLAYER_HEAD), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
            },
            null, 
            null
        ));
        
        return tabs;
    }
}
