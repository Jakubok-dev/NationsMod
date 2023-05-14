package me.jakubok.nationsmod.entity.goal;

import java.util.Optional;
import java.util.Random;

import me.jakubok.nationsmod.entity.human.HumanEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;

public class HumanMeetGoal extends Goal {

    public final HumanEntity instance;
    public HumanEntity target;
    private int timer = 0, particleCooldown = 0, requestTimer = 0;
    public HumanMeetGoal(HumanEntity human) {
        this.instance = human;
    }

    @Override
    public boolean canStart() {
        if (instance.getTheAggressiveness() >= 2)
            return false;
        if (requestTimer > 0) {
            requestTimer--;
            return false;
        }
        requestTimer = 600;
        double chance = 1d / (double)(1 + instance.getTheRelatives().size());
        Random rng = new Random();
        if (rng.nextDouble(1) > chance)
            return false;

        Optional<HumanEntity> target = this.instance.world.getEntitiesByType(TypeFilter.instanceOf(HumanEntity.class), new Box(this.instance.getX() - 32, this.instance.getY() - 32, this.instance.getZ() - 32, this.instance.getX() + 32, this.instance.getY() + 32, this.instance.getZ() + 32), (HumanEntity human) -> {
            return human.canHangOut(this.instance);
        }).stream().findAny();
        if (target.isPresent())
            this.target = target.get();
        return target.isPresent();
    }
    
    @Override
    public boolean shouldContinue() {
        return this.target.isAlive() && this.timer <= 120;
    }

    @Override
    public void stop() {
        this.target = null;
        this.timer = 0;
        this.particleCooldown = 0;
    }

    @Override
    public void tick() {
        if (this.particleCooldown == 0) {
            this.instance.getWorld().sendEntityStatus(instance, (byte)4);
            this.target.getWorld().sendEntityStatus(target, (byte)4);
            this.particleCooldown = 20;
        }
        this.instance.getLookControl().lookAt(this.target, 10.0f, this.instance.getMaxLookPitchChange());
        this.instance.getNavigation().startMovingTo(this.target, 2f);
        ++this.timer;
        this.particleCooldown--;
        if (this.timer >= this.getTickCount(120) && this.instance.squaredDistanceTo(this.target) < 9.0) {
            this.instance.hangOut(this.target);
            this.timer = 130;
        }
    }
}
