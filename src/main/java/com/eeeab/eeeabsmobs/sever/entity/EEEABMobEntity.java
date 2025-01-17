package com.eeeab.eeeabsmobs.sever.entity;

import com.eeeab.eeeabsmobs.client.sound.BossMusicPlayer;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.util.damage.DamageAdaptation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * <b>EEEABMobEntity</b><br/>
 */
public abstract class EEEABMobEntity extends PathfinderMob {
    private final EMBossInfoServer bossInfo = new EMBossInfoServer(this);
    private final DamageAdaptation intervalProtector;
    private DamageSource killDataCause;//死亡的伤害源
    public DamageSource lastDamageSource;//受到的伤害源
    public Player killDataAttackingPlayer;
    public float targetDistance = -1;//与实体距离
    public float targetAngle = -1;//目标与实体之间的角度
    public boolean active;
    public int frame;
    public boolean dropAfterDeathAnim = true;//死亡掉落动画
    public int killDataRecentlyHit;
    public LivingEntity blockEntity = null;
    private static final byte MAKE_POOF_ID = 60;
    private static final byte PLAY_BOSS_MUSIC_ID = 77;
    private static final byte STOP_BOSS_MUSIC_ID = 78;
    private static final UUID HEALTH_UUID = UUID.fromString("cca33d36-6842-43d8-b615-0cad4460a18a");
    private static final UUID ATTACK_UUID = UUID.fromString("e1b02986-1699-4120-a687-40419a294482");

    public EEEABMobEntity(EntityType<? extends EEEABMobEntity> type, Level level) {
        super(type, level);
        this.xpReward = this.getEntityReward().getXp();
        this.intervalProtector = new DamageAdaptation(50, 9, 0.35F, 0.9995F, true).setAdaptBypassesDamage(true);
        //加载配置文件并修改值
        EMConfigHandler.AttributeConfig config = this.getAttributeConfig();
        if (config != null) {
            AttributeInstance healthAttribute = this.getAttribute(Attributes.MAX_HEALTH);
            if (healthAttribute != null) {
                double finalValue = healthAttribute.getBaseValue() * config.healthMultiplier.get() - healthAttribute.getBaseValue();
                healthAttribute.addTransientModifier(new AttributeModifier(HEALTH_UUID, "Reset health by config", finalValue, AttributeModifier.Operation.ADDITION));
                this.setHealth(this.getMaxHealth());
            }
            AttributeInstance attackAttribute = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackAttribute != null) {
                double finalValue = attackAttribute.getBaseValue() * config.attackMultiplier.get() - attackAttribute.getBaseValue();
                attackAttribute.addTransientModifier(new AttributeModifier(ATTACK_UUID, "Reset attack damage by config", finalValue, AttributeModifier.Operation.ADDITION));
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource);
    }

    /**
     * @return 是否免疫爆炸
     */
    @Override
    public boolean ignoreExplosion() {
        return super.ignoreExplosion();
    }

    /**
     * @return 是否在实体上渲染着火效果
     */
    @Override
    public boolean displayFireAnimation() {
        return super.displayFireAnimation();
    }

    /**
     * @return 是否免疫摔伤
     */
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return super.causeFallDamage(fallDistance, multiplier, damageSource);
    }

    /**
     * @return 是否可以被添加药水效果
     */
    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return super.addEffect(effectInstance, entity);
    }

    @Override
    public void tick() {
        super.tick();
        frame++;
        if (getTarget() != null) {
            //获取当前目标实体的模型(减去模型宽度的1/2)与当前实体之间的距离
            targetDistance = distanceTo(getTarget()) - getTarget().getBbWidth() / 2f;
            targetAngle = (float) getAngleBetweenEntities(this, getTarget());
            //System.out.println("targetDistance: " + targetDistance+" || targetAngle: "+targetAngle);
        }
        if (!level.isClientSide) {
            //if (tickCount % 20 == 0) this.setFoundTarget(getTarget() != null);
            if (getBossMusic() != null) {
                if (!canPlayMusic()) {
                    this.level.broadcastEntityEvent(this, STOP_BOSS_MUSIC_ID);
                } else if (!canHandOffMusic()) {
                    this.level.broadcastEntityEvent(this, PLAY_BOSS_MUSIC_ID);
                }
            }
        }
    }


    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (tickCount % 4 == 0) bossInfo.update();
    }

    @Override
    public void setHealth(float health) {
        if (!this.level.isClientSide && health < this.getHealth()) {
            health = this.getNewHealthByCap(health, this.getDamageCap());
            this.lastDamageSource = null;
        }
        super.setHealth(health);
    }

    protected float getNewHealthByCap(float health, EMConfigHandler.DamageCapConfig config) {
        if (config != null) {
            float oldDamage = this.getHealth() - health;
            float newDamage = oldDamage;
            float damageCap = config.damageCap.get().floatValue();
            if (this.lastDamageSource == null) {
                newDamage = ModEntityUtils.actualDamageIsCalculatedBasedOnArmor(Math.min(oldDamage, damageCap), this.getArmorValue(), (float) this.getAttributeValue(Attributes.ARMOR_TOUGHNESS), 1F);
            } else if (!(this.lastDamageSource == DamageSource.OUT_OF_WORLD || this.lastDamageSource == DamageSource.GENERIC)) {
                newDamage = Math.min(oldDamage, damageCap);
            }
            if (this.intervalProtect()) newDamage = this.intervalProtector.damageAfterAdaptingOnce(this, this.lastDamageSource, newDamage);
            health = this.getHealth() - newDamage;
        }
        return health;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level.isClientSide) {
            this.lastDamageSource = source;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected final void tickDeath() {
        this.dying();
        ++this.deathTime;
        int deathDuration = getDeathDuration();
        if (this.deathTime >= deathDuration && !this.level.isClientSide()) {
            lastHurtByPlayer = killDataAttackingPlayer;
            lastHurtByPlayerTime = killDataRecentlyHit;
            if (dropAfterDeathAnim && killDataCause != null) {
                this.dropAllDeathLoot(killDataCause);
            }
            this.level.broadcastEntityEvent(this, (byte) 60);
            this.remove(RemovalReason.KILLED);
        }
    }

    protected void dying() {
    }

    protected int getDeathDuration() {
        return 20;
    }

    @Override
    protected void dropAllDeathLoot(DamageSource source) {
        if (!dropAfterDeathAnim || deathTime > 0) {
            super.dropAllDeathLoot(source);
        }
    }

    @Override
    public void die(DamageSource source) {
        if (!this.dead) {
            killDataCause = source;
            killDataRecentlyHit = this.lastHurtByPlayerTime;
            killDataAttackingPlayer = lastHurtByPlayer;
        }
        super.die(source);
        if (!this.isRemoved()) {
            bossInfo.update();
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return doHurtTarget(entity, 1.0F, 1.0F);
    }

    public boolean doHurtTarget(Entity entity, float damageMultiplier, float knockBackMultiplier) {
        return doHurtTarget(entity, damageMultiplier, knockBackMultiplier, false);
    }

    //复制自: net.minecraft.world.entity.Mob.doHurtTarget(Entity entity)
    public boolean doHurtTarget(Entity entity, float damageMultiplier, float knockBackMultiplier, boolean canDisableShield) {
        float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageMultiplier;
        float f1 = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK) * knockBackMultiplier;
        if (entity instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entity).getMobType());
            f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            entity.setSecondsOnFire(i * 4);
        }

        boolean flag = entity.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            if (f1 > 0.0F && entity instanceof LivingEntity) {
                ((LivingEntity) entity).knockback((double) (f1 * 0.5F), (double) Mth.sin(this.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(this.getYRot() * ((float) Math.PI / 180F))));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            if (entity instanceof Player player) {
                if (canDisableShield)
                    this.maybeDisableShield(player, this.getMainHandItem(), player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
            }

            this.doEnchantDamageEffects(this, entity);
            this.setLastHurtMob(entity);
        }

        return flag;
    }

    //复制自: net.minecraft.world.entity.Mob.maybeDisableShield(Player player, ItemStack mobItemStack, ItemStack playerItemStack)
    private void maybeDisableShield(Player player, ItemStack mobItemStack, ItemStack playerItemStack) {
        if (!mobItemStack.isEmpty() && !playerItemStack.isEmpty() && mobItemStack.getItem() instanceof AxeItem && playerItemStack.is(Items.SHIELD)) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.level.broadcastEntityEvent(player, (byte) 30);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == PLAY_BOSS_MUSIC_ID) {
            BossMusicPlayer.playBossMusic(this, getBossMusic());
        } else if (id == STOP_BOSS_MUSIC_ID) {
            BossMusicPlayer.stopBossMusic(this);
        } else if (id == MAKE_POOF_ID) {
            makePoofParticles();
        } else {
            super.handleEntityEvent(id);
        }
    }

    protected void makePoofParticles() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    protected void registerGoals() {
        this.registerCustomGoals();
    }

    protected void registerCustomGoals() {
    }

    protected boolean canBePushedByEntity(Entity entity) {
        return true;
    }

    @Override
    public void push(Entity entityIn) {
        if (!this.isSleeping()) {
            if (!this.isPassengerOfSameVehicle(entityIn)) {
                if (!entityIn.noPhysics && !this.noPhysics) {
                    double d0 = entityIn.getX() - this.getX();
                    double d1 = entityIn.getZ() - this.getZ();
                    double d2 = Mth.absMax(d0, d1);
                    if (d2 >= (double) 0.01F) {
                        d2 = Math.sqrt(d2);
                        d0 = d0 / d2;
                        d1 = d1 / d2;
                        double d3 = 1.0D / d2;
                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }

                        d0 = d0 * d3;
                        d1 = d1 * d3;
                        d0 = d0 * (double) 0.05F;
                        d1 = d1 * (double) 0.05F;
                        if (!this.isVehicle()) {
                            if (canBePushedByEntity(entityIn)) {
                                this.push(-d0, 0.0D, -d1);
                            }
                        }

                        if (!entityIn.isVehicle()) {
                            entityIn.push(d0, 0.0D, d1);
                        }
                    }

                }
            }
        }
    }

    /**
     * 初始化NBT数据
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    /**
     * 退出时保存自定义NBT(不然数据将会重置)
     */
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    /**
     * 加载世界时读写自定义NBT
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    public List<LivingEntity> getNearByLivingEntities(double range) {
        return this.getNearByEntities(LivingEntity.class, range, range, range, range);
    }

    public List<LivingEntity> getNearByLivingEntities(double rangeX, double height, double rangeZ, double radius) {
        return this.getNearByEntities(LivingEntity.class, rangeX, height, rangeZ, radius);
    }

    /**
     * @return 获取特定范围所有实体
     */
    public <T extends Entity> List<T> getNearByEntities(Class<T> entityClass, double x, double y, double z, double radius) {
        return level.getEntitiesOfClass(entityClass, getBoundingBox().inflate(x, y, z), targetEntity -> targetEntity != this &&
                distanceTo(targetEntity) <= radius + targetEntity.getBbWidth() / 2f && targetEntity.getY() <= getY() + y);
    }

    /**
     * @return 获取实体之间的角度
     */
    public double getAngleBetweenEntities(Entity attacker, Entity target) {
        return Math.atan2(target.getZ() - attacker.getZ(), target.getX() - attacker.getX()) * (180 / Math.PI) + 90;
    }

    /**
     * 推开其他实体
     */
    protected void pushEntitiesAway(float X, float Y, float Z, float radius) {
        List<LivingEntity> entityList = getNearByLivingEntities(X, Y, Z, radius);
        for (Entity entity : entityList) {
            if (!entity.noPhysics && entity.isPickable()) {
                double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                entity.setDeltaMovement(-0.10 * Math.cos(angle), entity.getDeltaMovement().y, -0.10 * Math.sin(angle));
            }
        }
    }

    /**
     * @return 指定坐标添加弧度
     */
    public Vec3 circlePosition(Vec3 targetVec3, float radius, float speed, boolean direction, int circleFrame, float offset) {
        double theta = (direction ? 1 : -1) * circleFrame * 0.5 * speed / radius + offset;
        return targetVec3.add(radius * Math.cos(theta), 0, radius * Math.sin(theta));
    }

    /**
     * 使目标短暂失去行动力
     *
     * @param source   效果源自实体
     * @param target   目标实体
     * @param duration 持续时间(tick)
     * @param force    是否强制添加
     */
    public void stun(@Nullable LivingEntity source, LivingEntity target, int duration, boolean force) {
        if (target.isBlocking() || this.isAlliedTo(target) || target instanceof Player player && (player.isSpectator() || player.isCreative())) return;
        ModEntityUtils.addEffectStackingAmplifier(source, target, EffectInit.VERTIGO_EFFECT.get(), duration, 1, false, false, true, true, force);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return null;
    }

    protected EMConfigHandler.DamageCapConfig getDamageCap() {
        return null;
    }

    protected boolean intervalProtect() {
        return false;
    }

    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_NORMAL;
    }

    protected boolean setDarkenScreen() {
        return false;
    }

    protected boolean showBossBloodBars() {
        return false;
    }

    protected BossEvent.BossBarColor bossBloodBarsColor() {
        return BossEvent.BossBarColor.PURPLE;
    }

    public float getHealthPercentage() {
        return (this.getHealth() / this.getMaxHealth()) * 100;
    }

    public SoundEvent getBossMusic() {
        return null;
    }

    protected boolean canPlayMusic() {
        return !isSilent() && getTarget() instanceof Player;
    }

    protected boolean canHandOffMusic() {
        return false;
    }

    public boolean canPlayerHearMusic(Player player) {
        return player != null
                && canAttack(player)
                && distanceTo(player) < 50 * 50;
    }
}