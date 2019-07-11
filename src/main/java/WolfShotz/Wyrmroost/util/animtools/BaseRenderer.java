package WolfShotz.Wyrmroost.util.animtools;

import net.minecraft.client.renderer.entity.model.RendererModel;

public class BaseRenderer extends RendererModel
{
    public float defaultPosX, defaultPosY, defaultPosZ;
    public float defaultRotX, defaultRotY, defaultRotZ;
    public float defaultOffX, defaultOffY, defaultOffZ;

    public BaseRenderer(BaseModel model, String boxname) {
        super(model, boxname);
    }

    /** Store the position of a box. */
    public void storeBoxPose() {
        defaultPosX = rotationPointX;
        defaultPosY = rotationPointY;
        defaultPosZ = rotationPointZ;

        defaultRotX = rotateAngleX;
        defaultRotY = rotateAngleY;
        defaultRotZ = rotateAngleZ;

        defaultOffX = offsetX;
        defaultOffY = offsetY;
        defaultOffZ = offsetZ;
    }

    /** Restore box position */
    public void restoreBoxPose() {
        rotationPointX = defaultPosX;
        rotationPointY = defaultPosY;
        rotationPointZ = defaultPosZ;

        rotateAngleX = defaultRotX;
        rotateAngleY = defaultRotY;
        rotateAngleZ = defaultRotZ;

        offsetX = defaultOffX;
        offsetY = defaultOffY;
        offsetZ = defaultOffZ;
    }
}
