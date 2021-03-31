package com.github.wolfshotz.wyrmroost.entities.projectile.breath;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
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
    public FireBreathEntity(EntityType<?> type, World level)
    {
        super(type, level);
    }

    public FireBreathEntity(TameableDragonEntity shooter)
    {
        super(WREntities.FIRE_BREATH.get(), shooter);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (isInWater())
        {
            if (random.nextDouble() <= 0.25d) playSound(SoundEvents.FIRE_EXTINGUISH, 1, 1);
            for (int i = 0; i < 15; i++)
                level.addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), Mafs.nextDouble(random) * 0.2f, random.nextDouble() * 0.08f, Mafs.nextDouble(random) * 0.2f);
            remove();
            return;
        }

        Vector3d motion = getDeltaMovement();
        double x = getX() + motion.x + (random.nextGaussian() * 0.2);
        double y = getY() + motion.y + (random.nextGaussian() * 0.2) + 0.5d;
        double z = getZ() + motion.z + (random.nextGaussian() * 0.2);
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
    }

    @Override
    public void onBlockImpact(BlockPos pos, Direction direction)
    {
        super.onBlockImpact(pos, direction);
        if (level.isClientSide) return;

        BlockState state = level.getBlockState(pos);
        if (CampfireBlock.canLight(state))
        {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
            return;
        }

        double flammability = WRConfig.BREATH_FIRE_SPREAD.get();
        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK) && WRConfig.canGrief(level) && flammability != 0) // respect game rules
        {
            BlockPos offset = pos.relative(direction);

            if (level.getBlockState(offset).isAir(level, offset) && (flammability == 1 || random.nextDouble() <= flammability))
                level.setBlock(offset, AbstractFireBlock.getState(level, offset), 11);
        }
    }

    @Override
    public void onEntityImpact(Entity entity)
    {
        if (level.isClientSide) return;

        float damage = (float) shooter.getAttributeValue(WREntities.Attributes.PROJECTILE_DAMAGE.get());
        if (level.isRainingAt(entity.blockPosition())) damage *= 0.75f;

        if (entity.fireImmune()) damage *= 0.25; // impact damage
        else entity.setSecondsOnFire(8);

        entity.hurt(getDamageSource(random.nextDouble() > 0.2? "fireBreath0" : "fireBreath1"), damage);
    }

    @Override
    public DamageSource getDamageSource(String name)
    {
        return super.getDamageSource(name).setIsFire();
    }

    @Override // Because we do it better.
    public boolean displayFireAnimation()
    {
        return false;
    }

    @Override
    public boolean isOnFire()
    {
        return true;
    }
}
