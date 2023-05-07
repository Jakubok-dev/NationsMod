package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.block.BorderSignBlockItem;
import me.jakubok.nationsmod.items.BorderRegistrator;
import me.jakubok.nationsmod.items.Constitution;
import me.jakubok.nationsmod.items.DistrictDeclaration;
import me.jakubok.nationsmod.items.Parchment;
import me.jakubok.nationsmod.items.NationIndependenceDeclaration;
import me.jakubok.nationsmod.items.TownIndependenceDeclaration;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final ItemGroup ITEM_GROUP;

    public static final Constitution CONSTITUTION = new Constitution();

    public static final TownIndependenceDeclaration TOWN_INDEPENDENCE_DECLARATION = new TownIndependenceDeclaration();
    public static final NationIndependenceDeclaration NATION_INDEPENDENCE_DECLARATION = new NationIndependenceDeclaration();
    public static final DistrictDeclaration DISTRICT_DECLARATION = new DistrictDeclaration();

    public static final BorderRegistrator BORDER_REGISTRATOR = new BorderRegistrator();

    public static final Parchment PARCHMENT = new Parchment();

    public static final Item HUMAN_SPAWN_EGG = new SpawnEggItem(EntityRegistry.HUMAN, 0x00AFAF, 0xC69680, new FabricItemSettings());

    public static final BorderSignBlockItem BORDER_SIGN_BLOCK_ITEM = new BorderSignBlockItem();

    static {
        Registry.register(Registries.ITEM, new Identifier(NationsMod.MOD_ID, "constitution"), CONSTITUTION);
        Registry.register(Registries.ITEM, new Identifier(NationsMod.MOD_ID, "parchment"), PARCHMENT);
        Registry.register(Registries.ITEM, new Identifier(NationsMod.MOD_ID, "district_declaration"), DISTRICT_DECLARATION);
        Registry.register(Registries.ITEM, new Identifier(NationsMod.MOD_ID, "town_independence_declaration"), TOWN_INDEPENDENCE_DECLARATION);
        Registry.register(Registries.ITEM, new Identifier(NationsMod.MOD_ID, "nation_independence_declaration"), NATION_INDEPENDENCE_DECLARATION);
        Registry.register(Registries.ITEM, new Identifier(NationsMod.MOD_ID, "border_registrator"), BORDER_REGISTRATOR);
        Registry.register(Registries.ITEM, new Identifier(NationsMod.MOD_ID, "human_spawn_egg"), HUMAN_SPAWN_EGG);
        Registry.register(Registries.ITEM, new Identifier(NationsMod.MOD_ID, "border_sign"), BORDER_SIGN_BLOCK_ITEM);

        ITEM_GROUP = FabricItemGroup.builder(new Identifier(NationsMod.MOD_ID, "item_group"))
        .icon(() -> new ItemStack(ItemRegistry.CONSTITUTION))
        .entries((context, entries) -> {
            entries.add(CONSTITUTION);
            entries.add(PARCHMENT);
            entries.add(TOWN_INDEPENDENCE_DECLARATION);
            entries.add(NATION_INDEPENDENCE_DECLARATION);
            entries.add(DISTRICT_DECLARATION);
            entries.add(BORDER_REGISTRATOR);
            entries.add(BORDER_SIGN_BLOCK_ITEM);
            entries.add(HUMAN_SPAWN_EGG);
        })
        .build();
    }

    public static void init() { }
}
