package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class WoodGroup extends WoodType
{
    private static final List<WoodGroup> REGISTRY = new ArrayList<>();

    public final RegistryObject<Block> planks;
    public final RegistryObject<Block> log;
    public final RegistryObject<Block> strippedLog;
    public final RegistryObject<Block> wood;
    public final RegistryObject<Block> strippedWood;
    public final RegistryObject<Block> slab;
    public final RegistryObject<Block> pressurePlate;
    public final RegistryObject<Block> fence;
    public final RegistryObject<Block> fenceGate;
    public final RegistryObject<Block> trapDoor;
    public final RegistryObject<Block> stairs;
    public final RegistryObject<Block> button;
    public final RegistryObject<Block> door;
    public final RegistryObject<Block> sign;
    public final RegistryObject<Block> wallSign;
    public final RegistryObject<Block> ladder;
    public final RegistryObject<Block> bookshelf;

    private WoodGroup(String name, Builder builder)
    {
        super(Wyrmroost.MOD_ID + ":" + name);

        this.planks = register(name + "_planks", builder.planks, 5, 20, builder.flammable);
        this.strippedLog = register("stripped_" + name + "_log", builder.strippedLog, 5, 5, builder.flammable);
        this.log = register(name + "_log", () -> builder.log.apply(strippedLog), 5, 5, builder.flammable);
        this.strippedWood = register("stripped_" + name + "_wood", builder.strippedWood, 5, 5, builder.flammable);
        this.wood = register(name + "_wood", () -> builder.wood.apply(strippedWood), 5, 5, builder.flammable);
        this.slab = register(name + "_slab", builder.slab, 5, 20, builder.flammable);
        this.pressurePlate = WRBlocks.register(name + "_pressure_plate", builder.pressurePlate);
        this.fence = register(name + "_fence", builder.fence, 5, 20, builder.flammable);
        this.fenceGate = register(name + "_fence_gate", builder.fenceGate, 5, 20, builder.flammable);
        this.trapDoor = WRBlocks.register(name + "_trapdoor", builder.trapDoor, WRBlocks.extend().cutoutRenderer());
        this.stairs = register(name + "_stairs", () -> builder.stairs.apply(getPlanks()), 5, 20, builder.flammable);
        this.button = WRBlocks.register(name + "_button", builder.button);
        this.door = WRBlocks.register(name + "_door", builder.door, WRBlocks.extend().cutoutRenderer());
        this.sign = WRBlocks.register(name + "_sign", () -> builder.sign.apply(this), WRBlocks.extend().item(b -> new SignItem(new Item.Properties().stacksTo(16).tab(WRBlocks.BLOCKS_ITEM_GROUP), b, getWallSign())));
        this.wallSign = WRBlocks.register(name + "_wall_sign", () -> builder.wallSign.apply(this, sign), WRBlocks.extend().noItem());
        this.ladder = WRBlocks.register(name + "_ladder", builder.ladder, WRBlocks.extend().cutoutRenderer());
        this.bookshelf = register(name + "_bookshelf", builder.bookshelf, 30, 20, builder.flammable);

        WoodType.register(this);
        REGISTRY.add(this);
    }

    public Block getPlanks()
    {
        return planks.get();
    }

    public Block getLog()
    {
        return log.get();
    }

    public Block getStrippedLog()
    {
        return strippedLog.get();
    }

    public Block getWood()
    {
        return wood.get();
    }

    public Block getStrippedWood()
    {
        return strippedWood.get();
    }

    public Block getSlab()
    {
        return slab.get();
    }

    public Block getPressurePlate()
    {
        return pressurePlate.get();
    }

    public Block getFence()
    {
        return fence.get();
    }

    public Block getFenceGate()
    {
        return fenceGate.get();
    }

    public Block getTrapDoor()
    {
        return trapDoor.get();
    }

    public Block getStairs()
    {
        return stairs.get();
    }

    public Block getButton()
    {
        return button.get();
    }

    public Block getDoor()
    {
        return door.get();
    }

    public Block getSign()
    {
        return sign.get();
    }

    public Block getWallSign()
    {
        return wallSign.get();
    }

    public Block getLadder()
    {
        return ladder.get();
    }

    public Block getBookshelf()
    {
        return bookshelf.get();
    }

    private static RegistryObject<Block> register(String name, Supplier<Block> sup, int fireSpread, int fireDestruction, boolean flammable)
    {
        WRBlocks.BlockExtension extension = WRBlocks.extend();
        if (flammable) extension.flammability(fireSpread, fireDestruction);
        return WRBlocks.register(name, sup, extension);
    }

    public static List<WoodGroup> registry()
    {
        return REGISTRY;
    }

    public static WoodGroup create(String name, MaterialColor color, MaterialColor logColor)
    {
        return new Builder(color, logColor).build(name);
    }

    public static Builder builder(MaterialColor color, MaterialColor logColor)
    {
        return new Builder(color, logColor);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private Supplier<Block> planks;
        private Function<Supplier<Block>, Block> log;
        private Supplier<Block> strippedLog;
        private Function<Supplier<Block>, Block> wood;
        private Supplier<Block> strippedWood;
        private Supplier<Block> slab;
        private Supplier<Block> pressurePlate;
        private Supplier<Block> fence;
        private Supplier<Block> fenceGate;
        private Supplier<Block> trapDoor;
        private Function<Block, Block> stairs;
        private Supplier<Block> button;
        private Supplier<Block> door;
        private BiFunction<WoodGroup, Supplier<Block>, Block> wallSign;
        private Function<WoodGroup, Block> sign;
        private Supplier<Block> ladder;
        private Supplier<Block> bookshelf;
        private boolean flammable = true;

        public Builder(MaterialColor color, MaterialColor logColor)
        {
            planks = () -> new Block(props(color));
            log = stripped -> new LogBlock(color, logColor, stripped);
            strippedLog = () -> new RotatedPillarBlock(LogBlock.properties(color, logColor));
            wood = stripped -> new LogBlock(logColor, logColor, stripped);
            strippedWood = () -> new RotatedPillarBlock(LogBlock.properties(color, color));
            slab = () -> new SlabBlock(props(color));
            pressurePlate = () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, props(color).noCollission().strength(0.5f));
            fence = () -> new FenceBlock(props(color));
            fenceGate = () -> new FenceGateBlock(props(color));
            trapDoor = () -> new TrapDoorBlock(props(color).strength(3f).noOcclusion().isValidSpawn((s, r, p, e) -> false));
            stairs = planks -> new StairsBlock(planks::defaultBlockState, props(color));
            button = () -> new WoodButtonBlock(AbstractBlock.Properties.of(Material.DECORATION).noCollission().harvestTool(ToolType.AXE).strength(0.5f).sound(SoundType.WOOD));
            door = () -> new DoorBlock(props(color).strength(3f).noOcclusion());
            wallSign = (group, sign) -> new WRSignBlock.Wall(props(color).noCollission().strength(1f).lootFrom(sign), group);
            sign = group -> new WRSignBlock(props(color).noCollission().strength(1f), group);
            ladder = () -> new LadderBlock(WRBlocks.properties(Material.DECORATION, SoundType.LADDER).strength(0.4f).noOcclusion());
            bookshelf = BookshelfBlock::new;
        }

        public Builder() {}

        public Builder nonFlammable()
        {
            this.flammable = false;
            return this;
        }

        public Builder log(Function<Supplier<Block>, Block> sup)
        {
            this.log = sup;
            return this;
        }

        public Builder strippedLog(Supplier<Block> sup)
        {
            this.strippedLog = sup;
            return this;
        }

        public Builder wood(Function<Supplier<Block>, Block> sup)
        {
            this.wood = sup;
            return this;
        }

        public Builder strippedWood(Supplier<Block> sup)
        {
            this.strippedWood = sup;
            return this;
        }

        public WoodGroup build(String name)
        {
            return new WoodGroup(name, this);
        }

        private static AbstractBlock.Properties props(MaterialColor color)
        {
            return AbstractBlock.Properties
                    .of(Material.WOOD, color)
                    .strength(2f, 3f)
                    .harvestTool(ToolType.AXE)
                    .sound(SoundType.WOOD);
        }
    }
}
