package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.event.SetupItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.world.GameRules;

public class DragonBreedGoal extends BreedGoal
{
    private final AbstractDragonEntity dragon;
    private boolean canInAir, straight;
    
    /**
     * @param canInAir - Setter to have dragons "get it on" in the air! Yeah idfk I blame the community.
     */
    public DragonBreedGoal(AbstractDragonEntity dragon, boolean canInAir, boolean straight) {
        super(dragon, 1.0d);
        
        this.dragon = dragon;
        this.straight = straight;
        this.canInAir = canInAir;
    }
    
    @Override
    public boolean shouldExecute() {
//        if (!canInAir) return false; TODO
        if (straight)
            return super.shouldExecute() && ((AbstractDragonEntity) field_75391_e).getGender() == !dragon.getGender();
        else return super.shouldExecute();
    }
    
    /**
     * Spawns an egg item at the dragons location when bred.
     * Forge breed events are taking in <code>AgeableEntity</code> as a param, thus, they cannot be posted.
     * Mod Makers: Feel free to PR a workaround if you need this!
     */
    @Override
    protected void spawnBaby() {
        CompoundNBT tag = new CompoundNBT();
        ItemStack eggStack = new ItemStack(SetupItems.DRAGON_EGG.get());
    
        tag.putString("dragonType", EntityType.getKey(dragon.getType()).toString());
        tag.putInt("hatchTime", dragon.getEggProperties().getHatchTime());
        eggStack.setTag(tag);
    
        ItemEntity eggItem = new ItemEntity(world, dragon.posX, dragon.posY, dragon.posZ, eggStack);
        ServerPlayerEntity serverplayerentity = animal.getLoveCause();
        
        if (serverplayerentity == null && field_75391_e.getLoveCause() != null) serverplayerentity = field_75391_e.getLoveCause();
    
        if (serverplayerentity != null) serverplayerentity.addStat(Stats.ANIMALS_BRED);
    
        animal.setGrowingAge(6000);
        field_75391_e.setGrowingAge(6000);
        animal.resetInLove();
        field_75391_e.resetInLove();
        eggItem.setMotion(0, animal.getHeight() / 2, 0);
        world.addEntity(eggItem);
        if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
            world.addEntity(new ExperienceOrbEntity(world, dragon.posX, dragon.posY, dragon.posZ, dragon.getRNG().nextInt(7) + 1));
    }
}
