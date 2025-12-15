package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
import net.minecraft.world.item.Rarity;

public class ItemBloodyAltarEye extends ItemFindStructureEye {
    public ItemBloodyAltarEye() {
        super(new Properties().rarity(Rarity.RARE).stacksTo(16).fireResistant(), ModTagKey.EYE_OF_BLOODY_ALTAR, 0.58F, 0.27F, 0.27F);
    }
}
