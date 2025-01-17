package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.util.EMTabGroup;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.world.item.Rarity;

public class ItemGulingEye extends ItemFindStructureEye {
    public ItemGulingEye() {
        super(new Properties().tab(EMTabGroup.TABS).rarity(Rarity.RARE).stacksTo(16).fireResistant(), EMTagKey.EYE_OF_GULING, 0.619F, 0.788F, 0.94F);
    }
}
