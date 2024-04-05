package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationAbstractGoal;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.EnumSet;
import java.util.List;

//冲向目标
public class GuardianPounceAttackGoal extends AnimationAbstractGoal<EntityNamelessGuardian> {
    private final EntityNamelessGuardian entity;
    private Vec3 pounceVec = Vec3.ZERO;
    private final float speedMultiplier;
    private boolean isPowered;
    private float madnessSpeedMultiplier;
    private int consecutive;

    public GuardianPounceAttackGoal(EntityNamelessGuardian entity, float speedMultiplier) {
        super(entity);
        this.entity = entity;
        this.speedMultiplier = speedMultiplier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public void start() {
        super.start();
        this.isPowered = entity.isPowered();
        this.madnessSpeedMultiplier = this.speedMultiplier;
        if (this.isPowered) {
            this.madnessSpeedMultiplier += 0.5F;
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.madnessSpeedMultiplier = 0F;
        this.isPowered = false;
        this.consecutive = 0;
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_2 || animation == EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_1 || animation == EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_3;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        double moveSpeed = this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
        float baseDamageMultiplier = this.isPowered ? 0.8F : 0.6F;
        if (this.entity.getAnimation() == EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_1) {
            int animationDuration = EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_1.getDuration();
            this.entity.setDeltaMovement(0, this.entity.onGround() ? 0 : this.entity.getDeltaMovement().y(), 0);
            if (target != null) {
                this.entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            }
            int tick = this.entity.getAnimationTick();
            if (tick == 1) {
                entity.playSound(SoundInit.NAMELESS_GUARDIAN_PRE_POUNCE.get(), 1.5F, entity.getVoicePitch());
            } else if (tick >= animationDuration - 1) {
                if (target != null) {
                    this.pounceVec = findTargetPoint(this.entity, target);
                    this.entity.playAnimation(EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_2);
                } else {
                    this.entity.playAnimation(EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_3);
                }
            }
        } else if (this.entity.getAnimation() == EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_2) {
            int tick = this.entity.getAnimationTick();
            int keyFrame = this.isPowered ? 24 : 28;
            if (tick < keyFrame) {
                this.entity.setDeltaMovement(this.pounceVec.x * moveSpeed * this.madnessSpeedMultiplier, -this.entity.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get()) * 5.0F, this.pounceVec.z * moveSpeed * this.speedMultiplier);
                if (this.entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                    this.entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                }
                if (!this.entity.level().isClientSide &&
                        /* 判断是否开启生物破坏规则,这关乎那些不想被怪物破坏方块的玩家考虑~ */
                        ModEntityUtils.canMobDestroy(this.entity)) {
                    AABB bb = this.entity.getBoundingBox();
                    int minx = Mth.floor(bb.minX - 0.75D);
                    int miny = Mth.floor(bb.minY + 0.0D);
                    int minz = Mth.floor(bb.minZ - 0.75D);
                    BlockPos min = new BlockPos(minx, miny, minz);

                    int maxx = Mth.floor(bb.maxX + 0.75D);
                    int maxy = Mth.floor(bb.maxY + 0.15D);
                    int maxz = Mth.floor(bb.maxZ + 0.75D);
                    BlockPos max = new BlockPos(maxx, maxy, maxz);
                    if (this.entity.level().hasChunksAt(min, max)) {
                        BlockPos.betweenClosedStream(min, max).
                                filter((pos) -> ModEntityUtils.canDestroyBlock(this.entity.level(), pos, this.entity,
                                        /* 高于2.0即不能破坏,不能让它什么都能撞坏~ */ 1.9F)
                                        && this.entity.level().getBlockEntity(pos) == null).
                                forEach((pos) ->
                                        this.entity.level().destroyBlock(pos, true));
                    }
                }
                if (tick % 2 == 0) {
                    //List<LivingEntity> livingEntities = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(0.5, 0, 0.5));
                    List<LivingEntity> livingEntities = this.entity.getNearByLivingEntities(3F, 4F, 3F, 3F);
                    for (LivingEntity hitEntity : livingEntities) {
                        if (hitEntity == this.entity) {
                            continue;
                        }
                        float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(this.entity, hitEntity);
                        float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - this.entity.getZ()) * (hitEntity.getZ() - this.entity.getZ()) + (hitEntity.getX() - this.entity.getX()) * (hitEntity.getX() - this.entity.getX())) - hitEntity.getBbWidth() / 2F;

                        if (entityHitDistance <= 3F && (entityRelativeAngle <= 120 / 2F && entityRelativeAngle >= -120 / 2F) || (entityRelativeAngle >= 360 - 120 / 2F || entityRelativeAngle <= -360 + 120 / 2F)) {
                            this.entity.guardianHurtTarget(this.entity, hitEntity, 0.05F, 1F, baseDamageMultiplier, false, false);
                            double ratioX = Math.sin(this.entity.getYRot() * ((float) Math.PI / 180F));
                            double ratioZ = (-Math.cos(this.entity.getYRot() * ((float) Math.PI / 180F)));
                            ModEntityUtils.forceKnockBack(hitEntity, 1.5f, ratioX, ratioZ, 0.01f, false);
                            double duration = 1.5;
                            if (Difficulty.HARD.equals(this.entity.level().getDifficulty())) duration = 2.5;
                            if (hitEntity instanceof Player player && !player.isCreative() && !player.isBlocking()) {
                                player.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), (int) (duration * 20), 0, false, false, true));
                            } else if (!(hitEntity instanceof Player) && !hitEntity.isBlocking()) {
                                hitEntity.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), (int) (duration * 20), 0, false, false, true));
                            }
                        }
                    }
                }
            } else if (tick == keyFrame) {
                this.entity.playAnimation(EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_3);
            }
        } else if (this.entity.getAnimation() == EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_3) {
            this.entity.setDeltaMovement(0, entity.getDeltaMovement().y(), 0);
            int maxExtraConsecutive = 2;
            if (this.consecutive < maxExtraConsecutive && entity.getTarget() != null && this.entity.getAnimationTick() <= 6 && this.entity.getAnimationTick() > 1 && checkModeOrPreventTimeouts() && this.entity.targetDistance < 16 && this.entity.targetDistance > 4
                    && ((this.entity.hasEffect(EffectInit.VERTIGO_EFFECT.get()) && this.entity.getRandom().nextInt(3 - consecutive) == 0) || this.entity.getRandom().nextInt(10) == 0)) {
                this.consecutive++;
                this.entity.playAnimation(EntityNamelessGuardian.POUNCE_ATTACK_ANIMATION_1);
            }
        }
    }

    public static Vec3 findTargetPoint(Entity attacker, Entity target) {
        Vec3 vec3 = target.position();
        return (new Vec3(vec3.x - attacker.getX(), 0.0, vec3.z - attacker.getZ())).normalize();
    }

    private boolean checkModeOrPreventTimeouts() {
        return this.entity.isChallengeMode() || (this.entity.getMadnessTick() > 200 && this.entity.isPowered());
    }

}
