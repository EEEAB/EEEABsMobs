package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.world.item.Rarity;

public class ItemBloodyAltarEye extends ItemFindStructureEye {
    public ItemBloodyAltarEye() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(16).fireResistant(), EMTagKey.EYE_OF_BLOODY_ALTAR, 0.255F, 0.05F, 0.05F);
    }
}
