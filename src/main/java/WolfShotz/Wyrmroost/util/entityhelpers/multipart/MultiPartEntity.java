package WolfShotz.Wyrmroost.util.entityhelpers.multipart;

import WolfShotz.Wyrmroost.event.SetupEntities;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class MultiPartEntity extends Entity
{
    protected LivingEntity host;
    public float radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier;
    
    public MultiPartEntity(FMLPlayMessages.SpawnEntity packet, World world) {
        super(SetupEntities.multiPartEntity, world);
    }
    
    public MultiPartEntity(LivingEntity host, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(SetupEntities.multiPartEntity, host.world);
        this.host = host;
        
        this.radius = radius;
        this.angleYaw = (angleYaw + 90f) * (MathUtils.PI / 180.0F);
        this.offsetY = offsetY;
        
        this.damageMultiplier = damageMultiplier;
        resize(sizeX, sizeY);
    }
    
    @Override
    public void tick() {
        System.out.println(getPositionVec());
        
        if (!world.isRemote) {
            if (!host.isAlive()) { // Our host is dead, so we shouldnt exist!
                remove();
                return;
            }
            collideWithNearbyEntities();
            if (host == null) {
                ModUtils.L.error("Unknown host type; removing.");
                remove();
                return;
            }
            setPositionAndUpdate(host.posX + radius * Math.cos(host.renderYawOffset * (Math.PI / 180f) + angleYaw), host.posY + offsetY, host.posZ + radius * Math.sin(host.renderYawOffset * (Math.PI / 180f) + angleYaw));
        }
        
        super.baseTick();
    }
    
    public void resize(float sizeX, float sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        recalculateSize();
    }
    
    @Override
    public EntitySize getSize(Pose poseIn) { return EntitySize.flexible(sizeX, sizeY); }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        return host.attackEntityFrom(source, damage * damageMultiplier);
    }
    
    @Override
    public boolean isEntityEqual(Entity entity) { return this == entity || host == entity; }
    
    @Override
    protected void registerData() { }
    
    @Override
    protected void readAdditional(CompoundNBT compound) { }
    
    @Override
    protected void writeAdditional(CompoundNBT compound) { }
    
    @Override
    public boolean canBeCollidedWith() { return true; }
    
    public void collideWithNearbyEntities() {
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(0.20000000298023224d, 0, 0.20000000298023224d));
        entities.stream().filter(entity -> entity != host && !(entity instanceof MultiPartEntity) && entity.canBePushed()).forEach(entity -> entity.applyEntityCollision(host));
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) { }
    
    @Override
    public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}
