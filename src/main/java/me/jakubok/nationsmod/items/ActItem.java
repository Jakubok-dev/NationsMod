package me.jakubok.nationsmod.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class ActItem extends Item {
    public ActItem() {
        super(
                new FabricItemSettings()
                .maxCount(1)
        );
    }
}
