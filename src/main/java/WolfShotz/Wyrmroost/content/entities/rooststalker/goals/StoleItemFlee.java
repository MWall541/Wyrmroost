package WolfShotz.Wyrmroost.content.entities.rooststalker.goals;

import WolfShotz.Wyrmroost.content.entities.ai.goals.NonTamedAvoidGoal;
import WolfShotz.Wyrmroost.content.entities.rooststalker.RoostStalkerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class StoleItemFlee extends NonTamedAvoidGoal
{
    RoostStalkerEntity dragon;
    
    public StoleItemFlee(RoostStalkerEntity dragon) {
        super(dragon, 7f, 1.15f, 1f);
        this.dragon = dragon;
    }
    
    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !dragon.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty();
    }
}
