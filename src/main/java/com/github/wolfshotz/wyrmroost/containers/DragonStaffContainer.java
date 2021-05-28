package com.github.wolfshotz.wyrmroost.containers;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.containers.util.StaffUISlot;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.staff.action.StaffAction;
import com.github.wolfshotz.wyrmroost.registry.WRIO;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragonStaffContainer extends Container
{
    public final TameableDragonEntity dragon;
    public final PlayerInventory playerInv;
    public final List<StaffAction> actions = new ArrayList<>();
    public final List<ITextComponent> toolTips = new ArrayList<>();

    public DragonStaffContainer(int id, PlayerInventory playerInv, TameableDragonEntity dragon)
    {
        super(WRIO.DRAGON_STAFF.get(), id);
        this.dragon = dragon;
        this.playerInv = playerInv;

        ModUtils.createPlayerContainerSlots(playerInv, 17, 12, StaffUISlot::new, this::addSlot);

        dragon.applyStaffInfo(this);
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return dragon.isAlive();
    }

    //for public access
    @Override
    public Slot addSlot(Slot slot)
    {
        return super.addSlot(slot);
    }

    public DragonStaffContainer addStaffActions(StaffAction... actions)
    {
        Collections.addAll(this.actions, actions);
        return this;
    }

    public DragonStaffContainer addTooltip(ITextComponent text)
    {
        toolTips.add(text);
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    public static DragonStaffContainer factory(int id, PlayerInventory playerInv, PacketBuffer buf)
    {
        return new DragonStaffContainer(id, playerInv, (TameableDragonEntity) ClientEvents.getLevel().getEntity(buf.readInt()));
    }
}
