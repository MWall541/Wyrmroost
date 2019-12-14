package WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.util.MathUtils;
import WolfShotz.Wyrmroost.util.entityhelpers.PlayerMount;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class CanariWyvernEntity extends AbstractDragonEntity implements PlayerMount.IShoulderMount
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
            
            if (PlayerMount.getShoulderEntityCount(player) < 2) {
                setSit(true);
                startRiding(player, true);
                
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();
        
        Entity entity = getRidingEntity();
        
        if (!entity.isAlive()) {
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
        
        double xOffset = checkShoulderOccupants(player)? -0.35f : 0.35f;
        
        Vec3d vec3d1 = MathUtils.calculateYawAngle(player.renderYawOffset, xOffset, 0.1).add(player.posX, 0, player.posZ);
        setPosition(vec3d1.x, player.posY + 1.4, vec3d1.z);
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
