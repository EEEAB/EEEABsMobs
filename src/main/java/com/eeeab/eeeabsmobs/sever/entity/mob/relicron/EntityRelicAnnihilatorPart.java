package com.eeeab.eeeabsmobs.sever.entity.mob.relicron;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.ModEntityPart;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import net.minecraft.world.damagesource.DamageSource;

public class EntityRelicAnnihilatorPart extends ModEntityPart<EntityRelicAnnihilator> {
    public EntityRelicAnnihilatorPart(EntityRelicAnnihilator parent, String partName, float width, float height) {
        super(parent, partName, width, height);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!this.isInvulnerableTo(source) && (ModEntityUtils.isProjectileSource(source) || !source.isIndirect())) {
            if (damage > 1F && this.enabled() && this.entity.hurt(source, damage, true)) {
                this.entity.setBlind(true);
                this.entity.playAnimation(EntityRelicAnnihilator.STUN_ANIMATION);
                return true;
            }
        }
        return false;
    }

    private boolean enabled() {
        int tick = this.entity.getAnimationTick();
        Animation animation = this.entity.getAnimation();
        if (EntityRelicAnnihilator.LASER_ANIMATION == animation && tick > 10) return true;
        if (EntityRelicAnnihilator.TRICKSHOT_ANIMATION1 == animation && tick >= 15) return true;
        if (EntityRelicAnnihilator.SHOT_ANIMATION1 == animation && tick >= 20) return true;
        return EntityRelicAnnihilator.SHOT_ANIMATION2 == animation || EntityRelicAnnihilator.TRICKSHOT_ANIMATION2 == animation;
    }
}
