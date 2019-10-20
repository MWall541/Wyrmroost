package WolfShotz.Wyrmroost.content.io.container.base;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.items.DragonArmorItem;
import WolfShotz.Wyrmroost.event.SetupIO;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ContainerBase<T extends AbstractDragonEntity> extends Container
{
    public T dragon;
    
    public ContainerBase(T dragon, ContainerType<?> type, int windowID) {
        super(type, windowID);
        this.dragon = dragon;
    }
    
    public ContainerBase(T dragon, int windowID) {
        super(SetupIO.baseContainer, windowID);
        this.dragon = dragon;
    }
    
    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) { return dragon.getDistance(playerIn) < 10; }
    
    public void buildSlotArea(IInventory inventory, int index, int initialX, int initialY, int length, int height) {
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < length; ++x) {
                addSlot(new Slot(inventory, index, initialX + x * 18, initialY + y * 18));
                ++index;
            }
        }
    }
    
    public void buildSlotArea(ISlotArea slotArea, int index, int initialX, int initialY, int length, int height) {
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < length; ++x) {
                addSlot(slotArea.slotPos(index, initialX + x * 18, initialY + y * 18));
                ++index;
            }
        }
    }
    
    public void buildPlayerSlots(PlayerInventory playerInv, int initialX, int initialY) {
        buildSlotArea(playerInv, 9, initialX, initialY, 9, 3); // Player inv
        buildSlotArea(playerInv, 0, initialX, initialY + 58, 9, 1); // Hotbar
    }
    
    public SlotItemHandler buildSaddleSlot(IItemHandler handler, int x, int y) {
        return new SlotItemHandler(handler, 0, x, y) {
            @Override public boolean isItemValid(ItemStack stack) { return stack.getItem() == Items.SADDLE; }
    
            @Override public boolean isEnabled() { return !dragon.isChild(); }
    
            @Override public void onSlotChanged() { if (!getStack().isEmpty()) dragon.playSound(SoundEvents.ENTITY_HORSE_SADDLE, 1f, 1f); }
        };
    }
    
    public SlotItemHandler buildArmorSlot(IItemHandler handler, int x, int y) {
        return new SlotItemHandler(handler, 1, x, y) {
            Predicate<Item> isArmor = DragonArmorItem.class::isInstance;
        
            @Override public boolean isItemValid(@Nonnull ItemStack stack) { return isArmor.test(stack.getItem()); }
        
            @Override public void onSlotChanged() { dragon.setArmored(); }
            
            @Override
            public ResourceLocation getBackgroundLocation() { return ModUtils.location("textures/io/slots/armor_slot.png"); }
        };
    }
    
    /**
     * If this dragon does not have a saddle slot, this shouldnt be used. else, override.
     */
    public int getSaddleSlot() { return -1; }
}
