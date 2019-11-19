package WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.content.io.container.DragonFruitDrakeContainer;
import WolfShotz.Wyrmroost.content.world.CapabilityOverworld;
import WolfShotz.Wyrmroost.registry.ModItems;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.DragonFollowOwnerGoal;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.SharedEntityGoals;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.SleepGoal;
import com.github.alexthe666.citadel.animation.Animation;
import com.google.common.collect.Lists;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SaddleItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(3, sitGoal = new SitGoal(this));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1d, false));
        goalSelector.addGoal(5, new DragonBreedGoal(this, false, true));
        goalSelector.addGoal(6, new DragonFollowOwnerGoal(this, 1.2d, 12d, 3d));
        goalSelector.addGoal(7, SharedEntityGoals.wanderAvoidWater(this, 1));
        goalSelector.addGoal(8, SharedEntityGoals.lookAtNoSleeping(this, 7f));
        goalSelector.addGoal(8, SharedEntityGoals.lookRandomlyNoSleeping(this));
        
        targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp(DragonFruitDrakeEntity.class));
        targetSelector.addGoal(2, SharedEntityGoals.nonTamedTargetGoal(this, PlayerEntity.class, 2, true, true, EntityPredicates.CAN_AI_TARGET));
        
        goalSelector.addGoal(2, new SleepGoal(this, false) {
            @Override
            public boolean shouldExecute() {
                if (--sleepTimeout > 0) return false;
                if (isInWaterOrBubbleColumn() || isFlying()) return false;
                int bounds = world.isDaytime()? 1200 : 300;
                return (!isTamed() || isSitting()) && rand.nextInt(bounds) == 0;
            }
    
            @Override
            public boolean shouldContinueExecuting() {
                if (!isSleeping()) return false;
                int bounds = world.isDaytime()? 600 : 150;
                if (rand.nextInt(bounds) == 0) return false;
                if ((dragon.isTamed() && !dragon.isSitting()) || dragon.isBeingRidden()) return false;
                if (dragon.getAttackTarget() != null || !dragon.getNavigator().noPath() || dragon.isAngry()) return false;
                return !dragon.isInWaterOrBubbleColumn() && !dragon.isFlying();
            }
        });
    }
    
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.232524f);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20d);
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
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack) {
        if (super.processInteract(player, hand, stack)) return true;
        
        if (stack.getItem() instanceof SaddleItem && !isSaddled() && !isChild()) {
            getInvCap().ifPresent(i -> {
                i.insertItem(0, stack, false);
                consumeItemFromStack(player, stack);
            });
            
            return true;
        }
        
        if (isSaddled() && !isChild() && !player.isSneaking()) {
            setSit(false);
            player.startRiding(this);
            
            return true;
        }
        
        if (isOwner(player) && player.isSneaking()) {
            setSit(true);
            
            return true;
        }
        
        return false;
    }
    
    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) { return sizeIn.height; }
    
    @Override
    public double getMountedYOffset() { return super.getMountedYOffset() + 0.1d; }
    
    @Override
    public boolean isShearable(@Nonnull ItemStack item, IWorldReader world, BlockPos pos) { return shearCooldownTime <= 0; }
    
    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune) {
        playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1f, 1f);
        shearCooldownTime = 12000;
        return Lists.newArrayList(new ItemStack(ModItems.FOOD_DRAGON_FRUIT.get(), 1));
    }
    
    public static boolean canSpawnHere(EntityType type, IWorld worldIn, SpawnReason reason, BlockPos pos, Random random) {
        World world = worldIn.getWorld();
        
        return world.getDimension() instanceof OverworldDimension && world.getCapability(CapabilityOverworld.OW_CAP).map(CapabilityOverworld::isSpawnsTriggered).orElse(false);
    }
    
    @Override
    public boolean canFly() { return false; }
    
    @Override
    public List<Item> getFoodItems() {
        List<Item> foods = Tags.Items.CROPS.getAllElements().stream().filter(i -> i.getItem() != Items.NETHER_WART).collect(Collectors.toList());
        Collections.addAll(foods, ModItems.FOOD_DRAGON_FRUIT.get(), Items.APPLE, Items.SWEET_BERRIES);
        return foods;
    }
    
    @Override
    public DragonEggProperties createEggProperties() { return new DragonEggProperties(0.45f, 0.75f, 9600); }
    
    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInv, PlayerEntity player) { return new DragonFruitDrakeContainer(this, playerInv, windowID); }
    
    @Override
    public ItemStackHandler createInv() { return new ItemStackHandler(1); }
    
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION}; }
}
