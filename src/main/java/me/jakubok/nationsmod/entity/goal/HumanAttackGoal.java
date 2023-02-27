package me.jakubok.nationsmod.entity.goal;

import me.jakubok.nationsmod.entity.HumanEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

public class HumanAttackGoal extends MeleeAttackGoal {

    public HumanAttackGoal(HumanEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }
    
    @Override
    public void start() {
        super.start();
        mob.setAttacking(true);
        if (mob.world.isClient)
            mob.swingHand(Hand.MAIN_HAND, false);
        else mob.swingHand(Hand.MAIN_HAND, true);
    }

    @Override
    public void stop() {
        super.stop();
        mob.setAttacking(false);
    }
}
