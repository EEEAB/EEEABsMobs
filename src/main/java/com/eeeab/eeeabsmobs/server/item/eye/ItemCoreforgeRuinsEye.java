package com.eeeab.eeeabsmobs.server.item.eye;

import com.eeeab.eeeabsmobs.server.init.ParticleInit;
import com.eeeab.eeeabsmobs.server.util.ModTagKey;
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
