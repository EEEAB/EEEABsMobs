package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.world.item.Rarity;

public class ItemGulingEye extends ItemFindStructureEye {
    public ItemGulingEye() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(16), EMTagKey.EYE_OF_GULING, 0.05F, 0.15F, 0.255F);
    }
}
