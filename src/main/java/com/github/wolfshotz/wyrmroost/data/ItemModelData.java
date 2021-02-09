package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.items.CoinDragonItem;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.block.*;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.*;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
class ItemModelData extends ItemModelProvider
{
    private final List<Item> REGISTERED = new ArrayList<>();

    ItemModelData(DataGenerator generator, ExistingFileHelper fileHelper)
    {
        super(generator, Wyrmroost.MOD_ID, fileHelper);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void registerModels()
    {
        // path constants
        final ModelFile itemGenerated = uncheckedModel(mcLoc("item/generated"));
        final ModelFile spawnEggTemplate = uncheckedModel(mcLoc("item/template_spawn_egg"));
        final ModelFile bucket = uncheckedModel("forge:item/bucket");

        item(WRItems.DRAGON_STAFF.get()).parent(uncheckedModel("item/handheld"));
        customInventoryItem(WRBlocks.OSERI_WOOD.getFence(), fromBlockTexture(WRBlocks.OSERI_WOOD.getPlanks()), "fence");
        customInventoryItem(WRBlocks.OSERI_WOOD.getButton(), fromBlockTexture(WRBlocks.OSERI_WOOD.getPlanks()), "button");

        getBuilder("desert_wyrm_alive").parent(itemGenerated).texture("layer0", resource("desert_wyrm_alive"));
        item(WRItems.LDWYRM.get()).override().predicate(Wyrmroost.rl("is_alive"), 1f).model(uncheckedModel(resource("desert_wyrm_alive")));

        getBuilderFor(WRItems.DRAGON_EGG.get())
                .parent(uncheckedModel("builtin/entity"))
                .guiLight(BlockModel.GuiLight.FRONT)
                .transforms()
                .transform(ModelBuilder.Perspective.GUI).rotation(160, 8, 30).translation(21, 6, 0).scale(1.5f).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(180, 10, 4).translation(13, 10, -10).scale(1).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).rotation(180, 10, 4).translation(-2, 11, -12).scale(1).end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).rotation(253, 65, 0).translation(8, 2, 10).scale(0.75f).end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).rotation(253, 65, 0).translation(3, 13, 7).scale(0.75f).end()
                .transform(ModelBuilder.Perspective.GROUND).rotation(180, 0, 0).translation(4, 8, -5).scale(0.55f).end();

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

        // All items that do not require custom attention
        for (Item item : ModUtils.getRegistryEntries(WRItems.REGISTRY))
        {
            if (REGISTERED.contains(item) || item == net.minecraft.item.Items.AIR) continue;

            if (item instanceof SpawnEggItem)
            {
                getBuilderFor(item).parent(spawnEggTemplate);
                continue;
            }

            if (item instanceof BucketItem)
            {
                getBuilderFor(item).parent(bucket);
                continue;
            }

            if (item instanceof BlockItem)
            {
                Block block = ((BlockItem) item).getBlock();
                ResourceLocation registry = item.getRegistryName();
                String path = registry.getPath();

                if (block instanceof TrapDoorBlock) path += "_bottom";
                else if (block instanceof BushBlock || block instanceof AbstractPlantBlock || block instanceof VineBlock)
                {
                    item(item, Wyrmroost.rl("block/" + block.getRegistryName().getPath()));
                    continue;
                }
                else if (block instanceof DoorBlock)
                {
                    item(block);
                    continue;
                }

                getBuilderFor(item).parent(uncheckedModel(registry.getNamespace() + ":block/" + path));
                continue;
            }

            item(item);
        }
    }

    @Override
    public String getName()
    {
        return "Wyrmroost Item Models";
    }

    private static ResourceLocation resource(String path)
    {
        return Wyrmroost.rl("item/" + path);
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
        ResourceLocation registry = item.asItem().getRegistryName();
        String path = registry.getPath();
        ModelBuilder<?> inv = getBuilder(path + "_inventory").parent(uncheckedModel("block/" + parent + "_inventory")).texture("texture", texture);
        return getBuilderFor(item).parent(inv);
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
