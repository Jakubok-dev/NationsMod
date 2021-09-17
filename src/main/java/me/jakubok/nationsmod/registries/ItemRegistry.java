package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.items.BorderRegistrator;
import me.jakubok.nationsmod.items.Constitution;
import me.jakubok.nationsmod.items.NationIndependenceDeclaration;
import me.jakubok.nationsmod.items.TownIndependenceDeclaration;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    public static final Constitution CONSTITUTION = new Constitution();

    public static final TownIndependenceDeclaration TOWN_INDEPENDENCE_DECLARATION = new TownIndependenceDeclaration();
    public static final NationIndependenceDeclaration NATION_INDEPENDENCE_DECLARATION = new NationIndependenceDeclaration();

    public static final BorderRegistrator BORDER_REGISTRATOR = new BorderRegistrator();

    static {
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "constitution"), CONSTITUTION);

        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "town_independence_declaration"), TOWN_INDEPENDENCE_DECLARATION);
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "nation_independence_declaration"), NATION_INDEPENDENCE_DECLARATION);
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "border_registrator"), BORDER_REGISTRATOR);
    }

    public static void init() { }
}
