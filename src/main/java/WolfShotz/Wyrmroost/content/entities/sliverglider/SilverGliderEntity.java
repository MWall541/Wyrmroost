package WolfShotz.Wyrmroost.content.entities.sliverglider;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
{
    public SilverGliderEntity(EntityType<? extends SilverGliderEntity> entity, World world) {
        super(entity, world);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(30d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.20989d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4.0d);
    }

    // ================================
    //           Entity NBT
    // ================================
    /** Set The chances this dragon can be an albino. Set it to 0 to have no chance */
    @Override
    public int getAlbinoChances() { return 0; }

    // ================================

    /** Array Containing all of the dragons food items */
    @Override
    protected Item[] getFoodItems() { return new Item[] {Items.TROPICAL_FISH, Items.COD, Items.SALMON, Items.COOKED_COD, Items.COOKED_SALMON}; }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) { return null; }

    // == Entity Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[0]; }
    // ==
}
