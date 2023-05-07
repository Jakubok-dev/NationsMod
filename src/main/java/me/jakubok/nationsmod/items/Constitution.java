package me.jakubok.nationsmod.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class Constitution extends Item {

    public Constitution() {
        super(
            new FabricItemSettings()
            .rarity(Rarity.UNCOMMON)
        );
    }
    
}
