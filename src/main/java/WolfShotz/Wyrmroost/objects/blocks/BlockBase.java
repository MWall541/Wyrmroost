package WolfShotz.Wyrmroost.objects.blocks;

import WolfShotz.Wyrmroost.objects.items.ItemBase;
import WolfShotz.Wyrmroost.objects.items.ItemList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

/**
 * Blockbase - Helper Class allowing for easier block registration
 */
public class BlockBase extends Block
{
    private boolean isBeaconBase = false;

    /**
     * @param name The Resource Location
     * @param material Material the block is
     * @param toughness How hard and how resistant the block is
     * @param sound the sound the block makes
     */
    public BlockBase(String name, ItemGroup group, Material material, float toughness, SoundType sound) {
        super(Block.Properties.create(material).hardnessAndResistance(toughness).sound(sound));
        setRegistryName(name);

        BlockList.BLOCKS.add(this);
        ItemList.ITEMS.add(new BlockItem(this, new Item.Properties().group(group)).setRegistryName(name));
    }

    /**
     * Constructor used for more advanced block properties
     * @param name The Resource Location
     * @param material Material the block is
     * @param hardness How hard the block is (i.e how fast it takes to break it)
     * @param resistance How resistant the block is to explosives
     * @param light The light level the block produces
     * @param sound The sound the block makes
     * @param isBeaconBase Does this block work with beacons?
     */
    public BlockBase(String name, ItemGroup group, Material material, float hardness, float resistance, int light, SoundType sound, boolean isBeaconBase) {
        super(Block.Properties.create(material).hardnessAndResistance(hardness, resistance).lightValue(light).sound(sound));
        setRegistryName(name);
        this.isBeaconBase = isBeaconBase;

        BlockList.BLOCKS.add(this);
        ItemList.ITEMS.add(new BlockItem(this, new Item.Properties().group(group)).setRegistryName(name));
    }

    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) { return isBeaconBase; }
}
