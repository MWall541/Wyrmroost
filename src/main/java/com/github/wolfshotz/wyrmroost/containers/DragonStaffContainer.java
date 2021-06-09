package com.github.wolfshotz.wyrmroost.containers;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.screen.DragonStaffScreen;
import com.github.wolfshotz.wyrmroost.client.screen.widgets.CollapsibleWidget;
import com.github.wolfshotz.wyrmroost.containers.util.DynamicSlot;
import com.github.wolfshotz.wyrmroost.containers.util.Slot3D;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInventory;
import com.github.wolfshotz.wyrmroost.items.staff.action.StaffAction;
import com.github.wolfshotz.wyrmroost.registry.WRIO;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.wolfshotz.wyrmroost.client.ClientEvents.getClient;

public class DragonStaffContainer extends Container
{
    public final TameableDragonEntity dragon;
    public final PlayerInventory playerInv;
    public final List<StaffAction> actions = new ArrayList<>();
    public final List<ITextComponent> toolTips = new ArrayList<>();
    public final List<Widget> widgets = new ArrayList<>();

    public DragonStaffContainer(int id, PlayerInventory playerInv, TameableDragonEntity dragon)
    {
        super(WRIO.DRAGON_STAFF.get(), id);
        this.dragon = dragon;
        this.playerInv = playerInv;

        CollapsibleWidget playerView = collapsibleWidget(0, 0, 193, 97, CollapsibleWidget.BOTTOM);
        ModUtils.createPlayerContainerSlots(playerInv, 17, 12, DynamicSlot::new, playerView::addSlot);
        addCollapsible(playerView);

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

    public DragonStaffContainer slot(Slot slot)
    {
        addSlot(slot);
        return this;
    }

    public DragonStaffContainer addStaffActions(StaffAction... actions)
    {
        if (dragon.level.isClientSide) Collections.addAll(this.actions, actions);
        return this;
    }

    public DragonStaffContainer addTooltip(ITextComponent text)
    {
        if (dragon.level.isClientSide) toolTips.add(text);
        return this;
    }

    public DragonStaffContainer addWidget(Widget widget)
    {
        if (dragon.level.isClientSide) widgets.add(widget);
        return this;
    }

    public DragonStaffContainer addCollapsible(CollapsibleWidget widget)
    {
        widget.slots.forEach(this::addSlot);
        return addWidget(widget);
    }

    @SuppressWarnings("ConstantConditions")
    public static DragonStaffContainer factory(int id, PlayerInventory playerInv, PacketBuffer buf)
    {
        return new DragonStaffContainer(id, playerInv, (TameableDragonEntity) ClientEvents.getLevel().getEntity(buf.readInt()));
    }

    public static void open(ServerPlayerEntity player, TameableDragonEntity dragon)
    {
        NetworkHooks.openGui(player, dragon, b -> b.writeInt(dragon.getId()));
    }

    public static Slot3D accessorySlot(DragonInventory i, int index, int x, int y, int z, @Nonnull Vec2d iconUV)
    {
        return (Slot3D) new Slot3D(i, index, x, y, z)
                .condition(() -> getClient().screen instanceof DragonStaffScreen && ((DragonStaffScreen) getClient().screen).showAccessories())
                .iconUV(iconUV);
    }

    public static CollapsibleWidget collapsibleWidget(int u0, int v0, int width, int height, byte direction)
    {
        return new CollapsibleWidget(u0, v0, width, height, direction, DragonStaffScreen.SPRITES);
    }
}
