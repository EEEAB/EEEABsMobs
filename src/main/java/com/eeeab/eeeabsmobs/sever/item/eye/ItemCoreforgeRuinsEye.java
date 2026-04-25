package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.Rarity;

public class ItemCoreforgeRuinsEye extends ItemFindStructureEye {
    public ItemCoreforgeRuinsEye() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(16).fireResistant(), ModTagKey.EYE_OF_COREFORGE_RUINS);
    }

    @Override
    public ParticleOptions getTrailParticle() {
        return ParticleInit.GUARDIAN_SPARK.get();
    }
}
