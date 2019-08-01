package WolfShotz.Wyrmroost.content.entities.sliverglider;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
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
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(2.1d);
    }

    // ================================
    //           Entity NBT
    // ================================
    /** Set The chances this dragon can be an albino. Set it to 0 to have no chance */
    @Override
    public int getAlbinoChances() { return 0; }

    // ================================


    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (stack == ItemStack.EMPTY && !world.isRemote) {
            player.startRiding(this);
        }

        return super.processInteract(player, hand);
    }

    @Override
    public void travel(Vec3d vec3d) {
        if (getControllingPassenger() instanceof LivingEntity) {
            LivingEntity rider = (LivingEntity) getControllingPassenger();

            double x = posX;
            double y = posY;
            double z = posZ;

            // control direction with movement keys
            if (rider.moveStrafing != 0 || rider.moveForward != 0) {
                Vec3d wp = rider.getLookVec();

                if (rider.moveForward < 0) wp = wp.rotateYaw((float) Math.PI);
                else if (rider.moveStrafing > 0) wp = wp.rotateYaw((float) Math.PI * 0.5f);
                else if (rider.moveStrafing < 0) wp = wp.rotateYaw((float) Math.PI * -0.5f);

                x += wp.getX() * 10;
                y += wp.getY() * 10;
                z += wp.getZ() * 10;
            }

            // lift off with a jump
            if (!isFlying()) {
                if (ModUtils.isEntityJumping(rider)) {
                    jump();
                    setFlying(true);
                }
            }

            getMoveHelper().setMoveTo(x, y, z, 1);

        }

    }

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