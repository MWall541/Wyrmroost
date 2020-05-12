package WolfShotz.Wyrmroost.entities.dragon.helpers.goals;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.GameRules;

public class DragonBreedGoal extends BreedGoal
{
    private final AbstractDragonEntity dragon;
    private boolean canInAir, straight;

    /**
     * @param dragon   duh
     * @param canInAir I uh. hm.
     * @param straight Should respect genders, otherwise bang the same one!
     */
    public DragonBreedGoal(AbstractDragonEntity dragon, boolean canInAir, boolean straight)
    {
        super(dragon, 1.0d);
        
        this.dragon = dragon;
        this.straight = straight;
        this.canInAir = canInAir;
    }
    
    @Override
    public boolean shouldExecute()
    {
        if (straight)
            return super.shouldExecute() && ((AbstractDragonEntity) targetMate).isMale() == !dragon.isMale();
        else return super.shouldExecute();
    }
    
    /**
     * Spawns an egg item at the dragons location when bred.
     * Forge breed events are taking in <code>AgeableEntity</code> as a param, thus, they cannot be posted.
     * Mod Makers: Feel free to PR a workaround if you need this!
     */
    @Override
    protected void spawnBaby()
    {
        dragon.createChild(null);
        ServerPlayerEntity serverplayerentity = animal.getLoveCause();

        if (serverplayerentity == null && targetMate.getLoveCause() != null)
            serverplayerentity = targetMate.getLoveCause();

        if (serverplayerentity != null) serverplayerentity.addStat(Stats.ANIMALS_BRED);

        animal.setGrowingAge(6000);
        targetMate.setGrowingAge(6000);
        animal.resetInLove();
        targetMate.resetInLove();
        if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
            world.addEntity(new ExperienceOrbEntity(world, dragon.getPosX(), dragon.getPosY(), dragon.getPosZ(), dragon.getRNG().nextInt(7) + 1));
    }
}
