package com.github.wolfshotz.wyrmroost.entities.projectile.breath;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class FireBreathEntity extends BreathWeaponEntity
{
    public FireBreathEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    public FireBreathEntity(AbstractDragonEntity shooter)
    {
        super(WREntities.FIRE_BREATH.get(), shooter);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (isTouchingWater())
        {
            if (random.nextDouble() <= 0.25d) playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1, 1);
            for (int i = 0; i < 15; i++)
                world.addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), Mafs.nextDouble(random) * 0.2f, random.nextDouble() * 0.08f, Mafs.nextDouble(random) * 0.2f);
            remove();
            return;
        }

        Vector3d motion = getVelocity();
        double x = getX() + motion.x + (random.nextGaussian() * 0.2);
        double y = getY() + motion.y + (random.nextGaussian() * 0.2) + 0.5d;
        double z = getZ() + motion.z + (random.nextGaussian() * 0.2);
        world.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
    }

    @Override
    public void onBlockImpact(BlockPos pos, Direction direction)
    {
        super.onBlockImpact(pos, direction);
        if (world.isClientSide) return;

        BlockState state = world.getBlockState(pos);
        if (CampfireBlock.method_30035(state))
        {
            world.setBlockState(pos, state.with(BlockStateProperties.LIT, true), 11);
            return;
        }

        double flammability = WRConfig.fireBreathFlammability;
        if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK) && WRConfig.canGrief(world) && flammability != 0) // respect game rules
        {
            BlockPos offset = pos.offset(direction);

            if (world.getBlockState(offset).isAir(world, offset) && (flammability == 1 || random.nextDouble() <= flammability))
                world.setBlockState(offset, AbstractFireBlock.getState(world, offset), 11);
        }
    }

    @Override
    public void onEntityImpact(Entity entity)
    {
        if (world.isClientSide) return;

        float damage = (float) shooter.getAttributeValue(WREntities.Attributes.PROJECTILE_DAMAGE.get());
        if (world.hasRain(entity.getBlockPos())) damage *= 0.75f;

        if (entity.isFireImmune()) damage *= 0.25; // impact damage
        else entity.setOnFireFor(8);

        entity.damage(getDamageSource(random.nextDouble() > 0.2? "fireBreath0" : "fireBreath1"), damage);
    }

    @Override
    public DamageSource getDamageSource(String name)
    {
        return super.getDamageSource(name).setFire();
    }

    @Override // Because we do it better.
    public boolean doesRenderOnFire()
    {
        return false;
    }

    @Override
    public boolean isOnFire()
    {
        return true;
    }
}
