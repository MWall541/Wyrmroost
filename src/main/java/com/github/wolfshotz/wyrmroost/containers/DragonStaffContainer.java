package com.github.wolfshotz.wyrmroost.containers;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.staff.action.StaffAction;
import com.github.wolfshotz.wyrmroost.registry.WRIO;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragonStaffContainer extends Container
{
    private final TameableDragonEntity dragon;
    public final PlayerInventory playerInv;
    private final List<StaffAction> actions = new ArrayList<>();

    public DragonStaffContainer(int id, PlayerInventory playerInv, TameableDragonEntity dragon)
    {
        super(WRIO.DRAGON_STAFF.get(), id);
        this.dragon = dragon;
        this.playerInv = playerInv;

        ModUtils.createPlayerContainerSlots(playerInv, 0, 86, this::addSlot);

        dragon.applyStaffInfo(this);
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return dragon.isAlive();
    }

    //for public access
    @Override
    public Slot addSlot(Slot p_75146_1_)
    {
        return super.addSlot(p_75146_1_);
    }

    public DragonStaffContainer addStaffActions(StaffAction... actions)
    {
        Collections.addAll(this.actions, actions);
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    public static DragonStaffContainer factory(int id, PlayerInventory playerInv, PacketBuffer buf)
    {
        return new DragonStaffContainer(id, playerInv, (TameableDragonEntity) ClientEvents.getLevel().getEntity(buf.readInt()));
    }
}
