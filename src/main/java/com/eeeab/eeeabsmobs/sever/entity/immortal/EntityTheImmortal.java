package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.control.EMBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EMPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import com.eeeab.eeeabsmobs.sever.util.damge.DamageAdaptation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

//创建于 2023/1/17
public class EntityTheImmortal extends EntityAbsImmortal implements IBoss {
    public final Animation dieAnimation = Animation.create(60);
    public final Animation spawnAnimation = Animation.create(100);
    public final Animation switchStage2Animation = Animation.create(90);
    public final Animation switchStage3Animation = Animation.create(80);
    private final Animation[] animations = new Animation[]{
            dieAnimation,
            spawnAnimation,
            switchStage2Animation,
            switchStage3Animation
    };
    private static final EntityDataAccessor<Integer> DATA_STAGE = SynchedEntityData.defineId(EntityTheImmortal.class, EntityDataSerializers.INT);
    private static final UUID HEALTH_UUID = UUID.fromString("E2F534E6-4A55-4B72-A9D2-3157D084A281");
    private static final UUID ARMOR_UUID = UUID.fromString("FA2FB8E8-FFE8-4D77-B23B-E0110D4A175F");
    private static final UUID ATTACK_UUID = UUID.fromString("F8DBD65D-3C4A-4851-83D3-4CB22964A196");
    private boolean switching;
    private DamageSource lastDamageSource;
    private final DamageAdaptation damageAdaptation;
    public final ControlledAnimation glowControllerAnimation = new ControlledAnimation(40);

    public EntityTheImmortal(EntityType<? extends EntityAbsImmortal> type, Level level) {
        super(type, level);
        this.active = false;
        this.damageAdaptation = new DamageAdaptation(50, 10000, 0.05F, 0.8F, true);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_BOSS;
    }

    @Override
    public float getStepHeight() {
        return 3F;
    }

    @Override//可以站立的流体
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.LAVA);
    }

    @Override//是否免疫摔伤
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    @Override//添加药水效果
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return this.isActive() && ModEntityUtils.isBeneficial(effectInstance.getEffect()) && super.addEffect(effectInstance, entity);
    }

    @Override//强制添加药水效果
    public void forceAddEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (this.isActive() && ModEntityUtils.isBeneficial(effectInstance.getEffect()))
            super.forceAddEffect(effectInstance, entity);
    }

    @Override//添加效果时额外条件
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return this.isActive() && ModEntityUtils.isBeneficial(effectInstance.getEffect()) && super.canBeAffected(effectInstance);
    }

    @Override//是否在实体上渲染着火效果
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    @Override//是否被流体推动
    public boolean isPushedByFluid() {
        return false;
    }

    @Override//被方块阻塞
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean isDeadOrDying() {
        return super.isDeadOrDying() && this.getStage() == Stage.STAGE3;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    @NotNull
    protected BodyRotationControl createBodyControl() {
        return new EMBodyRotationControl(this);
    }

    @Override
    @NotNull
    protected PathNavigation createNavigation(Level level) {
        return new EMPathNavigateGround(this, level);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Animation animation = this.getAnimation();
        return !this.isActive() || ((animation == this.switchStage2Animation || animation == this.switchStage3Animation) || super.isInvulnerableTo(damageSource));
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.IMMORTAL.THE_IMMORTAL.combatConfig;
    }

    @Override
    protected boolean showBossBloodBars() {
        return EMConfigHandler.COMMON.OTHER.enableShowBloodBars.get();
    }

    @Override
    protected BossEvent.BossBarColor bossBloodBarsColor() {
        return BossEvent.BossBarColor.WHITE;
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (!this.level().isClientSide) {
            if (animation == this.switchStage2Animation || animation == this.switchStage3Animation) {
                this.switching = false;
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityAbsImmortal.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8D) {
            @Override
            public boolean canUse() {
                return EntityTheImmortal.this.active && super.canUse();
            }
        });
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, Mob.class, 6.0F));
    }

    @Override
    protected void registerCustomGoals() {
        super.registerCustomGoals();
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> switchStage2Animation));
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> switchStage3Animation));
        this.goalSelector.addGoal(2, new AnimationActivate<>(this, () -> spawnAnimation));
    }

    @Override
    public void tick() {
        super.tick();
        this.floatImmortal();
        this.glowControllerAnimation.updatePrevTimer();

        EMAnimationHandler.INSTANCE.updateAnimations(this);

        this.pushEntitiesAway(2F, getBbHeight(), 2F, 2F);
        LivingEntity lookTarget = this.getLastAttacker();
        if (this.getTarget() != null) {
            lookTarget = this.getTarget();
        }
        if (this.getAnimation() == this.switchStage2Animation || this.getAnimation() == this.switchStage3Animation) {
            if (lookTarget != null && lookTarget.isAlive()) {
                this.getLookControl().setLookAt(lookTarget, 30F, 30F);
            }
        } else if (this.getAnimation() == this.spawnAnimation) {
            if (this.getAnimationTick() > 20) {
                if (lookTarget != null && lookTarget.isAlive()) {
                    this.getLookControl().setLookAt(lookTarget, 30F, 30F);
                }
            }
        }

        if (!this.isActive()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Animation animation = this.getAnimation();
        if (!this.level().isClientSide) {
            if (animation == this.switchStage2Animation || animation == this.switchStage3Animation) {
                this.heal(this.getMaxHealth() / animation.getDuration());
            }
            this.damageAdaptation.tick(this);
        }
        this.glowControllerAnimation.incrementOrDecreaseTimer(this.isActive());
    }

    @Override
    public void setHealth(float health) {
        //判断是否扣血
        if (health < this.getHealth()) {
            if (this.lastDamageSource == null || !this.lastDamageSource.is(EMTagKey.GENERAL_UNRESISTANT_TO)) {
                float damage = Math.min(this.getHealth() - health, 20);
                health = this.getHealth() - damage;
            }
        }
        super.setHealth(health);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            this.lastDamageSource = source;
            float damaged = this.damageAdaptation.damageAfterAdaptingOnce(source, damage);
            boolean flag = super.hurt(source, damaged);
            //判断是否切换阶段
            if (this.getHealth() <= 0 && this.getStage() != Stage.STAGE3) {
                this.nextStage(Stage.byStage(this.getStage().type + 1));
            }
            return flag;
        }
    }

    private void floatImmortal() {
        if (this.isInLava()) {
            CollisionContext collisioncontext = CollisionContext.of(this);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.1D, 0.0D));
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_STAGE, Stage.STAGE1.type);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("stage", this.getStage().type);
        compound.putBoolean("switching", this.switching);
        compound.putBoolean("isActive", this.isActive());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_STAGE, compound.getInt("stage"));
        this.switching = compound.getBoolean("switching");
        this.setActive(compound.getBoolean("isActive"));
        if (this.switching) {
            if (this.getStage() == Stage.STAGE1) {
                this.nextStage(Stage.STAGE2);
            } else if (this.getStage() == Stage.STAGE2) {
                this.playAnimation(switchStage2Animation);
            } else if (this.getStage() == Stage.STAGE3) {
                this.playAnimation(switchStage3Animation);
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.is(Items.AIR)) {
            return InteractionResult.PASS;
        } else if (this.isActive() || this.isNoAi()) {
            return InteractionResult.PASS;
        } else {
            if (!player.getAbilities().instabuild) {
                System.out.println("物品数量减1");
                //itemStack.shrink(1);
            }
            this.playAnimation(this.spawnAnimation);
            this.setActive(true);
            return InteractionResult.SUCCESS;
        }
    }

    @Nullable
    @Override
    //在初始生成时调用
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        this.setActive(false);
        return spawnDataIn;
    }


    public Stage getStage() {
        return Stage.byStage(this.entityData.get(DATA_STAGE));
    }

    @Override
    public boolean isGlow() {
        return !this.glowControllerAnimation.isStop();
    }

    @Override
    public void setOwner(@Nullable EntityAbsImmortal owner) {

    }

    @Override
    public void setOwnerUUID(UUID uuid) {

    }

    @Override
    public void setSpawnParticle(int amount) {

    }

    @Override
    public boolean isSummon() {
        return false;
    }

    @Override
    public Animation getDeathAnimation() {
        return this.dieAnimation;
    }

    @Override
    public Animation[] getAnimations() {
        return this.animations;
    }

    /**
     * 不朽者切换阶段调用此方法
     *
     * @param stage 下一阶段
     */
    private void nextStage(Stage stage) {
        Animation playAnimation;
        try {
            this.switching = true;
            playAnimation = null;
            switch (this.getStage()) {
                case STAGE1 -> {
                    System.out.println("阶段1");
                    if (stage == Stage.STAGE2) {
                        playAnimation = this.switchStage2Animation;
                    }
                }
                case STAGE2 -> {
                    System.out.println("阶段2");
                    if (stage == Stage.STAGE3) {
                        playAnimation = this.switchStage3Animation;
                    }
                }
                case STAGE3 -> {
                    System.out.println("阶段3");
                }
            }
            if (this.getHealth() <= 0) this.setHealth(0.1F);
        } finally {
            this.changeAttribute(stage.addHealth, stage.addArmor, stage.addAttack);
            this.entityData.set(DATA_STAGE, stage.type);
        }
        this.playAnimation(playAnimation);
    }

    private void changeAttribute(float addHealth, float addArmor, float addAttack) {
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
        AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
        health.removePermanentModifier(HEALTH_UUID);
        armor.removePermanentModifier(ARMOR_UUID);
        attack.removePermanentModifier(ATTACK_UUID);
        health.addPermanentModifier(new AttributeModifier(HEALTH_UUID, "Immortal add health", addHealth, AttributeModifier.Operation.ADDITION));
        attack.addPermanentModifier(new AttributeModifier(ATTACK_UUID, "Immortal add attack", addAttack, AttributeModifier.Operation.ADDITION));
        armor.addPermanentModifier(new AttributeModifier(ARMOR_UUID, "Immortal add armor", addArmor, AttributeModifier.Operation.ADDITION));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0D).
                add(Attributes.MOVEMENT_SPEED, 0.32D).
                add(Attributes.FOLLOW_RANGE, 64.0D).
                add(Attributes.ATTACK_DAMAGE, 1.0D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    public enum Stage {
        STAGE1(1, 0F, 0F, 0F),
        STAGE2(2, 250F, 15F, 0F),
        STAGE3(3, 150F, 10F, 5F);

        Stage(int type, float addHealth, float addArmor, float addAttack) {
            this.type = type;
            this.addHealth = addHealth;
            this.addArmor = addArmor;
            this.addAttack = addAttack;
        }

        public final int type;
        public final float addHealth;
        public final float addArmor;
        public final float addAttack;

        public static Stage byStage(int type) {
            for (Stage value : values()) {
                if (value.type == type) {
                    return value;
                }
            }
            return STAGE1;
        }
    }
}
