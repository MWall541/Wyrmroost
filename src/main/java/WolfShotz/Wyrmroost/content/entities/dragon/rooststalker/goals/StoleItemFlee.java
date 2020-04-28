package WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class StoleItemFlee extends AvoidEntityGoal<PlayerEntity>
{
    RoostStalkerEntity dragon;

    public StoleItemFlee(RoostStalkerEntity dragon)
    {
        super(dragon, PlayerEntity.class, 7f, 1.15f, 1f); // MAPPERS ARE ASS HOLES! SPEEDS ARE SWAPPED FFS FORGE
        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute()
    {
        return !dragon.isTamed()
                && !dragon.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty()
                && super.shouldExecute();
    }
}
