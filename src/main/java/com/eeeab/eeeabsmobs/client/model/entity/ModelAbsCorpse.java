package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityAbsCorpse;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpse;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public abstract class ModelAbsCorpse<T extends EntityAbsCorpse> extends EMCanSpawnEntityModel<T> {

    @Override
    protected Animation getSpawnAnimation() {
        return EntityCorpse.SPAWN_ANIMATION;
    }

    @Override
    protected double handsOffset() {
        return -90;
    }
}
