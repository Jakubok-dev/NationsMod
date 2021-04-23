package me.Jakubok.nations.util;

import me.Jakubok.nations.Nations;
import me.Jakubok.nations.block.NationPhillarItemBase;
import me.Jakubok.nations.item.ConstitutionBase;
import me.Jakubok.nations.item.HealthOfNationBase;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {

    public static final Item CONSTITUTION = new ConstitutionBase();
    public static final Item HEALTH_OF_NATION = new HealthOfNationBase();

    // BlockItems
    public static final BlockItem NATION_PHILLAR_ITEM = new NationPhillarItemBase(Blocks.NATION_PHILLAR);

    public static void init() {
        Registry.register(Registry.ITEM, new Identifier(Nations.MOD_ID, "constitution"), CONSTITUTION);
        Registry.register(Registry.ITEM, new Identifier(Nations.MOD_ID, "nation_phillar"), NATION_PHILLAR_ITEM);
        Registry.register(Registry.ITEM, new Identifier(Nations.MOD_ID, "health_of_nation"), HEALTH_OF_NATION);
    }
}
