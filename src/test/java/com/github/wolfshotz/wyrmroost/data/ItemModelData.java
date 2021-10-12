package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.StoneGroup;
import com.github.wolfshotz.wyrmroost.blocks.WoodGroup;
import com.github.wolfshotz.wyrmroost.items.CoinDragonItem;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.block.*;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.TieredItem;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ConstantConditions")
class ItemModelData extends ItemModelProvider
{
    private final List<Item> REGISTERED = new ArrayList<>();

    ItemModelData(DataGenerator generator, ExistingFileHelper fileHelper)
    {
        super(generator, Wyrmroost.MOD_ID, fileHelper);
    }

    void manualOverrides()
    {
        final ModelFile itemGenerated = uncheckedModel(mcLoc("item/generated"));
        final ModelFile ister = uncheckedModel("builtin/entity");

        getBuilder("desert_wyrm_alive").parent(itemGenerated).texture("layer0", resource("desert_wyrm_alive"));
        item(WRItems.LDWYRM.get()).override().predicate(Wyrmroost.id("is_alive"), 1f).model(uncheckedModel(resource("desert_wyrm_alive")));

        getBuilderFor(WRItems.DRAGON_EGG.get())
                .parent(ister)
                .guiLight(BlockModel.GuiLight.FRONT)
                .transforms()
                .transform(ModelBuilder.Perspective.GUI).rotation(160, 8, 30).translation(21, 6, 0).scale(1.5f).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(180, 10, 4).translation(13, 10, -10).scale(1).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).rotation(180, 10, 4).translation(-2, 11, -12).scale(1).end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).rotation(253, 65, 0).translation(8, 2, 10).scale(0.75f).end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).rotation(253, 65, 0).translation(3, 13, 7).scale(0.75f).end()
                .transform(ModelBuilder.Perspective.GROUND).rotation(180, 0, 0).translation(4, 8, -5).scale(0.55f).end();

        getBuilderFor(WRItems.TARRAGON_TOME.get()).parent(ister).guiLight(BlockModel.GuiLight.FRONT);
        getBuilder("book_sprite").parent(itemGenerated).texture("layer0", resource(WRItems.TARRAGON_TOME.getId().getPath()));
        item(WRBlocks.HOARFROST.get(), modLoc("block/hoarfrost_spines"));

        final ItemModelBuilder cdBuilder = item(WRItems.COIN_DRAGON.get());
        for (int i = 1; i < 5; i++)
        {
            String path = "coin_dragon" + i;
            ResourceLocation rl = resource(path);
            getBuilder(path)
                    .parent(itemGenerated)
                    .texture("layer0", rl);
            cdBuilder.override()
                    .predicate(CoinDragonItem.VARIANT_OVERRIDE, i)
                    .model(uncheckedModel(rl));
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void registerModels()
    {
        manualOverrides();

        for (WoodGroup w : WoodGroup.registry())
        {
            ResourceLocation texture = fromBlockTexture(w.getPlanks());
            customInventoryItem(w.getFence(), texture, "fence");
            customInventoryItem(w.getButton(), texture, "button");
        }

        for (StoneGroup s : StoneGroup.registry())
        {
            ResourceLocation texture = fromBlockTexture(s.getStone());
            if (s.wall != null) customInventoryItem(s.getWall(), "wall", texture, "wall");
            if (s.button != null) customInventoryItem(s.getButton(), texture, "button");
        }

        final ModelFile spawnEggTemplate = uncheckedModel(mcLoc("item/template_spawn_egg"));
        final Set<Item> registered = ModUtils.getRegistryEntries(WRItems.REGISTRY);

        REGISTERED.forEach(registered::remove);
        for (Item item : registered)
        {
            if (item instanceof SpawnEggItem) getBuilderFor(item).parent(spawnEggTemplate);
            else if (item instanceof BlockItem)
            {
                Block block = ((BlockItem) item).getBlock();
                ResourceLocation registry = item.getRegistryName();
                String path = registry.getPath();

                if (block instanceof TrapDoorBlock) path += "_bottom";
                if (block instanceof BushBlock || block instanceof AbstractPlantBlock || block instanceof VineBlock || block instanceof LadderBlock || block instanceof AbstractCoralPlantBlock)
                {
                    item(item, Wyrmroost.id("block/" + block.getRegistryName().getPath()));
                    continue;
                }
                if (block instanceof DoorBlock || block instanceof StandingSignBlock)
                {
                    item(block);
                    continue;
                }
                if (block.defaultBlockState().hasProperty(BlockStateProperties.LAYERS))
                {
                    getBuilderFor(block).parent(uncheckedModel(fromBlockTexture(block) + "_height2"));
                    continue;
                }

                getBuilderFor(item).parent(uncheckedModel(registry.getNamespace() + ":block/" + path));
            }
            else item(item);
        }
    }

    @Override
    public String getName()
    {
        return "Wyrmroost Item Models";
    }

    private static ResourceLocation resource(String path)
    {
        return Wyrmroost.id("item/" + path);
    }

    private ItemModelBuilder item(IItemProvider item)
    {
        return item(item, resource(item.asItem().getRegistryName().getPath()));
    }

    private ItemModelBuilder item(IItemProvider item, ResourceLocation path)
    {
        ItemModelBuilder builder = getBuilderFor(item).parent(uncheckedModel(item instanceof TieredItem? "item/handheld" : "item/generated"));

        // texture
        if (existingFileHelper.exists(path, ResourcePackType.CLIENT_RESOURCES, ".png", "textures"))
            builder.texture("layer0", path);
        else
            Wyrmroost.LOG.warn("Missing Texture for Item: {}, model will not be registered.", item.asItem().getRegistryName());

        return builder;
    }

    private ItemModelBuilder customInventoryItem(IItemProvider item, ResourceLocation texture, String parent)
    {
        return customInventoryItem(item, "texture", texture, parent);
    }

    private ItemModelBuilder customInventoryItem(IItemProvider item, String textureKey, ResourceLocation texture, String parent)
    {
        return getBuilderFor(item)
                .parent(uncheckedModel("block/" + parent + "_inventory"))
                .texture(textureKey, texture);
    }

    private ItemModelBuilder getBuilderFor(IItemProvider item)
    {
        REGISTERED.add(item.asItem());
        return getBuilder(item.asItem().getRegistryName().getPath());
    }

    private ResourceLocation fromBlockTexture(Block block)
    {
        ResourceLocation reg = block.getRegistryName();
        return new ResourceLocation(reg.getNamespace() + ":block/" + reg.getPath());
    }

    private static ModelFile.UncheckedModelFile uncheckedModel(ResourceLocation path)
    {
        return new ModelFile.UncheckedModelFile(path);
    }

    private static ModelFile.UncheckedModelFile uncheckedModel(String path)
    {
        return new ModelFile.UncheckedModelFile(path);
    }
}
