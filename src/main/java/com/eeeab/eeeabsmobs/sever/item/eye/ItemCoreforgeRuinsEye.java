package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
import net.minecraft.core.particles.ParticleOptions;

public class ItemCoreforgeRuinsEye extends ItemFindStructureEye {
    public ItemCoreforgeRuinsEye() {
        super(new Properties().stacksTo(16).fireResistant(), ModTagKey.EYE_OF_COREFORGE_RUINS);
    }

    @Override
    public ParticleOptions getTrailParticle() {
        return ParticleInit.GUARDIAN_SPARK.get();
    }
}
