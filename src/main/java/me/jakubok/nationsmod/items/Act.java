package me.jakubok.nationsmod.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class Act extends Item {
    public Act() {
        super(
                new FabricItemSettings()
                .maxCount(1)
        );
    }
}
