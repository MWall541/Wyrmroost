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
import net.minecraft.entity.LivingEntity;
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
    public FireBreathEntity(EntityType<?> type, World world) { super(type, world); }

    public FireBreathEntity(AbstractDragonEntity shooter)
    {
        super(WREntities.FIRE_BREATH.get(), shooter);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (isInWater())
        {
            if (rand.nextDouble() <= 0.25d) playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1, 1);
            for (int i = 0; i < 15; i++)
                world.addParticle(ParticleTypes.SMOKE, getPosX(), getPosY(), getPosZ(), Mafs.nextDouble(rand) * 0.2f, rand.nextDouble() * 0.08f, Mafs.nextDouble(rand) * 0.2f);
            remove();
            return;
        }

        Vector3d motion = getMotion();
        double x = getPosX() + motion.x + (rand.nextGaussian() * 0.2);
        double y = getPosY() + motion.y + (rand.nextGaussian() * 0.2) + 0.5d;
        double z = getPosZ() + motion.z + (rand.nextGaussian() * 0.2);
        world.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
    }

    @Override
    public void onBlockImpact(BlockPos pos, Direction direction)
    {
        super.onBlockImpact(pos, direction);
        if (world.isRemote) return;

        BlockState state = world.getBlockState(pos);
        if (CampfireBlock.canBeLit(state))
        {
            world.setBlockState(pos, state.with(BlockStateProperties.LIT, true), 11);
            return;
        }

        double flammability = WRConfig.fireBreathFlammability;
        if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK) && WRConfig.canGrief(world) && flammability != 0) // respect game rules
        {
            BlockPos offset = pos.offset(direction);

            if (world.getBlockState(offset).isAir(world, offset) && (flammability == 1 || rand.nextDouble() <= flammability))
                world.setBlockState(offset, AbstractFireBlock.getFireForPlacement(world, offset), 11);
        }
    }

    @Override
    public void onEntityImpact(Entity entity)
    {
        if (world.isRemote) return;
        if (entity == shooter) return;
        if (entity.isImmuneToFire()) return;
        if (entity instanceof LivingEntity && shooter.isOnSameTeam(entity)) return;

        float damage = (float) shooter.getAttribute(WREntities.Attributes.PROJECTILE_DAMAGE.get()).getValue();
        if (world.isRainingAt(entity.getPosition())) damage *= 0.75f;

        entity.setFire(8);
        entity.attackEntityFrom(getDamageSource("fireBreath"), damage);
    }

    @Override
    public DamageSource getDamageSource(String name) { return super.getDamageSource(name).setFireDamage(); }

    @Override // Because we do it better.
    public boolean canRenderOnFire() { return false; }

    @Override
    public boolean isBurning() { return true; }
}
