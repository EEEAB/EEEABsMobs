package com.eeeab.eeeabsmobs.sever.entity.impl.test;

import com.eeeab.eeeabsmobs.sever.entity.ai.control.EEBodyRotationControl;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationAttackGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.navigate.EEPathNavigateGround;
import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class EntityTest extends EEEABMobLibrary {
    public static final Animation ATTACK_ANIMATION = Animation.create(20);
    private int attackTick;
    private boolean attacking;
    private static final Animation[] ANIMATIONS = {
            ATTACK_ANIMATION
    };

    public EntityTest(EntityType<? extends EntityTest> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8F));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationAttackGoal<>(this, ATTACK_ANIMATION, 12, 2.5f, 1f, 1f));
    }

    @Override
    public void tick() {
        super.tick();
        this.setYRot(this.yBodyRot);
        AnimationHandler.INSTANCE.updateAnimations(this);
        MeleeAttackAI();
    }

    private void MeleeAttackAI() {
        if (!this.level().isClientSide && this.getTarget() != null && !this.getTarget().isAlive()) setTarget(null);

        if (attackTick > 0) {
            attackTick--;
        }

        if (this.getTarget() != null) {
            LivingEntity target = getTarget();
            this.getLookControl().setLookAt(target, 30F, 30F);
            if (targetDistance > 6) {
                this.getNavigation().moveTo(target, 1.0D);
            }
            if (attackTick <= 0 && getSensing().hasLineOfSight(target)) {
                attacking = true;
                if (getAnimation() == NO_ANIMATION) {
                    this.getNavigation().moveTo(target, 1.0D);
                }
            }

            if (attacking && getAnimation() == NO_ANIMATION && getSensing().hasLineOfSight(target)) {
                if (targetDistance < 3) {
                    this.playAnimation(ATTACK_ANIMATION);
                    attackTick = 20;
                    attacking = false;
                }
            }
        } else {
            attacking = false;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        SpawnGroupData data = super.finalizeSpawn(accessor, instance, spawnType, spawnGroupData, compoundTag);
        this.populateDefaultEquipmentSlots(getRandom(), instance);
        return data;
    }

    protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance instance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
        this.setDropChance(EquipmentSlot.OFFHAND, 0);

    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    @NotNull
    protected BodyRotationControl createBodyControl() {
        return new EEBodyRotationControl(this);
    }

    @Override
    @NotNull
    protected PathNavigation createNavigation(Level level) {
        return new EEPathNavigateGround(this, level);
    }
}
