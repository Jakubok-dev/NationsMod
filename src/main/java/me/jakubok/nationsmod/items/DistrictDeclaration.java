package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.NationsMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class DistrictDeclaration extends Item implements Declaration {

    public DistrictDeclaration() {
        super(
            new FabricItemSettings()
            .group(NationsMod.ITEM_GROUP)
            .rarity(Rarity.COMMON)
        );
    }

    @Override
    public String type() {
        return "district";
    }
    
}
