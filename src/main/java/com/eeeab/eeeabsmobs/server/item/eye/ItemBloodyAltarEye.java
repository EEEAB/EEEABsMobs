package com.eeeab.eeeabsmobs.server.item.eye;

import com.eeeab.eeeabsmobs.server.init.ParticleInit;
import com.eeeab.eeeabsmobs.server.util.ModTagKey;
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
