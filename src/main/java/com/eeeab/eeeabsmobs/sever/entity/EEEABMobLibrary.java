package com.eeeab.eeeabsmobs.sever.entity;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * <b>EEEABMobLibrary</b><br/>
 */
public abstract class EEEABMobLibrary extends EEEABMobEntity implements EMAnimatedEntity, IEntityAdditionalSpawnData {
    private int animationTick;
    private final Animation noAnimation = Animation.create(0);
    private Animation animation = noAnimation;
    public boolean canplayHurtAnimation = true;//可以播放受伤动画
    public boolean hurtInterruptsAnimation = false;//伤害中断其他动画


    public EEEABMobLibrary(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
    }

    public Animation getDeathAnimation() {
        return this.noAnimation;
    }

    public Animation getHurtAnimation() {
        return this.noAnimation;
    }


    @Override
    public boolean hurt(DamageSource source, float damage) {
        boolean attack = super.hurt(source, damage);
        if (attack && this.getHealth() > 0.0F && (this.isNoAnimation() || this.hurtInterruptsAnimation) && this.canplayHurtAnimation) {
            this.playAnimation(this.getHurtAnimation());
        }
        return attack;
    }

    @Override
    public void tick() {
        super.tick();
        this.checkAnimationLegality();
    }

    private void checkAnimationLegality() {
        if (!EMConfigHandler.COMMON.OTHER.enableAnimationLegalityLogPrint.get()) return;
        if (this.tickCount % 200 == 0) {
            Animation[] animations = this.getAnimations();
            if (animations != null && this.isAlive()) {
                List<Animation> filterAnimations = Arrays.stream(animations).filter(a -> a != this.noAnimation && a != this.getAnimation()
                        && a.isStarted() && !a.isSuperposition()).toList();
                for (Animation animation : filterAnimations) {
                    EEEABMobs.LOGGER.warn("{} → there is illegal action data: Mob= {} Animation= {}[{}]",
                            this.level().isClientSide ? "Client" : "Server",
                            this.getName().getString(), animation,
                            ArrayUtils.indexOf(this.getAnimations(), animation));
                }
            }
        }
    }

    @Override
    protected void dying() {
        if (this.getAnimation() != this.getDeathAnimation()) {
            this.stopAllSuperpositionAnimation();
            this.playAnimation(this.getDeathAnimation());
        }
    }

    @Override
    protected int getDeathDuration() {
        Animation death;
        if ((death = getDeathAnimation()) != this.getNoAnimation()) {
            return death.getDuration();
        }
        return 20;
    }

    public void playAnimation(Animation animation) {
        if (animation != null) EMAnimationHandler.INSTANCE.sendEMAnimationMessage(this, animation);
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        if (animation != this.getNoAnimation()) {
            onAnimationStart(animation);
        } else {
            onAnimationFinish(this.animation);
        }
        this.animation = animation;
    }

    @Override
    public @NotNull Animation getNoAnimation() {
        return this.noAnimation;
    }

    public boolean isNoAnimation() {
        return this.animation == this.getNoAnimation();
    }

    public void stopAllSuperpositionAnimation() {
        EMAnimationHandler.INSTANCE.sendEMAnimationMessage(this, true);
    }

    protected void onAnimationStart(Animation animation) {
    }

    protected void onAnimationFinish(Animation animation) {
    }

    //用于在生成时需要在服务器和客户端之间进行通信的额外信息的实体的接口
    //由服务器在构造生成数据包时调用。应将数据添加到提供的流中
    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(ArrayUtils.indexOf(this.getAnimations(), this.getAnimation()));
        buffer.writeInt(this.getAnimationTick());
    }

    //客户端在收到实体生成数据包时调用。应以与写入数据相同的方式从流中读出数据
    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        yRotO = getYRot();
        yBodyRotO = yBodyRot = yHeadRotO = yHeadRot;
        int animOrdinal = additionalData.readInt();
        int animTick = additionalData.readInt();
        this.setAnimation(animOrdinal == -1 ? this.noAnimation : this.getAnimations()[animOrdinal]);
        this.setAnimationTick(animTick);
    }
}
