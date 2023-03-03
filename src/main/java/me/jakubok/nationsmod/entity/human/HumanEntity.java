package me.jakubok.nationsmod.entity.human;

import java.util.Random;
import java.util.UUID;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.entity.goal.HumanAttackGoal;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;

public class HumanEntity extends PathAwareEntity implements Angerable {

    //protected static final TrackedData<Byte> HUMAN_MODEL_PARTS = DataTracker.registerData(HumanEntity.class, TrackedDataHandlerRegistry.BYTE);

    public Random random = new Random();
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    private int angerTime = 0;
    private UUID angryAt = null; 
    public static final TrackedData<HumanData> HUMAN_DATA = DataTracker.registerData(HumanEntity.class, HumanData.HUMAN_DATA_HANDLER);

    public HumanEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
    }
    
    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(2, new LookAroundGoal(this));
        this.goalSelector.add(2, new WanderAroundGoal(this, 2f));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new HumanAttackGoal(this, 3f, false));
        this.targetSelector.add(1, new ActiveTargetGoal<LivingEntity>(this, LivingEntity.class, false, this::shouldAngerAt));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HUMAN_DATA, new HumanData());
    }

    public HumanData getHumanData() {
        return this.dataTracker.get(HUMAN_DATA);
    }

    public void setHumanData(HumanData data) {
        this.dataTracker.set(HUMAN_DATA, data);
    }

    @Override
    public Text getName() {
        return this.getHumanData().name.getText();
    }

    @Override
    public int getAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setAngerTime(int ticks) {
        this.angerTime = ticks;
    }

    @Override
    public UUID getAngryAt() {
        return this.angryAt;
    }

    @Override
    public void setAngryAt(UUID uuid) {
        this.angryAt = uuid;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof LivingEntity) {
            this.setAngryAt(source.getAttacker().getUuid());
            this.chooseRandomAngerTime();
        }
        return super.damage(source, amount);
    }

    @Override
    public void animateDamage() {
        this.hurtTime = this.maxHurtTime = 10;
        this.knockbackVelocity = 0.0f;
    }

    @Override
    protected void loot(ItemEntity item) {
        super.loot(item);
        this.hideItemToTheInventory();
    }

    @Override
    public void dropInventory() {
        HumanInventory inv = this.getHumanData().inventory;
        for (int i = 0; i < inv.size(); i++) {
            if(inv.getStack(i) != ItemStack.EMPTY)
                this.dropStack(inv.getStack(i));
        }
    }

    public void hideItemToTheInventory() {
        ItemStack stack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        this.addItemToTheInventory(stack);
    }

    public void equipItemFromTheInventory(int slot) {
        ItemStack stack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        HumanInventory inv = this.getHumanData().inventory;
        ItemStack stack2 = inv.getStack(slot);
        inv.setStack(slot, stack);
        this.equipStack(EquipmentSlot.MAINHAND, stack2);
    }

    public void addItemToTheInventory(ItemStack stack) {
        HumanInventory inv = this.getHumanData().inventory;
        if (inv.canInsert(stack)) {
            inv.addStack(stack);
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    public boolean equipTheBestSword() {
        HumanInventory inv = this.getHumanData().inventory;
        if (inv.swords.isEmpty())
            return false;
        HumanInventory.Entry entry = inv.swords.peek();
        this.equipItemFromTheInventory(entry.getValue());
        return true;
    }
    public boolean equipTheBestAxe() {
        HumanInventory inv = this.getHumanData().inventory;
        if (inv.axes.isEmpty())
            return false;
        HumanInventory.Entry entry = inv.axes.peek();
        this.equipItemFromTheInventory(entry.getValue());
        return true;
    }
    public boolean equipTheBestShovel() {
        HumanInventory inv = this.getHumanData().inventory;
        if (inv.shovels.isEmpty())
            return false;
        HumanInventory.Entry entry = inv.shovels.peek();
        this.equipItemFromTheInventory(entry.getValue());
        return true;
    }
    public boolean equipTheBestHoe() {
        HumanInventory inv = this.getHumanData().inventory;
        if (inv.hoes.isEmpty())
            return false;
        HumanInventory.Entry entry = inv.hoes.peek();
        this.equipItemFromTheInventory(entry.getValue());
        return true;
    }

    @Override
    public boolean shouldAngerAt(LivingEntity entity) {
        boolean result = Angerable.super.shouldAngerAt(entity);
        if (result)
            return true;

        switch(this.getHumanData().xenophobia) {
            case 0:
            case 1:
                return false;
            case 2:
            case 3:
                if (entity instanceof PlayerEntity || entity instanceof HumanEntity)
                    return true;
                return false;
            default:
                return true;
        }
        
    }

    public boolean isPartVisible(PlayerModelPart modelPart) {
        //return (this.getDataTracker().get(HUMAN_MODEL_PARTS) & modelPart.getBitFlag()) == modelPart.getBitFlag();
        return true;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setHumanData(new HumanData(nbt.getCompound(NationsMod.MOD_ID + "_humanData")));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put(NationsMod.MOD_ID + "_humanData", this.getHumanData().writeToNbtAndReturn(new NbtCompound()));
    }

    public static Builder getHumanAttributes() {
        return createMobAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f).add(EntityAttributes.GENERIC_ATTACK_SPEED).add(EntityAttributes.GENERIC_LUCK);
    }
}
