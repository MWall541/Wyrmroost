package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.PetalsBlock;
import com.github.wolfshotz.wyrmroost.blocks.ThinLogBlock;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Set;

@SuppressWarnings("ConstantConditions")
class BlockModelData extends BlockStateProvider
{
    BlockModelData(DataGenerator generator, ExistingFileHelper fileHelper)
    {
        super(generator, Wyrmroost.MOD_ID, fileHelper);
    }

    void manualOverrides()
    {
        vine(WRBlocks.MOSS_VINE.get());
        snowy(WRBlocks.MULCH.get());
        snowy(WRBlocks.FROSTED_GRASS.get());
        woodGroup(WRBlocks.OSERI_WOOD);
        woodGroup(WRBlocks.SAL_WOOD);
        woodGroup(WRBlocks.PRISMARINE_CORIN_WOOD, true);
        woodGroup(WRBlocks.SILVER_CORIN_WOOD, true);
        woodGroup(WRBlocks.TEAL_CORIN_WOOD, true);
        woodGroup(WRBlocks.RED_CORIN_WOOD, true);
        woodGroup(WRBlocks.DYING_CORIN_WOOD, true);
    }

    @Override
    protected void registerStatesAndModels()
    {
        models().getBuilder("vine").texture("particle", "#vine")
                .element()
                    .from(0, 0, 0.8f)
                    .to(16, 16, 0.8f)
                    .shade(false)
                    .face(Direction.NORTH).uvs(16, 0, 0, 16).texture("#vine").tintindex(0).end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#vine").tintindex(0).end()
                .end();

        manualOverrides();

        Set<Block> registered = ModUtils.getRegistryEntries(WRBlocks.REGISTRY);
        registered.removeAll(registeredBlocks.keySet());
        for (Block block : registered)
        {
            if (block instanceof TallFlowerBlock) tallFlower(block);
            else if (block instanceof BushBlock || block instanceof AbstractPlantBlock) cross(block);
            else if (block instanceof PetalsBlock) petals(block);
            else if (!(block instanceof FlowingFluidBlock)) simpleBlock(block);
        }
    }

    void woodGroup(WRBlocks.WoodGroup group, boolean thinLogs)
    {
        ResourceLocation planks = blockTexture(group.getPlanks());

        if (thinLogs)
        {
            corin((ThinLogBlock) group.getLog(), false);
            corin((ThinLogBlock) group.getStrippedLog(), false);
            corin((ThinLogBlock) group.getWood(), true);
            corin((ThinLogBlock) group.getStrippedWood(), true);
        }
        else
        {
            logBlock((RotatedPillarBlock) group.getLog());
            logBlock((RotatedPillarBlock) group.getStrippedLog());
            allSidedAxis((RotatedPillarBlock) group.getWood(), blockTexture(group.getLog()));
            allSidedAxis((RotatedPillarBlock) group.getStrippedWood(), blockTexture(group.getStrippedLog()));
        }

        simpleBlock(group.getPlanks());
        slabBlock((SlabBlock) group.getSlab(), planks, planks);
        pressurePlate(group.getPressurePlate(), planks);
        fenceBlock((FenceBlock) group.getFence(), planks);
        fenceGateBlock((FenceGateBlock) group.getFenceGate(), planks);
        TrapDoorBlock trapDoor = (TrapDoorBlock) group.getTrapDoor();
        trapdoorBlock(trapDoor, blockTexture(trapDoor), true);
        stairsBlock((StairsBlock) group.getStairs(), planks);
        button((AbstractButtonBlock) group.getButton(), planks);
        String door = group.getDoor().getRegistryName().getPath();
        doorBlock((DoorBlock) group.getDoor(), modLoc("block/" + door + "_bottom"), modLoc("block/" + door + "_top"));
        sign((StandingSignBlock) group.getSign(), planks);
        sign((WallSignBlock) group.getWallSign(), planks);
        ladder((LadderBlock) group.getLadder());
        bookshelf(group.getBookshelf(), planks);
    }

    void woodGroup(WRBlocks.WoodGroup group)
    {
        woodGroup(group, false);
    }

    void corin(ThinLogBlock block, boolean allSided)
    {
        String name = block.getRegistryName().getPath();
        BlockModelBuilder model = models().withExistingParent(name, modLoc("corin"))
                .texture("side", name = "block/" + name)
                .texture("top", name + (allSided? "" : "_top"));
        axisBlock(block, model, model);
    }

    void bookshelf(Block block, ResourceLocation topPlanks)
    {
        String name = block.getRegistryName().getPath();
        simpleBlock(block, models().cubeColumn(name, modLoc("block/" + name), topPlanks));
    }

    void tallFlower(Block block)
    {
        String path = "block/" + block.getRegistryName().getPath();
        getVariantBuilder(block).forAllStates(state ->
        {
            String half = path + (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER? "_bottom" : "");
            return ConfiguredModel.builder().modelFile(models().cross(half, modLoc(half))).build();
        });
    }

    void ladder(LadderBlock ladder)
    {
        String name = ladder.getRegistryName().getPath();
        ResourceLocation texture = modLoc("block/" + name);
        horizontalBlock(ladder, models().withExistingParent(name, mcLoc("ladder"))
                .texture("particle", texture)
                .texture("texture", texture));
    }

    void sign(AbstractSignBlock block, ResourceLocation texture)
    {
        getVariantBuilder(block)
                .partialState()
                .setModels(ConfiguredModel.builder()
                        .modelFile(models()
                                .getBuilder(block.getRegistryName().getPath())
                                .texture("particle", texture))
                        .build());
    }

    void button(AbstractButtonBlock block, ResourceLocation texture)
    {
        getVariantBuilder(block).forAllStates(state ->
        {
            boolean powered = state.getValue(AbstractButtonBlock.POWERED);
            AttachFace face = state.getValue(HorizontalFaceBlock.FACE);
            Direction direction = state.getValue(HorizontalBlock.FACING);

            int x = 0;
            int y = (int) direction.getOpposite().toYRot();

            switch (face)
            {
                case WALL:
                    x = 90; break;
                case CEILING:
                    x = 180; break;
                case FLOOR:
                default:
                    break;
            }

            String path = block.getRegistryName().getPath() + (powered? "_pressed" : "");
            ResourceLocation parent = mcLoc("button" + (powered? "_pressed" : ""));
            ModelBuilder<?> model = models().withExistingParent(path, parent).texture("texture", texture);
            return ConfiguredModel.builder().modelFile(model).rotationX(x).rotationY(y).uvLock(face == AttachFace.WALL).build();
        });
    }

    void allSidedAxis(RotatedPillarBlock pillar, ResourceLocation texture)
    {
        ModelBuilder<?> model = models().cubeColumn(pillar.getRegistryName().getPath(), texture, texture);
        axisBlock(pillar, model, model);
    }

    void pressurePlate(Block block, ResourceLocation texture)
    {
        String path = block.getRegistryName().getPath();

        getVariantBuilder(block).forAllStates(state ->
        {
            boolean powered = state.getValue(PressurePlateBlock.POWERED);
            String actualPath = path + (powered? "_down" : "");
            ResourceLocation parent = mcLoc("block/pressure_plate_" + (powered? "down" : "up"));
            ModelBuilder<?> model = models().withExistingParent(actualPath, parent).texture("texture", texture);
            return ConfiguredModel.builder().modelFile(model).build();
        });
    }

    void petals(Block block)
    {
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().withExistingParent(block.getRegistryName().getPath(), modLoc("petals")).texture("particle", blockTexture(block)))
                .rotationY(state.getValue(PetalsBlock.AXIS) == Direction.Axis.X? 90 : 0)
                .build());
    }

    void cross(Block block)
    {
        getVariantBuilder(block)
                .partialState()
                .setModels(new ConfiguredModel(models().singleTexture(block.getRegistryName().getPath(), mcLoc("block/tinted_cross"), "cross", blockTexture(block))));
    }

    void snowy(Block block)
    {
        String name = block.getRegistryName().getPath();

        getVariantBuilder(block).forAllStates(state ->
        {
            boolean snowy = state.getValue(SnowyDirtBlock.SNOWY);
            if (snowy)
                return ConfiguredModel.builder().modelFile(models().getExistingFile(mcLoc("grass_block_snow"))).build();
            else
                return ConfiguredModel.builder().modelFile(models().cubeBottomTop(name, modLoc("block/" + name + "_side"), mcLoc("block/dirt"), modLoc("block/" + name + "_top"))).build();
        });
    }

    void vine(Block block)
    {
        String path = block.getRegistryName().getPath();
        BlockModelBuilder model = models().withExistingParent(path, modLoc("vine")).texture("vine", blockTexture(block));
        getMultipartBuilder(block)
                .part().modelFile(model).rotationX(270).uvLock(true).addModel().condition(VineBlock.UP, true).end()
                .part().modelFile(model).addModel().condition(VineBlock.NORTH, true).end()
                .part().modelFile(model).rotationY(270).uvLock(true).addModel().condition(VineBlock.WEST, true).end()
                .part().modelFile(model).rotationY(180).uvLock(true).addModel().condition(VineBlock.SOUTH, true).end()
                .part().modelFile(model).rotationY(90).addModel().condition(VineBlock.EAST, true).end();
    }
}
