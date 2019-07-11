package WolfShotz.Wyrmroost.util.animtools;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;

public class BaseModel<T extends Entity> extends EntityModel<T>
{
    public void setDefaultPoses() { boxList.forEach(box -> ((BaseRenderer) box).storeBoxPose()); }

    public void restorePoses() { boxList.forEach(box -> ((BaseRenderer) box).restoreBoxPose()); }
}
