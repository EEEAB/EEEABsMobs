package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.world.item.Rarity;

public class ItemAncientTombEye extends ItemFindStructureEye {
    public ItemAncientTombEye() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(16), EMTagKey.EYE_OF_ANCIENT_TOMB, 0.047F, 0.146F, 0.179F);
    }
}
