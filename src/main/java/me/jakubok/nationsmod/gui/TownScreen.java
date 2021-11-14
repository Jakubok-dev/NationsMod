package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.gui.tabs.TabOption;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class TownScreen extends TabWindow {

    public TownScreen(Town town) {
        super(
            Text.of(town.getName()),
            new TabOption[] {
                new TabOption(
                    Text.of("General"), 
                    new ItemStack(ItemRegistry.CONSTITUTION), 
                    null, 
                    null, 
                    null
                ),
            }
        );
    }
}
