package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.block.BorderSign;
import me.jakubok.nationsmod.block.BorderSignEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockRegistry {

    public static final BorderSign BORDER_SIGN = new BorderSign();
    public static final BlockEntityType<BorderSignEntity> BORDER_SIGN_ENTITY_TYPE;

    static {
        Registry.register(Registries.BLOCK, new Identifier(NationsMod.MOD_ID, "border_sign"), BORDER_SIGN);

        BORDER_SIGN_ENTITY_TYPE =  Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(NationsMod.MOD_ID, "border_sign_entity"), FabricBlockEntityTypeBuilder.create(BorderSignEntity::new, BORDER_SIGN).build(null));
    }

    public static void init() {}
}
