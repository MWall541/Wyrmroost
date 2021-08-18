package com.github.wolfshotz.wyrmroost.containers;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.screen.DragonControlScreen;
import com.github.wolfshotz.wyrmroost.client.screen.widgets.CollapsibleWidget;
import com.github.wolfshotz.wyrmroost.containers.util.DynamicSlot;
import com.github.wolfshotz.wyrmroost.containers.util.Slot3D;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInventory;
import com.github.wolfshotz.wyrmroost.items.book.action.BookAction;
import com.github.wolfshotz.wyrmroost.registry.WRIO;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.github.wolfshotz.wyrmroost.client.ClientEvents.getClient;

public class BookContainer extends Container
{
    public final TameableDragonEntity dragon;
    public final PlayerInventory playerInv;
    public final List<BookAction> actions = new ArrayList<>();
    public final List<ITextComponent> toolTips = new ArrayList<>();
    public final List<CollapsibleWidget> collapsibles = new ArrayList<>();

    public BookContainer(int id, PlayerInventory playerInv, TameableDragonEntity dragon)
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

    public BookContainer slot(Slot slot)
    {
        addSlot(slot);
        return this;
    }

    public BookContainer addAction(BookAction... actions)
    {
        if (dragon.level.isClientSide) Collections.addAll(this.actions, actions);
        return this;
    }

    public BookContainer addTooltip(ITextComponent text)
    {
        if (dragon.level.isClientSide) toolTips.add(text);
        return this;
    }

    public BookContainer addCollapsible(CollapsibleWidget widget)
    {
        widget.slots.forEach(this::addSlot);
        collapsibles.add(widget);
        return this;
    }

    public static Slot3D accessorySlot(DragonInventory i, int index, int x, int y, int z, @Nonnull Vec2d iconUV)
    {
        return (Slot3D) new Slot3D(i, index, x, y, z)
                .condition(() -> getClient().screen instanceof DragonControlScreen && ((DragonControlScreen) getClient().screen).showAccessories())
                .iconUV(iconUV);
    }

    public static CollapsibleWidget collapsibleWidget(int u0, int v0, int width, int height, byte direction)
    {
        return new CollapsibleWidget(u0, v0, width, height, direction, DragonControlScreen.SPRITES);
    }

    public static BookContainer factory(int id, PlayerInventory playerInv, PacketBuffer buf)
    {
        return new BookContainer(id, playerInv, fromBytes(buf));
    }

    public static void open(ServerPlayerEntity player, TameableDragonEntity dragon)
    {
        NetworkHooks.openGui(player, dragon, b -> toBytes(dragon, b));
    }

    private static void toBytes(TameableDragonEntity entity, PacketBuffer buffer)
    {
        buffer.writeVarInt(entity.getId());

        Collection<EffectInstance> effects = entity.getActiveEffects();
        buffer.writeVarInt(effects.size());

        for (EffectInstance instance : effects)
        {
            buffer.writeByte(Effect.getId(instance.getEffect()) & 255);
            buffer.writeVarInt(Math.min(instance.getDuration(), 32767));
            buffer.writeByte(instance.getAmplifier() & 255);

            byte flags = 0;
            if (instance.isAmbient()) flags |= 1;
            if (instance.isVisible()) flags |= 2;
            if (instance.showIcon()) flags |= 4;

            buffer.writeByte(flags);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static TameableDragonEntity fromBytes(PacketBuffer buf)
    {
        TameableDragonEntity dragon = (TameableDragonEntity) ClientEvents.getLevel().getEntity(buf.readVarInt());
        dragon.getActiveEffectsMap().clear();

        int series = buf.readVarInt();
        for (int i = 0; i < series; i++)
        {
            byte flags;
            EffectInstance instance = new EffectInstance(Effect.byId(buf.readByte() & 0xFF),
                    buf.readVarInt(),
                    buf.readByte(),
                    ((flags = buf.readByte()) & 1) == 1,
                    (flags & 2) == 2,
                    (flags & 4) == 4);
            instance.setNoCounter(instance.getDuration() == 32767);
            dragon.forceAddEffect(instance);
        }

        return dragon;
    }
}
