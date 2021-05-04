package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.particle.PetalParticle;
import com.github.wolfshotz.wyrmroost.client.particle.data.ColoredParticleData;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

public class WRParticles<T extends IParticleData> extends ParticleType<T>
{
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Wyrmroost.MOD_ID);

    public static final RegistryObject<ParticleType<ColoredParticleData>> PETAL = register("petal", false, ColoredParticleData::codec, ColoredParticleData::read, () -> PetalParticle::new);

    private final Codec<T> codec;
    private final Supplier<BetterParticleFactory<T>> factory;

    public WRParticles(boolean alwaysShow, Function<ParticleType<T>, Codec<T>> codec, IParticleData.IDeserializer<T> deserializer, Supplier<BetterParticleFactory<T>> factory)
    {
        super(alwaysShow, deserializer);
        this.codec = codec.apply(this);
        this.factory = factory;
    }

    public WRParticles(boolean alwaysShow, ParticleType<T> wrapped, Supplier<BetterParticleFactory<T>> factory)
    {
        this(alwaysShow, t -> wrapped.codec(), wrapped.getDeserializer(), factory);
    }

    @Override
    public Codec<T> codec()
    {
        return codec;
    }

    public void bake()
    {
        ClientEvents.getClient().particleEngine.register(this, sprite -> ((d, w, x, y, z, xS, yS, zS) ->
                factory.get().create(d, w, sprite, x, y, z, xS, yS, zS)));
    }

    public static RegistryObject<ParticleType<BasicParticleType>> basic(String name, boolean alwaysShow, Supplier<BetterParticleFactory<BasicParticleType>> factory)
    {
        return REGISTRY.register(name, () -> new WRParticles<>(alwaysShow, new BasicParticleType(false), factory));
    }

    public static <T extends IParticleData> RegistryObject<ParticleType<T>> register(String name, boolean alwaysShow, Function<ParticleType<T>, Codec<T>> codec, IReader<T> reader, Supplier<BetterParticleFactory<T>> factory)
    {
        return REGISTRY.register(name, () -> new WRParticles<>(alwaysShow, codec, reader, factory));
    }

    public interface BetterParticleFactory<T extends IParticleData>
    {
        Particle create(T data, ClientWorld world, IAnimatedSprite spriteSheet, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed);
    }

    public interface IReader<T extends IParticleData> extends IParticleData.IDeserializer<T>
    {
        @Override
        T fromCommand(ParticleType<T> type, StringReader reader) throws CommandSyntaxException;

        @Override
        default T fromNetwork(ParticleType<T> type, PacketBuffer buffer)
        {
            try
            {
                return buffer.readWithCodec(type.codec());
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to read particle data from buffer: " + e);
            }
        }
    }
}
