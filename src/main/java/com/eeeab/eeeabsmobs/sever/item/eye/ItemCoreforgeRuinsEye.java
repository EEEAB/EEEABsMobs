package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
import net.minecraft.world.item.Rarity;

public class ItemCoreforgeRuinsEye extends ItemFindStructureEye {
    public ItemCoreforgeRuinsEye() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(16).fireResistant(), ModTagKey.EYE_OF_CORE_BASTION, 0.619F, 0.788F, 0.94F);
    }
}
