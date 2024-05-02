package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianBlade;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class GuardianLobedAttackGoal extends AnimationCommonGoal<EntityNamelessGuardian> {
    private boolean isPowered;

    public GuardianLobedAttackGoal(EntityNamelessGuardian entity, Animation animation) {
        super(entity, animation, true);
    }

    @Override
    public void start() {
        super.start();
        this.isPowered = entity.isPowered();
    }

    @Override
    public void tick() {
        entity.setDeltaMovement(0, entity.isOnGround() ? 0 : entity.getDeltaMovement().y, 0);
        int maxFallingDistance = isPowered ? 8 : 6;
        int tick = entity.getAnimationTick();
        LivingEntity target = entity.getTarget();
        if (target != null) entity.lookAt(target, 30F, 30F);
        if ((!isPowered && tick >= 22 && tick < 34) || (isPowered && tick >= 22 && tick < 45)) {
            if (tick == 23) {
                if (isPowered) doSpawnBlade();
                entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 1F + entity.getRandom().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(entity.level, entity.position(), 20, 0.125F, 5, 20);
            } else if (tick == 24) {
                entity.playSound(SoundEvents.STONE_BREAK, 1.5F, 1.0F);
            }
            entity.lobedAttack(tick - 13, 2.0, 0.0, maxFallingDistance, (isPowered ? 1.0F : 0.8F));
        }
    }

    private void doSpawnBlade() {
        final int count = 8;
        float offset = (float) Math.toRadians(entity.getRandom().nextFloat() * 360 - 180);
        for (int i = 0; i < count; ++i) {
            float f1 = (float) (entity.getYRot() + (i + offset) * (float) Math.PI * (2.0 / count));
            double x = entity.getX() + Mth.cos(f1) * 3D;
            double y = entity.getY();
            double z = entity.getZ() + Mth.sin(f1) * 3D;
            EntityGuardianBlade blade = new EntityGuardianBlade(entity.level, entity, x, y, z, f1, true);
            entity.level.addFreshEntity(blade);
        }
    }
}
