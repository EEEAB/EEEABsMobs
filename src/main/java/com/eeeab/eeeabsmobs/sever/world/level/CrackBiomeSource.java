package com.eeeab.eeeabsmobs.sever.world.level;

import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.stream.Stream;

public class CrackBiomeSource extends BiomeSource {
    public static final Codec<CrackBiomeSource> CODEC = RecordCodecBuilder.create((p_255555_) ->
            p_255555_.group(RegistryOps.retrieveElement(ModResourceKey.PRINCIPAL_ISLAND_BIOME),
                            RegistryOps.retrieveElement(Biomes.END_HIGHLANDS),
                            RegistryOps.retrieveElement(Biomes.END_MIDLANDS),
                            RegistryOps.retrieveElement(Biomes.SMALL_END_ISLANDS),
                            RegistryOps.retrieveElement(Biomes.END_BARRENS))
                    .apply(p_255555_, p_255555_.stable(CrackBiomeSource::new)));
    private final Holder<Biome> end;
    private final Holder<Biome> highlands;
    private final Holder<Biome> midlands;
    private final Holder<Biome> islands;
    private final Holder<Biome> barrens;

    public static CrackBiomeSource create(HolderGetter<Biome> pBiomeGetter) {
        return new CrackBiomeSource(pBiomeGetter.getOrThrow(ModResourceKey.PRINCIPAL_ISLAND_BIOME),
                pBiomeGetter.getOrThrow(Biomes.END_HIGHLANDS), pBiomeGetter.getOrThrow(Biomes.END_MIDLANDS),
                pBiomeGetter.getOrThrow(Biomes.SMALL_END_ISLANDS), pBiomeGetter.getOrThrow(Biomes.END_BARRENS));
    }

    private CrackBiomeSource(Holder<Biome> p_220678_, Holder<Biome> p_220679_, Holder<Biome> p_220680_, Holder<Biome> p_220681_, Holder<Biome> p_220682_) {
        this.end = p_220678_;
        this.highlands = p_220679_;
        this.midlands = p_220680_;
        this.islands = p_220681_;
        this.barrens = p_220682_;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(this.end, this.highlands, this.midlands, this.islands, this.barrens);
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int pX, int pY, int pZ, Climate.Sampler pSampler) {
        int i = QuartPos.toBlock(pX);
        int j = QuartPos.toBlock(pY);
        int k = QuartPos.toBlock(pZ);
        int l = SectionPos.blockToSectionCoord(i);
        int i1 = SectionPos.blockToSectionCoord(k);
        if ((long) l * (long) l + (long) i1 * (long) i1 <= 384L) {
            return this.end;
        } else {
            int j1 = (SectionPos.blockToSectionCoord(i) * 2 + 1) * 8;
            int k1 = (SectionPos.blockToSectionCoord(k) * 2 + 1) * 8;
            double d0 = pSampler.erosion().compute(new DensityFunction.SinglePointContext(j1, j, k1));
            if (d0 > 0.25D) {
                return this.highlands;
            } else if (d0 >= -0.0625D) {
                return this.midlands;
            } else {
                return d0 < -0.21875D ? this.islands : this.barrens;
            }
        }
    }
}