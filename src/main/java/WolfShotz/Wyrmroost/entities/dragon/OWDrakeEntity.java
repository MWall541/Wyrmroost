package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.animation.Animation;
import WolfShotz.Wyrmroost.client.screen.staff.StaffScreen;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.containers.util.SlotBuilder;
import WolfShotz.Wyrmroost.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.entities.dragon.helpers.goals.*;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.items.DragonArmorItem;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.NetworkUtils;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.QuikMaths;
import WolfShotz.Wyrmroost.util.TickFloat;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.entity.SharedMonsterAttributes.*;

/**
 * Created by WolfShotz 7/10/19 - 22:18
 */
public class OWDrakeEntity extends AbstractDragonEntity
{
    public static final int SADDLE_SLOT = 0;
    public static final int ARMOR_SLOT = 1;
    public static final int CHEST_SLOT = 2;
    private static final UUID SPRINTING_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier SPRINTING_SPEED_BOOST = (new AttributeModifier(SPRINTING_ID, "Sprinting speed boost", 1.15F, AttributeModifier.Operation.MULTIPLY_TOTAL)).setSaved(false);

    // Dragon Entity Animations
    public static final Animation GRAZE_ANIMATION = new Animation(35);
    public static final Animation HORN_ATTACK_ANIMATION = new Animation(15);
    public static final Animation ROAR_ANIMATION = new Animation(86);
    public static final Animation TALK_ANIMATION = new Animation(20);
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);

    // Dragon Entity Data
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(OWDrakeEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ARMOR = EntityDataManager.createKey(OWDrakeEntity.class, DataSerializers.ITEMSTACK);

    public OWDrakeEntity(EntityType<? extends OWDrakeEntity> drake, World world)
    {
        super(drake, world);

        registerVariantData(2, true);
        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());

        sitTimer.set(isSitting()? 1 : 0);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new ControlledAttackGoal(this, 1, true, 2.1, d -> NetworkUtils.sendAnimationPacket(d, HORN_ATTACK_ANIMATION)));
        goalSelector.addGoal(6, CommonGoalWrappers.followOwner(this, 1.2d, 12f, 3f));
        goalSelector.addGoal(7, new DragonBreedGoal(this, true));
        goalSelector.addGoal(8, new GrazeGoal(this, 2, GRAZE_ANIMATION));
        goalSelector.addGoal(9, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(10, CommonGoalWrappers.lookAt(this, 10f));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, PlayerEntity.class, false));
        targetSelector.addGoal(4, new HurtByTargetGoal(this)
        {
            @Override
            public boolean shouldExecute() { return super.shouldExecute() && !isChild(); }
        });
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        
        getAttribute(MAX_HEALTH).setBaseValue(50d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.20989d);
        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(10);
        getAttribute(FOLLOW_RANGE).setBaseValue(20d);
        getAttribute(ATTACK_KNOCKBACK).setBaseValue(3.2d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(5d);
    }
    
    // ================================
    //           Entity Data
    // ================================

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(SADDLED, false);
        dataManager.register(ARMOR, ItemStack.EMPTY);
    }

    public boolean hasChest() { return getStackInSlot(CHEST_SLOT) != ItemStack.EMPTY; }

    public boolean isSaddled() { return dataManager.get(SADDLED); }

    public DragonArmorItem getArmor() { return (DragonArmorItem) dataManager.get(ARMOR).getItem(); }

    public void setArmored(Item armor)
    {
        if (!(armor instanceof DragonArmorItem)) armor = null;
        dataManager.set(ARMOR, new ItemStack(armor));
        if (!world.isRemote)
        {
            IAttributeInstance attribute = getAttribute(SharedMonsterAttributes.ARMOR);
            if (attribute.getModifier(DragonArmorItem.ARMOR_UUID) != null)
                attribute.removeModifier(DragonArmorItem.ARMOR_UUID);
            if (armor != null)
            {
                attribute.applyModifier(new AttributeModifier("Armor Modifier", ((DragonArmorItem) armor).getDmgReduction(), AttributeModifier.Operation.ADDITION).setSaved(true));
                playSound(SoundEvents.ENTITY_HORSE_ARMOR, 1, 1);
            }
        }
    }

    public boolean isArmored() { return dataManager.get(ARMOR).getItem() instanceof DragonArmorItem; }

    public void setSprinting(boolean sprinting)
    {
        if (isSprinting() == sprinting) return;

        IAttributeInstance attribute = getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        
        super.setSprinting(sprinting);
        
        if (attribute.getModifier(SPRINTING_ID) != null) attribute.removeModifier(SPRINTING_SPEED_BOOST);
        if (sprinting) attribute.applyModifier(SPRINTING_SPEED_BOOST);
    }

    @Override
    public int getSpecialChances() { return 100; }

    @Override
    public DragonInvHandler createInv() { return new DragonInvHandler(this, 24); }
    
    // ================================
    
    @Override
    public void livingTick()
    {
        sitTimer.add((isSitting() || isSleeping())? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.04f : -0.1f);

        if (!world.isRemote)
        {
            if ((getAttackTarget() == null || !getAttackTarget().isAlive()) && isAngry()) setAngry(false);
            setSprinting(isAngry());
        }

        if (getAnimation() == ROAR_ANIMATION)
        {
            if (getAnimationTick() == 1)
                playSound(WRSounds.OWDRAKE_ROAR.get(), 2.5f, 1f);
            if (getAnimationTick() == 15)
            {
                for (Entity e : getEntitiesNearby(5)) // Dont get too close now ;)
                {
                    if (e instanceof OWDrakeEntity) continue;
                    double angle = (QuikMaths.getAngle(getPosX(), e.getPosX(), getPosZ(), e.getPosZ()) + 90) * Math.PI / 180;
                    double x = 1.2 * (-Math.cos(angle));
                    double z = 1.2 * (-Math.sin(angle));
                    e.addVelocity(x, 0.4d, z);
                }
            }
            if (getAnimationTick() > 15)
            {
                for (Entity e : getEntitiesNearby(20))
                {
                    if (!(e instanceof LivingEntity) || e instanceof OWDrakeEntity) continue;
                    ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 120));
                }

                if (!isTamed() && !getPassengers().isEmpty())
                {
                    for (Entity e : getPassengers())
                    {
                        e.stopRiding();
                        e.setMotion(QuikMaths.nextPseudoDouble(getRNG()) * 3.5, 0.8, QuikMaths.nextPseudoDouble(getRNG()) * 3.5);
                    }
                }
            }
        }

        if (getAnimation() == HORN_ATTACK_ANIMATION)
        {
            prevRotationYaw = renderYawOffset = rotationYaw = rotationYawHead;
            if (getAnimationTick() == 8)
            {
                playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1, 0.5f);
                world.playSound(getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.AMBIENT, 1f, 0.5f, false);
                AxisAlignedBB size = getBoundingBox().shrink(0.2);
                AxisAlignedBB aabb = size.offset(QuikMaths.calculateYawAngle(renderYawOffset, 0, size.getXSize() * 1.2));
                attackInAABB(aabb);
            }
        }
        super.livingTick();
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.playerInteraction(player, hand, stack)) return true;

        if (stack.getItem() == Items.SADDLE && !isSaddled() && !isChild())
        {
            if (!world.isRemote) setStackInSlot(SADDLE_SLOT, stack);
            consumeItemFromStack(player, stack);
            return true;
        }

        if (isSaddled() && !isBreedingItem(stack) && !isChild() && ((!isTamed() && !isInWater()) || isOwner(player)))
        {
            setSit(false);
            if (!world.isRemote) player.startRiding(this);
            setHomePos(Optional.empty());

            return true;
        }

        if (isFoodItem(stack) && isChild() && !isTamed())
        {
            tame(getRNG().nextInt(10) == 0, player);
            consumeItemFromStack(player, stack);

            return true;
        }
        
        return false;
    }

    @Override
    public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);

        if (!world.isRemote && !isTamed() && passenger instanceof LivingEntity)
        {
            int rand = getRNG().nextInt(100);

            if (passenger instanceof PlayerEntity && rand == 0) tame(true, (PlayerEntity) passenger);
            else if (rand % 20 == 0 && getAnimation() != ROAR_ANIMATION && EntityPredicates.CAN_AI_TARGET.test(passenger))
                setAttackTarget((LivingEntity) passenger);
        }
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        if (slot == SADDLE_SLOT)
        {
            dataManager.set(SADDLED, !stack.isEmpty());
            if (!stack.isEmpty()) playSound(SoundEvents.ENTITY_HORSE_SADDLE, 1, 1);
        }

        if (slot == ARMOR_SLOT) setArmored(stack.getItem());
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
    {
        Biome biome = worldIn.getBiome(new BlockPos(this));
        Set<Biome> biomes = BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA);

        if (biomes.contains(biome)) setVariant(1);
        else setVariant(0);

        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void handleSleep()
    {
        if (!isSleeping()
                && --sleepCooldown <= 0
                && !world.isDaytime()
                && (!isTamed() || isSitting())
                && !isBeingRidden()
                && getAttackTarget() == null
                && getNavigator().noPath()
                && !isAngry()
                && !isInWaterOrBubbleColumn()
                && !isFlying()
                && getRNG().nextInt(300) == 0) setSleeping(true);
        else if (isSleeping() && world.isDaytime() && getRNG().nextInt(150) == 0) setSleeping(false);
    }

    @Override
    protected boolean canBeRidden(Entity entityIn)
    {
        if (!super.canBeRidden(entityIn)) return false;
        if (isTamed()) return true;
        return getAnimation() != HORN_ATTACK_ANIMATION && getAnimation() != ROAR_ANIMATION;
    }

    @Override
    public boolean canFly() { return false; }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier)
    {
        return super.onLivingFall(distance - 2, damageMultiplier);
    }

    @Override
    public void setAttackTarget(@Nullable LivingEntity target)
    {
        if (target != null && getAttackTarget() != target)
        {
            setAngry(true);
            if (!isTamed() && getAnimation() != OWDrakeEntity.ROAR_ANIMATION)
                NetworkUtils.sendAnimationPacket(OWDrakeEntity.this, OWDrakeEntity.ROAR_ANIMATION);
        }
        super.setAttackTarget(target);
    }
    
    @Override
    public void eatGrassBonus()
    {
        if (isChild()) addGrowth(60);
        if (getHealth() < getMaxHealth()) heal(4f);
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        if (ticksExisted % 2 == 0) playSound(SoundEvents.ENTITY_COW_STEP, 0.3f, 1);
        
        super.playStepSound(pos, blockIn);
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.OWDRAKE_IDLE.get();
    }
    
    @Override
    public void playAmbientSound()
    {
        if (!isSleeping())
        {
            if (noActiveAnimation()) setAnimation(TALK_ANIMATION);
            SoundEvent soundevent = getAmbientSound();
            if (soundevent != null) playSound(soundevent, 1, 1);
        }
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return WRSounds.OWDRAKE_HURT.get();
    }
    
    @Override
    protected void playHurtSound(DamageSource source)
    {
        if (noActiveAnimation()) setAnimation(TALK_ANIMATION);
        
        super.playHurtSound(source);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return WRSounds.OWDRAKE_DEATH.get(); }

    @Override
    public void performGenericAttack() { setAnimation(HORN_ATTACK_ANIMATION); }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (isSitting() || isSleeping()) size = size.scale(1, 0.75f);
        return size;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) { return 2 + rand.nextInt(3); }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        screen.actions.add(StaffAction.INVENTORY);
        screen.addAction(StaffAction.TARGETING);
        super.addScreenInfo(screen);
    }

    @Override
    public void addContainerInfo(DragonInvContainer container)
    {
        super.addContainerInfo(container);

        DragonInvHandler inv = container.inventory;

        container.addSlot(new SlotBuilder(inv, SADDLE_SLOT, 17, 45).only(Items.SADDLE));
        container.addSlot(new SlotBuilder(inv, ARMOR_SLOT, 17, 63).only(DragonArmorItem.class));
        container.addSlot(new SlotBuilder(inv, CHEST_SLOT, 17, 81).only(Items.CHEST).limit(1).canTake(p -> inv.isEmptyAfter(CHEST_SLOT)));
        container.makeSlots(3, 51, 45, 7, 3, (i, x, z) -> new SlotBuilder(inv, i, x, z).condition(this::hasChest));
    }

    @Override
    public void setMountCameraAngles(boolean backView)
    {
        if (backView) GlStateManager.translated(0, -0.5d, 0.5d);
        else GlStateManager.translated(0, 0, -3d);
    }

    @Override
    public DragonEggProperties createEggProperties()
    {
        return new DragonEggProperties(0.65f, 1f, 18000)
                .setCustomTexture(Wyrmroost.rl("textures/entity/dragon/owdrake/egg.png"));
    }

    @Override
    public Collection<Item> getFoodItems() { return new ArrayList<>(Tags.Items.CROPS_WHEAT.getAllElements()); }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, GRAZE_ANIMATION, HORN_ATTACK_ANIMATION, ROAR_ANIMATION};
    }
}
