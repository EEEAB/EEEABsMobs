package com.eeeab.eeeabsmobs.sever.world.structure;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityGulingSentinelHeavy;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalShaman;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.StructuresInit;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Map;
import java.util.Optional;

public class StructureGuling extends Structure {
    public static final Codec<StructureGuling> CODEC = simpleCodec(StructureGuling::new);
    private static final ResourceLocation ANCIENT_TOMB_1 = new ResourceLocation(EEEABMobs.MOD_ID, "guling_1");
    private static final ResourceLocation ANCIENT_TOMB_2 = new ResourceLocation(EEEABMobs.MOD_ID, "guling_2");
    private static final ResourceLocation ANCIENT_TOMB_3 = new ResourceLocation(EEEABMobs.MOD_ID, "guling_3");
    private static final ResourceLocation ANCIENT_TOMB_4 = new ResourceLocation(EEEABMobs.MOD_ID, "guling_4");
    private static final Map<ResourceLocation, BlockPos> OFFSET = new ImmutableMap.Builder<ResourceLocation, BlockPos>()
            .put(ANCIENT_TOMB_1, new BlockPos(0, 1, 0))
            .put(ANCIENT_TOMB_2, new BlockPos(0, 1, 0))
            .put(ANCIENT_TOMB_3, new BlockPos(0, 1, 0))
            .put(ANCIENT_TOMB_4, new BlockPos(0, 1, 0))
            .build();


    protected StructureGuling(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        BlockPos blockPos = new BlockPos(context.chunkPos().getMinBlockX(), -64, context.chunkPos().getMinBlockZ());
        return Optional.of(new GenerationStub(blockPos, (value) -> {
            generatePieces(value, context, blockPos);
        }));
    }

    private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context, BlockPos blockpos) {
        Rotation rotation = Rotation.NONE;
        start(context.structureTemplateManager(), blockpos, rotation, builder, context.random());
    }

    //拼接解构
    private static void start(StructureTemplateManager manager, BlockPos pos, Rotation rotation, StructurePiecesBuilder builder, WorldgenRandom random) {
        int x = pos.getX();
        int z = pos.getZ();
        BlockPos rotationOffset = (new BlockPos(27, 1, 27)).rotate(rotation);
        BlockPos blockPos = rotationOffset.offset(x, pos.getY(), z);
        builder.addPiece(new Piece(manager, ANCIENT_TOMB_1, rotation, blockPos));

        rotationOffset = new BlockPos(1, 1, 27).rotate(rotation);
        blockPos = rotationOffset.offset(x, pos.getY(), z);
        builder.addPiece(new Piece(manager, ANCIENT_TOMB_2, rotation, blockPos));

        rotationOffset = new BlockPos(1, 1, 1).rotate(rotation);
        blockPos = rotationOffset.offset(x, pos.getY(), z);
        builder.addPiece(new Piece(manager, ANCIENT_TOMB_3, rotation, blockPos));

        rotationOffset = new BlockPos(27, 1, 1).rotate(rotation);
        blockPos = rotationOffset.offset(x, pos.getY(), z);
        builder.addPiece(new Piece(manager, ANCIENT_TOMB_4, rotation, blockPos));
    }


    @Override
    public StructureType<?> type() {
        return StructuresInit.GULING_STRUCTURE.get();
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public static class Piece extends TemplateStructurePiece {
        public Piece(StructureTemplateManager manager, ResourceLocation location, Rotation rotation, BlockPos blockPos) {
            super(StructuresInit.GUL.get(), 0, manager, location, location.toString(), makeSettings(rotation), makePosition(location, blockPos));
        }

        public Piece(StructureTemplateManager templateManagerIn, CompoundTag tagCompound) {
            super(StructuresInit.GUL.get(), tagCompound, templateManagerIn, (location) -> makeSettings(Rotation.valueOf(tagCompound.getString("Rot"))));
        }

        public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
            this(context.structureTemplateManager(), tag);
        }


        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            BlockIgnoreProcessor blockignoreprocessor = BlockIgnoreProcessor.STRUCTURE_BLOCK;
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(blockignoreprocessor).addProcessor(new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE));
        }

        private static BlockPos makePosition(ResourceLocation location, BlockPos blockPos) {
            return blockPos.offset(StructureGuling.OFFSET.get(location));
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
            super.addAdditionalSaveData(context, compoundTag);
            compoundTag.putString("Rot", this.placeSettings.getRotation().name());
        }


        @Override
        protected void handleDataMarker(String function, BlockPos blockPos, ServerLevelAccessor levelAccessor, RandomSource source, BoundingBox box) {
            if ("boss_spawn".equals(function)) {
                levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                EntityNamelessGuardian guardian = (EntityInit.NAMELESS_GUARDIAN.get()).create(levelAccessor.getLevel());
                if (guardian != null) {
                    guardian.moveTo(blockPos, 0, 0);
                    guardian.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
                    levelAccessor.addFreshEntity(guardian);
                }
            }

            if ("elite_spawn".equals(function)) {
                levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                EntityGulingSentinelHeavy heavy = (EntityInit.GULING_SENTINEL_HEAVY.get()).create(levelAccessor.getLevel());
                if (heavy != null) {
                    heavy.moveTo(blockPos, 0, 0);
                    heavy.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
                    levelAccessor.addFreshEntity(heavy);
                }
            }
        }

    }
}
