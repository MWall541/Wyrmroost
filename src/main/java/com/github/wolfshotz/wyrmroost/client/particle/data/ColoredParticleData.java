package com.github.wolfshotz.wyrmroost.client.particle.data;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class ColoredParticleData implements IParticleData
{
    private final ParticleType<?> type;
    private final int rgb;

    public ColoredParticleData(ParticleType<?> type, int rgb)
    {
        this.type = type;
        this.rgb = rgb;
    }

    public static Codec<ColoredParticleData> codec(ParticleType<ColoredParticleData> type)
    {
        return Codec.INT.xmap(i -> new ColoredParticleData(type, i), d -> d.rgb);
    }

    public static ColoredParticleData read(ParticleType<ColoredParticleData> type, StringReader reader) throws CommandSyntaxException
    {
        reader.expect(' ');
        return new ColoredParticleData(type, reader.readInt());
    }

    @Override
    public ParticleType<?> getType()
    {
        return type;
    }

    @Override
    public void writeToNetwork(PacketBuffer buffer)
    {
        buffer.writeInt(rgb);
    }

    @Override
    public String writeToString()
    {
        return type.getRegistryName() + " " + rgb;
    }

    public float red()
    {
        return ((rgb >> 16) & 0xff) / 255f;
    }

    public float green()
    {
        return ((rgb >> 8) & 0xff) / 255f;
    }

    public float blue()
    {
        return (rgb & 0xff) / 255f;
    }
}
