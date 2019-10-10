package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class ButterflyLeviathanEntity extends AbstractDragonEntity
{
    public ButterflyLeviathanEntity(EntityType<? extends ButterflyLeviathanEntity> blevi, World world) {
        super(blevi, world);
    }
    
    @Override
    protected void registerData() {
        super.registerData();
        
        dataManager.register(VARIANT, rand.nextInt(2));
    }
    
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
    
        compound.putInt("variant", getVariant());
    }
    
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        
        setVariant(compound.getInt("variant"));
    }
    
    @Override
    public boolean canFly() { return false; }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    protected Item[] getFoodItems() {
        return new Item[0];
    }
    
    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }
}
