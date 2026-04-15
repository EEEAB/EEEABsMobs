package com.eeeab.eeeabsmobs.sever.entity.mob.immortal;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.AnimationRangeAI;
import com.eeeab.animate.server.ai.animation.*;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.AnimationHandler;
import com.eeeab.eeeabsmobs.client.particle.ParticleOrb;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ModLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.CopyOwnerTargetGoal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public abstract class EntityAbsImmortalSkeleton extends EntityAbsImmortal implements VariantHolder<EntityAbsImmortalSkeleton.ClassType> {
    public static final Animation SWINGARM_ANIMATION = Animation.create(15);
    public static final Animation MELEE_ANIMATION1 = Animation.create(15);
    public static final Animation MELEE_ANIMATION2 = Animation.create(15);
    public static final Animation BOW_ANIMATION = Animation.create(30);
    public static final Animation CROSSBOW_CHANGE_ANIMATION = Animation.create(30);
    public static final Animation CROSSBOW_HOLD_ANIMATION = Animation.create(10);
    public static final Animation CAST_ANIMATION = Animation.create(35);
    public static final Animation BLOCK_ANIMATION = Animation.create(10);
    public static final Animation SPAWN_ANIMATION = Animation.create(20);
    public static final Animation PUTUP_ANIMATION = Animation.create(30);
    private static final Animation[] animations = new Animation[]{
            SWINGARM_ANIMATION,
            MELEE_ANIMATION1,
            MELEE_ANIMATION2,
            BOW_ANIMATION,
            CROSSBOW_CHANGE_ANIMATION,
            CROSSBOW_HOLD_ANIMATION,
            CAST_ANIMATION,
            BLOCK_ANIMATION,
            SPAWN_ANIMATION,
            PUTUP_ANIMATION,
    };
    protected int timeUntilBlock;

    public EntityAbsImmortalSkeleton(EntityType<? extends EntityAbsImmortal> type, Level level) {
        super(type, level);
        this.active = true;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.92f;
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.immortals.immortalSkeleton.combatConfig;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, EntityAbsImmortal.class).setAlertOthers());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new ModLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new ModLookAtGoal(this, Mob.class, 6.0F));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, SPAWN_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, SWINGARM_ANIMATION, 8, 2.5F, 1.0F, 1.0F));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, MELEE_ANIMATION1, 8, 2.8F, 1.0F, 1.0F));
        this.goalSelector.addGoal(1, new AnimationAreaMelee<>(this, MELEE_ANIMATION2, 7, 2.8F, 1.0F, 1.0F, 90F, 2.8F, true));
        this.goalSelector.addGoal(1, new AnimationBlock<>(this, BLOCK_ANIMATION));
        this.targetSelector.addGoal(2, new CopyOwnerTargetGoal<>(this));
        this.goalSelector.addGoal(5, new AnimationMeleeAI<>(this, 1.05D, 10 + this.random.nextInt(10), EntityAbsImmortalSkeleton::checkConformCareerWeapon, SWINGARM_ANIMATION));
    }

    @Override
    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
        //if (getAnimation() != NO_ANIMATION) getNavigation().stop();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.attackTick > 0) {
            this.attackTick--;
        }
        if (this.timeUntilBlock > 0) {
            this.timeUntilBlock--;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        boolean hitFlag = true;
        byte pierceLevel = 0;
        float attackArc = 160F;
        if (entity != null) {
            float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(this, entity.position());
            hitFlag = (entityRelativeAngle <= attackArc / 2f && entityRelativeAngle >= -attackArc / 2f) || (entityRelativeAngle >= 360 - attackArc / 2f || entityRelativeAngle <= -attackArc + 90f / 2f);
        }
        if (source.getDirectEntity() instanceof AbstractArrow arrow) pierceLevel = arrow.getPierceLevel();
        if (hitFlag && this.checkHoldItemCanBlock() && entity instanceof LivingEntity livingEntity && !source.is(DamageTypeTags.BYPASSES_ARMOR) && !source.is(DamageTypeTags.BYPASSES_SHIELD) && (this.isNoAnimation() || this.getAnimation() == BLOCK_ANIMATION)) {
            this.blockEntity = livingEntity;
            if (livingEntity.canDisableShield() || damage >= this.getMaxHealth()) {
                this.playSound(SoundEvents.SHIELD_BREAK);
                this.timeUntilBlock = 100;
            } else if (pierceLevel == 0) {
                this.playSound(SoundEvents.SHIELD_BLOCK);
            }
            if (this.getAnimation() != BLOCK_ANIMATION) this.playAnimation(BLOCK_ANIMATION);
            if (pierceLevel > 0) {
                damage *= 0.6F + Math.min(pierceLevel * 0.1F, 0.4F);
                return super.hurt(source, damage);
            }
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 12) {
            this.doEnhanceEffect();
        } else if (id == 13) {
            this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
        } else {
            super.handleEntityEvent(id);
        }
    }

    //使用远程攻击攻击指定的实体
    public void performRangedAttack(LivingEntity target) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof ProjectileWeaponItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, BowItem.getPowerForTime(20));
        if (this.getMainHandItem().getItem() instanceof BowItem)
            abstractarrow = ((BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrow);
        abstractarrow.setBaseDamage(this.getAttributeValue(Attributes.ATTACK_DAMAGE) / 2);
        double x = target.getX() - this.getX();
        double y = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double z = target.getZ() - this.getZ();
        double d0 = Math.sqrt(x * x + z * z) * (double) 0.2F;
        abstractarrow.shoot(x, y + d0, z, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack arrowStack, float distanceFactor) {
        return ProjectileUtil.getMobArrow(this, arrowStack, distanceFactor);
    }

    @Nullable
    @Override
    //在初始生成时调用
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        RandomSource inRandom = worldIn.getRandom();
        this.populateDefaultEquipmentSlots(inRandom, difficultyIn);
        this.populateDefaultEquipmentEnchantments(inRandom, difficultyIn);
        return spawnDataIn;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyIn) {
        ClassType classType = this.getVariant();
        this.setItemSlot(EquipmentSlot.MAINHAND, classType.holdItems[randomSource.nextInt(classType.holdItems.length)].getDefaultInstance());
    }

    protected abstract int getClassId();

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundInit.IMMORTAL_SKELETON_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.IMMORTAL_SKELETON_DEATH.get();
    }

    @Override
    public Animation[] getAnimations() {
        return animations;
    }

    @Override
    public Animation getSpawnAnimation() {
        return SPAWN_ANIMATION;
    }

    @Override
    public @NotNull EntityAbsImmortalSkeleton.ClassType getVariant() {
        return ClassType.byId(getClassId());
    }

    @Override
    public void setVariant(ClassType classType) {
    }

    public static AttributeSupplier setAttributes(float addHealth, float addAttack, float addArmor, float addSpeed) {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 25.0D + addHealth).
                add(Attributes.MOVEMENT_SPEED, 0.28D + addSpeed).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.ATTACK_DAMAGE, 1.0D + addAttack).
                add(Attributes.ATTACK_KNOCKBACK, 0D).
                add(Attributes.ARMOR, 0D + addArmor).
                add(Attributes.KNOCKBACK_RESISTANCE, 0.5D).build();
    }

    protected void doEnhanceEffect() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 9; i++) {
                this.level().addParticle(new ParticleOrb.OrbData(1F, 0.78F, 0F, 2F, 30), this.getRandomX(1.5), this.getY(this.random.nextDouble() * 0.2), this.getRandomZ(1.5), 0, 0.2 + this.random.nextDouble() * 0.05, 0);
            }
        }
    }

    public boolean checkHoldItemCanBlock() {
        if (this.getOffhandItem().getItem() instanceof ShieldItem || this.getMainHandItem().getItem() instanceof ShieldItem) {
            return this.timeUntilBlock <= 0;
        }
        return false;
    }

    public boolean checkHoldItemIsCareerWeapon(ClassType... classType) {
        for (ClassType type : classType) {
            if (this.getVariant() == type) {
                for (Item holdItem : type.holdItems) {
                    if (this.getMainHandItem().is(holdItem)) {
                        return this.active;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkConformCareerWeapon() {
        return this.getVariant() == ClassType.NONE || !this.checkHoldItemIsCareerWeapon(this.getVariant());
    }

    protected <T extends EntityAbsImmortalSkeleton & RangedAttackMob> void addRangeAI(T entity) {
        this.goalSelector.addGoal(1, new CrossBowAI<>(entity));
        this.goalSelector.addGoal(1, new AnimationRange<>(entity, BOW_ANIMATION, 25, null));
        int randomInterval = this.random.nextInt(10);
        Predicate<T> flag = e -> e.active;
        this.goalSelector.addGoal(3, new AnimationRangeAI<>(entity, 1D, 20 + randomInterval, 10F, Items.CROSSBOW, flag, CROSSBOW_CHANGE_ANIMATION));
        this.goalSelector.addGoal(3, new AnimationRangeAI<>(entity, 1D, 25 + randomInterval, 12F, Items.BOW, flag, BOW_ANIMATION));
    }

    static class CrossBowAI<T extends EntityAbsImmortalSkeleton & RangedAttackMob> extends AnimationAI<T> {
        public CrossBowAI(T entity) {
            super(entity);
        }

        @Override
        protected boolean test(Animation animation) {
            return animation == CROSSBOW_CHANGE_ANIMATION || animation == CROSSBOW_HOLD_ANIMATION;
        }

        @Override
        public void start() {
            super.start();
            this.entity.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.entity, item -> item instanceof CrossbowItem));
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.stopUsingItem();
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = this.entity.getTarget();
            int tick = this.entity.getAnimationTick();
            Animation animation = this.entity.getAnimation();
            if (target != null) {
                this.entity.getLookControl().setLookAt(target, 30F, 30F);
                if (CROSSBOW_CHANGE_ANIMATION == animation) {
                    boolean isLoaded = this.entity.isHolding(is -> is.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(is));
                    if (tick == animation.getDuration() - 1 || isLoaded) {
                        if (!isLoaded && this.entity.isHolding(is -> is.getItem() instanceof CrossbowItem)) {
                            CrossbowItem.setCharged(this.entity.getUseItem(), true);
                        }
                        this.entity.playAnimation(CROSSBOW_HOLD_ANIMATION);
                    }
                } else if (CROSSBOW_HOLD_ANIMATION == animation) {
                    if (tick == 9) {
                        this.entity.performRangedAttack(target, 0F);
                        if (this.entity.isHolding(is -> is.getItem() instanceof CrossbowItem)) {
                            CrossbowItem.setCharged(this.entity.getUseItem(), false);
                        }
                    }
                }
            }
        }
    }

    public enum ClassType implements StringRepresentable {
        NONE(-1, "none", 1.15F, Items.AIR, ItemInit.IMMORTAL_BONE.get()),
        MAGE(0, "mage", 1.15F, Items.AIR),
        ARCHER(1, "archer", 1.2F, Items.BOW, Items.CROSSBOW),
        WARRIOR(2, "warrior", 1.2F, Items.STONE_SWORD),
        KNIGHT(3, "knight", 1.25F, ItemInit.IMMORTAL_AXE.get(), ItemInit.IMMORTAL_SWORD.get(), Items.BOW, Items.CROSSBOW);

        private static final IntFunction<ClassType> BY_ID = ByIdMap.sparse(c -> c.id, values(), NONE);
        public final int id;
        public final String name;
        public final float scale;
        public final Item[] holdItems;

        ClassType(int id, String name, float scale, Item... holdItems) {
            this.id = id;
            this.name = name;
            this.scale = scale;
            this.holdItems = holdItems;
        }

        public static ClassType byId(int id) {
            return BY_ID.apply(id);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
