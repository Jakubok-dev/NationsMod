package me.jakubok.nationsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jakubok.nationsmod.entity.human.HumanEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin extends PathAwareEntity {
    protected HostileEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    private boolean _nationsMod_Init = true;

    @Inject(at = @At("RETURN"), method = "tickMovement")
    private void angerAtHumans(CallbackInfo info) {
        if (_nationsMod_Init) {
            _nationsMod_Init = false;
            this.targetSelector.add(3, new ActiveTargetGoal<HumanEntity>(this, HumanEntity.class, true));
        }
    }
}
