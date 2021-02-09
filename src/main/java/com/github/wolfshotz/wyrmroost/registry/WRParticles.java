package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class WRParticles
{
    public static final Codec<RedstoneParticleData> TEST_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.FLOAT.fieldOf("r").forGetter(instance -> instance.red),
            Codec.FLOAT.fieldOf("g").forGetter(instance -> instance.green),
            Codec.FLOAT.fieldOf("b").forGetter(instance -> instance.blue),
            Codec.FLOAT.fieldOf("scale").forGetter(instance -> instance.alpha))
            .apply(builder, RedstoneParticleData::new));

    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Wyrmroost.MOD_ID);

    public static final RegistryObject<BasicParticleType> PETAL;

    @OnlyIn(value = Dist.CLIENT, _interface = ParticleManager.IParticleMetaFactory.class)
    public static class Type<T extends IParticleData> extends ParticleType<T> implements ParticleManager.IParticleMetaFactory<T>
    {
        private final Codec<T> codec;
        private final Supplier<Supplier<IParticleFactory<T>>> factory;
        private Supplier<Supplier<IAnimatedSprite>> sprite;

        public Type(IParticleData.IDeserializer<T> deserializer, Function<Type<T>, Codec<T>> codec, Supplier<Supplier<IParticleFactory<T>>> particleFactory)
        {
            super(false, deserializer);
            this.codec = codec.apply(this);
            this.factory = particleFactory;
        }

        @Override
        public Codec<T> func_230522_e_()
        {
            return codec;
        }

        public IAnimatedSprite getSprite()
        {
            return sprite.get().get();
        }

        @Override
        public IParticleFactory<T> create(IAnimatedSprite sprite)
        {
            this.sprite = () -> () -> sprite;
            return factory.get().get();
        }
    }
}
