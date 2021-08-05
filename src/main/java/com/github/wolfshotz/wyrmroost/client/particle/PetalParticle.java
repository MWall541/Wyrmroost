package com.github.wolfshotz.wyrmroost.client.particle;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.particle.data.ColoredParticleData;
import com.github.wolfshotz.wyrmroost.registry.WRParticles;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class PetalParticle extends SpriteTexturedParticle
{
    private final double xSway;
    private final double zSway;

    public PetalParticle(ColoredParticleData data, ClientWorld level, IAnimatedSprite sprite, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
    {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        lifetime = 400;
        gravity = 0.0007f;
        xd = 0;
        yd = 0;
        zd = 0;
        xSway = random.nextDouble();
        zSway = 1 - xSway;

        final float offset = 0.085f;
        rCol = MathHelper.clamp(data.red() + (offset * (2f * random.nextFloat() - 1f)), 0, 1);
        gCol = MathHelper.clamp(data.green() + (offset * (2f * random.nextFloat() - 1f)), 0, 1);
        bCol = MathHelper.clamp(data.blue() + (offset * (2f * random.nextFloat() - 1f)), 0, 1);
        quadSize = 0.15f * (random.nextFloat() * 0.5f + 0.5f) * 2.0f;

        pickSprite(sprite);
    }

    public static void play(World level, BlockPos pos, Random random, int color)
    {
        if (!level.getBlockState(pos = pos.below()).isCollisionShapeFullBlock(level, pos))
        {
            level.addParticle(new ColoredParticleData(WRParticles.PETAL.get(), color),
                    pos.getX() + random.nextDouble(),
                    pos.getY() + 0.75,
                    pos.getZ() + random.nextDouble(),
                    0,
                    0,
                    0);
        }
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick()
    {
        xo = x;
        yo = y;
        zo = z;

        if (age++ >= lifetime && alpha == 1) remove();
        else
        {
            yd -= gravity;
            xd = (MathHelper.cos(age * 0.1f) * 0.1f) * xSway;
            zd = (MathHelper.sin(age * 0.1f) * 0.1f) * zSway;
            move(xd, yd, zd);
            xd *= 0.98f;
            yd *= 0.98f;
            zd *= 0.98f;
            if (onGround || alpha < 1)
            {
                xd *= 0.7f;
                zd *= 0.7f;
                if (alpha <= 0) remove();
                else alpha = Math.max(0, MathHelper.lerp(ClientEvents.getPartialTicks(), alpha, alpha - 0.25f));
            }
        }
    }
}
