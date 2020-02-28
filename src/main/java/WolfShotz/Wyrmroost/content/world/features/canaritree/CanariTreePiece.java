package WolfShotz.Wyrmroost.content.world.features.canaritree;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class CanariTreePiece extends TemplateStructurePiece
{
    private final Rotation rotation;

    public CanariTreePiece(TemplateManager templateManager, BlockPos position, Rotation rotation)
    {
        super(WRWorld.CANARI_TREE_PIECE, 0);
        this.rotation = rotation;
        this.templatePosition = position;
        loadTemplate(templateManager);
    }

    public CanariTreePiece(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(WRWorld.CANARI_TREE_PIECE, nbt);
        this.rotation = Rotation.valueOf(nbt.getString("Rot"));
        loadTemplate(templateManager);
    }

    private void loadTemplate(TemplateManager manager)
    {
        Template template = manager.getTemplateDefaulted(Wyrmroost.rl("canari_tree"));
        PlacementSettings settings = new PlacementSettings().setIgnoreEntities(true).setRotation(rotation).addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK);
        setup(template, templatePosition, settings);
    }

    @Override
    protected void readAdditional(CompoundNBT tag)
    {
        super.readAdditional(tag);
        tag.putString("Rot", rotation.name());
    }

    @Override
    protected void handleDataMarker(String s, BlockPos blockPos, IWorld iWorld, Random random, MutableBoundingBox mutableBoundingBox) {}
}
