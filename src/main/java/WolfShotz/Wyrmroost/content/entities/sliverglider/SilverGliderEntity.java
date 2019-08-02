package WolfShotz.Wyrmroost.content.entities.sliverglider;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
{
    protected boolean isGliding = false;
    protected boolean isDiving = false;

    public SilverGliderEntity(EntityType<? extends SilverGliderEntity> entity, World world) {
        super(entity, world);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(30d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.20989d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4.0d);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(1.2d);
    }

    // ================================
    //           Entity NBT
    // ================================
    /** Set The chances this dragon can be an albino. Set it to 0 to have no chance */
    @Override
    public int getAlbinoChances() { return 0; }

    // ================================

    @Override
    public void updateRidden() {
        super.updateRidden();

        Entity entity = getRidingEntity();

        isGliding = false;
        isDiving = false;

        if (entity != null) {
            if (!entity.isAlive()) {
                stopRiding();
                return;
            }

            setMotion(Vec3d.ZERO);

            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                if (player.isSneaking()) {
                    stopRiding();
                    return;
                }

                if (ModUtils.isEntityJumping(player) && ModUtils.getAltitude(player) > 1 && !player.abilities.isFlying) {
                    boolean shouldGlide = entity.getMotion().y < 0;
                    double yMotion = shouldGlide? 0.6d : 1;
                    double xMotion = (entity.getMotion().x >= 0.5? 0.8 : 1.1);

                    if (player.getLookVec().y < -0.7) {
                        yMotion = 1;
                        isDiving = true;
                    }

                    Vec3d glideMotion = new Vec3d(xMotion, yMotion, entity.getMotion().z);

                    entity.setMotion(entity.getMotion().mul(glideMotion));
                    if (shouldGlide) isGliding = true;
                }

                prevRotationPitch = rotationPitch = player.rotationPitch;

                rotationYawHead = renderYawOffset = prevRotationYaw = rotationYaw = player.rotationYaw;
                setRotation(player.rotationYawHead, rotationPitch);

                Vec3d offset = new Vec3d(player.posX, player.posY + 1.85, player.posZ + 0.8 * -0.4);
                offset.rotateYaw((float) Math.toRadians(-rotationYaw));

                setPosition(player.posX, player.posY + 1.85d, player.posZ);
            }
        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (stack.isEmpty()) {
            startRiding(player, true);
            return true;
        }

        return super.processInteract(player, hand);
    }

    @Override
    public void travel(Vec3d vec3d) {
        if (isBeingRidden() && getControllingPassenger() instanceof LivingEntity) {
            LivingEntity rider = (LivingEntity) getControllingPassenger();
            float f = rider.moveForward, s = rider.moveStrafing;
            boolean moving = (f != 0 || s != 0);
            Vec3d target = new Vec3d(s, vec3d.y, f);

            if (moving) {
                rotationYaw = rider.rotationYaw;
                prevRotationYaw = rotationYaw;
                setRotation(rotationYaw, rotationPitch);
                renderYawOffset = rotationYaw;
                rotationYawHead = renderYawOffset;
            }

            if (isFlying()) {
                Vec3d riderLook = rider.getLookVec();
                double yEuclid = (moving? riderLook.y : Math.sin(ticksExisted / 2) * 0.06);
                double flySpeed = getAttribute(FLYING_SPEED).getValue();
                Vec3d flyTarget = new Vec3d(riderLook.x * flySpeed, yEuclid, riderLook.z * flySpeed);


                if (!moving) { // Slow down!
                    double x = getMotion().x * 0.8d;
                    double y = yEuclid + getMotion().y * 0.8d;
                    double z = getMotion().z * 0.8d;

                    flyTarget = new Vec3d(x, y, z);
                }



                setNoGravity(true);
                setMotion(flyTarget);
                move(MoverType.SELF, getMotion());

                return;
            } else {
                if (ModUtils.isEntityJumping(rider)) { // Start Flying
                    setFlying(true);
                    return;
                }

                float speed = (float) getAttribute(MOVEMENT_SPEED).getValue() * (rider.isSprinting() ? 2 : 1);

                setNoGravity(false);
                setSprinting(rider.isSprinting());
                setAIMoveSpeed(speed);
                super.travel(target);

                return;
            }
        }
        super.travel(vec3d);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) { return super.isInvulnerableTo(source) || getRidingEntity() != null; }

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