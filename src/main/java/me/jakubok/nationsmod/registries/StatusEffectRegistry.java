package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.effect.XenophobiaEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StatusEffectRegistry {
    
    public static final StatusEffect XENOPHOBIA = new XenophobiaEffect();

    static {
        Registry.register(Registry.STATUS_EFFECT, new Identifier(NationsMod.MOD_ID, "xenophobia"), XENOPHOBIA);
    }
    
    public static void init() {}
}
