package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.effect.AggressivenessEffect;
import me.jakubok.nationsmod.effect.FriendlinessEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StatusEffectRegistry {
    
    public static final StatusEffect AGGRESSIVENESS = new AggressivenessEffect();
    public static final StatusEffect FRIENDLINESS = new FriendlinessEffect();

    static {
        Registry.register(Registry.STATUS_EFFECT, new Identifier(NationsMod.MOD_ID, "aggressiveness"), AGGRESSIVENESS);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(NationsMod.MOD_ID, "friendliness"), FRIENDLINESS);
    }
    
    public static void init() {}
}
