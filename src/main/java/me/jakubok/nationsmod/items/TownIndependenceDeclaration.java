package me.jakubok.nationsmod.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class TownIndependenceDeclaration extends Item implements Declaration {

    public TownIndependenceDeclaration() {
        super(
            new FabricItemSettings()
            .rarity(Rarity.RARE)
        );
    }

    @Override
    public String type() {
        return "town_independence";
    }
    
}
