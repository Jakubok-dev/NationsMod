package me.jakubok.nationsmod.effect;

import me.jakubok.nationsmod.entity.human.HumanEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class FriendlinessEffect extends StatusEffect {
    // 1 level: person functions normally
    // 2 level: person helps people who are their relatives
    // 3 level: person helps people from the town
    // 4 level: person helps people from the nation
    // 5 level: person helps everyone

    public FriendlinessEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xDCC600);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity instanceof HumanEntity) {
            int aggressiveness = ((HumanEntity)entity).getTheAggressiveness();
            aggressiveness -= amplifier + 1;
            ((HumanEntity)entity).setTheAggressiveness(aggressiveness);
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof HumanEntity) {
            int aggressiveness = ((HumanEntity)entity).getTheAggressiveness();
            aggressiveness += amplifier + 1;
            ((HumanEntity)entity).setTheAggressiveness(aggressiveness);
        }
    }
}
