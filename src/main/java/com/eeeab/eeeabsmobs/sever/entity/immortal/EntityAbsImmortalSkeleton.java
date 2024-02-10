package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.*;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationMeleeAttackGoal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public abstract class EntityAbsImmortalSkeleton extends EntityAbsImmortal implements RangedAttackMob {
    public static final Animation DIE_ANIMATION = Animation.create(60);
    public static final Animation HURT_ANIMATION = Animation.create(8);
    public static final Animation MELEE_ATTACK_1_ANIMATION = Animation.create(14);
    public static final Animation MELEE_ATTACK_2_ANIMATION = Animation.create(16);
    public static final Animation MELEE_ATTACK_3_ANIMATION = Animation.create(20);
    public static final Animation RANGED_ATTACK_ANIMATION = Animation.create(30);
    public static final Animation BLOCK_ANIMATION = Animation.create(10);
    public static final Animation SPAWN_ANIMATION = Animation.create(40);
    public static final Animation ROAR_ANIMATION = Animation.create(45);
    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            MELEE_ATTACK_1_ANIMATION,
            MELEE_ATTACK_2_ANIMATION,
            MELEE_ATTACK_3_ANIMATION,
            RANGED_ATTACK_ANIMATION,
            BLOCK_ANIMATION,
            SPAWN_ANIMATION,
            ROAR_ANIMATION,
    };
    private static final EntityDataAccessor<Integer> DATA_HANDED_STATE = SynchedEntityData.defineId(EntityAbsImmortalSkeleton.class, EntityDataSerializers.INT);

    private boolean moveLeftOrRight = false;

    public EntityAbsImmortalSkeleton(EntityType<? extends EntityAbsImmortal> type, Level level) {
        super(type, level);
        this.reassessAttackModeGoal();
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
        if (hitFlag && this.getWeaponType().CanBlock() && entity instanceof LivingEntity livingEntity && !source.is(DamageTypeTags.BYPASSES_ARMOR)
                && (this.getAnimation() == NO_ANIMATION || this.getAnimation() == HURT_ANIMATION || this.getAnimation() == BLOCK_ANIMATION)) {
            this.blockEntity = livingEntity;
            this.playSound(SoundEvents.SHIELD_BLOCK);
            this.playAnimation(BLOCK_ANIMATION);
            return false;
        }
        return super.hurt(source, damage);
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, Player.class, 8.0F));
    }


    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationDieGoal<>(this));
        this.goalSelector.addGoal(1, new AnimationHurtGoal<>(this, false));
        this.goalSelector.addGoal(1, new AnimationActivateGoal<>(this, SPAWN_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationProjectileAttackGoal<>(this, RANGED_ATTACK_ANIMATION, 19, null));
        this.goalSelector.addGoal(1, new AnimationAttackGoal<>(this, MELEE_ATTACK_1_ANIMATION, 6, 3.0f, 1.0f, 1.0f));
        this.goalSelector.addGoal(1, new AnimationAttackGoal<>(this, MELEE_ATTACK_2_ANIMATION, 8, 3.0f, 1.0f, 15.0f));
        this.goalSelector.addGoal(1, new AnimationAttackGoal<>(this, MELEE_ATTACK_3_ANIMATION, 12, 2.5f, 1.0f, 1.0f));
        this.goalSelector.addGoal(1, new AnimationBlockGoal<>(this, BLOCK_ANIMATION));
        this.goalSelector.addGoal(2, new AnimationMeleeAttackGoal<>(this, 1D, e -> e.getWeaponType() == AttackType.MAIN_HANDED || e.getWeaponType() == AttackType.MAIN_HANDED_SHIELD, MELEE_ATTACK_1_ANIMATION, MELEE_ATTACK_2_ANIMATION));
        this.goalSelector.addGoal(2, new AnimationMeleeAttackGoal<>(this, 1D, 5, e -> e.getWeaponType() == AttackType.NONE || e.getWeaponType() == AttackType.NONE_SHIELD, MELEE_ATTACK_3_ANIMATION));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.getTarget() != null && !this.getTarget().isAlive()) setTarget(null);
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.getWeaponType() == AttackType.BOW || this.getWeaponType() == AttackType.BOW_SHIELD) {
            if (this.getTarget() != null && !isNoAi() && isActive()) {
                LivingEntity target = getTarget();
                final double attackRange = 9.0D;
                this.getLookControl().setLookAt(target, 30F, 30F);
                if (targetDistance <= 4) {
                    this.getMoveControl().strafe(-0.5F, 0.0F);
                } else if (!(targetDistance > attackRange)) {
                    if (tickCount % 20 == 0 && this.getRandom().nextFloat() < 0.25F) {
                        moveLeftOrRight = !moveLeftOrRight;
                    }
                    if (tickCount % 2 == 0) this.getMoveControl().strafe(0.0F, moveLeftOrRight ? 0.5F : -0.5F);
                }
                if (targetDistance >= attackRange) {
                    //this.getMoveControl().strafe(0.0F, 0.0F);
                    this.getNavigation().moveTo(target, 1.0D);
                }

                if (attackTick == 0 && getSensing().hasLineOfSight(target)) {
                    attacking = true;
                }
                if (attacking && getAnimation() == NO_ANIMATION && getSensing().hasLineOfSight(target)) {
                    if (targetDistance <= attackRange) {
                        if (!(targetDistance < 2.5 || targetDistance < 3.5 && ModEntityUtils.checkTargetComingCloser(this, target))) {
                            this.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem));
                            this.playAnimation(RANGED_ATTACK_ANIMATION);
                            attackTick = 50 + this.random.nextInt(20);
                            attacking = false;
                        }
                    }
                }
                if (attacking && getSensing().hasLineOfSight(target)) {
                    if (targetDistance < 2.5 || targetDistance < 3.5 && ModEntityUtils.checkTargetComingCloser(this, target)) {
                        if (this.isUsingItem()) this.stopUsingItem();
                        this.playAnimation(MELEE_ATTACK_2_ANIMATION);
                        attackTick = 20;
                        attacking = false;
                    }
                }
            } else {
                attacking = false;
            }
        }
        if (getAnimation() != NO_ANIMATION) {
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
    //使用远程攻击攻击指定的实体
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, BowItem.getPowerForTime(20));
        if (this.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem)
            abstractarrow = ((net.minecraft.world.item.BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrow);

        double x = target.getX() - this.getX();
        double y = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double z = target.getZ() - this.getZ();
        double d0 = Math.sqrt(x * x + z * z) * (double) 0.2F;

        //将实体箭发射到指定坐标 shoot(x轴 y轴 z轴 速度 误差)
        abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (1.0D * level().getDifficulty().getId()));
        // abstractarrow.setKnockback(abstractarrow.getKnockback() + 1);
        abstractarrow.shoot(x, y + d0, z, 1.6F, (float) 1);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack arrowStack, float distanceFactor) {
        return ProjectileUtil.getMobArrow(this, arrowStack, distanceFactor);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HANDED_STATE, AttackType.NONE.getState());
    }

    @Nullable
    @Override
    //在初始生成时调用
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.reassessAttackModeGoal();
        this.setCanPickUpLoot(true);
        //this.setStyle(Util.getRandom(MobSkinStyle.values(), randomsource));//TODO 待实现变种皮肤
        return spawnDataIn;
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    @Override
    protected Animation getSpawnAnimation() {
        return SPAWN_ANIMATION;
    }

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
        return ANIMATIONS;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        //compound.putInt("state", this.getWeaponType().getState());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.reassessAttackModeGoal();
        this.setCanPickUpLoot(true);
        //this.setWeaponType(AttackType.byState(compound.getInt("state")));
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        super.setItemSlot(equipmentSlot, itemStack);
        //this.setDropChance(equipmentSlot, 0);
        if (!this.level().isClientSide) {
            this.reassessAttackModeGoal();
        }
    }

    public AttackType getWeaponType() {
        return AttackType.byState(this.entityData.get(DATA_HANDED_STATE));
    }

    public void setWeaponType(AttackType attackType) {
        this.entityData.set(DATA_HANDED_STATE, attackType.getState());
    }

    protected void reassessAttackModeGoal() {
        if (!this.level().isClientSide) {
            AttackType type;
            //判断主手物品
            ItemStack mainHandItem = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem));
            if (mainHandItem.is(Items.BOW)) {
                type = AttackType.BOW;
            } else if (!getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                type = AttackType.MAIN_HANDED;
            } else {
                type = AttackType.NONE;
            }
            //判断副手物品
            ItemStack offhandItem = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.ShieldItem));
            if (offhandItem.is(Items.SHIELD)) {
                if (type == AttackType.BOW) {
                    type = AttackType.BOW_SHIELD;
                } else if (type == AttackType.MAIN_HANDED) {
                    type = AttackType.MAIN_HANDED_SHIELD;
                } else {
                    type = AttackType.NONE_SHIELD;
                }
            }
            this.setWeaponType(type);
        }
    }

    public enum AttackType {
        NONE(0),
        MAIN_HANDED(1),
        BOW(2),
        MAIN_HANDED_SHIELD(3, true),
        BOW_SHIELD(4, true),
        NONE_SHIELD(5, true);

        private final int state;
        private boolean canBlock;

        AttackType(int state) {
            this(state, false);
        }

        AttackType(int state, boolean canBlock) {
            this.state = state;
            this.canBlock = canBlock;
        }

        public int getState() {
            return state;
        }


        public void setBlock(boolean canBlock) {
            this.canBlock = canBlock;
        }

        public boolean CanBlock() {
            return canBlock;
        }

        public static AttackType byState(int id) {
            for (AttackType attackType : values()) {
                if (id == attackType.state) {
                    return attackType;
                }
            }
            return NONE;
        }
    }
}
