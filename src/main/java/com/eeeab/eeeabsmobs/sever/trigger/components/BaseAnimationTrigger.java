package com.eeeab.eeeabsmobs.sever.trigger.components;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.eeeabsmobs.sever.trigger.TriggerContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class BaseAnimationTrigger extends BaseCombatTrigger {
    private ResourceLocation targetId;

    public BaseAnimationTrigger(ResourceLocation entityId, int promptLevel, boolean oncePerEncounter) {
        super(null, entityId, promptLevel, oncePerEncounter);
    }

    @Override
    public boolean shouldTrigger(ServerPlayer player, ResourceLocation entityId, TriggerContext context) {
        if (targetId == null) {
            if (context.getEntity() instanceof AnimatedEntity entity) {
                targetId = new ResourceLocation(entityId.getNamespace(), entityId.getPath() + "_" + entity.getAnimation().getHintId());
            }
        }
        double range = context.getFollowRange();
        return targetId != null && player.distanceToSqr(context.getEntity()) <= (range * range);
    }

    @Override
    public ResourceLocation getTriggerKey() {
        return targetId;
    }

    @Override
    protected boolean checkSpecificConditions(ServerPlayer player, TriggerContext context) {
        return true;
    }
}
