package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.EnumSet;

public class GuardianPounceAttackGoal extends AnimationAI<EntityNamelessGuardian> {
    private final EntityNamelessGuardian entity;
    private Vec3 pounceVec = Vec3.ZERO;
    private final float speedMultiplier;
    private double moveSpeed;
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
        pounceVec = Vec3.ZERO;
        moveSpeed = entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
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
        float baseDamageMultiplier = isPowered ? 0.8F : 0.6F;
        if (entity.getAnimation() == entity.pounceAttackAnimation1) {
            entity.anchorToGround();
            int tick = entity.getAnimationTick();
            if (tick == 1) {
                entity.playSound(SoundInit.NAMELESS_GUARDIAN_PRE_POUNCE.get(), 1.5F, entity.getVoicePitch());
            } else if (tick >= entity.pounceAttackAnimation1.getDuration() - 1) {
                if (target != null) {
                    double radians = Math.toRadians(entity.getYRot() + 90);
                    pounceVec = new Vec3(Math.cos(radians), 0, Math.sin(radians));
                    entity.playAnimation(entity.pounceAttackAnimation2);
                } else {
                    entity.playAnimation(entity.pounceAttackAnimation3);
                }
            }
            if (target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            }
        } else if (entity.getAnimation() == entity.pounceAttackAnimation2) {
            int tick = entity.getAnimationTick();
            int keyFrame = isPowered ? 24 : 28;
            if (tick < keyFrame && pounceVec.length() != 0) {
                entity.setDeltaMovement(pounceVec.x * moveSpeed * madnessSpeedMultiplier, -entity.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get()) * 5.0F, pounceVec.z * moveSpeed * speedMultiplier);
                if (!entity.level().isClientSide && ModEntityUtils.canMobDestroy(entity)) {
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
                                filter((pos) -> ModEntityUtils.canDestroyBlock(entity.level(), pos, entity, 2F)).
                                forEach((pos) -> entity.level().destroyBlock(pos, entity.checkCanDropItems()));
                    }
                }
                if (tick % 2 == 0) {
                    double width = entity.getBbWidth() * 1.2;
                    entity.rangeAttack(width, 5F, width, width, 120F, 120F, hitEntity -> {
                        entity.guardianHurtTarget(entity, hitEntity, 0.05F, 1.0F, baseDamageMultiplier, false, false, false);
                        double ratioX = Math.sin(entity.getYRot() * ((float) Math.PI / 180F));
                        double ratioZ = (-Math.cos(entity.getYRot() * ((float) Math.PI / 180F)));
                        ModEntityUtils.forceKnockBack(entity, hitEntity, 1.5F, ratioX, ratioZ, true);
                        double duration = 1.5;
                        if (Difficulty.HARD.equals(entity.level().getDifficulty())) duration = 2.5;
                        entity.stun(null, hitEntity, (int) (duration * 20), entity.isChallengeMode());
                    });
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

    private boolean checkModeOrPreventTimeouts() {
        return entity.isChallengeMode() || (entity.getMadnessTick() > 200 && entity.isPowered());
    }
}
