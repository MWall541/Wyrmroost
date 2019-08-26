package WolfShotz.Wyrmroost.content.blocks.eggblock;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.event.SetupBlocks;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class EggTileEntity extends TileEntity implements ITickableTileEntity
{
    EntityType<? extends AbstractDragonEntity> dragonType;
    public int hatchTimer = 12000; // Default to 10 mins
    public boolean activated = false;
    
    public EggTileEntity() { super(SetupBlocks.teegg); }
    
    @Override
    public void tick() {
        if (!world.isRemote) {
            if (!activated) return; // Dont Start hatching until we confirm! (Right click)
    
            if (dragonType == null) { // This Shouldnt happen...
                world.setBlockState(getPos(), Blocks.AIR.getDefaultState());
                ModUtils.L.error("Dragon Egg TE @ {} dragonType was invalid!", getPos());
                return;
            }
    
            if (hatchTimer > 0) { // cant hatch yet!
                --hatchTimer;
                return;
            }
    
            AbstractDragonEntity dragonChild = dragonType.create(world);
    
            dragonChild.setGrowingAge(-(dragonChild.hatchTimer * 2));
            dragonChild.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
            world.addEntity(dragonChild);
            remove();
            world.setBlockState(getPos(), Blocks.AIR.getDefaultState());
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 1, 1, false);
        }
    }
    
    /* Save */
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("dragonType", EntityType.getKey(dragonType).toString());
        compound.putInt("hatchTimer", hatchTimer);
        compound.putBoolean("activated", activated);
        
        return super.write(compound);
    }
    
    /* Load */
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return tag;
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) { read(pkt.getNbtCompound()); }
    
    /* Load */
    @Override
    public void read(CompoundNBT compound) {
        dragonType = (EntityType<AbstractDragonEntity>) EntityType.byKey(compound.getString("dragonType")).orElse(null);
        hatchTimer = compound.getInt("hatchTimer");
        activated = compound.getBoolean("activated");
        
        super.read(compound);
    }
}
