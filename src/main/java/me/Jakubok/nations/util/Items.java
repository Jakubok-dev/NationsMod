package me.Jakubok.nations.util;

import me.Jakubok.nations.Nations;
import me.Jakubok.nations.block.NationPillarItemBase;
import me.Jakubok.nations.item.ConstitutionBase;
import me.Jakubok.nations.item.HeartOfNationBase;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {

    public static final Item CONSTITUTION = new ConstitutionBase();
    public static final Item HEART_OF_NATION = new HeartOfNationBase();

    // BlockItems
    public static final BlockItem NATION_PILLAR_ITEM = new NationPillarItemBase(Blocks.NATION_PILLAR);

    public static void init() {
        Registry.register(Registry.ITEM, new Identifier(Nations.MOD_ID, "constitution"), CONSTITUTION);
        Registry.register(Registry.ITEM, new Identifier(Nations.MOD_ID, "nation_pillar"), NATION_PILLAR_ITEM);
        Registry.register(Registry.ITEM, new Identifier(Nations.MOD_ID, "heart_of_nation"), HEART_OF_NATION);
    }
}
