package WolfShotz.Wyrmroost.entities.projectile.breath;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class FireBreathEntity extends BreathWeaponEntity
{
    public FireBreathEntity(EntityType<?> type, World world) { super(type, world); }

    public FireBreathEntity(AbstractDragonEntity shooter) { super(WREntities.FIRE_BREATH.get(), shooter); }

    @Override
    public void tick()
    {
        super.tick();

        if (isInWater())
        {
            if (rand.nextInt(4) == 0) playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1, 1);
            for (int i = 0; i < 15; i++)
                world.addParticle(ParticleTypes.SMOKE, getPosX(), getPosY(), getPosZ(), Mafs.nextDouble(rand) * 0.2f, rand.nextDouble() * 0.08f, Mafs.nextDouble(rand) * 0.2f);
            remove();
            return;
        }

        Vec3d motion = getMotion();
        double x = getPosX() + motion.x + (rand.nextGaussian() * 0.2);
        double y = getPosY() + motion.y + (rand.nextGaussian() * 0.2) + 0.5d;
        double z = getPosZ() + motion.z + (rand.nextGaussian() * 0.2);
        world.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
    }

    @Override
    public void onBlockImpact(BlockRayTraceResult result)
    {
        super.onBlockImpact(result);
        if (world.isRemote) return;

        BlockPos pos = result.getPos();
        BlockState state = world.getBlockState(pos);
        if (FlintAndSteelItem.isUnlitCampfire(state))
        {
            world.setBlockState(pos, state.with(BlockStateProperties.LIT, true), 11);
            return;
        }

        int flammability = WRConfig.fireBreathFlammability;
        if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK) && world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) && flammability != 999) // respect game rules
        {
            Direction face = result.getFace();
            BlockPos offset = pos.offset(face);

            if ((world.getBlockState(offset).isAir(world, offset)) && rand.nextInt(flammability) < Math.max(25, state.getFlammability(world, pos, face)))
                world.setBlockState(offset, ((FireBlock) Blocks.FIRE).getStateForPlacement(world, offset), 11);
        }
    }

    @Override
    public void onEntityImpact(Entity entity)
    {
        if (world.isRemote) return;
        if (entity == shooter) return;
        if (entity.isImmuneToFire()) return;
        if (entity instanceof LivingEntity && shooter.isOnSameTeam(entity)) return;

        float damage = (float) shooter.getAttribute(AbstractDragonEntity.PROJECTILE_DAMAGE).getValue();
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

    @Override
    protected float getMotionFactor() { return 1f; }
}
