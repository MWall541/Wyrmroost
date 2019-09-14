package WolfShotz.Wyrmroost.content.entities.dragonegg;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.event.SetupEntities;
import WolfShotz.Wyrmroost.event.SetupItems;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnMobPacket;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class DragonEggEntity extends LivingEntity
{
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
    
    public void setHatchTime(int hatchTime) {
        dataManager.set(HATCH_TIME, hatchTime);
//        this.hatchTime = hatchTime;
    }
    public int getHatchTime() {
//        if (hatchTime != dataManager.get(HATCH_TIME)) // Sync hatch times
//            dataManager.set(HATCH_TIME, hatchTime);
        
        return dataManager.get(HATCH_TIME);
    }
    
    // ================================
    
    
    @Override
    public void livingTick() {
        super.livingTick();
        
        if (world.isRemote && ticksExisted % 3 == 0) {
            double x = posX + rand.nextGaussian() * 0.2d;
            double y = posY + rand.nextDouble() + getHeight() / 2;
            double z = posZ + rand.nextGaussian() * 0.2d;
            world.addParticle(new RedstoneParticleData(1, 1, 0, 0.5f), x, y, z, 0, 0, 0);
        }
    }
    
    @Override
    public void tick() {
        super.tick();
    
        System.out.println(getHatchTime());
        
        if (getDragonType() == null) {
            safeError();
            return;
        }
        
        int hatchTime = getHatchTime();
        if (hatchTime > 0) {
            setHatchTime(--hatchTime);
        } else hatch();
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
        EntityType type = EntityType.byKey(getDragonType()).orElse(null);
        
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
        
        remove();
        if (!world.isRemote) {
            world.addEntity(dragon);
            dragon.setPosition(posX + 0.5d, posY, posZ + 0.5d);
            dragon.setGrowingAge(-(dragon.hatchTimer * 2));
        }
        world.playSound(posX, posY, posZ, SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 1, 1, false);
        
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
            
            ItemEntity itemEntity = new ItemEntity(world, posX, posY, posZ, itemStack);
            
            itemEntity.setMotion(rand.nextGaussian() * 0.05d, 0.25d, rand.nextGaussian() * 0.05d);
            world.addEntity(itemEntity);
            
            remove();
            
            return true;
        }
        
        return false;
    }
    
    public DragonTypes getDragonTypeEnum() {
        EntityType type = EntityType.byKey(getDragonType()).orElse(null);
    
        for (DragonTypes value : DragonTypes.values()) if (value.getType() == type) return value;
        return null;
    }
    
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
    
    public enum DragonTypes
    {
        DRAKE(SetupEntities.overworld_drake),
        SILVER_GLIDER(SetupEntities.silver_glider),
        ROOST_STALKER(SetupEntities.roost_stalker);
        
        private EntityType dragonType;
        private DragonTypes[] types = new DragonTypes[values().length];
        
        DragonTypes(EntityType type) { this.dragonType = type; }
        
        public EntityType getType() { return dragonType; }
    }
}
