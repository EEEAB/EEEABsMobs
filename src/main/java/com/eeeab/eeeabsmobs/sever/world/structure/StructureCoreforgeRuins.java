package com.eeeab.eeeabsmobs.sever.world.structure;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityAbsRelicron;
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

public class StructureCoreforgeRuins extends Structure {
    public static final Codec<StructureCoreforgeRuins> CODEC = simpleCodec(StructureCoreforgeRuins::new);
    private static final ResourceLocation ANCIENT_TOMB_1 = new ResourceLocation(EEEABMobs.MOD_ID, "coreforge_ruins1");
    private static final ResourceLocation ANCIENT_TOMB_2 = new ResourceLocation(EEEABMobs.MOD_ID, "coreforge_ruins2");
    private static final ResourceLocation ANCIENT_TOMB_3 = new ResourceLocation(EEEABMobs.MOD_ID, "coreforge_ruins3");
    private static final ResourceLocation ANCIENT_TOMB_4 = new ResourceLocation(EEEABMobs.MOD_ID, "coreforge_ruins4");
    private static final Map<ResourceLocation, BlockPos> OFFSET = new ImmutableMap.Builder<ResourceLocation, BlockPos>()
            .put(ANCIENT_TOMB_1, new BlockPos(0, 1, 0))
            .put(ANCIENT_TOMB_2, new BlockPos(0, 1, 0))
            .put(ANCIENT_TOMB_3, new BlockPos(0, 1, 0))
            .put(ANCIENT_TOMB_4, new BlockPos(0, 1, 0))
            .build();

    protected StructureCoreforgeRuins(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        BlockPos blockPos = new BlockPos(context.chunkPos().getMinBlockX(), -20, context.chunkPos().getMinBlockZ());
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
        BlockPos rotationOffset = (new BlockPos(1, 49, 1)).rotate(rotation);
        BlockPos blockPos = rotationOffset.offset(x, pos.getY(), z);
        builder.addPiece(new Piece(manager, ANCIENT_TOMB_1, rotation, blockPos));

        rotationOffset = new BlockPos(1, 1, -47).rotate(rotation);
        blockPos = rotationOffset.offset(x, pos.getY(), z);
        builder.addPiece(new Piece(manager, ANCIENT_TOMB_2, rotation, blockPos));

        rotationOffset = new BlockPos(1, 1, 1).rotate(rotation);
        blockPos = rotationOffset.offset(x, pos.getY(), z);
        builder.addPiece(new Piece(manager, ANCIENT_TOMB_3, rotation, blockPos));

        rotationOffset = new BlockPos(1, 1, 49).rotate(rotation);
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
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(blockignoreprocessor).addProcessor(new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)).setKeepLiquids(false);
        }

        private static BlockPos makePosition(ResourceLocation location, BlockPos blockPos) {
            return blockPos.offset(StructureCoreforgeRuins.OFFSET.get(location));
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
            super.addAdditionalSaveData(context, compoundTag);
            compoundTag.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String function, BlockPos blockPos, ServerLevelAccessor levelAccessor, RandomSource source, BoundingBox box) {
            if ("domain_warder_spawn".equals(function)) {
                spawnGulingEntity(EntityInit.REALM_WARDEN.get().create(levelAccessor.getLevel()), blockPos, levelAccessor);
            }

            if ("annihilator_spawn".equals(function)) {
                spawnGulingEntity(EntityInit.RELIC_ANNIHILATOR.get().create(levelAccessor.getLevel()), blockPos, levelAccessor);
            }

            if ("earthshaker_spawn".equals(function)) {
                spawnGulingEntity(EntityInit.RELIC_EARTHSHAKER.get().create(levelAccessor.getLevel()), blockPos, levelAccessor);
            }

            if ("ripper_spawn".equals(function)) {
                spawnGulingEntity(EntityInit.RELIC_RIPPER.get().create(levelAccessor.getLevel()), blockPos, levelAccessor);
            }

            if ("observer_spawn".equals(function)) {
                spawnGulingEntity(EntityInit.RELIC_OBSERVER.get().create(levelAccessor.getLevel()), blockPos, levelAccessor);
            }
        }

        private static void spawnGulingEntity(EntityAbsRelicron absGuling, BlockPos blockPos, ServerLevelAccessor levelAccessor) {
            levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
            if (absGuling != null) {
                absGuling.moveTo(blockPos, 0, 0);
                absGuling.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
                absGuling.setActive(false);
                absGuling.setAlwaysActive(false);
                levelAccessor.addFreshEntity(absGuling);
            }
        }
    }
}
