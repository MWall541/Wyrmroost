package WolfShotz.Wyrmroost.content.entities.canari;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CanariEntity extends AbstractDragonEntity
{
    public CanariEntity(EntityType<? extends CanariEntity> canari, World world) {
        super(canari, world);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20989d);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0d);
    }

    // ================================
    //           Entity NBT
    // ================================
    /** Set The chances this dragon can be an albino. Set it to 0 to have no chance */
    @Override
    public int getAlbinoChances() { return 50; }
    // ================================

    /** Array Containing all of the dragons food items */
    @Override
    protected Item[] getFoodItems() { return new Item[0]; } //TODO

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) { return null; }

    // == Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[0]; }
    // ==
}
