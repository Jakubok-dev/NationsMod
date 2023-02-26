package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.items.BorderRegistrator;
import me.jakubok.nationsmod.items.Constitution;
import me.jakubok.nationsmod.items.DistrictDeclaration;
import me.jakubok.nationsmod.items.Parchment;
import me.jakubok.nationsmod.items.NationIndependenceDeclaration;
import me.jakubok.nationsmod.items.TownIndependenceDeclaration;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static final Constitution CONSTITUTION = new Constitution();

    public static final TownIndependenceDeclaration TOWN_INDEPENDENCE_DECLARATION = new TownIndependenceDeclaration();
    public static final NationIndependenceDeclaration NATION_INDEPENDENCE_DECLARATION = new NationIndependenceDeclaration();
    public static final DistrictDeclaration DISTRICT_DECLARATION = new DistrictDeclaration();

    public static final BorderRegistrator BORDER_REGISTRATOR = new BorderRegistrator();

    public static final Parchment DOCUMENT_PAPER = new Parchment();

    public static final Item HUMAN_SPAWN_EGG = new SpawnEggItem(EntityRegistry.HUMAN, 0x00AFAF, 0xC69680, new FabricItemSettings().group(NationsMod.ITEM_GROUP));

    static {
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "constitution"), CONSTITUTION);
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "parchment"), DOCUMENT_PAPER);
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "district_declaration"), DISTRICT_DECLARATION);
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "town_independence_declaration"), TOWN_INDEPENDENCE_DECLARATION);
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "nation_independence_declaration"), NATION_INDEPENDENCE_DECLARATION);
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "border_registrator"), BORDER_REGISTRATOR);
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "human_spawn_egg"), HUMAN_SPAWN_EGG);
    }

    public static void init() { }
}
