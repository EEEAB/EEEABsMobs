package com.eeeab.eeeabsmobs.client.particle.lib;

import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.data.AdvancedBlockParticleData;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class AdvancedBlockParticle extends AdvancedParticleBase {
    private final BlockPos pos;
    private final float uo, vo;

    public AdvancedBlockParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ,
                                 double scale, double r, double g, double b, double a,
                                 double airDiffusionSpeed, double duration, boolean emissive, boolean canCollide,
                                 ParticleComponent[] components, BlockState state, BlockPos blockPos) {
        super(world, x, y, z, motionX, motionY, motionZ, new ParticleRotation.FaceCamera((float) 0), scale,
                r, g, b, a, airDiffusionSpeed, duration, emissive, canCollide,
                components, null);
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
        this.pos = blockPos != null ? blockPos : BlockPos.containing(x, y, z);
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;
        if (net.minecraftforge.client.extensions.common.IClientBlockExtensions.of(state).areBreakingParticlesTinted(state, world, pos)) {
            int tint = Minecraft.getInstance().getBlockColors().getColor(state, world, pos, 0);
            this.rCol *= (float) (tint >> 16 & 255) / 255.0F;
            this.gCol *= (float) (tint >> 8 & 255) / 255.0F;
            this.bCol *= (float) (tint & 255) / 255.0F;
            this.red = this.rCol;
            this.green = this.gCol;
            this.blue = this.bCol;
        }
        this.gravity = 1F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F * 16.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F * 16.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F * 16.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F * 16.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    @Override
    public int getLightColor(float partialTick) {
        int light = this.level.hasChunkAt(this.pos) ? LevelRenderer.getLightColor(this.level, this.pos) : 0;
        if (this.emissive) {
            int skyLight = (light >> 16) & 0xFF;
            return 240 | (skyLight << 16);
        }
        return light;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class Factory implements ParticleProvider<AdvancedBlockParticleData> {
        public Factory(SpriteSet sprite) {
        }

        @Override
        public Particle createParticle(AdvancedBlockParticleData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AdvancedBlockParticle(
                    level, x, y, z, xSpeed, ySpeed, zSpeed, data.getScale(),
                    data.getRed(), data.getGreen(), data.getBlue(), data.getAlpha(),
                    data.getAirDiffusionSpeed(), data.getDuration(),
                    data.isEmissive(), data.getCanCollide(),
                    data.getComponents(), data.getState(), data.getPos()
            );
        }
    }

    public static AdvancedBlockParticleData createBlockParticleData(BlockState state, @Nullable BlockPos pos, double x, double y, double z,
                                                                    double scale, double r, double g, double b, double a,
                                                                    double drag, double duration, boolean emissive,
                                                                    boolean canCollide, ParticleComponent[] components) {
        BlockPos finalPos = pos != null ? pos : new BlockPos.MutableBlockPos(x, y, z);
        return new AdvancedBlockParticleData(scale, r, g, b, a, drag, duration, emissive, canCollide, components, state, finalPos);
    }

    public static void spawnParticle(Level level, @Nullable BlockPos pos, BlockState state, double x, double y, double z, double motionX, double motionY, double motionZ, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        AdvancedBlockParticleData data = createBlockParticleData(state, pos, x, y, z, scale, r, g, b, a, drag, duration,
                emissive, canCollide, components);
        level.addParticle(data, x, y, z, motionX, motionY, motionZ);
    }
}