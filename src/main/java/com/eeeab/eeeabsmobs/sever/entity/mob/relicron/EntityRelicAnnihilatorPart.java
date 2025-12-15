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
        if (!this.isInvulnerableTo(source) && (ModEntityUtils.isProjectileSource(source) || ModEntityUtils.checkDirectEntityConsistency(source))) {
            if (damage > 1F && this.enabled() && this.entity.hurt(source, damage, true)) {
                this.entity.setBlind(true);
                this.entity.playAnimation(this.entity.stunAnimation);
                return true;
            }
        }
        return false;
    }

    private boolean enabled() {
        int tick = this.entity.getAnimationTick();
        Animation animation = this.entity.getAnimation();
        if (animation == this.entity.laserAnimation && tick > 10) return true;
        if (animation == this.entity.trickshot1Animation && tick >= 15) return true;
        if (animation == this.entity.shot1Animation && tick >= 20) return true;
        return animation == this.entity.shot2Animation || animation == this.entity.trickshot2Animation;
    }
}
