package com.github.wolfshotz.wyrmroost.entities.dragonegg;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.items.DragonEggItem;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.TickFloat;
import net.minecraft.entity.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class DragonEggEntity extends Entity implements IEntityAdditionalSpawnData
{
    private static final int HATCH_ID = 1;
    private static final int WIGGLE_ID = 2;
    public static final String DATA_HATCH_TIME = "HatchTime";
    public static final String DATA_DRAGON_TYPE = "DragonType";
    private static final int UPDATE_CONDITIONS_INTERVAL = 50; // in ticks, this is for performance reasons

    public EntityType<AbstractDragonEntity> containedDragon;
    public DragonEggProperties properties; // cache for speed
    public Direction wiggleDirection = Direction.NORTH;
    public TickFloat wiggleTime = new TickFloat().setLimit(0, 1);
    public boolean correctConditions = false;
    public boolean wiggling = false;
    public int hatchTime;

    public DragonEggEntity(EntityType<? extends DragonEggEntity> type, World world)
    {
        super(type, world);
    }

    public DragonEggEntity(EntityType<AbstractDragonEntity> type, int hatchTime, World world)
    {
        super(WREntities.DRAGON_EGG.get(), world);
        this.containedDragon = type;
        this.hatchTime = hatchTime;
    }

    public DragonEggEntity(FMLPlayMessages.SpawnEntity packet, World world)
    {
        super(WREntities.DRAGON_EGG.get(), world);
        this.containedDragon = ModUtils.getEntityTypeByKey(packet.getAdditionalData().readString());
    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void initDataTracker()
    {
    }

    @Override
    public void readCustomDataFromTag(CompoundNBT compound)
    {
        containedDragon = ModUtils.getEntityTypeByKey(compound.getString(DATA_DRAGON_TYPE));
        hatchTime = compound.getInt(DATA_HATCH_TIME);
    }

    @Override
    public void writeCustomDataToTag(CompoundNBT compound)
    {
        compound.putString(DATA_DRAGON_TYPE, getDragonKey());
        compound.putInt(DATA_HATCH_TIME, hatchTime);
    }

    public String getDragonKey()
    {
        return EntityType.getId(containedDragon).toString();
    }

    // ================================

    @Override
    public void tick()
    {
        if (!world.isClientSide && containedDragon == null)
        {
            safeError();
            return;
        }

        super.tick();
        updateMotion();

        if (age % UPDATE_CONDITIONS_INTERVAL == 0)
        {
            boolean flag = getProperties().getConditions().test(this);
            if (flag != correctConditions) this.correctConditions = flag;
        }

        if (correctConditions)
        {
            if (world.isClientSide)
            {
                if (age % 3 == 0)
                {
                    double x = getX() + random.nextGaussian() * 0.2d;
                    double y = getY() + random.nextDouble() + getHeight() / 2;
                    double z = getZ() + random.nextGaussian() * 0.2d;
                    world.addParticle(new RedstoneParticleData(1f, 1f, 0, 0.5f), x, y, z, 0, 0, 0);
                }
                wiggleTime.add(wiggling? 0.4f : -0.4f);
                if (wiggleTime.get() == 1) wiggling = false;
            }
            else
            {
                if (--hatchTime <= 0)
                {
                    world.sendEntityStatus(this, (byte) HATCH_ID); // notify client
                    hatch();
                }
                else if (random.nextInt(Math.max(hatchTime / 2, 5)) == 0)
                    world.sendEntityStatus(this, (byte) WIGGLE_ID);
            }
        }
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier)
    {
        if (distance > 3)
        {
            crack(5);
            return true;
        }
        return false;
    }

    private void updateMotion()
    {
        boolean flag = getVelocity().y <= 0.0D;
        double d1 = getY();
        double d0 = 0.5d;

        move(MoverType.SELF, getVelocity());
        if (!hasNoGravity() && !isSprinting())
        {
            Vector3d vec3d2 = getVelocity();
            double d2;
            if (flag && Math.abs(vec3d2.y - 0.005D) >= 0.003D && Math.abs(vec3d2.y - d0 / 16.0D) < 0.003D) d2 = -0.003D;
            else d2 = vec3d2.y - d0 / 16.0D;

            setVelocity(vec3d2.x, d2, vec3d2.z);
        }

        Vector3d vec3d6 = getVelocity();
        if (horizontalCollision && doesNotCollide(vec3d6.x, vec3d6.y + (double) 0.6F - getY() + d1, vec3d6.z))
        {
            setVelocity(vec3d6.x, 0.3F, vec3d6.z);
        }
    }

    @Override
    public void handleStatus(byte id)
    {
        switch (id)
        {
            case HATCH_ID:
                hatch();
                break;
            case WIGGLE_ID:
                wiggle();
                break;
            default:
                super.handleStatus(id);
        }
    }

    /**
     * Called to hatch the dragon egg
     * Usage: <P>
     * - Get the dragon EntityType (If its something it shouldnt, safely fail) <P>
     * - Set the dragons growing age to a baby <P>
     * - Set the position of that dragon to the position of this egg <P>
     * - Remove this entity (the egg) and play any effects
     */
    public void hatch()
    {
        if (!world.isClientSide)
        {
            AbstractDragonEntity newDragon = containedDragon.create(world);
            if (newDragon == null)
            {
                safeError();
                return;
            }
            newDragon.refreshPositionAndAngles(getX(), getY(), getZ(), 0, 0);
            newDragon.setBreedingAge(getProperties().getGrowthTime());
            newDragon.initialize((IServerWorld) world, world.getLocalDifficulty(getBlockPos()), SpawnReason.BREEDING, null, null);
            world.spawnEntity(newDragon);
        }
        else
        {
            crack(25);
            world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 1, 1, false);
        }
        remove();
    }

    public void crack(int intensity)
    {
        world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 1, 1, false);
        float f = getWidth() * getHeight();
        f += f;
        for (int i = 0; i < f * intensity; ++i)
        {
            double x = getX() + (Mafs.nextDouble(random) * getWidth() / 2);
            double y = getY() + (Mafs.nextDouble(random) * getHeight());
            double z = getZ() + (Mafs.nextDouble(random) * getWidth() / 2);
            double xMot = Mafs.nextDouble(random) * getWidth() * 0.35;
            double yMot = random.nextDouble() * getHeight() * 0.5;
            double zMot = Mafs.nextDouble(random) * getWidth() * 0.35;
            world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.EGG)), x, y, z, xMot, yMot, zMot);
        }
    }

    public void wiggle()
    {
        if (wiggleTime.get() > 0) return;
        wiggleDirection = Direction.Plane.HORIZONTAL.random(random);
        wiggling = true;
        crack(5);
    }

    /**
     * Called When the dragon type of the egg is not what it should be.
     */
    private void safeError()
    {
        Wyrmroost.LOG.error("THIS ISNT A DRAGON WTF KIND OF ABOMINATION IS THIS HATCHING?!?! Unknown Entity Type for Dragon Egg @ {}", getBlockPos());
        remove();
    }

    @Override
    public boolean damage(DamageSource source, float amount)
    {
        ItemStack stack = DragonEggItem.getStack(containedDragon, hatchTime);
        InventoryHelper.spawn(world, getX(), getY(), getZ(), stack);
        remove();

        return true;
    }

    public DragonEggProperties getProperties()
    {
        if (properties == null) return properties = DragonEggProperties.get(containedDragon);
        return properties;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return DragonEggItem.getStack(containedDragon);
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        return getProperties().getDimensions();
    }

    public EntitySize getDimensions()
    {
        return getProperties().getDimensions();
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    public boolean collides()
    {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        buffer.writeString(getDragonKey());
    }

    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
    }
}
