package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.ai.AnimationAI;
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

public class GuardianPounceAttackGoal extends AnimationAI<EntityNamelessGuardian> {
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
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public void start() {
        super.start();
        isPowered = entity.isPowered();
        madnessSpeedMultiplier = speedMultiplier;
        if (isPowered) {
            madnessSpeedMultiplier += 0.5F;
        }
    }

    @Override
    public void stop() {
        super.stop();
        madnessSpeedMultiplier = 0F;
        isPowered = false;
        consecutive = 0;
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.pounceAttackAnimation1 || animation == entity.pounceAttackAnimation2 || animation == entity.pounceAttackAnimation3;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        double moveSpeed = entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
        float baseDamageMultiplier = isPowered ? 0.8F : 0.6F;
        if (entity.getAnimation() == entity.pounceAttackAnimation1) {
            entity.setDeltaMovement(0, entity.onGround() ? 0 : entity.getDeltaMovement().y(), 0);
            if (target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            }
            int tick = entity.getAnimationTick();
            if (tick == 1) {
                pounceVec = Vec3.ZERO;
                entity.playSound(SoundInit.NAMELESS_GUARDIAN_PRE_POUNCE.get(), 1.5F, entity.getVoicePitch());
            } else if (tick >= entity.pounceAttackAnimation1.getDuration() - 1) {
                if (target != null) {
                    pounceVec = findTargetPoint(entity, target);
                    entity.playAnimation(entity.pounceAttackAnimation2);
                } else {
                    entity.playAnimation(entity.pounceAttackAnimation3);
                }
            }
        } else if (entity.getAnimation() == entity.pounceAttackAnimation2) {
            int tick = entity.getAnimationTick();
            int keyFrame = isPowered ? 24 : 28;
            if (tick < keyFrame && pounceVec.length() != 0) {
                entity.setDeltaMovement(pounceVec.x * moveSpeed * madnessSpeedMultiplier, -entity.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get()) * 5.0F, pounceVec.z * moveSpeed * speedMultiplier);
                if (entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                    entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                }
                if (!entity.level().isClientSide &&
                        /* 判断是否开启生物破坏规则,这关乎那些不想被怪物破坏方块的玩家考虑~ */
                        ModEntityUtils.canMobDestroy(entity)) {
                    AABB bb = entity.getBoundingBox();
                    int minx = Mth.floor(bb.minX - 0.75D);
                    int miny = Mth.floor(bb.minY + 0.0D);
                    int minz = Mth.floor(bb.minZ - 0.75D);
                    BlockPos min = new BlockPos(minx, miny, minz);

                    int maxx = Mth.floor(bb.maxX + 0.75D);
                    int maxy = Mth.floor(bb.maxY + 0.15D);
                    int maxz = Mth.floor(bb.maxZ + 0.75D);
                    BlockPos max = new BlockPos(maxx, maxy, maxz);
                    if (entity.level().hasChunksAt(min, max)) {
                        BlockPos.betweenClosedStream(min, max).
                                filter((pos) -> ModEntityUtils.canDestroyBlock(entity.level(), pos, entity,
                                        /* 高于2.0即不能破坏,不能让它什么都能撞坏~ */ 1.9F)
                                        && entity.level().getBlockEntity(pos) == null).
                                forEach((pos) ->
                                        entity.level().destroyBlock(pos, false));
                    }
                }
                if (tick % 2 == 0) {
                    //List<LivingEntity> livingEntities = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(0.5, 0, 0.5));
                    List<LivingEntity> livingEntities = entity.getNearByLivingEntities(3F, 4F, 3F, 3F);
                    for (LivingEntity hitEntity : livingEntities) {
                        if (hitEntity == entity) {
                            continue;
                        }
                        float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                        float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;

                        if (entityHitDistance <= 3F && (entityRelativeAngle <= 120 / 2F && entityRelativeAngle >= -120 / 2F) || (entityRelativeAngle >= 360 - 120 / 2F || entityRelativeAngle <= -360 + 120 / 2F)) {
                            entity.guardianHurtTarget(entity, hitEntity, 0.05F, 1.0F, baseDamageMultiplier, false, false, false);
                            double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                            double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                            ModEntityUtils.forceKnockBack(hitEntity, 1.5f, ratioX, ratioZ, 0.01f, false);
                            double duration = 1.5;
                            if (Difficulty.HARD.equals(entity.level().getDifficulty())) duration = 2.5;
                            if (hitEntity instanceof Player player && !player.isCreative() && !player.isBlocking()) {
                                player.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), (int) (duration * 20), 0, false, false, true));
                            } else if (!(hitEntity instanceof Player) && !hitEntity.isBlocking()) {
                                hitEntity.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), (int) (duration * 20), 0, false, false, true));
                            }
                        }
                    }
                }
            } else {
                entity.playAnimation(entity.pounceAttackAnimation3);
            }
        } else if (entity.getAnimation() == entity.pounceAttackAnimation3) {
            entity.setDeltaMovement(0, entity.getDeltaMovement().y(), 0);
            int maxExtraConsecutive = 2;
            if (consecutive < maxExtraConsecutive && entity.getTarget() != null && entity.getAnimationTick() <= 6 && entity.getAnimationTick() > 1 && checkModeOrPreventTimeouts() && entity.targetDistance < 16 && entity.targetDistance > 4
                    && ((entity.hasEffect(EffectInit.VERTIGO_EFFECT.get()) && entity.getRandom().nextInt(3 - consecutive) == 0) || entity.getRandom().nextInt(10) == 0)) {
                consecutive++;
                entity.playAnimation(entity.pounceAttackAnimation1);
            }
        }
    }

    public static Vec3 findTargetPoint(Entity attacker, Entity target) {
        Vec3 vec3 = target.position();
        return (new Vec3(vec3.x - attacker.getX(), 0.0, vec3.z - attacker.getZ())).normalize();
    }

    private boolean checkModeOrPreventTimeouts() {
        return entity.isChallengeMode() || (entity.getMadnessTick() > 200 && entity.isPowered());
    }

}
