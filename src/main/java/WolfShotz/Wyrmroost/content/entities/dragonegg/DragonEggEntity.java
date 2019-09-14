package WolfShotz.Wyrmroost.content.entities.dragonegg;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.event.SetupItems;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnGlobalEntityPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class DragonEggEntity extends Entity implements IAnimatedEntity
{
    public int hatchTime;
    private int animationTick;
    private Animation animation;
    
    private static final DataParameter<String> DRAGON_TYPE = EntityDataManager.createKey(DragonEggEntity.class, DataSerializers.STRING);
    private static final DataParameter<Integer> HATCH_TIME = EntityDataManager.createKey(DragonEggEntity.class, DataSerializers.VARINT);
    
    public DragonEggEntity(EntityType<? extends DragonEggEntity> dragonEgg, World world) {
        super(dragonEgg, world);
    }
    
    
    @Override
    protected void registerData() {
        dataManager.register(DRAGON_TYPE, "");
        dataManager.register(HATCH_TIME, 1200);
    }
    
    // ================================
    //           Entity NBT
    // ================================
    
    @Override
    protected void readAdditional(CompoundNBT compound) {
        setDragonType(compound.getString("dragonType"));
        setHatchTime(compound.getInt("hatchTime"));
    }
    
    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putString("dragonType", getDragonType());
        compound.putInt("hatchTime", getHatchTime());
    }
    
    public void setDragonType(String dragonType) { dataManager.set(DRAGON_TYPE, dragonType); }
    public String getDragonType() { return dataManager.get(DRAGON_TYPE); }
    
    public void setHatchTime(int hatchTime) {
        dataManager.set(HATCH_TIME, hatchTime);
        this.hatchTime = hatchTime;
    }
    public int getHatchTime() {
        if (hatchTime != dataManager.get(HATCH_TIME)) // Sync hatch times
            dataManager.set(HATCH_TIME, hatchTime);
        
        return dataManager.get(HATCH_TIME);
    }
    
    // ================================
    
    @Override
    public void tick() {
        super.tick();
        
        if (getDragonType() == null) remove();
        
        if (hatchTime > 0) --hatchTime;
        if (hatchTime <= 0) hatch();
    }
    
    public void hatch() {
        EntityType type = EntityType.byKey(getDragonType()).orElse(null);
        
        if (type == null) {
            ModUtils.L.error("DragonEggEntity @ {} return a null dragon type!", getPosition());
            return;
        }
        
        Entity entity = type.create(world);
        
        if (!(entity instanceof AbstractDragonEntity)) {
            ModUtils.L.error("THIS ISNT A DRAGON WTF KIND OF ABOMINATION IS THIS HATCHING?!?! Unknown Entity Type for Dragon Egg @ {}", getPosition());
            return;
        }
        
        AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
        dragon.setGrowingAge(-(dragon.hatchTimer * 2));
        remove();
        dragon.setPosition(posX + 0.5d, posY, posZ + 0.5d);
        world.playSound(posX, posY, posZ, SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 1, 1, false);
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
            
            itemEntity.setMotion(Math.min(rand.nextDouble(), 0.25d), Math.min(rand.nextDouble(), 0.3d), Math.min(rand.nextDouble(), 0.25d));
            world.addEntity(itemEntity);
            
            remove();
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public IPacket<?> createSpawnPacket() { return new SSpawnGlobalEntityPacket(this); }
    
    @Override
    public int getAnimationTick() { return animationTick; }
    
    @Override
    public void setAnimationTick(int animationTick) { this.animationTick = animationTick; }
    
    @Override
    public Animation getAnimation() { return this.animation; }
    
    @Override
    public void setAnimation(Animation animation) {
        setAnimationTick(0);
        setAnimation(animation);
    }
    //TODO
    @Override
    public Animation[] getAnimations() { return new Animation[0]; }
}
