package com.eeeab.eeeabsmobs.client.particle.lib.data;

import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.lib.ParticleRotation;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AdvancedBlockParticleData extends AdvancedParticleData {
    public static final Deserializer<AdvancedBlockParticleData> DESERIALIZER = new Deserializer<>() {
        public AdvancedBlockParticleData fromCommand(ParticleType<AdvancedBlockParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double airDrag = reader.readDouble();
            reader.expect(' ');
            double red = reader.readDouble();
            reader.expect(' ');
            double green = reader.readDouble();
            reader.expect(' ');
            double blue = reader.readDouble();
            reader.expect(' ');
            double alpha = reader.readDouble();
            reader.expect(' ');
            double scale = reader.readDouble();
            reader.expect(' ');
            boolean emissive = reader.readBoolean();
            reader.expect(' ');
            double duration = reader.readDouble();
            reader.expect(' ');
            BlockState blockState = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), reader, false).blockState();
            return new AdvancedBlockParticleData(scale, red, green, blue, alpha, airDrag, duration, emissive, false, new ParticleComponent[]{}, blockState, BlockPos.ZERO);
        }

        public AdvancedBlockParticleData fromNetwork(ParticleType<AdvancedBlockParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            double airDrag = buffer.readFloat();
            double red = buffer.readFloat();
            double green = buffer.readFloat();
            double blue = buffer.readFloat();
            double alpha = buffer.readFloat();
            double scale = buffer.readFloat();
            boolean emissive = buffer.readBoolean();
            double duration = buffer.readFloat();
            buffer.readFloat();
            BlockState state = buffer.readById(Block.BLOCK_STATE_REGISTRY);
            return new AdvancedBlockParticleData(scale, red, green, blue, alpha, airDrag, duration, emissive, false, new ParticleComponent[]{}, state, BlockPos.ZERO);
        }
    };

    private final BlockState state;
    private final BlockPos pos;

    public AdvancedBlockParticleData(double scale, double r, double g, double b, double a,
                                     double airDiffusionSpeed, double duration, boolean emissive, boolean canCollide,
                                     ParticleComponent[] components, BlockState state, BlockPos pos) {
        super(ParticleInit.BLOCK.get(), new ParticleRotation.FaceCamera((float) 0), scale, r, g, b, a, airDiffusionSpeed, duration,
                emissive, canCollide, components, false);
        this.state = state;
        this.pos = pos;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        super.writeToNetwork(buffer);
        buffer.writeId(Block.BLOCK_STATE_REGISTRY, this.state);
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public String writeToString() {
        return super.writeToString() + " " +
                BlockStateParser.serialize(this.state) + " " +
                this.pos.getX() + " " + this.pos.getY() + " " + this.pos.getZ();
    }

    @OnlyIn(Dist.CLIENT)
    public BlockState getState() {
        return state;
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getPos() {
        return pos;
    }

    public static Codec<AdvancedBlockParticleData> CODEC_BLOCK() {
        return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                        Codec.DOUBLE.fieldOf("scale").forGetter(AdvancedBlockParticleData::getScale),
                        Codec.DOUBLE.fieldOf("r").forGetter(AdvancedBlockParticleData::getRed),
                        Codec.DOUBLE.fieldOf("g").forGetter(AdvancedBlockParticleData::getGreen),
                        Codec.DOUBLE.fieldOf("b").forGetter(AdvancedBlockParticleData::getBlue),
                        Codec.DOUBLE.fieldOf("a").forGetter(AdvancedBlockParticleData::getAlpha),
                        Codec.DOUBLE.fieldOf("airDiffusionSpeed").forGetter(AdvancedBlockParticleData::getAirDiffusionSpeed),
                        Codec.DOUBLE.fieldOf("duration").forGetter(AdvancedBlockParticleData::getDuration),
                        Codec.BOOL.fieldOf("emissive").forGetter(AdvancedBlockParticleData::isEmissive),
                        Codec.BOOL.fieldOf("canCollide").forGetter(AdvancedParticleData::getCanCollide),
                        BlockState.CODEC.fieldOf("getState").forGetter(AdvancedBlockParticleData::getState),
                        BlockPos.CODEC.fieldOf("getPos").forGetter(AdvancedBlockParticleData::getPos)
                ).apply(codecBuilder, (scale, r, g, b, a, drag, duration, emissive, canCollide, state, pos) ->
                        new AdvancedBlockParticleData( scale, r, g, b, a, drag, duration, emissive, canCollide, new ParticleComponent[]{}, state, pos))
        );
    }
}