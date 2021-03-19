package com.github.wolfshotz.wyrmroost.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.RedstoneParticleData;

public class PetalParticle extends SpriteTexturedParticle
{
    public PetalParticle(RedstoneParticleData data, ClientWorld level, IAnimatedSprite sprite, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
    {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
