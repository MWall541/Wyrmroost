package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.entityhelpers.multipart.IMultiPartEntity;
import WolfShotz.Wyrmroost.util.entityhelpers.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ButterflyLeviathanEntity extends AbstractDragonEntity implements IMultiPartEntity
{
    // Multipart
    public MultiPartEntity headPart;
    public MultiPartEntity wingLeftPart;
    public MultiPartEntity wingRightPart;
    public MultiPartEntity tail1Part;
    public MultiPartEntity tail2Part;
    public MultiPartEntity tail3Part;
    
    public ButterflyLeviathanEntity(EntityType<? extends ButterflyLeviathanEntity> blevi, World world) {
        super(blevi, world);
    
        headPart = createPart(this, 4.2f, 0, 0.75f, 2.25f, 1.75f);
        wingLeftPart = createPart(this, 5f, -90, 0.35f, 2.25f, 3.15f);
        wingRightPart = createPart(this, 5f, 90, 0.35f, 2.25f, 3.15f);
        tail1Part = createPart(this, 4.5f, 180, 0.35f, 2.25f, 2.25f, 0.85f);
        tail2Part = createPart(this, 8f, 180, 0.35f, 2.25f, 2.25f, 0.75f);
        tail3Part = createPart(this, 12f, 180, 0.5f, 2f, 2f, 0.5f);
    }
    
    // ================================
    //           Entity NBT
    // ================================
    
    @Override
    protected void registerData() {
        super.registerData();
        
        dataManager.register(VARIANT, rand.nextInt(2));
    }
    
    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
    
        nbt.putInt("variant", getVariant());
    }
    
    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        
        setVariant(nbt.getInt("variant"));
    }
    
    // =================================
    
    @Override
    public void tick() {
        super.tick();
        tickParts();
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack) {
        if (super.processInteract(player, hand, stack)) return true;
        
        return false;
    }
    
    @Override
    public MultiPartEntity[] getParts() { return new MultiPartEntity[] {headPart, wingLeftPart, wingRightPart, tail1Part, tail2Part, tail3Part}; }
    
    @Override
    public boolean canFly() { return false; }
    
    @Override
    public boolean canBeRiddenInWater(Entity rider) { return true; }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    protected Item[] getFoodItems() { return new Item[] {Items.SEAGRASS}; }
    
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION}; }
}
