package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.items.CoinDragonItem;
import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Simple Entity really, just bob and down in the same spot, and land to sleep at night. Easy.
 */
public class CoinDragonEntity extends MobEntity
{
    public static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(CoinDragonEntity.class, DataSerializers.VARINT);
    public static String DATA_VARIANT = "Variant";

    public CoinDragonEntity(EntityType<? extends CoinDragonEntity> type, World worldIn) { super(type, worldIn); }

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(VARIANT, rand.nextInt(5));
    }

    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putInt(DATA_VARIANT, getVariant());
    }

    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        setVariant(compound.getInt(DATA_VARIANT));
    }

    public int getVariant() { return dataManager.get(VARIANT); }

    public void setVariant(int variant) { dataManager.set(VARIANT, variant); }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4);
    }

    // move up if too low, move down if too high, else, just bob up and down
    @Override
    public void travel(Vec3d positionIn)
    {
        double moveSpeed = 0.4d; // getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
        double yMot;
        double altitiude = getAltitude();
        if (altitiude < 1) yMot = moveSpeed;
        else if (altitiude > 2) yMot = -moveSpeed;
        else yMot = Math.cos(ticksExisted * 0.2);

        setMotion(getMotion().add(0, yMot, 0));
        move(MoverType.SELF, getMotion());
        setMotion(getMotion().scale(0.91));
    }

    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand)
    {
        if (player.getHeldItem(hand).interactWithEntity(player, this, hand)) return true;

        ItemStack stack = new ItemStack(WRItems.COIN_DRAGON.get());
        stack.getOrCreateTag().put(CoinDragonItem.DATA_ENTITY, serializeNBT());
        if (hasCustomName()) stack.setDisplayName(getCustomName());
        ItemEntity itemEntity = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), stack);
        double x = player.getPosX() - getPosX();
        double y = player.getPosY() - getPosY();
        double z = player.getPosZ() - getPosZ();
        itemEntity.setMotion(x * 0.1, y * 0.1 + Math.sqrt(Math.sqrt(x * x + y * y + z * z)) * 0.08, z * 0.1);
        world.addEntity(itemEntity);
        return true;
    }

    public double getAltitude()
    {
        BlockPos.Mutable pos = new BlockPos.Mutable(getPosition().down());
        while (pos.getY() > 0 && !world.getBlockState(pos).isSolid()) pos.setY(pos.getY() - 1);
        return getPosY() - pos.getY();
    }
}