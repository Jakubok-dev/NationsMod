package me.Jakubok.nations.item;

import me.Jakubok.nations.Nations;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class ConstitutionBase extends Item {
    public ConstitutionBase() {
        super(new FabricItemSettings()
        .group(Nations.nations_tab));
    }
}
