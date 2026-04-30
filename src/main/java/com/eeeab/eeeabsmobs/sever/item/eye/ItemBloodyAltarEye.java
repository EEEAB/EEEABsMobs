package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
import net.minecraft.core.particles.ParticleOptions;

public class ItemBloodyAltarEye extends ItemFindStructureEye {
    public ItemBloodyAltarEye() {
        super(new Properties().stacksTo(16).fireResistant(), ModTagKey.EYE_OF_BLOODY_ALTAR);
    }

    @Override
    public ParticleOptions getTrailParticle() {
        return ParticleInit.WARLOCK_HEAL.get();
    }
}
