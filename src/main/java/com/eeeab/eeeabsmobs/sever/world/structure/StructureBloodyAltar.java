package com.eeeab.eeeabsmobs.sever.world.structure;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseWarlock;
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
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Map;
import java.util.Optional;

public class StructureBloodyAltar extends Structure {
    public static final Codec<StructureBloodyAltar> CODEC = simpleCodec(StructureBloodyAltar::new);
    private static final ResourceLocation BLOODY_ALTAR = new ResourceLocation(EEEABMobs.MOD_ID, "bloody_altar");
    private static final Map<ResourceLocation, BlockPos> OFFSET = new ImmutableMap.Builder<ResourceLocation, BlockPos>()
            .put(BLOODY_ALTAR, new BlockPos(0, 1, 0))
            .build();

    protected StructureBloodyAltar(StructureSettings settings) {
        super(settings);
    }

    public static void generatePieces(StructureTemplateManager templateManager, BlockPos pos, Rotation rotation, StructurePieceAccessor pieceList, RandomSource random) {
        int x = pos.getX();
        int z = pos.getZ();
        BlockPos rotationOffSet = (new BlockPos(0, 0, 0)).rotate(rotation);
        BlockPos blockpos = rotationOffSet.offset(x, pos.getY(), z);
        pieceList.addPiece(new Piece(templateManager, BLOODY_ALTAR, rotation, blockpos));
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        int i = context.chunkPos().x >> 16;
        int j = context.chunkPos().z >> 16;
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setSeed((long) (i ^ j << 9) ^ context.seed());
        worldgenrandom.nextInt();
        StructureTemplate structuretemplate = context.structureTemplateManager().getOrCreate(BLOODY_ALTAR);
        BlockPos blockpos = new BlockPos(structuretemplate.getSize().getX() / 2, 0, structuretemplate.getSize().getZ() / 2);
        BlockPos blockPos1 = context.chunkPos().getWorldPosition();
        BlockPos blockPos2 = new BlockPos(blockPos1.getX(), 31, blockPos1.getZ());
        return Optional.of(new Structure.GenerationStub(blockpos, (builder) -> {
            generatePieces(context.structureTemplateManager(), blockPos2, Rotation.getRandom(context.random()), builder, context.random());
        }));
    }

    @Override
    public StructureType<?> type() {
        return StructuresInit.BLOODY_ALTAR_STRUCTURE.get();
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public void afterPlace(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, PiecesContainer pieces) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = level.getMinBuildHeight();//获取当前维度最低Y轴
        BoundingBox boundingbox = pieces.calculateBoundingBox();
        int j = boundingbox.minY();//获取当前结构最低Y轴

        //填充结构地下空气方块(包含流体)
        for(int k = box.minX(); k <= box.maxX(); ++k) {
            for(int l = box.minZ(); l <= box.maxZ(); ++l) {
                blockpos$mutableblockpos.set(k, j, l);
                if (!level.isEmptyBlock(blockpos$mutableblockpos) && boundingbox.isInside(blockpos$mutableblockpos) && pieces.isInsidePiece(blockpos$mutableblockpos)) {
                    for(int i1 = j - 1; i1 > i; --i1) {
                        blockpos$mutableblockpos.setY(i1);
                        if (!level.isEmptyBlock(blockpos$mutableblockpos) && !level.getBlockState(blockpos$mutableblockpos).liquid()) {
                            break;
                        }
                        level.setBlock(blockpos$mutableblockpos, Blocks.BLACKSTONE.defaultBlockState(), 2);
                    }
                }
            }
        }

    }

    public static class Piece extends TemplateStructurePiece {
        public Piece(StructureTemplateManager manager, ResourceLocation location, Rotation rotation, BlockPos pos) {
            super(StructuresInit.BA.get(), 0, manager, location, location.toString(), makeSettings(rotation), makePosition(location, pos));
        }

        public Piece(StructureTemplateManager templateManagerIn, CompoundTag tagCompound) {
            super(StructuresInit.BA.get(), tagCompound, templateManagerIn, (location) -> makeSettings(Rotation.valueOf(tagCompound.getString("Rot"))));
        }

        public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
            this(context.structureTemplateManager(), tag);
        }

        private static BlockPos makePosition(ResourceLocation location, BlockPos blockPos) {
            return blockPos.offset(StructureBloodyAltar.OFFSET.get(location));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            BlockIgnoreProcessor blockignoreprocessor = BlockIgnoreProcessor.STRUCTURE_BLOCK;
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(blockignoreprocessor).addProcessor(new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE));
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
            super.addAdditionalSaveData(context, compoundTag);
            compoundTag.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String function, BlockPos blockPos, ServerLevelAccessor levelAccessor, RandomSource source, BoundingBox box) {
            if ("elite_spawn".equals(function)) {
                levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                EntityCorpseWarlock warlock = (EntityInit.CORPSE_WARLOCK.get()).create(levelAccessor.getLevel());
                if (warlock != null) {
                    warlock.moveTo(blockPos, 0, 0);
                    warlock.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
                    warlock.setRestPos(blockPos);
                    levelAccessor.addFreshEntity(warlock);
                }
            }
        }
    }
}
