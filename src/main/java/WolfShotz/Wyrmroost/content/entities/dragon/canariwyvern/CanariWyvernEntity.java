package WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

public class CanariWyvernEntity extends AbstractDragonEntity
{
    public CanariWyvernEntity(EntityType<? extends AbstractDragonEntity> dragon, World world) {
        super(dragon, world);
    }
    
    @Override
    protected void registerData() {
        super.registerData();
        
        dataManager.register(VARIANT, getRNG().nextInt(5));
    }
    
    @Override
    public int getSpecialChances() { return 0; }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack) {
        if (super.processInteract(player, hand, stack)) return true;
        
        
        
        if (isOwner(player)) {
            if (player.isSneaking()) {
                setSit(!isSitting());
                
                return true;
            }
            
            setSit(true);
            startRiding(player);
            return true;
        }
        
        return false;
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();
        
        Entity entity = getRidingEntity();
        if (entity.isAlive()) {
            stopRiding();
            return;
        }
        if (!(entity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) entity;
        if (player.isSneaking() && !player.abilities.isFlying) {
            stopRiding();
            return;
        }
    
        rotationYaw = player.rotationYawHead;
        rotationPitch = player.rotationPitch;
        setRotation(rotationYaw, rotationPitch);
        rotationYawHead = player.rotationYawHead;
        prevRotationYaw = player.rotationYawHead;
        setPosition(player.posX, player.posY + 1.8, player.posZ);
    }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    public List<Item> getFoodItems() {
        return null;
    }
    
    @Override
    public DragonEggProperties createEggProperties() {
        return null;
    }
    
    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }
}
