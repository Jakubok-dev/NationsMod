package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.NationsMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class Constitution extends Item {

    public Constitution() {
        super(
            new FabricItemSettings()
            .rarity(Rarity.UNCOMMON)
            .group(NationsMod.ITEM_GROUP)
        );
    }
    
}
