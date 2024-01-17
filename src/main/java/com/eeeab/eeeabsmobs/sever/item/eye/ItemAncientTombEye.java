package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.util.EETabGroup;
import com.eeeab.eeeabsmobs.sever.util.EETagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ItemAncientTombEye extends ItemFindStructureEye {

    //new Properties().tab(EETabGroup.TABS).rarity(Rarity.RARE).stacksTo(16)
    public ItemAncientTombEye(Item.Properties properties) {
        super(properties, EETagKey.EYE_OF_ANCIENT_TOMB, 0.047F, 0.146F, 0.179F);
    }
}
