package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.client.animation.TickFloat;
import WolfShotz.Wyrmroost.entities.dragon.helpers.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.goals.FlyerWanderGoal;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Collection;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
{
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat sleepTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1);

    public TemptGoal temptGoal;

    public SilverGliderEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());
        registerVariantData(3, true); // For females, this value is redundant
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(20d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.24);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.36);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(3, temptGoal = CommonGoalWrappers.nonTamedTemptGoal(this, 0.8d, true, Ingredient.fromItems(getFoodItems().toArray(new Item[0]))));
        goalSelector.addGoal(4, CommonGoalWrappers.nonTamedAvoidGoal(this, PlayerEntity.class, 16f, 0.8f));
        goalSelector.addGoal(5, new DragonBreedGoal(this, true));
        goalSelector.addGoal(6, CommonGoalWrappers.followOwner(this, 1, 10f, 0.2f));
        goalSelector.addGoal(8, CommonGoalWrappers.lookAt(this, 10f));
        goalSelector.addGoal(9, new LookRandomlyGoal(this));

        goalSelector.addGoal(8, new FlyerWanderGoal(this, true)
        {
            @Override
            public Vec3d getPosition()
            {
                Vec3d vec3d = super.getPosition();
                if (vec3d != null && isFlying())
                {
                    for (int i = 1; i < 4; i++) // avoid water: y value is always positive
                        if (world.getBlockState(SilverGliderEntity.this.getPosition().down(i)).getMaterial().isLiquid())
                            return new Vec3d(vec3d.x, Math.abs(vec3d.y), vec3d.z);
                }
                return vec3d;
            }
        });
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        sitTimer.add((isSitting() || isSleeping())? 0.2f : -0.2f);
        sleepTimer.add(isSleeping()? 0.05f : -0.02f);
        flightTimer.add(isFlying()? -0.1f : 0.1f);
    }

    @Override
    public void updateRidden()
    {
        super.updateRidden();

        if (!(getRidingEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) getRidingEntity();

        if (shouldGlide(player))
        {

        }
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.playerInteraction(player, hand, stack)) return true;

        // tame
        if (!isTamed() && !world.isRemote && temptGoal.isRunning() && isBreedingItem(stack))
        {
            tame(getRNG().nextInt(5) == 0, player);
            eat(stack);
            return true;
        }

        if (isOwner(player)) // owner only actions
        {
            // sit
            if (player.isSneaking())
            {
                setSit(!isSitting());
                return true;
            }

            // ride player
            if (player.getPassengers().size() < 3)
            {
                startRiding(player, true);
                setSit(false);
                clearAI();
                return true;
            }
        }

        return false;
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        if (!isFlying()) super.travel(vec3d);

        moveRelative(0.1f, vec3d);
        move(MoverType.SELF, getMotion());
        setMotion(getMotion().scale(0.6f));
    }

    public boolean shouldGlide(PlayerEntity player)
    {
        return player.isJumping
                && !player.abilities.isFlying
                && !player.isElytraFlying()
                && !world.containsAnyLiquid(player.getBoundingBox())
                && canFly()
                && player.getPassengers().indexOf(this) == 0
                && getAltitude(false) - player.getHeight() > 1.75;
    }

    @Override
    public void doSpecialEffects()
    {
        if (ticksExisted % 5 == 0)
        {
            double x = getPosX() + getRNG().nextGaussian();
            double y = getPosY() + getRNG().nextDouble();
            double z = getPosZ() + getRNG().nextGaussian();
            world.addParticle(new RedstoneParticleData(1f, 0.8f, 0, 1f), x, y, z, 0, 0.1925f, 0);
        }
    }

    @Override
    public int getSpecialChances() { return 500; }

    @Override
    public Collection<Item> getFoodItems() { return ItemTags.FISHES.getAllElements(); }

    @Override
    public DragonEggProperties createEggProperties() { return new DragonEggProperties(0.4f, 0.65f, 12000); }

    public static void setSpawnConditions()
    {
        ModUtils.getBiomesByTypes(BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.BEACH)
                .forEach(b -> b.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WREntities.SILVER_GLIDER.get(), 10, 1, 4)));
        EntitySpawnPlacementRegistry.register(WREntities.SILVER_GLIDER.get(),
                EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS,
                Heightmap.Type.MOTION_BLOCKING,
                (glider, world, reason, pos, rand) ->
                {
                    if (reason == SpawnReason.SPAWNER) return true;
                    Block block = world.getBlockState(pos.down(1)).getBlock();
                    return block == Blocks.AIR || block == Blocks.SAND && world.getLightSubtracted(pos, 0) > 8;
                }
        );
    }
}
