package me.jakubok.nationsmod.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class ActAndQuill extends Item {
    public ActAndQuill() {
        super(
            new FabricItemSettings()
                    .maxCount(1)
        );
    }
}
