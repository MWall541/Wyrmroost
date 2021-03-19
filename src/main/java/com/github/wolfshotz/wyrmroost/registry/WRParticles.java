package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.mojang.serialization.Codec;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class WRParticles
{
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Wyrmroost.MOD_ID);

    public static <T extends IParticleData> RegistryObject<ParticleType<T>> register(String name, boolean alwaysShow, Codec<T> codec, IParticleData.IDeserializer<T> deserializer, Supplier<BetterParticleFactory<T>> factory)
    {
        return REGISTRY.register(name, () -> new WRParticleType<>(alwaysShow, codec, deserializer, factory));
    }

    public interface BetterParticleFactory<T extends IParticleData>
    {
        Particle create(T data, ClientWorld world, IAnimatedSprite spriteSheet, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed);
    }

    public static class WRParticleType<T extends IParticleData> extends ParticleType<T>
    {
        private final Codec<T> codec;
        private final Supplier<BetterParticleFactory<T>> factory;

        public WRParticleType(boolean alwaysShow, Codec<T> codec, IParticleData.IDeserializer<T> deserializer, Supplier<BetterParticleFactory<T>> factory)
        {
            super(alwaysShow, deserializer);
            this.codec = codec;
            this.factory = factory;
        }

        @Override
        public Codec<T> codec()
        {
            return codec;
        }

        public BetterParticleFactory<T> getFactory()
        {
            return factory.get();
        }
    }
}
