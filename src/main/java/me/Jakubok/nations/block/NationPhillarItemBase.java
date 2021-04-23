package me.Jakubok.nations.block;

import me.Jakubok.nations.Nations;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class NationPhillarItemBase extends BlockItem {
    public NationPhillarItemBase(Block block) {
        super(block, new FabricItemSettings().group(Nations.nations_tab));
    }
}
