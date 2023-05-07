package me.jakubok.nationsmod.block;

import me.jakubok.nationsmod.registries.BlockRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;

public class BorderSignBlockItem extends BlockItem {

    public BorderSignBlockItem() {
        super(BlockRegistry.BORDER_SIGN, new FabricItemSettings());
    }
    
}
