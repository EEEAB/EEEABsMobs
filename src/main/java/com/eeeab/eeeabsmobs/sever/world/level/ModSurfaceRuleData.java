package com.eeeab.eeeabsmobs.sever.world.level;

import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public class ModSurfaceRuleData {
    private static SurfaceRules.RuleSource makeStateRule(Block pBlock) {
        return SurfaceRules.state(pBlock.defaultBlockState());
    }

    public static SurfaceRules.RuleSource voidCrack() {
        SurfaceRules.RuleSource surface = makeStateRule(BlockInit.ERODED_SOIL.get());
        SurfaceRules.RuleSource middle = makeStateRule(BlockInit.BLIGHTED_STONE.get());
        SurfaceRules.RuleSource bottom = makeStateRule(BlockInit.DARK_EROSION_ROCK.get());
        SurfaceRules.ConditionSource conditionSource0 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(34), 0);
        SurfaceRules.ConditionSource conditionSource1 = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource conditionSource2 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(54), 0);
        SurfaceRules.ConditionSource dirtNoiseCondition = SurfaceRules.noiseCondition(Noises.SURFACE, -0.4, 0.4);
        SurfaceRules.RuleSource ruleSource = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(conditionSource0, SurfaceRules.ifTrue(conditionSource1, surface)))),
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(conditionSource2, SurfaceRules.ifTrue(dirtNoiseCondition, surface)), middle,
                        SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, true, 4, CaveSurface.FLOOR), middle)
                )
        );
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("dark_erosion_rock", VerticalAnchor.absolute(24), VerticalAnchor.absolute(32)), bottom));
        builder.add(ruleSource);
        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }
}
