package WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.event.SetupItems;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.SharedEntityGoals;
import com.github.alexthe666.citadel.animation.Animation;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class DragonFruitDrakeEntity extends AbstractDragonEntity implements IShearable
{
    private int shearCooldownTime;
    
    public DragonFruitDrakeEntity(EntityType<? extends DragonFruitDrakeEntity> dragon, World world) {
        super(dragon, world);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        
        goalSelector.addGoal(5, SharedEntityGoals.nonTamedTemptGoal(this, 0.8f, false, Ingredient.fromItems(getFoodItems().toArray(new Item[0]))));
    }
    
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.9986521f);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20d);
        getAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(20d);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4d);
    }
    
    // ================================
    //           Entity NBT
    // ================================
    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        
        nbt.putInt("shearcooldown", shearCooldownTime);
    }
    
    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        
        this.shearCooldownTime = nbt.getInt("shearcooldown");
    }
    // ================================
    
    @Override
    public void tick() {
        super.tick();
        
        if (shearCooldownTime > 0) --shearCooldownTime;
    }
    
    @Override
    public boolean isShearable(@Nonnull ItemStack item, IWorldReader world, BlockPos pos) { return shearCooldownTime <= 0; }
    
    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune) {
        playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1f, 1f);
        shearCooldownTime += 12000;
        return Lists.newArrayList(new ItemStack(SetupItems.foodDragonFruit, 1));
    }
    
    @Override
    public boolean canFly() { return false; }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    public List<Item> getFoodItems() {
        List<Item> foods = Tags.Items.CROPS.getAllElements().stream().filter(i -> i.getItem() != Items.NETHER_WART).collect(Collectors.toList());
        Collections.addAll(foods, SetupItems.foodDragonFruit, Items.APPLE, Items.SWEET_BERRIES);
        return foods;
    }
    
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION}; }
}
