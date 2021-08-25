package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.block.BorderSign;
import me.jakubok.nationsmod.block.BorderSignBlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {

    public static final BorderSign BORDER_SIGN = new BorderSign();

    static {
        Registry.register(Registry.BLOCK, new Identifier(NationsMod.MOD_ID, "border_sign"), BORDER_SIGN);
        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "border_sign"), new BorderSignBlockItem());
    }

    public static void init() {}
}
