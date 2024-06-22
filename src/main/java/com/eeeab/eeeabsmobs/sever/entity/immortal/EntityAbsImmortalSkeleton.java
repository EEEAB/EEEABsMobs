package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.AnimationRangeAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationBlock;
import com.eeeab.animate.server.ai.animation.AnimationMelee;
import com.eeeab.animate.server.ai.animation.AnimationRange;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.OwnerCopyTargetGoal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityAbsImmortalSkeleton extends EntityAbsImmortal implements RangedAttackMob {
    public final Animation swingArmAnimation = Animation.create(15);
    public final Animation meleeAnimation = Animation.create(15);
    public final Animation bowAnimation = Animation.create(30);
    public final Animation crossBowChangeAnimation = Animation.create(30);
    public final Animation crossBowHoldAnimation = Animation.create(10);
    public final Animation castAnimation = Animation.create(30);
    public final Animation blockAnimation = Animation.create(10);
    public final Animation spawnAnimation = Animation.create(20);
    public final Animation roarAnimation = Animation.create(45);
    private final Animation[] animations = new Animation[]{
            swingArmAnimation,
            meleeAnimation,
            bowAnimation,
            crossBowChangeAnimation,
            crossBowHoldAnimation,
            castAnimation,
            blockAnimation,
            spawnAnimation,
            roarAnimation
    };
    private static final EntityDataAccessor<Integer> DATA_CAREER_TYPE = SynchedEntityData.defineId(EntityAbsImmortalSkeleton.class, EntityDataSerializers.INT);
    private static final UUID ATTACK_UUID = UUID.fromString("2D305FA6-B041-4300-80F0-D04F95A65BA8");
    private static final UUID HEALTH_UUID = UUID.fromString("6C41A484-299F-4DB7-A8DF-75713D68D2DE");
    private static final UUID ARMOR_UUID = UUID.fromString("CF54F152-37B6-4A40-A7E5-5D0715B98423");
    private static final UUID SPEED_UUID = UUID.fromString("69163DAB-E9E5-7769-C147-31A405D27167");

    public EntityAbsImmortalSkeleton(EntityType<? extends EntityAbsImmortal> type, Level level) {
        super(type, level);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.9F;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 6.0F));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, () -> spawnAnimation));
        this.goalSelector.addGoal(1, new AnimationAI<>(this) {

            @Override
            protected boolean test(Animation animation) {
                return animation == this.entity.crossBowChangeAnimation || animation == this.entity.crossBowHoldAnimation;
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
                    if (animation == this.entity.crossBowChangeAnimation) {
                        if (tick == animation.getDuration() - 1) {
                            this.entity.playAnimation(this.entity.crossBowHoldAnimation);
                        }
                    } else if (animation == this.entity.crossBowHoldAnimation) {
                        if (tick == 9) {
                            this.entity.performRangedAttack(target, 0F);
                        }
                    }
                }
            }
        });
        this.goalSelector.addGoal(1, new AnimationAI<>(this, true, true) {

            @Override
            protected boolean test(Animation animation) {
                return animation == this.entity.castAnimation;
            }

            @Override
            public void tick() {
                int tick = this.entity.getAnimationTick();
                if (tick == 20) {
                    if (this.entity.getTarget() != null) {
                        LivingEntity livingEntity = this.entity.getTarget();
                        double minY = Math.min(livingEntity.getY(), this.entity.getY());
                        double maxY = Math.max(livingEntity.getY(), this.entity.getY()) + 1.0D;
                        Vec3 point1 = ModEntityUtils.checkSummonEntityPoint(this.entity, this.entity.getX() - 1, this.entity.getZ(), minY, maxY);
                        summonEntity(point1);
                        Vec3 point2 = ModEntityUtils.checkSummonEntityPoint(this.entity, this.entity.getX() + 1, this.entity.getZ(), minY, maxY);
                        summonEntity(point2);
                    }
                }
            }

            private void summonEntity(Vec3 vec3) {
                EntityImmortalGolem entity = EntityInit.IMMORTAL_GOLEM.get().create(this.entity.level);
                if (!this.entity.level.isClientSide && entity != null && vec3 != null) {
                    entity.setInitSpawn();
                    entity.finalizeSpawn((ServerLevel) this.entity.level, this.entity.level.getCurrentDifficultyAt(new BlockPos(vec3)), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                    entity.setOwner(this.entity);
                    entity.setSummonAliveTime(20 * (15 + this.entity.random.nextInt(15)));
                    Difficulty difficulty = this.entity.level.getDifficulty();
                    entity.setDangerous(this.entity.random.nextInt(10 - difficulty.getId()) == 0);
                    entity.setPos(vec3);
                    level.addFreshEntity(entity);
                }
            }
        });
        this.goalSelector.addGoal(1, new AnimationRange<>(this, () -> bowAnimation, 25, null));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, () -> swingArmAnimation, 6, 2.5F, 1.0F, 1.0F));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, () -> meleeAnimation, 5, 3.0F, 1.0F, 1.0F));
        this.goalSelector.addGoal(1, new AnimationBlock<>(this, () -> blockAnimation));
        this.targetSelector.addGoal(2, new OwnerCopyTargetGoal<>(this));
        this.goalSelector.addGoal(3, new AnimationRangeAI<>(this, 1D, 20, 10F, Items.CROSSBOW, e -> e.active && e.checkHoldItemIsCareerWeapon(CareerType.ARCHER, CareerType.KNIGHT), () -> crossBowChangeAnimation));
        this.goalSelector.addGoal(3, new AnimationRangeAI<>(this, 1D, 25, 12F, Items.BOW, e -> e.active && e.checkHoldItemIsCareerWeapon(CareerType.ARCHER, CareerType.KNIGHT), () -> bowAnimation));
        this.goalSelector.addGoal(3, new AnimationRangeAI<>(this, 1D, 300, 16F, null, e -> e.active && e.checkHoldItemIsCareerWeapon(CareerType.MAGE), () -> castAnimation));
        this.goalSelector.addGoal(4, new AnimationMeleeAI<>(this, 1D, 10, e -> e.checkHoldItemIsCareerWeapon(CareerType.WARRIOR, CareerType.KNIGHT), () -> meleeAnimation));
        this.goalSelector.addGoal(5, new AnimationMeleeAI<>(this, 1.05D, 10, EntityAbsImmortalSkeleton::checkConformCareerWeapon, () -> swingArmAnimation));
    }

    @Override
    public void tick() {
        super.tick();
        EMAnimationHandler.INSTANCE.updateAnimations(this);
        if (this.getCareerType() == CareerType.ARCHER) {
            if (this.getTarget() != null && !isNoAi() && isActive()) {
                LivingEntity target = getTarget();
                if (attackTick == 0 && getSensing().hasLineOfSight(target)) {
                    attacking = true;
                }
                if (attacking && getSensing().hasLineOfSight(target) && canAttack(target)) {
                    if (targetDistance < 1.5 || targetDistance < 2.5 && ModEntityUtils.checkTargetComingCloser(this, target)) {
                        this.stopUsingItem();
                        this.playAnimation(this.meleeAnimation);
                        attackTick = 20;
                        attacking = false;
                    }
                }
            } else {
                attacking = false;
            }
        }
        if (getAnimation() != this.getNoAnimation()) {
            getNavigation().stop();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (attackTick > 0) {
            attackTick--;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        boolean hitFlag = true;
        float attackArc = 220;
        if (entity != null) {
            float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(this, entity.position());
            hitFlag = (entityRelativeAngle <= attackArc / 2f && entityRelativeAngle >= -attackArc / 2f) || (entityRelativeAngle >= 360 - attackArc / 2f || entityRelativeAngle <= -attackArc + 90f / 2f);
        }
        if (hitFlag && this.checkHoldItemCanBlock() && entity instanceof LivingEntity livingEntity && !source.isBypassInvul() && (this.getAnimation() == this.getNoAnimation() || this.getAnimation() == this.blockAnimation)) {
            this.blockEntity = livingEntity;
            this.playSound(SoundEvents.SHIELD_BLOCK);
            if (this.getAnimation() != this.blockAnimation) this.playAnimation(this.blockAnimation);
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    //使用远程攻击攻击指定的实体
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof ProjectileWeaponItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, BowItem.getPowerForTime(20));
        if (this.getMainHandItem().getItem() instanceof BowItem)
            abstractarrow = ((BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrow);
        double x = target.getX() - this.getX();
        double y = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double z = target.getZ() - this.getZ();
        double d0 = Math.sqrt(x * x + z * z) * (double) 0.2F;
        abstractarrow.shoot(x, y + d0, z, 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack arrowStack, float distanceFactor) {
        return ProjectileUtil.getMobArrow(this, arrowStack, distanceFactor);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CAREER_TYPE, CareerType.NONE.id);
    }

    @Nullable
    @Override
    //在初始生成时调用
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        RandomSource inRandom = worldIn.getRandom();
        int id = this.getCareerId(inRandom);
        this.setCareerType(id);
        AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        CareerType careerType = CareerType.byId(id);
        if (attack == null || health == null || armor == null || speed == null) return spawnDataIn;
        attack.removeModifier(ATTACK_UUID);
        health.removeModifier(HEALTH_UUID);
        armor.removeModifier(ARMOR_UUID);
        speed.removeModifier(SPEED_UUID);
        attack.addPermanentModifier(new AttributeModifier(ATTACK_UUID, "Add career attack", careerType.attack, AttributeModifier.Operation.ADDITION));
        health.addPermanentModifier(new AttributeModifier(HEALTH_UUID, "Add career health", careerType.health, AttributeModifier.Operation.ADDITION));
        armor.addPermanentModifier(new AttributeModifier(ARMOR_UUID, "Add career armor", careerType.armor, AttributeModifier.Operation.ADDITION));
        speed.addPermanentModifier(new AttributeModifier(SPEED_UUID, "Add career speed", careerType.speed, AttributeModifier.Operation.ADDITION));
        this.setHealth(this.getMaxHealth());
        this.populateDefaultEquipmentSlots(inRandom, difficultyIn);
        this.populateDefaultEquipmentEnchantments(inRandom, difficultyIn);
        //this.setLeftHanded(inRandom.nextFloat() < 0.05F);
        return spawnDataIn;
    }

    protected abstract int getCareerId(RandomSource random);

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
        return this.animations;
    }

    @Override
    public Animation getSpawnAnimation() {
        return this.spawnAnimation;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("careerType", this.getCareerType().id);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCareerType(compound.getInt("careerType"));
    }


    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyIn) {
        CareerType careerType = this.getCareerType();
        this.setItemSlot(EquipmentSlot.MAINHAND, careerType.holdItems[randomSource.nextInt(careerType.holdItems.length)].getDefaultInstance());
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.ATTACK_DAMAGE, 1.0D).
                add(Attributes.ATTACK_KNOCKBACK, 0D).
                add(Attributes.KNOCKBACK_RESISTANCE, 0D);
    }

    public CareerType getCareerType() {
        return CareerType.byId(this.entityData.get(DATA_CAREER_TYPE));
    }

    public void setCareerType(int id) {
        this.entityData.set(DATA_CAREER_TYPE, id);
    }

    public boolean checkHoldItemCanBlock() {
        return this.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ShieldItem
                || this.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ShieldItem;
    }

    public boolean checkHoldItemIsCareerWeapon(CareerType... careerType) {
        for (CareerType type : careerType) {
            if (this.getCareerType() == type) {
                for (Item holdItem : type.holdItems) {
                    if (this.getMainHandItem().is(holdItem)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkConformCareerWeapon() {
        return this.getCareerType() == CareerType.NONE || !this.checkHoldItemIsCareerWeapon(this.getCareerType());
    }

    public boolean isWarrior() {
        return CareerType.WARRIOR == this.getCareerType();
    }

    public boolean isArcher() {
        return CareerType.ARCHER == this.getCareerType();
    }

    public boolean isMage() {
        return CareerType.MAGE == this.getCareerType();
    }

    public boolean isKnight() {
        return CareerType.KNIGHT == this.getCareerType();
    }

    public enum CareerType {
        NONE(-1, 5F, 4F, 0F, 0F, 1.05F, Items.AIR, ItemInit.IMMORTAL_BONE.get()),
        MAGE(0, 20F, 0F, 2F, 0.08F, 1.05F, Items.AIR),
        ARCHER(1, 10F, 6F, 4F, 0.05F, 1.1F, Items.BOW, Items.CROSSBOW),
        WARRIOR(2, 15F, 5F, 6F, -0.02F, 1.1F, Items.STONE_SWORD),
        KNIGHT(3, 25F, 6F, 8F, -0.02F, 1.2F, ItemInit.IMMORTAL_AXE.get(), ItemInit.IMMORTAL_SWORD.get(), Items.BOW, Items.CROSSBOW),
        ;

        CareerType(int id, float health, float attack, float armor, float speed, float scale, Item... holdItems) {
            this.id = id;
            this.health = health;
            this.attack = attack;
            this.armor = armor;
            this.speed = speed;
            this.scale = scale;
            this.holdItems = holdItems;
        }

        public final int id;
        public final float health;
        public final float attack;
        public final float armor;
        public final float speed;
        public final float scale;
        public final Item[] holdItems;

        public static CareerType byId(int id) {
            for (CareerType value : values()) {
                if (value.id == id) {
                    return value;
                }
            }
            return NONE;
        }
    }
}
