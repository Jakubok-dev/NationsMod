package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.block.BorderSign;
import me.jakubok.nationsmod.block.BorderSignBlockItem;
import me.jakubok.nationsmod.block.BorderSignEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {

    public static final BorderSign BORDER_SIGN = new BorderSign();
    public static final BlockEntityType<BorderSignEntity> BORDER_SIGN_ENTITY_TYPE;

    static {
        Registry.register(Registry.BLOCK, new Identifier(NationsMod.MOD_ID, "border_sign"), BORDER_SIGN);

        Registry.register(Registry.ITEM, new Identifier(NationsMod.MOD_ID, "border_sign"), new BorderSignBlockItem());

        BORDER_SIGN_ENTITY_TYPE =  Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(NationsMod.MOD_ID, "border_sign_entity"), FabricBlockEntityTypeBuilder.create(BorderSignEntity::new, BORDER_SIGN).build(null));
    }

    public static void init() {}
}
