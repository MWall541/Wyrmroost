package WolfShotz.Wyrmroost.content.entities.dragonegg;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.event.SetupEntities;
import WolfShotz.Wyrmroost.event.SetupItems;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnMobPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class DragonEggEntity extends LivingEntity implements IAnimatedEntity
{
    private int animationTick;
    private Animation animation = NO_ANIMATION;
    @OnlyIn(Dist.CLIENT)
    public boolean wiggleInvert, wiggleInvert2;
    
    public static final Animation WIGGLE_ANIMATION = Animation.create(10);
    
    private static final DataParameter<String> DRAGON_TYPE = EntityDataManager.createKey(DragonEggEntity.class, DataSerializers.STRING);
    private static final DataParameter<Integer> HATCH_TIME = EntityDataManager.createKey(DragonEggEntity.class, DataSerializers.VARINT);
    
    public DragonEggEntity(EntityType<? extends DragonEggEntity> dragonEgg, World world) {
        super(dragonEgg, world);
    }
    
    
    @Override
    protected void registerData() {
        super.registerData();
        
        dataManager.register(DRAGON_TYPE, "");
        dataManager.register(HATCH_TIME, 1200);
    }
    
    // ================================
    //           Entity NBT
    // ================================
    
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        
        setDragonType(compound.getString("dragonType"));
        setHatchTime(compound.getInt("hatchTime"));
    }
    
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        
        compound.putString("dragonType", getDragonType());
        compound.putInt("hatchTime", getHatchTime());
    }
    
    public void setDragonType(String dragonType) { dataManager.set(DRAGON_TYPE, dragonType); }
    public String getDragonType() { return dataManager.get(DRAGON_TYPE); }
    
    public void setHatchTime(int hatchTime) { dataManager.set(HATCH_TIME, hatchTime); }
    public int getHatchTime() { return dataManager.get(HATCH_TIME); }
    
    // ================================
    
    @Override
    public void livingTick() {
        super.livingTick();
        
        EntityType type = ModUtils.getTypeByString(getDragonType());
        if (type != null) {
            Entity entity = type.create(world);
            Random rand = new Random(3257965L); // Use a seed so server + client is synced
            int bounds = Math.max(getHatchTime() / 150, 3);
            
            if (entity instanceof AbstractDragonEntity && getHatchTime() < ((AbstractDragonEntity) entity).hatchTimer / 2 && rand.nextInt(bounds) == 0 && getAnimation() != WIGGLE_ANIMATION) {
                setAnimation(WIGGLE_ANIMATION);
                playSound(SoundEvents.ENTITY_TURTLE_EGG_CRACK, 1, 1);
            }
        }
        
        if (world.isRemote && ticksExisted % 3 == 0) {
            double x = posX + rand.nextGaussian() * 0.2d;
            double y = posY + rand.nextDouble() + getHeight() / 2;
            double z = posZ + rand.nextGaussian() * 0.2d;
            world.addParticle(new RedstoneParticleData(1f, 1f, 0, 0.5f), x, y, z, 0, 0, 0);
        }
        
        
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (getAnimation() != NO_ANIMATION) {
            ++animationTick;
            if (animationTick >= animation.getDuration()) setAnimation(NO_ANIMATION);
        }
        
        try {
            DragonTypes type = getDragonTypeEnum();
            if (getWidth() != type.getWidth() || getHeight() != type.getHeight()) recalculateSize();
            int hatchTime = getHatchTime();
            if (hatchTime > 0) {
                setHatchTime(--hatchTime);
            }
            else hatch();
        }
        catch (NullPointerException e) { safeError(); }
    }
    
    /**
     * Called to hatch the dragon egg
     * Usage: <P>
     *  - Get the dragon EntityType (If its something it shouldnt, safely fail) <P>
     *  - Set the dragons growing age to a baby <P>
     *  - Set the position of that dragon to the position of this egg <P>
     *  - Remove this entity (the egg) and play any effects
     */
    public void hatch() {
        EntityType type = ModUtils.getTypeByString(getDragonType());
        if (type == null) {
            safeError();
            return;
        }
        Entity entity = type.create(world);
        if (!(entity instanceof AbstractDragonEntity)) {
            safeError();
            return;
        }
        AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
        
        if (!world.isRemote) {
            dragon.setPosition(posX, posY, posZ);
            dragon.setGrowingAge(-(dragon.hatchTimer * 2));
            dragon.onInitialSpawn(world, world.getDifficultyForLocation(getPosition()), SpawnReason.BREEDING, null, null);
            world.addEntity(dragon);
        } else {
            for (int i = 0; i < getWidth() * 25; ++i) {
                double x = rand.nextGaussian() * 0.2f;
                double y = rand.nextDouble() * 0.45f;
                double z = rand.nextGaussian() * 0.2f;
                world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(SetupItems.dragonEgg)), posX, posY, posZ, x, y, z);
            }
        }
        world.playSound(posX, posY, posZ, SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 1, 1, false);
        remove();
    }
    
    /**
     * Called When the dragon type of the egg is not what it should be.
     */
    private void safeError() {
        ModUtils.L.error("THIS ISNT A DRAGON WTF KIND OF ABOMINATION IS THIS HATCHING?!?! Unknown Entity Type for Dragon Egg @ {}", getPosition());
        remove();
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getImmediateSource() instanceof PlayerEntity) {
            CompoundNBT tag = new CompoundNBT();
            
            tag.putInt("hatchTime", getHatchTime());
            tag.putString("dragonType", getDragonType());
            ItemStack itemStack = new ItemStack(SetupItems.dragonEgg);
            itemStack.setTag(tag);
            InventoryHelper.spawnItemStack(world, posX, posY, posZ, itemStack);
            remove();
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public EntitySize getSize(Pose pose) {
        DragonTypes type = getDragonTypeEnum();
        
        if (type == null) return super.getSize(pose);
        else return super.getSize(pose).scale(type.getWidth(), type.getHeight());
    }
    
    public DragonTypes getDragonTypeEnum() {
        EntityType type = ModUtils.getTypeByString(getDragonType());
    
        for (DragonTypes value : DragonTypes.values()) if (value.getType() == type) return value;
        return null;
    }
    
    public static DragonTypes getDragonTypeEnum(String typeKey) {
        EntityType type = ModUtils.getTypeByString(typeKey);
        
        for (DragonTypes value : DragonTypes.values()) if (value.getType() == type) return value;
        return null;
    }
    
    @Override
    protected boolean isMovementBlocked() { return true; }
    
    @Override
    public boolean canBePushed() { return false; }
    
    @Override
    protected void collideWithEntity(Entity entityIn) { }
    
    // This is needed because it seems to be ignored on server world...
    @Override
    public void onKillCommand() { remove(); }
    
    @Override
    public Iterable<ItemStack> getArmorInventoryList() { return NonNullList.withSize(4, ItemStack.EMPTY); }
    
    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) { return ItemStack.EMPTY; }
    
    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) { }
    
    @Override
    public HandSide getPrimaryHand() { return HandSide.LEFT; }
    
    @Override
    public IPacket<?> createSpawnPacket() { return new SSpawnMobPacket(this); }
    
    // === Animation ===
    @Override
    public int getAnimationTick() { return animationTick; }
    
    @Override
    public void setAnimationTick(int i) { this.animationTick = i; }
    
    @Override
    public Animation getAnimation() { return animation; }
    
    @Override
    public void setAnimation(Animation animation) {
        this.animation = animation;
        setAnimationTick(0);
        
        
        if (world.isRemote && animation == WIGGLE_ANIMATION) {
            wiggleInvert = rand.nextBoolean();
            wiggleInvert2 = rand.nextBoolean();
        }
    }
    
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION, WIGGLE_ANIMATION}; }
    
    // ================
    
    /**
     * Enum to define the hitbox sizes of the eggs depending on dragon type
     */
    public enum DragonTypes
    {
        DRAKE(SetupEntities.overworldDrake, 0.65f, 1f),
        SILVER_GLIDER(SetupEntities.silverGlider, 0.4f, 0.65f),
        ROOST_STALKER(SetupEntities.roostStalker, 0.25f, 0.35f);
        
        private EntityType dragonType;
        private float width, height;
        
        DragonTypes(EntityType type, float width, float height) {
            this.dragonType = type;
            this.width = width;
            this.height = height;
        }
        
        public EntityType getType() { return dragonType; }
        
        public float getWidth() { return width; }
        
        public float getHeight() { return height; }
        
    }
}
