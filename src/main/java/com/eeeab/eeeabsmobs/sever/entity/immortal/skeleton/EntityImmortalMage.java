package com.eeeab.eeeabsmobs.sever.entity.immortal.skeleton;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.ai.AnimationRangeAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalGolem;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityImmortalMage extends EntityAbsImmortalSkeleton {
    public EntityImmortalMage(EntityType<? extends EntityImmortalMage> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationAI<>(this, true, true) {

            @Override
            protected boolean test(Animation animation) {
                return animation == this.entity.castAnimation;
            }

            @Override
            public void tick() {
                int tick = this.entity.getAnimationTick();
                LivingEntity target = this.entity.getTarget();
                if (target != null) {
                    this.entity.getLookControl().setLookAt(target, 30F, 30F);
                    if (tick == 30) {
                        double minY = Math.min(target.getY(), this.entity.getY());
                        double maxY = Math.max(target.getY(), this.entity.getY()) + 1.0D;
                        Vec3 point1 = ModEntityUtils.checkSummonEntityPoint(this.entity, this.entity.getX() - 1, this.entity.getZ(), minY, maxY);
                        summonEntity(point1);
                        Vec3 point2 = ModEntityUtils.checkSummonEntityPoint(this.entity, this.entity.getX() + 1, this.entity.getZ(), minY, maxY);
                        summonEntity(point2);
                    }
                }
            }

            private void summonEntity(Vec3 vec3) {
                EntityImmortalGolem entity = EntityInit.IMMORTAL_GOLEM.get().create(this.entity.level());
                if (!this.entity.level().isClientSide && entity != null && vec3 != null) {
                    entity.setInitSpawn();
                    entity.finalizeSpawn((ServerLevel) this.entity.level(), this.entity.level().getCurrentDifficultyAt(BlockPos.containing(vec3.x, vec3.y, vec3.z)), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                    entity.setOwner(this.entity);
                    entity.setSummonAliveTime(20 * (15 + this.entity.random.nextInt(15)));
                    Difficulty difficulty = this.entity.level().getDifficulty();
                    entity.setDangerous(this.entity.random.nextInt(10 - difficulty.getId()) == 0);
                    entity.setPos(vec3);
                    level().addFreshEntity(entity);
                }
            }
        });
        this.goalSelector.addGoal(3, new AnimationRangeAI<>(this, 1D, 300 + this.random.nextInt(100), 16F, null, e -> e.active, () -> castAnimation));
        super.registerCustomGoals();
    }

    @Override
    public Animation getHurtAnimation() {
        return NO_ANIMATION;
    }

    @Override
    protected int getCareerId() {
        return CareerType.MAGE.id;
    }
}
