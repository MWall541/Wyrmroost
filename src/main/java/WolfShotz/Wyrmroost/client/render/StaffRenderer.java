package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.items.staff.DragonStaffItem;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * @deprecated - Needs cleaning up and merged with {@link RenderEvents}
 */
public class StaffRenderer
{
    public static void render(MatrixStack ms, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.DRAGON_STAFF.get());
        if (stack == null) return;
        AbstractDragonEntity dragon = DragonStaffItem.getBoundDragon(mc.world, stack);
        if (dragon == null) return;
        DragonStaffItem.getAction(stack).render(dragon, ms, partialTicks);
        EntityOutlineTesting.render(dragon, ms, partialTicks);
        dragon.getHomePos().ifPresent(pos -> RenderEvents.drawBlockPos(ms, pos, dragon.world, 4, 0xff0000ff));
    }
}
