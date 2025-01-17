package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.util.EMTabGroup;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.world.item.Rarity;

public class ItemBloodyAltarEye extends ItemFindStructureEye {
    public ItemBloodyAltarEye() {
        super(new Properties().tab(EMTabGroup.TABS).rarity(Rarity.RARE).stacksTo(16).fireResistant(), EMTagKey.EYE_OF_BLOODY_ALTAR, 0.58F, 0.27F, 0.27F);
    }
}
