package WolfShotz.Wyrmroost.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.items.LazySpawnEggItem;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.TieredItem;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;

import java.util.List;

@SuppressWarnings("ConstantConditions")
class Models
{
    private static ExistingFileHelper theGOODExistingFileHelper;

    static void provide(DataGenerator gen, ExistingFileHelper fileHelper)
    {
        theGOODExistingFileHelper = fileHelper;

        gen.addProvider(new Blocks(gen));
        gen.addProvider(new Items(gen));
    }

    private static class Blocks extends BlockStateProvider
    {
        private final List<String> MISSING_TEXTURES = Lists.newArrayList();

        public Blocks(DataGenerator generator)
        {
            super(generator, Wyrmroost.MOD_ID, theGOODExistingFileHelper);
        }

        @Override
        protected void registerStatesAndModels()
        {
            // All unregistered blocks will be done here. They will be simple blocks with all sides of the same texture
            // If this is unwanted, it is important to define so above
            for (Block block : ModUtils.getRegistryEntries(WRBlocks.REGISTRY))
            {
                if (registeredBlocks.containsKey(block)) continue;
                if (block instanceof FlowingFluidBlock) continue;

                ResourceLocation name = block.getRegistryName();
                if (!theGOODExistingFileHelper.exists(new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath()), ResourcePackType.CLIENT_RESOURCES, ".png", "textures"))
                    MISSING_TEXTURES.add(name.getPath().replace("block/", ""));
                else simpleBlock(block);
            }

            if (!MISSING_TEXTURES.isEmpty())
                Wyrmroost.LOG.error("Blocks are missing Textures! Models will not be registered: {}", MISSING_TEXTURES.toString());
        }
    }

    private static class Items extends ItemModelProvider
    {
        private final List<Item> REGISTERED = Lists.newArrayList();
        private final List<String> MISSING_TEXTURES = Lists.newArrayList();

        public Items(DataGenerator generator)
        {
            super(generator, Wyrmroost.MOD_ID, theGOODExistingFileHelper);
        }

        private static ResourceLocation resource(String path)
        {
            return Wyrmroost.rl("item/" + path);
        }

        public ItemModelBuilder item(Item item)
        {
            ItemModelBuilder builder = itemBare(item);

            // model
            String parent = (item instanceof TieredItem) ? "item/handheld" : "item/generated";
            builder.parent(new ModelFile.UncheckedModelFile(parent));

            // texture
            ResourceLocation texture = resource(item.getRegistryName().getPath());
            if (!theGOODExistingFileHelper.exists(texture, ResourcePackType.CLIENT_RESOURCES, ".png", "textures"))
            {
                MISSING_TEXTURES.add(texture.getPath().replace("item/", ""));
            }
            else builder.texture("layer0", texture);

            return builder;
        }

        public ItemModelBuilder itemBare(Item item)
        {
            REGISTERED.add(item);
            return getBuilder(item.getRegistryName().getPath());
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        protected void registerModels()
        {
            // Dragon Egg
            itemBare(WRItems.DRAGON_EGG.get())
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                    .guiLight(BlockModel.GuiLight.FRONT)
                    .transforms()
                    .transform(ModelBuilder.Perspective.GUI).rotation(160, 8, 30).translation(21, 6, 0).scale(1.5f).end()
                    .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(180, 10, 4).translation(13, 10, -10).scale(1).end()
                    .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).rotation(180, 10, 4).translation(-2, 11, -12).scale(1).end()
                    .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).rotation(253, 65, 0).translation(8, 2, 10).scale(0.75f).end()
                    .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).rotation(253, 65, 0).translation(3, 13, 7).scale(0.75f).end()
                    .transform(ModelBuilder.Perspective.GROUND).rotation(180, 0, 0).translation(4, 8, -5).scale(0.55f).end();

            // Minutus
            getBuilder("desert_wyrm_alive")
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", resource("desert_wyrm_alive"));
            item(WRItems.LDWYRM.get())
                    .override()
                    .predicate(Wyrmroost.rl("is_alive"), 1f)
                    .model(new ModelFile.UncheckedModelFile(resource("desert_wyrm_alive")));

            // Dragon Staff
            item(WRItems.DRAGON_STAFF.get()).parent(new ModelFile.UncheckedModelFile("item/handheld"));

            // SpawnEggs
            LazySpawnEggItem.EGG_TYPES.forEach(i -> itemBare(i).parent(new ModelFile.UncheckedModelFile(mcLoc("item/template_spawn_egg"))));

            // Item Blocks
//            item(WRBlocks.CINIS_ROOT.get().asItem());
            for (Block block : ModUtils.getRegistryEntries(WRBlocks.REGISTRY)) // All Standard ItemBlocks
            {
                if (REGISTERED.contains(block.asItem())) continue;
                if (block instanceof FlowingFluidBlock) // Buckets
                {
                    itemBare(((FlowingFluidBlock) block).getFluid().getFilledBucket()).parent(new ModelFile.UncheckedModelFile("forge:item/bucket"));
                    continue;
                }

                ResourceLocation path = block.getRegistryName();
                itemBare(block.asItem()).parent(new ModelFile.UncheckedModelFile(path.getNamespace() + ":block/" + path.getPath()));
            }

            // All items that do not require custom attention
            ModUtils.streamRegistry(WRItems.REGISTRY).filter(e -> !REGISTERED.contains(e)).forEach(this::item);

            if (!MISSING_TEXTURES.isEmpty())
                Wyrmroost.LOG.error("Items are missing Textures! Models will not be registered: {}", MISSING_TEXTURES.toString());
        }

        @Override
        public String getName() { return "Wyrmroost Item Models"; }
    }
}
