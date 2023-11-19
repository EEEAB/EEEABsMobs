package com.eeeab.eeeabsmobs.sever.world.structure;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortalKnight;
import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortalShaman;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.StructuresInit;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
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

public class AncientTombStructure extends Structure {
    public static final Codec<AncientTombStructure> CODEC = simpleCodec(AncientTombStructure::new);
    private static final ResourceLocation ANCIENT_TOMB_1 = new ResourceLocation(EEEABMobs.MOD_ID, "ancient_tomb_1");
    private static final ResourceLocation ANCIENT_TOMB_2 = new ResourceLocation(EEEABMobs.MOD_ID, "ancient_tomb_2");
    private static final ResourceLocation ANCIENT_TOMB_3 = new ResourceLocation(EEEABMobs.MOD_ID, "ancient_tomb_3");
    private static final ResourceLocation ANCIENT_TOMB_4 = new ResourceLocation(EEEABMobs.MOD_ID, "ancient_tomb_4");
    private static final Map<ResourceLocation, BlockPos> OFFSET = new ImmutableMap.Builder<ResourceLocation, BlockPos>()
            .put(ANCIENT_TOMB_1, new BlockPos(0, 1, 0))
            .put(ANCIENT_TOMB_2, new BlockPos(0, 1, 0))
            .put(ANCIENT_TOMB_3, new BlockPos(0, 1, 0))
            .put(ANCIENT_TOMB_4, new BlockPos(0, 1, 0))
            .build();


    protected AncientTombStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        BlockPos blockPos = new BlockPos(context.chunkPos().getMinBlockX(), -64, context.chunkPos().getMinBlockZ());
        return Optional.of(new GenerationStub(blockPos, (value) -> {
            generatePieces(value, context);
        }));
    }

    private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), -64, context.chunkPos().getMinBlockZ());
        //Rotation rotation = Rotation.getRandom(context.random());
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
        return StructuresInit.ANCIENT_TOMB_STRUCTURE.get();
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public static class Piece extends TemplateStructurePiece {
        public Piece(StructureTemplateManager manager, ResourceLocation location, Rotation rotation, BlockPos blockPos) {
            super(StructuresInit.ATP.get(), 0, manager, location, location.toString(), makeSettings(rotation), makePosition(location, blockPos));
        }

        public Piece(StructureTemplateManager templateManagerIn, CompoundTag tagCompound) {
            super(StructuresInit.ATP.get(), tagCompound, templateManagerIn, (location) -> makeSettings(Rotation.valueOf(tagCompound.getString("Rot"))));
        }

        public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
            this(context.structureTemplateManager(), tag);
        }


        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            BlockIgnoreProcessor blockignoreprocessor = BlockIgnoreProcessor.STRUCTURE_BLOCK;
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(blockignoreprocessor).addProcessor(new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE));
        }

        private static BlockPos makePosition(ResourceLocation location, BlockPos blockPos) {
            return blockPos.offset(AncientTombStructure.OFFSET.get(location));
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
            super.addAdditionalSaveData(context, compoundTag);
            compoundTag.putString("Rot", this.placeSettings.getRotation().name());
        }


        @Override
        protected void handleDataMarker(String function, BlockPos blockPos, ServerLevelAccessor levelAccessor, RandomSource source, BoundingBox box) {
            if ("nameless_guardian_spawn".equals(function)) {
                levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                EntityNamelessGuardian guardian = (EntityInit.NAMELESS_GUARDIAN.get()).create(levelAccessor.getLevel());
                if (guardian != null) {
                    guardian.moveTo(blockPos, 180, 180);
                    //guardian.moveTo(blockPos.getCenter());
                    guardian.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
                    levelAccessor.addFreshEntity(guardian);
                }
            }

            if ("painting".equals(function)) {
                levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                ResourceKey<PaintingVariant>[] variants = new ResourceKey[]{PaintingVariants.SKULL_AND_ROSES, PaintingVariants.WITHER, PaintingVariants.FIRE};
                Optional<Holder.Reference<PaintingVariant>> holder = BuiltInRegistries.PAINTING_VARIANT.getHolder(variants[levelAccessor.getRandom().nextInt(variants.length)]);
                if (holder.isPresent()) {
                    Painting painting = new Painting(levelAccessor.getLevel(), blockPos, this.placeSettings.getRotation().rotate(Direction.EAST), holder.get());
                    levelAccessor.addFreshEntity(painting);
                }
            }

            if ("immortal_shaman_spawn".equals(function)) {
                levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                EntityImmortalShaman shaman = (EntityInit.IMMORTAL_SHAMAN.get()).create(levelAccessor.getLevel());
                if (shaman != null) {
                    shaman.moveTo(blockPos.getCenter());
                    shaman.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
                    shaman.setYRot(shaman.getYRot() + 180);
                    levelAccessor.addFreshEntity(shaman);
                }
            }

            if ("immortal_knight_spawn".equals(function)) {
                levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                EntityImmortalKnight knight = (EntityInit.IMMORTAL_KNIGHT.get()).create(levelAccessor.getLevel());
                if (knight != null) {
                    knight.moveTo(blockPos.getCenter());
                    knight.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
                    knight.setYRot(knight.getYRot() + 180);
                    levelAccessor.addFreshEntity(knight);
                }
            }

            //TODO 暂未实装方块
            if ("key".equals(function)) {
                levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);

            }
        }

    }
}
