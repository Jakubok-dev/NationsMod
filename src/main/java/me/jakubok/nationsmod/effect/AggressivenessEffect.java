package me.jakubok.nationsmod.effect;

import me.jakubok.nationsmod.entity.human.HumanEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class AggressivenessEffect extends StatusEffect {
    // 1 level: person functions normally
    // 2 level: person attacks people from outside of the state
    // 3 level: person attacks people from outside of the town
    // 4 level: person attacks people who aren't their relatives
    // 5 level: person attacks everyone

    public AggressivenessEffect() {
        super(StatusEffectCategory.HARMFUL, 0x880044);
    }
    
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity instanceof HumanEntity) {
            ((HumanEntity)entity).getHumanData().aggressiveness += amplifier + 1;
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof HumanEntity) {
            ((HumanEntity)entity).getHumanData().aggressiveness -= amplifier + 1;
        }
    }
}
