package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.world.structure.StructureGuling;
import com.eeeab.eeeabsmobs.sever.world.structure.StructureBloodyAltar;
import com.eeeab.eeeabsmobs.sever.world.structure.StructureEMCustom;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructuresInit {
    private static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, EEEABMobs.MOD_ID);
    private static final DeferredRegister<StructurePieceType> PIECE_TYPE = DeferredRegister.create(Registries.STRUCTURE_PIECE, EEEABMobs.MOD_ID);
    public static final RegistryObject<StructureType<StructureEMCustom>> CONFIG_STRUCTURES = STRUCTURES.register("em_config", () -> explicitStructureTypeTyping(StructureEMCustom.CODEC));
    public static final RegistryObject<StructureType<StructureGuling>> GULING_STRUCTURE = STRUCTURES.register("guling", () -> explicitStructureTypeTyping(StructureGuling.CODEC));
    public static final RegistryObject<StructureType<StructureBloodyAltar>> BLOODY_ALTAR_STRUCTURE = STRUCTURES.register("bloody_altar", () -> explicitStructureTypeTyping(StructureBloodyAltar.CODEC));
    public static final RegistryObject<StructurePieceType> GUL = PIECE_TYPE.register("guling", () -> StructureGuling.Piece::new);
    public static final RegistryObject<StructurePieceType> BA = PIECE_TYPE.register("bloody_altar", () -> StructureBloodyAltar.Piece::new);

    /**
     * Originally, I had a double lambda ()->()-> for the RegistryObject line above, but it turns out that
     * some IDEs cannot resolve the typing correctly. This method explicitly states what the return type
     * is so that the IDE can put it into the DeferredRegistry properly.
     */
    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }

    public static void register(IEventBus iEventBus) {
        STRUCTURES.register(iEventBus);
        PIECE_TYPE.register(iEventBus);
    }
}
