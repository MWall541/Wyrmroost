package WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.util.QuikMaths;
import WolfShotz.Wyrmroost.util.entityutils.PlayerMount;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class CanariWyvernEntity extends AbstractDragonEntity implements PlayerMount.IShoulderMount
{
    public static final Animation FLAP_WINGS_ANIMATION = new Animation(22);
    public static final Animation CLEAN_FEATHERS_ANIMATION = new Animation(36);
    public static final Animation THREAT_ANIMATION = new Animation(40);

    public CanariWyvernEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        setImmune(new DamageSource("caustic_water"));
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        
        getAttribute(MAX_HEALTH).setBaseValue(16d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.2d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(5.5d);
    }
    
    @Override
    protected void registerData()
    {
        super.registerData();

        dataManager.register(VARIANT, getRNG().nextInt(5));
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);

        nbt.putInt("variant", getVariant());
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        setVariant(nbt.getInt("variant"));
    }

    @Override
    public int getSpecialChances() { return 0; }

    @Override
    public void livingTick()
    {
        super.livingTick();

        if (!isSleeping() && !isFlying() && getRidingEntity() == null && noActiveAnimation())
        {
            if (getRNG().nextInt(350) == 0) setAnimation(FLAP_WINGS_ANIMATION);
            if (getRNG().nextInt(350) == 0) setAnimation(CLEAN_FEATHERS_ANIMATION);
        }

//        if (getAnimation() == FLAP_WINGS_ANIMATION && getAnimationTick() == 3)
//            playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 1, 0.5f);
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.processInteract(player, hand, stack)) return true;

        if (isOwner(player))
        {
            if (player.isSneaking())
            {
                setSit(!isSitting());

                return true;
            }

            if (PlayerMount.getShoulderEntityCount(player) < 2)
            {
                setSit(true);
                startRiding(player, true);

                return true;
            }
        }
        return false;
    }
    
    @Override
    public void updateRidden()
    {
        super.updateRidden();
        
        Entity entity = getRidingEntity();
        
        if (!entity.isAlive())
        {
            stopRiding();
            return;
        }
        
        if (!(entity instanceof PlayerEntity)) return;
        
        PlayerEntity player = (PlayerEntity) entity;
        
        if (player.isSneaking() && !player.abilities.isFlying)
        {
            stopRiding();
            return;
        }
        
        rotationYaw = player.rotationYawHead;
        rotationPitch = player.rotationPitch;
        setRotation(rotationYaw, rotationPitch);
        rotationYawHead = player.rotationYawHead;
        prevRotationYaw = player.rotationYawHead;
        
        double xOffset = checkShoulderOccupants(player)? -0.35f : 0.35f;

        Vec3d vec3d1 = QuikMaths.calculateYawAngle(player.renderYawOffset, xOffset, 0.1).add(player.posX, 0, player.posZ);
        setPosition(vec3d1.x, player.posY + 1.4, vec3d1.z);
    }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    public List<Item> getFoodItems()
    {
        return null;
    }
    
    @Override
    public DragonEggProperties createEggProperties()
    {
        return new DragonEggProperties(0.35f, 0.5f, 6000)
                       .setConditions(c -> c.world.getBlockState(c.getPosition().down()).getBlock() == WRBlocks.CANARI_LEAVES.get());
    }
    
    @Override
    public WolfShotz.Wyrmroost.util.entityutils.client.animation.Animation[] getAnimations()
    {
        return new Animation[]{NO_ANIMATION, SLEEP_ANIMATION, WAKE_ANIMATION, FLAP_WINGS_ANIMATION, CLEAN_FEATHERS_ANIMATION, THREAT_ANIMATION};
    }
}
