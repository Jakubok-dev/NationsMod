package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.NationsMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class DocumentPaper extends Item {

    public DocumentPaper() {
        super(
            new FabricItemSettings()
            .group(NationsMod.ITEM_GROUP)
        );
    }
    
}
