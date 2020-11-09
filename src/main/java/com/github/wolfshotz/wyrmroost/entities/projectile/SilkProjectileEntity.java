//package com.github.wolfshotz.wyrmroost.entities.projectile;
//
//import com.github.wolfshotz.wyrmroost.registry.WREffects;
//import net.minecraft.block.Blocks;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.particles.BlockParticleData;
//import net.minecraft.particles.ParticleTypes;
//import net.minecraft.potion.EffectInstance;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.util.math.vector.Vector3d;
//import net.minecraft.world.World;
//
//public class SilkProjectileEntity extends DragonProjectileEntity
//{
//    public SilkProjectileEntity(EntityType<? extends DragonProjectileEntity> type, World worldIn)
//    {
//        super(type, worldIn);
//    }
//
//    @Override
//    public void hit(RayTraceResult result)
//    {
//        super.hit(result);
//        if (world.isRemote)
//        {
//            Vector3d pos = result.getHitVec();
//            for (int i = 0; i < 20; i++)
//                world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.COBWEB.getDefaultState()), pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
//        }
//        else remove();
//    }
//
//    @Override
//    public void onEntityImpact(Entity entity)
//    {
//        if (entity.getWidth() < 5 && entity.getHeight() < 5)
//        {
//            LivingEntity living = (LivingEntity) entity;
//            living.attackEntityFrom(getDamageSource("silk"), 3f);
//            living.addPotionEffect(new EffectInstance(WREffects.SILK.get(), 1200));
//            living.applyKnockback((float) getMotion().length(), entity.getPosX() - getPosX(), entity.getPosZ() - getPosZ());
//        }
//    }
//
//    @Override
//    protected float getMotionFactor()
//    {
//        return 1.1f;
//    }
//
//    @Override
//    public boolean hasNoGravity()
//    {
//        return false;
//    }
//
//    @Override
//    protected EffectType getEffectType()
//    {
//        return EffectType.RAYTRACE;
//    }
//}
