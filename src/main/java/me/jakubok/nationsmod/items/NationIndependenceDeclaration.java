package me.jakubok.nationsmod.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class NationIndependenceDeclaration extends Item implements Declaration {
    
    public NationIndependenceDeclaration() {
        super(
            new FabricItemSettings()
            .rarity(Rarity.RARE)
        );
    }

    @Override
    public String type() {
        return "nation_independence";
    }
}
