package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.administration.Town;
import net.minecraft.text.Text;

public class TownScreen extends SimpleWindow {

    public TownScreen(Town town) {
        super(Text.of(town.getName()));
    }

    
    
}
