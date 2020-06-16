package WolfShotz.Wyrmroost.entities.multipart;

import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class MultiPartEntity extends Entity implements IEntityAdditionalSpawnData
{
    public LivingEntity host;
    public float radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier;

    public MultiPartEntity(EntityType<? extends MultiPartEntity> multiPart, World world) { super(multiPart, world); }

    /**
     * @param host             entity were adding this part to
     * @param radius           how far the part is away from the host's center
     * @param angleYaw         angle at which the box is at
     * @param offsetY          how high the part is
     * @param sizeX            the width of the part
     * @param sizeY            the height of the part
     * @param damageMultiplier damage that is multiplied when hit by this part
     */
    public MultiPartEntity(LivingEntity host, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier)
    {
        super(WREntities.MULTIPART.get(), host.world);
        this.host = host;
        this.radius = radius;
        this.angleYaw = (angleYaw + 90f) * (Mafs.PI / 180f);
        this.offsetY = offsetY;
        this.damageMultiplier = damageMultiplier;

        resize(sizeX, sizeY);
    }

    /**
     * @param host     entity were adding this part to
     * @param radius   how far the part is away from the host's center
     * @param angleYaw angle at which the box is at
     * @param offsetY  how high the part is
     * @param sizeX    the width of the part
     * @param sizeY    the height of the part
     */
    public MultiPartEntity(LivingEntity host, float radius, float angleYaw, float offsetY, float sizeX, float sizeY)
    {
        this(host, radius, angleYaw, offsetY, sizeX, sizeY, 1f);
    }

    @Override
    public void tick()
    {
        // todo: Hitboxes do not reappear after host is unloaded and reloaded on client side...

        if (world.isRemote && (host == null || !host.isAlive())) // Our host is dead, so we shouldnt exist!
        {
            remove();
            return;
        }

        setPosition(host.getPosX() + radius * Math.cos(host.renderYawOffset * (Math.PI / 180f) + angleYaw), host.getPosY() + offsetY, host.getPosZ() + radius * Math.sin(host.renderYawOffset * (Math.PI / 180f) + angleYaw));
        collideWithNearbyEntities();

        super.baseTick();
    }
    
    @Override
    public boolean processInitialInteract(PlayerEntity player, Hand hand)
    {
        if (!(host instanceof AgeableEntity)) return host.processInitialInteract(player, hand);
        return ((AgeableEntity) host).processInteract(player, hand);
    }
    
    public void resizeAndPosition(float radius, float angleYaw, float offsetY, float sizeX, float sizeY)
    {
        this.radius = radius;
        this.angleYaw = (angleYaw + 90f) * (Mafs.PI / 180f);
        this.offsetY = offsetY;
        resize(sizeX, sizeY);
    }

    public void resize(float sizeX, float sizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        recalculateSize();
    }

    public void collideWithNearbyEntities()
    {
        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(0.20000000298023224d, 0, 0.20000000298023224d)))
        {
            if (entity == host) continue;
            if (entity instanceof MultiPartEntity && ((MultiPartEntity) entity).host == host) continue;
            if (!entity.canBePushed()) continue;
            host.applyEntityCollision(entity);
        }
    }

    @Override
    public EntitySize getSize(Pose poseIn) { return EntitySize.flexible(sizeX, sizeY); }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        return host.attackEntityFrom(source, damage * damageMultiplier);
    }


    @Override
    public boolean canBeCollidedWith() { return true; }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) { return host.getPickedResult(target); }

    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {}

    @Override
    public boolean isEntityEqual(Entity entity) { return this == entity || host == entity; }

    @Override
    public boolean equals(Object o) { return this == o || host == o; }

    @Override
    public int hashCode() { return host.getEntityId(); }

    @Override
    protected void registerData() {}

    @Override
    protected void readAdditional(CompoundNBT compound) {}

    @Override
    protected void writeAdditional(CompoundNBT compound) {}

    @Override
    public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    
    /**
     * Called by the server when constructing the spawn packet.
     * Data should be added to the provided stream.
     */
    @Override
    public void writeSpawnData(PacketBuffer buf)
    {
        buf.writeInt(host.getEntityId());
        buf.writeFloat(radius);
        buf.writeFloat(angleYaw);
        buf.writeFloat(offsetY);
        buf.writeFloat(damageMultiplier);
        buf.writeFloat(sizeX);
        buf.writeFloat(sizeY);
    }
    
    @Override
    public void readSpawnData(PacketBuffer buf)
    {
        this.host = (LivingEntity) world.getEntityByID(buf.readInt());
        this.radius = buf.readFloat();
        this.angleYaw = buf.readFloat();
        this.offsetY = buf.readFloat();
        this.damageMultiplier = buf.readFloat();
        
        //     sizeX,           sizeY
        resize(buf.readFloat(), buf.readFloat());
    }
}
