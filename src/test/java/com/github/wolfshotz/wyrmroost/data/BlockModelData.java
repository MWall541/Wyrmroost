package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.PetalsBlock;
import com.github.wolfshotz.wyrmroost.blocks.StoneGroup;
import com.github.wolfshotz.wyrmroost.blocks.ThinLogBlock;
import com.github.wolfshotz.wyrmroost.blocks.WoodGroup;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.github.wolfshotz.wyrmroost.registry.WRBlocks.*;

@SuppressWarnings("ConstantConditions")
class BlockModelData extends BlockStateProvider
{
    private final List<WoodGroup> woodGroups = new ArrayList<>(WoodGroup.registry());
    private final List<StoneGroup> stoneGroups = new ArrayList<>(StoneGroup.registry());

    BlockModelData(DataGenerator generator, ExistingFileHelper fileHelper)
    {
        super(generator, Wyrmroost.MOD_ID, fileHelper);
    }

    void manualOverrides()
    {
        vine(MOSS_VINE.get());
        layered(EMBERS.get(), blockTexture(EMBER_BLOCK.get()));
        layered(ASH.get(), blockTexture(ASH_BLOCK.get()));
        sandstoneType(WHITE_SANDSTONE.getStone(), WHITE_SANDSTONE.getStairs(), WHITE_SANDSTONE.getSlab(), WHITE_SANDSTONE.getChiseled(), CUT_WHITE_SANDSTONE.get());
        stoneGroups.remove(WHITE_SANDSTONE);
        slabBlock((SlabBlock) CUT_WHITE_SANDSTONE_SLAB.get(), blockTexture(CUT_WHITE_SANDSTONE.get()), blockTexture(CUT_WHITE_SANDSTONE.get()), modLoc("block/white_sandstone_top"), modLoc("block/white_sandstone_top"));
        wallBlock((WallBlock) WHITE_SANDSTONE.getWall(), blockTexture(WHITE_SANDSTONE.getStone()));
        simpleBlock(HOARFROST.get(), models().getExistingFile(modLoc("block/hoarfrost")));

        corinWood(PRISMARINE_CORIN_WOOD);
        corinWood(SILVER_CORIN_WOOD);
        corinWood(TEAL_CORIN_WOOD);
        corinWood(RED_CORIN_WOOD);
        corinWood(DYING_CORIN_WOOD);
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

        ModUtils.runAndClear(woodGroups, this::woodGroup);
        ModUtils.runAndClear(stoneGroups, this::stoneGroup);

        Set<Block> registered = ModUtils.getRegistryEntries(REGISTRY);
        registered.removeAll(registeredBlocks.keySet());
        for (Block block : registered)
        {
            if (block instanceof TallFlowerBlock) tallFlower(block);
            else if (block instanceof CropsBlock) crop((CropsBlock) block);
            else if (block instanceof BushBlock || block instanceof AbstractPlantBlock || block instanceof AbstractCoralPlantBlock)
                cross(block);
            else if (block instanceof PetalsBlock) petals(block);
            else if (block instanceof SilverfishBlock)
                simpleBlock(block, models().withExistingParent(block.getRegistryName().getPath(), ((SilverfishBlock) block).getHostBlock().getRegistryName()));
            else if (block instanceof SnowyDirtBlock) snowy(block);
            else if (!(block instanceof FlowingFluidBlock)) simpleBlock(block);
        }
    }

    void crop(CropsBlock block)
    {
        getVariantBuilder(block).forAllStates(state ->
        {
            String path = block.getRegistryName().getPath() + "_stage" + state.getValue(block.getAgeProperty());

            return ConfiguredModel.builder()
                    .modelFile(models().crop(path, modLoc("block/" + path)))
                    .build();
        });
    }

    void corinWood(WoodGroup group)
    {
        woodGroup(group, true);
        woodGroups.remove(group);
    }

    void woodGroup(WoodGroup group)
    {
        woodGroup(group, false);
    }

    void woodGroup(WoodGroup group, boolean thinLogs)
    {
        ResourceLocation planks = blockTexture(group.getPlanks());

        if (thinLogs)
        {
            ResourceLocation side = blockTexture(group.getLog());
            ResourceLocation strippedSide = blockTexture(group.getStrippedLog());
            corin((ThinLogBlock) group.getLog(), side, modLoc("block/" + group.getLog().getRegistryName().getPath() + "_top"));
            corin((ThinLogBlock) group.getStrippedLog(), strippedSide, modLoc("block/" + group.getStrippedLog().getRegistryName().getPath() + "_top"));
            corin((ThinLogBlock) group.getWood(), side, side);
            corin((ThinLogBlock) group.getStrippedWood(), strippedSide, strippedSide);
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

    void stoneGroup(StoneGroup group)
    {
        ResourceLocation texture = blockTexture(group.getStone());

        simpleBlock(group.getStone());
        if (group.stairs != null) stairsBlock((StairsBlock) group.getStairs(), texture);
        if (group.wall != null) wallBlock((WallBlock) group.getWall(), texture);
        if (group.slab != null) slabBlock(((SlabBlock) group.getSlab()), texture, texture);
        if (group.pressurePlate != null) pressurePlate(group.getPressurePlate(), texture);
        if (group.button != null) button(((StoneButtonBlock) group.getButton()), texture);
        if (group.cracked != null) simpleBlock(group.getCracked());
        if (group.chiseled != null) simpleBlock(group.getChiseled());
    }

    void corin(ThinLogBlock block, ResourceLocation sideTexture, ResourceLocation topTexture)
    {
        BlockModelBuilder model = models().withExistingParent(block.getRegistryName().getPath(), modLoc("corin"))
                .texture("side", sideTexture)
                .texture("top", topTexture);
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
        simpleBlock(block, models().getBuilder(block.getRegistryName().getPath()).texture("particle", texture));
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
                    x = 90;
                    break;
                case CEILING:
                    x = 180;
                    break;
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
        simpleBlock(block, models().singleTexture(block.getRegistryName().getPath(), mcLoc("block/tinted_cross"), "cross", blockTexture(block)));
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

    void layered(Block block, ResourceLocation texture)
    {
        getVariantBuilder(block).forAllStates(s ->
        {
            int value = s.getValue(SnowBlock.LAYERS);
            if (value == 8)
                return ConfiguredModel.builder().modelFile(models().cubeAll(block.getRegistryName().getPath(), texture)).build();
            else
            {
                value *= 2;
                String snow = "minecraft:block/snow_height" + value;
                String path = block.getRegistryName().getPath() + "_height" + value;
                return ConfiguredModel.builder()
                        .modelFile(models().withExistingParent(path, snow)
                                .texture("particle", texture)
                                .texture("texture", texture))
                        .build();
            }

        });
    }

    void bottomTopCube(Block block, ResourceLocation top, ResourceLocation side, ResourceLocation bottom)
    {
        String name = block.getRegistryName().getPath();
        simpleBlock(block, models().cubeBottomTop(name, side, bottom, top));
    }

    void bottomTopCube(Block block)
    {
        String name = block.getRegistryName().getPath();
        bottomTopCube(block, modLoc("block/" + name + "_top"), blockTexture(block), modLoc("block/" + name + "_bottom"));
    }

    void sandstoneType(Block sandstone, Block stairs, Block slab, Block... smoothTypes)
    {
        String name = sandstone.getRegistryName().getPath();
        ResourceLocation side = blockTexture(sandstone);
        ResourceLocation top = modLoc("block/" + name + "_top");
        ResourceLocation bottom = modLoc("block/" + name + "_bottom");

       bottomTopCube(sandstone, top, blockTexture(sandstone), bottom);
       stairsBlock((StairsBlock) stairs, side, bottom, top);
       slabBlock((SlabBlock) slab, side, side, bottom, top);
        for (Block b : smoothTypes)
        {
            bottomTopCube(b, top, blockTexture(b), top);
        }
    }
}
