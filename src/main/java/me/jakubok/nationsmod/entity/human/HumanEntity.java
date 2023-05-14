package me.jakubok.nationsmod.entity.human;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collection.Name;
import me.jakubok.nationsmod.entity.goal.HumanAttackGoal;
import me.jakubok.nationsmod.entity.goal.HumanMeetGoal;
import me.jakubok.nationsmod.registries.ModsTrackedDataHandlerRegistry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.Entity;
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
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;

public class HumanEntity extends PathAwareEntity implements Angerable {

    //protected static final TrackedData<Byte> HUMAN_MODEL_PARTS = DataTracker.registerData(HumanEntity.class, TrackedDataHandlerRegistry.BYTE);

    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    private int angerTime = 0;
    private UUID angryAt = null; 

    public static final TrackedData<Boolean> IS_A_FEMALE = DataTracker.registerData(HumanEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Name> NAME = DataTracker.registerData(HumanEntity.class, Name.NAME_HANDLER);
    public static final TrackedData<Integer> AGGRESSIVENESS = DataTracker.registerData(HumanEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Optional<UUID>> CITIZENSHIP = DataTracker.registerData(HumanEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    public static final TrackedData<HumanInventory> INVENTORY = DataTracker.registerData(HumanEntity.class, HumanInventory.HUMAN_INVENTORY_DATA_HANDLER);
    public static final TrackedData<List<UUID>> RELATIVES = DataTracker.registerData(HumanEntity.class, ModsTrackedDataHandlerRegistry.LIST_OF_UUID_HANDLER);

    public HumanEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
        if (!world.isClient()) {
            this.setTheGender(Math.floorMod(this.uuid.hashCode(), 2) > 0);
            this.setTheName(new Name(this.isAFemale(), this.uuid));
        }
    }
    
    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(2, new LookAroundGoal(this));
        this.goalSelector.add(2, new WanderAroundGoal(this, 2f));
        this.goalSelector.add(2, new HumanMeetGoal(this));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new HumanAttackGoal(this, 3f, false));
        this.targetSelector.add(1, new ActiveTargetGoal<LivingEntity>(this, LivingEntity.class, false, this::shouldAngerAt));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IS_A_FEMALE, false);
        this.dataTracker.startTracking(NAME, new Name(this.isAFemale()));
        this.dataTracker.startTracking(AGGRESSIVENESS, -2);
        this.dataTracker.startTracking(INVENTORY, new HumanInventory(27));
        this.dataTracker.startTracking(CITIZENSHIP, Optional.empty());
        this.dataTracker.startTracking(RELATIVES, new ArrayList<UUID>());
    }

    public Boolean isAFemale() {
        return this.dataTracker.get(IS_A_FEMALE);
    }
    public void setTheGender(Boolean isAFemale) {
        this.dataTracker.set(IS_A_FEMALE, isAFemale);
    }

    public Name getTheNameObject() {
        return this.dataTracker.get(NAME);
    }
    public void setTheName(Name name) {
        this.dataTracker.set(NAME, name);
    }

    public UUID getTheCitizenship() {
        return this.dataTracker.get(CITIZENSHIP).get();
    }
    public boolean hasACitizenship() {
        return this.dataTracker.get(CITIZENSHIP).isPresent();
    }
    public boolean setTheCitizenship(UUID citizenship, MinecraftServer server) {
        if (Town.fromUUID(citizenship, server) == null)
            return false;
        this.dataTracker.set(CITIZENSHIP, Optional.of(citizenship));
        return true;
    }
    private void setTheCitizenship(UUID citizenship) {
        this.dataTracker.set(CITIZENSHIP, Optional.of(citizenship));
    }

    public int getTheAggressiveness() {
        return this.dataTracker.get(AGGRESSIVENESS);
    }
    public void setTheAggressiveness(int value) {
        this.dataTracker.set(AGGRESSIVENESS, value);
    }

    public HumanInventory getTheInventory() {
        return this.dataTracker.get(INVENTORY);
    }
    public void setTheInventory(HumanInventory inv) {
        this.dataTracker.set(INVENTORY, inv);
    }

    public List<UUID> getTheRelatives() {
        return this.dataTracker.get(RELATIVES);
    }
    public void setTheRelatives(List<UUID> relatives) {
        this.dataTracker.set(RELATIVES, relatives);
    }

    @Override
    public Text getName() {
        return this.getTheNameObject().getText();
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
        this.produceParticles(ParticleTypes.ANGRY_VILLAGER);
        if (source.getAttacker() instanceof LivingEntity) {
            this.setAngryAt(source.getAttacker().getUuid());
            this.chooseRandomAngerTime();

            if (!this.world.isClient) {
                ServerWorld world = this.getServer().getWorld(this.world.getRegistryKey());
                List<? extends HumanEntity> witnesses = world.getEntitiesByType(TypeFilter.instanceOf(HumanEntity.class), (HumanEntity human) -> {
                    if (this.getDistance(this.getBlockPos(), human.getBlockPos()) > 64)
                        return false;
                    switch (this.getTheAggressiveness()) {
                        case 0:
                        case 1:
                            return true;
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            return false;
                        default:
                            return true;
                    }
                });

                witnesses.forEach(el -> {
                    if (el instanceof HumanEntity)
                        ((HumanEntity)el).answerHelpRequest((LivingEntity)source.getAttacker(), this);
                });
            }
        }
        return super.damage(source, amount);
    }

    protected double getDistance(BlockPos first, BlockPos second) {
        return Math.sqrt(
            Math.pow(Math.abs((double)(second.getX() - first.getX())), 2) +
            Math.pow(Math.abs((double)(second.getY() - first.getY())), 2) +
            Math.pow(Math.abs((double)(second.getZ() - first.getZ())), 2)
        );
    }

    public void answerHelpRequest(LivingEntity attacker, HumanEntity requester) {
        switch (this.getTheAggressiveness()) {
            case -5:
                this.setAngryAt(attacker.getUuid());
                this.chooseRandomAngerTime();
                return;
            case -2:
                if (this.isARelative(requester)) {
                    this.setAngryAt(attacker.getUuid());
                    this.chooseRandomAngerTime();
                }
                return;
            default:
                return;
        }
    }

    public boolean isARelative(HumanEntity entity) {
        return this.getTheRelatives().stream().filter(el -> el.equals(entity.getUuid())).findAny().isPresent();
    }

    @Override
    public void animateDamage(float jaw) {
        this.hurtTime = this.maxHurtTime = 10;
    }

    @Override
    protected void loot(ItemEntity item) {
        super.loot(item);
        this.hideItemToTheInventory();
    }

    @Override
    public void dropInventory() {
        HumanInventory inv = this.getTheInventory();
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
        HumanInventory inv = this.getTheInventory();
        ItemStack stack2 = inv.getStack(slot);
        inv.setStack(slot, stack);
        this.setTheInventory(inv);
        this.equipStack(EquipmentSlot.MAINHAND, stack2);
    }

    public void addItemToTheInventory(ItemStack stack) {
        HumanInventory inv = this.getTheInventory();
        if (inv.canInsert(stack)) {
            inv.addStack(stack);
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.setTheInventory(inv);
        }
    }

    public boolean equipTheBestSword() {
        HumanInventory inv = this.getTheInventory();
        if (inv.swords.isEmpty())
            return false;
        HumanInventory.Entry entry = inv.swords.peek();
        this.equipItemFromTheInventory(entry.getValue());
        return true;
    }
    public boolean equipTheBestAxe() {
        HumanInventory inv = this.getTheInventory();
        if (inv.axes.isEmpty())
            return false;
        HumanInventory.Entry entry = inv.axes.peek();
        this.equipItemFromTheInventory(entry.getValue());
        return true;
    }
    public boolean equipTheBestShovel() {
        HumanInventory inv = this.getTheInventory();
        if (inv.shovels.isEmpty())
            return false;
        HumanInventory.Entry entry = inv.shovels.peek();
        this.equipItemFromTheInventory(entry.getValue());
        return true;
    }
    public boolean equipTheBestHoe() {
        HumanInventory inv = this.getTheInventory();
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

        switch(this.getTheAggressiveness()) {
            case 0:
            case 1:
                return false;
            case 2:
            case 3:
            case 4:
                if (entity instanceof PlayerEntity || entity instanceof HumanEntity)
                    return true;
                return false;
            case 5:
                return true;
            default:
                return false;
        }
        
    }

    protected void produceParticles(ParticleEffect parameters) {
        for (int i = 0; i < 5; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.world.addParticle(parameters, this.getParticleX(1.0), this.getRandomBodyY() + 1.0, this.getParticleZ(1.0), d, e, f);
        }
    }

    public boolean canHangOut(HumanEntity other) {
        if (other == this)
            return false;
        if (this.getTheAggressiveness() >= 2)
            return false;
        Random rng = new Random();
        double chance = 1d/(double)(1 + this.getTheRelatives().size());
        if (rng.nextDouble(1d) > chance)
            return false;
        
        if (this.getDistance(this.getBlockPos(), other.getBlockPos()) > 64)
            return false;

        List<UUID> relatives = this.getTheRelatives();
        for (int i = 0; i < relatives.size(); i++) {
            if (relatives.get(i).equals(other.getUuid()))
                return false;
        }

        return true;
    }

    public void hangOut(HumanEntity entity) {
        List<UUID> relatives = this.getTheRelatives();
        relatives.add(entity.getUuid());
        this.setTheRelatives(relatives);
        this.produceParticles(ParticleTypes.HAPPY_VILLAGER);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 4) {
            this.produceParticles(ParticleTypes.HAPPY_VILLAGER);
        }
        super.handleStatus(status);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.world.isClient) {
            this.getTheRelatives().forEach(el -> {
                try {
                    HumanEntity human = ((HumanEntity)searchWorldsForAnEntity(el));
                    List<UUID> relatives = human.getTheRelatives();
                    relatives.remove(this.getUuid());
                    human.setTheRelatives(relatives);
                } catch (Exception ex) {}
            });
        }
    }

    public Entity searchWorldsForAnEntity(UUID id) {
        if (this.world.isClient)
            return null;
        AtomicReference<Entity> result = new AtomicReference<>();
        this.world.getServer().getWorlds().forEach(el -> {
            if (result.get() != null)
                return;
            result.set(el.getEntity(id));
        });
        return result.get();
    }

    public boolean isPartVisible(PlayerModelPart modelPart) {
        //return (this.getDataTracker().get(HUMAN_MODEL_PARTS) & modelPart.getBitFlag()) == modelPart.getBitFlag();
        return true;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setTheName(new Name(nbt.getCompound(NationsMod.MOD_ID + "_name")));
        this.setTheGender(nbt.getBoolean(NationsMod.MOD_ID + "_is_a_female"));
        this.setTheAggressiveness(nbt.getInt(NationsMod.MOD_ID + "_aggressiveness"));
        try {
            this.setTheCitizenship(nbt.getUuid(NationsMod.MOD_ID + "_citizenship"));
        } catch (Exception ex) {}
        HumanInventory inv = new HumanInventory(27);
        inv.readFromNbt(nbt.getCompound(NationsMod.MOD_ID + "_inventory"));
        this.setTheInventory(inv);

        NbtCompound relativesCompound = nbt.getCompound(NationsMod.MOD_ID + "_relatives");
        List<UUID> relatives = new ArrayList<>();
        for (int i = 0; i < relativesCompound.getInt("size"); i++)
            relatives.add(relativesCompound.getUuid("uuid" + i));
        this.setTheRelatives(relatives);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put(NationsMod.MOD_ID + "_name", this.getTheNameObject().writeToNbtAndReturn(new NbtCompound()));
        nbt.putBoolean(NationsMod.MOD_ID + "_is_a_female", this.isAFemale());
        nbt.putInt(NationsMod.MOD_ID + "_aggressiveness", this.getTheAggressiveness());
        try {
            nbt.putUuid(NationsMod.MOD_ID + "_citizenship", this.getTheCitizenship());
        } catch(Exception ex) {}
        nbt.put(NationsMod.MOD_ID + "_inventory", this.getTheInventory().writeToNbtAndReturn(new NbtCompound()));

        NbtCompound relativesCompound = new NbtCompound();
        List<UUID> relatives = this.getTheRelatives();
        relativesCompound.putInt("size", relatives.size());
        for (int i = 0; i < relatives.size(); i++)
            relativesCompound.putUuid("uuid" + i, relatives.get(i));
        nbt.put(NationsMod.MOD_ID + "_relatives", relativesCompound);
    }

    public static Builder getHumanAttributes() {
        return createMobAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f).add(EntityAttributes.GENERIC_ATTACK_SPEED).add(EntityAttributes.GENERIC_LUCK);
    }
}
