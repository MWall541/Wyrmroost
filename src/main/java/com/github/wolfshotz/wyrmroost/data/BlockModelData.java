package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.PetalsBlock;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
class BlockModelData extends BlockStateProvider
{
    private final List<Block> ignored = new ArrayList<>();

    BlockModelData(DataGenerator generator, ExistingFileHelper fileHelper)
    {
        super(generator, Wyrmroost.MOD_ID, fileHelper);
    }

    void manualOverrides()
    {
        vine(WRBlocks.MOSS_VINE.get());
        snowy(WRBlocks.MULCH.get());
        woodGroup(WRBlocks.OSERI_WOOD);
    }

    @Override
    protected void registerStatesAndModels()
    {
        manualOverrides();

        List<String> MISSING_TEXTURES = new ArrayList<>();
        ignored.addAll(registeredBlocks.keySet());
        for (Block block : ModUtils.getRegistryEntries(WRBlocks.REGISTRY))
        {
            if (ignored.contains(block)) continue;
            if (block instanceof FlowingFluidBlock) continue;

            ResourceLocation name = block.getRegistryName();
            if (!models().existingFileHelper.exists(new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath()), ResourcePackType.CLIENT_RESOURCES, ".png", "textures"))
            {
                MISSING_TEXTURES.add(name.getPath().replace("block/", ""));
                continue;
            }

            if (block instanceof BushBlock || block instanceof AbstractPlantBlock)
            {
                cross(block);
                continue;
            }

            if (block instanceof PetalsBlock)
            {
                petals(block);
                continue;
            }

            simpleBlock(block);
        }

        if (!MISSING_TEXTURES.isEmpty())
            Wyrmroost.LOG.error("Blocks are missing Textures! Models will not be registered: {}", MISSING_TEXTURES.toString());
    }

    void woodGroup(WRBlocks.WoodGroup group)
    {
        ResourceLocation planks = blockTexture(group.getPlanks());

        simpleBlock(group.getPlanks());
        logBlock((RotatedPillarBlock) group.getLog());
        logBlock((RotatedPillarBlock) group.getStrippedLog());
        allSidedAxis((RotatedPillarBlock) group.getWood(), blockTexture(group.getLog()));
        allSidedAxis((RotatedPillarBlock) group.getStrippedWood(), blockTexture(group.getStrippedLog()));
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
                .setModels(new ConfiguredModel(models().cross(block.getRegistryName().getPath(), blockTexture(block))));
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
        final String fileName = block.getRegistryName().getPath();
        final ResourceLocation texture = blockTexture(block);

        Wyrmroost.LOG.warn("VINE MODELS REGISTERED FOR: {}, A BLOCKSTATE JSON MUST BE MADE MANUALLY!", fileName);

        ignored.add(block);

        models().singleTexture(fileName + "_" + "u", mcLoc("vine_" + "u"), "vine", texture);
        for (int i = 1; i <= 4; i++)
        {
            for (int j = 1; j <= 2; j++)
            {
                boolean flag = j == 2;
                String name = formatVine(fileName, i, flag);
                String parent = formatVine("vine", i, flag);
                models().singleTexture(name, mcLoc(parent), "vine", texture);
                if (i == 2)
                    models().singleTexture(name + "_opposite", mcLoc(parent + "_opposite"), "vine", texture);
            }
        }
    }

    static String formatVine(String pre, int i, boolean u)
    {
        return pre + "_" + i + (u? "u" : "");
    }
}
