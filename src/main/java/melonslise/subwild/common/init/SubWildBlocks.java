package melonslise.subwild.common.init;

import java.util.function.Supplier;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.block.DrippingBlock;
import melonslise.subwild.common.block.CoalShardBlock;
import melonslise.subwild.common.block.EncasedOreBlock;
import melonslise.subwild.common.block.EncasedSpeleothemBlock;
import melonslise.subwild.common.block.FoxfireBlock;
import melonslise.subwild.common.block.IcicleBlock;
import melonslise.subwild.common.block.MeltingPatchBlock;
import melonslise.subwild.common.block.MoltenBlock;
import melonslise.subwild.common.block.PatchBlock;
import melonslise.subwild.common.block.PebbleBlock;
import melonslise.subwild.common.block.PuddleBlock;
import melonslise.subwild.common.block.RootsBlock;
import melonslise.subwild.common.block.SpeleothemBlock;
import melonslise.subwild.common.block.XpBlock;
import melonslise.subwild.common.item.ThrowablePebbleItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/*
 * 	Block#blocksMovement enables collision (note that older versions referenced Material flags here)
 */
public final class SubWildBlocks
{
	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SubWild.ID);

	// TODO Reimplement harvestLevel and harvestTool
	// FIXME Speleothem pushreaction
	public static final RegistryObject<Block>
		DIRT_STAIRS = add("dirt_stairs", () -> new StairBlock(Blocks.DIRT::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5F).sound(SoundType.GRASS))),
		DIRT_SLAB = add("dirt_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5F).sound(SoundType.GRASS))),
		DEEPSLATE_STAIRS = add("deepslate_stairs", () -> new StairBlock(Blocks.DEEPSLATE::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.DEEPSLATE))),
		DEEPSLATE_SLAB = add("deepslate_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE))),

		SHORT_FOXFIRE = add("short_foxfire", () -> new FoxfireBlock(BlockBehaviour.Properties.of().noCollission().strength(0f).sound(SoundType.NETHER_WART))),
		LONG_FOXFIRE = add("long_foxfire", () -> new FoxfireBlock(BlockBehaviour.Properties.of().noCollission().strength(0f).sound(SoundType.NETHER_WART))),

		LIGHT_BROWN_ROOTS = add("light_brown_roots", () -> new RootsBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.NETHER_WART))),
		BROWN_ROOTS = add("brown_roots", () -> new RootsBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.NETHER_WART))),
		WHITE_ROOTS = add("white_roots", () -> new RootsBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.NETHER_WART))),
		LIGHT_ORANGE_ROOTS = add("light_orange_roots", () -> new RootsBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.NETHER_WART))),
		DARK_BROWN_ROOTS = add("dark_brown_roots", () -> new RootsBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.NETHER_WART))),
		ORANGE_ROOTS = add("orange_roots", () -> new RootsBlock(BlockBehaviour.Properties.of().noCollission().strength(0.1f).sound(SoundType.NETHER_WART))),

		DIRT_PATCH = add("dirt_patch", () -> new PatchBlock(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.GRAVEL))),
		MOSSY_DIRT_PATCH = add("mossy_dirt_patch", () -> new PatchBlock(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.WET_GRASS))),
		PODZOL_PATCH = add("podzol_patch", () -> new PatchBlock(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.GRAVEL))),
		GRAVEL_PATCH = add("gravel_patch", () -> new PatchBlock(BlockBehaviour.Properties.of().strength(0.15f).sound(SoundType.GRAVEL))),
		SAND_PATCH = add("sand_patch", () -> new PatchBlock(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.SAND))),
		RED_SAND_PATCH = add("red_sand_patch", () -> new PatchBlock(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.SAND))),
		ICE_PATCH = add("ice_patch", () -> new MeltingPatchBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.1f).noOcclusion().sound(SoundType.GLASS))),
		WATER_PUDDLE = add("water_puddle", () -> new PuddleBlock(BlockBehaviour.Properties.of().replaceable().noCollission().strength(0f).sound(SubWildSoundTypes.WATER))),
		PEBBLE = add("pebble", () -> new PebbleBlock(BlockBehaviour.Properties.of().noCollission().strength(0f).sound(SoundType.STONE))),
		COAL_SHARD = add("coal_shard", () -> new CoalShardBlock(BlockBehaviour.Properties.of().noCollission().strength(0f).sound(SoundType.STONE))),
		SANDSTONE_PEBBLE = add("sandstone_pebble", () -> new PebbleBlock(BlockBehaviour.Properties.of().noCollission().strength(0f).sound(SoundType.SAND))),
		RED_SANDSTONE_PEBBLE = add("red_sandstone_pebble", () -> new PebbleBlock(BlockBehaviour.Properties.of().noCollission().strength(0f).sound(SoundType.SAND))),

		STONE_SPELEOTHEM = add("stone_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.STONE))),
		DEEPSLATE_SPELEOTHEM = add("deepslate_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE))),
		GRANITE_SPELEOTHEM = add("granite_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.GRANITE))),
		DIORITE_SPELEOTHEM = add("diorite_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.DIORITE))),
		ANDESITE_SPELEOTHEM = add("andesite_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.ANDESITE))),
		SANDSTONE_SPELEOTHEM = add("sandstone_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE))),
		RED_SANDSTONE_SPELEOTHEM = add("red_sandstone_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE))),
		OBSIDIAN_SPELEOTHEM = add("obsidian_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN))),
		BLACKSTONE_SPELEOTHEM = add("blackstone_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE))),
		BASALT_SPELEOTHEM = add("basalt_speleothem", () -> new SpeleothemBlock(BlockBehaviour.Properties.copy(Blocks.BASALT))),

		FROZEN_STONE_SPELEOTHEM = add("frozen_stone_speleothem", () -> new EncasedSpeleothemBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.5f).sound(SoundType.GLASS).noOcclusion(), STONE_SPELEOTHEM)),
		FROZEN_GRANITE_SPELEOTHEM = add("frozen_granite_speleothem", () -> new EncasedSpeleothemBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.5f).sound(SoundType.GLASS).noOcclusion(), GRANITE_SPELEOTHEM)),
		FROZEN_DIORITE_SPELEOTHEM = add("frozen_diorite_speleothem", () -> new EncasedSpeleothemBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.5f).sound(SoundType.GLASS).noOcclusion(), DIORITE_SPELEOTHEM)),
		FROZEN_ANDESITE_SPELEOTHEM = add("frozen_andesite_speleothem", () -> new EncasedSpeleothemBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.5f).sound(SoundType.GLASS).noOcclusion(), ANDESITE_SPELEOTHEM)),
		FROZEN_SANDSTONE_SPELEOTHEM = add("frozen_sandstone_speleothem", () -> new EncasedSpeleothemBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.5f).sound(SoundType.GLASS).noOcclusion(), SANDSTONE_SPELEOTHEM)),
		FROZEN_RED_SANDSTONE_SPELEOTHEM = add("frozen_red_sandstone_speleothem", () -> new EncasedSpeleothemBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.5f).sound(SoundType.GLASS).noOcclusion(), RED_SANDSTONE_SPELEOTHEM)),
		FROZEN_OBSIDIAN_SPELEOTHEM = add("frozen_obsidian_speleothem", () -> new EncasedSpeleothemBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.5f).sound(SoundType.GLASS).noOcclusion(), OBSIDIAN_SPELEOTHEM)),
		FROZEN_BLACKSTONE_SPELEOTHEM = add("frozen_blackstone_speleothem", () -> new EncasedSpeleothemBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.5f).sound(SoundType.GLASS).noOcclusion(), BLACKSTONE_SPELEOTHEM)),
		FROZEN_BASALT_SPELEOTHEM = add("frozen_basalt_speleothem", () -> new EncasedSpeleothemBlock(BlockBehaviour.Properties.of().friction(0.98f).strength(0.5f).sound(SoundType.GLASS).noOcclusion(), BASALT_SPELEOTHEM)),

		ICICLE = add("icicle", () -> new IcicleBlock(BlockBehaviour.Properties.copy(Blocks.ICE))),

		MOLTEN_STONE = add("molten_stone", () -> new MoltenBlock(moltenProps(Blocks.STONE))),
		MOLTEN_GRANITE = add("molten_granite", () -> new MoltenBlock(moltenProps(Blocks.GRANITE))),
		MOLTEN_DIORITE = add("molten_diorite", () -> new MoltenBlock(moltenProps(Blocks.DIORITE))),
		MOLTEN_ANDESITE = add("molten_andesite", () -> new MoltenBlock(moltenProps(Blocks.ANDESITE))),
		MOLTEN_SANDSTONE = add("molten_sandstone", () -> new MoltenBlock(moltenProps(Blocks.SANDSTONE))),
		MOLTEN_SMOOTH_SANDSTONE = add("molten_smooth_sandstone", () -> new MoltenBlock(moltenProps(Blocks.SMOOTH_SANDSTONE))),
		MOLTEN_RED_SANDSTONE = add("molten_red_sandstone", () -> new MoltenBlock(moltenProps(Blocks.RED_SANDSTONE))),
		MOLTEN_SMOOTH_RED_SANDSTONE = add("molten_smooth_red_sandstone", () -> new MoltenBlock(moltenProps(Blocks.SMOOTH_RED_SANDSTONE))),
		MOLTEN_OBSIDIAN = add("molten_obsidian", () -> new MoltenBlock(moltenProps(Blocks.OBSIDIAN))),
		MOLTEN_BLACKSTONE = add("molten_blackstone", () -> new MoltenBlock(moltenProps(Blocks.BLACKSTONE))),
		MOLTEN_BASALT = add("molten_basalt", () -> new MoltenBlock(moltenProps(Blocks.BASALT))),

		// TODO add to mossy tag?
		// TODO when 1.17 comes out: add extra recipe that uses a moss block instead of vines in line with Vanilla 1.17
		MOSSY_DIRT = add("mossy_dirt", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5F).sound(SoundType.GRASS))),
		MOSSY_SAND = add("mossy_sand", () -> new SandBlock(0xDBD3A0, BlockBehaviour.Properties.copy(Blocks.SAND).strength(0.5F).sound(SoundType.SAND))),
		MOSSY_RED_SAND = add("mossy_red_sand", () -> new SandBlock(0xA95821, BlockBehaviour.Properties.copy(Blocks.RED_SAND).strength(0.5F).sound(SoundType.SAND))),
		MOSSY_GRAVEL = add("mossy_gravel", () -> new GravelBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL).strength(0.6F).sound(SoundType.GRASS))),
		MOSSY_STONE = add("mossy_stone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F))), // TODO: make this drop mossy cobble when mined normally, mossy stone when mined with silk touch
		MOSSY_GRANITE = add("mossy_granite", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRANITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
		MOSSY_DIORITE = add("mossy_diorite", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIORITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
		MOSSY_ANDESITE = add("mossy_andesite", () -> new Block(BlockBehaviour.Properties.copy(Blocks.ANDESITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
		MOSSY_SANDSTONE = add("mossy_sandstone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).requiresCorrectToolForDrops().strength(0.8F))),
		MOSSY_SMOOTH_SANDSTONE = add("mossy_smooth_sandstone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SMOOTH_SANDSTONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F))),
		MOSSY_RED_SANDSTONE = add("mossy_red_sandstone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F))),
		MOSSY_SMOOTH_RED_SANDSTONE = add("mossy_smooth_red_sandstone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SMOOTH_RED_SANDSTONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F))),
		MOSSY_OBSIDIAN = add("mossy_obsidian", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).requiresCorrectToolForDrops().strength(50.0F, 1200.0F))),
		MOSSY_BLACKSTONE = add("mossy_blackstone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F))),
		MOSSY_BASALT = add("mossy_basalt", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BASALT).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT))),

		// FIXME add the rest of the variants?
		WET_STONE = add("wet_stone", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.STONE), ParticleTypes.DRIPPING_WATER)),
		WET_GRANITE = add("wet_granite", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.GRANITE), ParticleTypes.DRIPPING_WATER)),
		WET_DIORITE = add("wet_diorite", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.DIORITE), ParticleTypes.DRIPPING_WATER)),
		WET_ANDESITE = add("wet_andesite", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.ANDESITE), ParticleTypes.DRIPPING_WATER)),
		WET_OBSIDIAN = add("wet_obsidian", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN), ParticleTypes.DRIPPING_WATER)),

		HOT_STONE = add("hot_stone", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.STONE), ParticleTypes.DRIPPING_LAVA)),
		HOT_GRANITE = add("hot_granite", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.GRANITE), ParticleTypes.DRIPPING_LAVA)),
		HOT_DIORITE = add("hot_diorite", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.DIORITE), ParticleTypes.DRIPPING_LAVA)),
		HOT_ANDESITE = add("hot_andesite", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.ANDESITE), ParticleTypes.DRIPPING_LAVA)),
		HOT_SANDSTONE = add("hot_sandstone", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE), ParticleTypes.DRIPPING_LAVA)),
		HOT_SMOOTH_SANDSTONE = add("hot_smooth_sandstone", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.SMOOTH_SANDSTONE), ParticleTypes.DRIPPING_LAVA)),
		HOT_RED_SANDSTONE = add("hot_red_sandstone", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE), ParticleTypes.DRIPPING_LAVA)),
		HOT_SMOOTH_RED_SANDSTONE = add("hot_smooth_red_sandstone", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.SMOOTH_RED_SANDSTONE), ParticleTypes.DRIPPING_LAVA)),
		HOT_OBSIDIAN = add("hot_obsidian", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN), ParticleTypes.DRIPPING_LAVA)),
		HOT_BLACKSTONE = add("hot_blackstone", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE), ParticleTypes.DRIPPING_LAVA)),
		HOT_BASALT = add("hot_basalt", () -> new DrippingBlock(BlockBehaviour.Properties.copy(Blocks.BASALT), ParticleTypes.DRIPPING_LAVA)),

		DIRT_COAL_ORE = add("dirt_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5f), 0, 2)),
		DIRT_IRON_ORE = add("dirt_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5f))),
		DIRT_GOLD_ORE = add("dirt_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5f))),
		DIRT_LAPIS_ORE = add("dirt_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5f), 2, 5)),
		DIRT_REDSTONE_ORE = add("dirt_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5f))),
		DIRT_DIAMOND_ORE = add("dirt_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5f), 3, 7)),
		DIRT_EMERALD_ORE = add("dirt_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5f), 3, 7)),

		TUFF_COAL_ORE = add("tuff_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TUFF).strength(1.5f), 0, 2)),
		TUFF_IRON_ORE = add("tuff_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.TUFF).strength(1.5f))),
		TUFF_GOLD_ORE = add("tuff_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.TUFF).strength(1.5f))),
		TUFF_LAPIS_ORE = add("tuff_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TUFF).strength(1.5f), 2, 5)),
		TUFF_REDSTONE_ORE = add("tuff_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.TUFF).strength(1.5f))),
		TUFF_DIAMOND_ORE = add("tuff_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TUFF).strength(1.5f), 3, 7)),
		TUFF_EMERALD_ORE = add("tuff_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TUFF).strength(1.5f), 3, 7)),

		SANDSTONE_COAL_ORE = add("sandstone_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f), 0, 2)),
		SANDSTONE_IRON_ORE = add("sandstone_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f))),
		SANDSTONE_GOLD_ORE = add("sandstone_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f))),
		SANDSTONE_LAPIS_ORE = add("sandstone_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f), 2, 5)),
		SANDSTONE_REDSTONE_ORE = add("sandstone_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f))),
		SANDSTONE_DIAMOND_ORE = add("sandstone_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f), 3, 7)),
		SANDSTONE_EMERALD_ORE = add("sandstone_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f), 3, 7)),

		SMOOTH_SANDSTONE_COAL_ORE = add("smooth_sandstone_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f), 0, 2)),
		SMOOTH_SANDSTONE_IRON_ORE = add("smooth_sandstone_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f))),
		SMOOTH_SANDSTONE_GOLD_ORE = add("smooth_sandstone_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f))),
		SMOOTH_SANDSTONE_LAPIS_ORE = add("smooth_sandstone_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f), 2, 5)),
		SMOOTH_SANDSTONE_REDSTONE_ORE = add("smooth_sandstone_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f))),
		SMOOTH_SANDSTONE_DIAMOND_ORE = add("smooth_sandstone_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f), 3, 7)),
		SMOOTH_SANDSTONE_EMERALD_ORE = add("smooth_sandstone_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.6f), 3, 7)),

		RED_SANDSTONE_COAL_ORE = add("red_sandstone_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).strength(1.6f), 0, 2)),
		RED_SANDSTONE_IRON_ORE = add("red_sandstone_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).strength(1.6f))),
		RED_SANDSTONE_GOLD_ORE = add("red_sandstone_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).strength(1.6f))),
		RED_SANDSTONE_LAPIS_ORE = add("red_sandstone_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).strength(1.6f), 2, 5)),
		RED_SANDSTONE_REDSTONE_ORE = add("red_sandstone_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).strength(1.6f))),
		RED_SANDSTONE_DIAMOND_ORE = add("red_sandstone_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).strength(1.6f), 3, 7)),
		RED_SANDSTONE_EMERALD_ORE = add("red_sandstone_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).strength(1.6f), 3, 7)),

		SMOOTH_RED_SANDSTONE_COAL_ORE = add("smooth_red_sandstone_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SMOOTH_RED_SANDSTONE).strength(1.6f), 0, 2)),
		SMOOTH_RED_SANDSTONE_IRON_ORE = add("smooth_red_sandstone_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.SMOOTH_RED_SANDSTONE).strength(1.6f))),
		SMOOTH_RED_SANDSTONE_GOLD_ORE = add("smooth_red_sandstone_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.SMOOTH_RED_SANDSTONE).strength(1.6f))),
		SMOOTH_RED_SANDSTONE_LAPIS_ORE = add("smooth_red_sandstone_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SMOOTH_RED_SANDSTONE).strength(1.6f), 2, 5)),
		SMOOTH_RED_SANDSTONE_REDSTONE_ORE = add("smooth_red_sandstone_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.SMOOTH_RED_SANDSTONE).strength(1.6f))),
		SMOOTH_RED_SANDSTONE_DIAMOND_ORE = add("smooth_red_sandstone_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SMOOTH_RED_SANDSTONE).strength(1.6f), 3, 7)),
		SMOOTH_RED_SANDSTONE_EMERALD_ORE = add("smooth_red_sandstone_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SMOOTH_RED_SANDSTONE).strength(1.6f), 3, 7)),

		TERRACOTTA_COAL_ORE = add("terracotta_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f), 0, 2)),
		TERRACOTTA_IRON_ORE = add("terracotta_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f))),
		TERRACOTTA_GOLD_ORE = add("terracotta_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f))),
		TERRACOTTA_LAPIS_ORE = add("terracotta_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f), 2, 5)),
		TERRACOTTA_REDSTONE_ORE = add("terracotta_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f))),
		TERRACOTTA_DIAMOND_ORE = add("terracotta_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f), 3, 7)),
		TERRACOTTA_EMERALD_ORE = add("terracotta_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f), 3, 7)),

		WHITE_TERRACOTTA_COAL_ORE = add("white_terracotta_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.WHITE_TERRACOTTA).strength(2.5f), 0, 2)),
		WHITE_TERRACOTTA_IRON_ORE = add("white_terracotta_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.WHITE_TERRACOTTA).strength(2.5f))),
		WHITE_TERRACOTTA_GOLD_ORE = add("white_terracotta_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.WHITE_TERRACOTTA).strength(2.5f))),
		WHITE_TERRACOTTA_LAPIS_ORE = add("white_terracotta_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.WHITE_TERRACOTTA).strength(2.5f), 2, 5)),
		WHITE_TERRACOTTA_REDSTONE_ORE = add("white_terracotta_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.WHITE_TERRACOTTA).strength(2.5f))),
		WHITE_TERRACOTTA_DIAMOND_ORE = add("white_terracotta_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.WHITE_TERRACOTTA).strength(2.5f), 3, 7)),
		WHITE_TERRACOTTA_EMERALD_ORE = add("white_terracotta_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.WHITE_TERRACOTTA).strength(2.5f), 3, 7)),

		ORANGE_TERRACOTTA_COAL_ORE = add("orange_terracotta_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.ORANGE_TERRACOTTA).strength(2.5f), 0, 2)),
		ORANGE_TERRACOTTA_IRON_ORE = add("orange_terracotta_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.ORANGE_TERRACOTTA).strength(2.5f))),
		ORANGE_TERRACOTTA_GOLD_ORE = add("orange_terracotta_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.ORANGE_TERRACOTTA).strength(2.5f))),
		ORANGE_TERRACOTTA_LAPIS_ORE = add("orange_terracotta_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.ORANGE_TERRACOTTA).strength(2.5f), 2, 5)),
		ORANGE_TERRACOTTA_REDSTONE_ORE = add("orange_terracotta_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.ORANGE_TERRACOTTA).strength(2.5f))),
		ORANGE_TERRACOTTA_DIAMOND_ORE = add("orange_terracotta_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.ORANGE_TERRACOTTA).strength(2.5f), 3, 7)),
		ORANGE_TERRACOTTA_EMERALD_ORE = add("orange_terracotta_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.ORANGE_TERRACOTTA).strength(2.5f), 3, 7)),

		YELLOW_TERRACOTTA_COAL_ORE = add("yellow_terracotta_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.YELLOW_TERRACOTTA).strength(2.5f), 0, 2)),
		YELLOW_TERRACOTTA_IRON_ORE = add("yellow_terracotta_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.YELLOW_TERRACOTTA).strength(2.5f))),
		YELLOW_TERRACOTTA_GOLD_ORE = add("yellow_terracotta_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.YELLOW_TERRACOTTA).strength(2.5f))),
		YELLOW_TERRACOTTA_LAPIS_ORE = add("yellow_terracotta_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.YELLOW_TERRACOTTA).strength(2.5f), 2, 5)),
		YELLOW_TERRACOTTA_REDSTONE_ORE = add("yellow_terracotta_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.YELLOW_TERRACOTTA).strength(2.5f))),
		YELLOW_TERRACOTTA_DIAMOND_ORE = add("yellow_terracotta_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.YELLOW_TERRACOTTA).strength(2.5f), 3, 7)),
		YELLOW_TERRACOTTA_EMERALD_ORE = add("yellow_terracotta_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.YELLOW_TERRACOTTA).strength(2.5f), 3, 7)),

		LIGHT_GRAY_TERRACOTTA_COAL_ORE = add("light_gray_terracotta_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f), 0, 2)),
		LIGHT_GRAY_TERRACOTTA_IRON_ORE = add("light_gray_terracotta_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f))),
		LIGHT_GRAY_TERRACOTTA_GOLD_ORE = add("light_gray_terracotta_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f))),
		LIGHT_GRAY_TERRACOTTA_LAPIS_ORE = add("light_gray_terracotta_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f), 2, 5)),
		LIGHT_GRAY_TERRACOTTA_REDSTONE_ORE = add("light_gray_terracotta_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f))),
		LIGHT_GRAY_TERRACOTTA_DIAMOND_ORE = add("light_gray_terracotta_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f), 3, 7)),
		LIGHT_GRAY_TERRACOTTA_EMERALD_ORE = add("light_gray_terracotta_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).strength(2.5f), 3, 7)),

		BROWN_TERRACOTTA_COAL_ORE = add("brown_terracotta_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BROWN_TERRACOTTA).strength(2.5f), 0, 2)),
		BROWN_TERRACOTTA_IRON_ORE = add("brown_terracotta_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.BROWN_TERRACOTTA).strength(2.5f))),
		BROWN_TERRACOTTA_GOLD_ORE = add("brown_terracotta_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.BROWN_TERRACOTTA).strength(2.5f))),
		BROWN_TERRACOTTA_LAPIS_ORE = add("brown_terracotta_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BROWN_TERRACOTTA).strength(2.5f), 2, 5)),
		BROWN_TERRACOTTA_REDSTONE_ORE = add("brown_terracotta_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.BROWN_TERRACOTTA).strength(2.5f))),
		BROWN_TERRACOTTA_DIAMOND_ORE = add("brown_terracotta_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BROWN_TERRACOTTA).strength(2.5f), 3, 7)),
		BROWN_TERRACOTTA_EMERALD_ORE = add("brown_terracotta_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BROWN_TERRACOTTA).strength(2.5f), 3, 7)),

		RED_TERRACOTTA_COAL_ORE = add("red_terracotta_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA).strength(2.5f), 0, 2)),
		RED_TERRACOTTA_IRON_ORE = add("red_terracotta_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA).strength(2.5f))),
		RED_TERRACOTTA_GOLD_ORE = add("red_terracotta_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA).strength(2.5f))),
		RED_TERRACOTTA_LAPIS_ORE = add("red_terracotta_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA).strength(2.5f), 2, 5)),
		RED_TERRACOTTA_REDSTONE_ORE = add("red_terracotta_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA).strength(2.5f))),
		RED_TERRACOTTA_DIAMOND_ORE = add("red_terracotta_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA).strength(2.5f), 3, 7)),
		RED_TERRACOTTA_EMERALD_ORE = add("red_terracotta_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA).strength(2.5f), 3, 7)),

		ICE_COAL_ORE = add("ice_coal_ore", () -> xpOre(BlockBehaviour.Properties.of().strength(1f).friction(0.98F).noOcclusion().sound(SoundType.GLASS).requiresCorrectToolForDrops(), 0, 2)),
		ICE_IRON_ORE = add("ice_iron_ore", () -> encasedOre(BlockBehaviour.Properties.of().strength(1f).friction(0.98F).noOcclusion().sound(SoundType.GLASS))),
		ICE_GOLD_ORE = add("ice_gold_ore", () -> encasedOre(BlockBehaviour.Properties.of().strength(1f).friction(0.98F).noOcclusion().sound(SoundType.GLASS))),
		ICE_LAPIS_ORE = add("ice_lapis_ore", () -> xpOre(BlockBehaviour.Properties.of().strength(1f).friction(0.98F).noOcclusion().sound(SoundType.GLASS).requiresCorrectToolForDrops(), 2, 5)),
		ICE_REDSTONE_ORE = add("ice_redstone_ore", () -> xpOre(BlockBehaviour.Properties.of().strength(1f).friction(0.98F).noOcclusion().sound(SoundType.GLASS).requiresCorrectToolForDrops(), 1, 6)),
		ICE_DIAMOND_ORE = add("ice_diamond_ore", () -> xpOre(BlockBehaviour.Properties.of().strength(1f).friction(0.98F).noOcclusion().sound(SoundType.GLASS).requiresCorrectToolForDrops(), 3, 7)),
		ICE_EMERALD_ORE = add("ice_emerald_ore", () -> xpOre(BlockBehaviour.Properties.of().strength(1f).friction(0.98F).noOcclusion().sound(SoundType.GLASS).requiresCorrectToolForDrops(), 3, 7)),
		SNOW_COAL_ORE = add("snow_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK), 0, 2)),
		SNOW_IRON_ORE = add("snow_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK))),
		SNOW_GOLD_ORE = add("snow_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK))),
		SNOW_LAPIS_ORE = add("snow_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK), 2, 5)),
		SNOW_REDSTONE_ORE = add("snow_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK))),
		SNOW_DIAMOND_ORE = add("snow_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK), 3, 7)),
		SNOW_EMERALD_ORE = add("snow_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK), 3, 7)),

		BLACKSTONE_COAL_ORE = add("blackstone_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).strength(3f), 0, 2)),
		BLACKSTONE_IRON_ORE = add("blackstone_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).strength(3f))),
		BLACKSTONE_GOLD_ORE = add("blackstone_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).strength(3f))),
		BLACKSTONE_LAPIS_ORE = add("blackstone_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).strength(3f), 2, 5)),
		BLACKSTONE_REDSTONE_ORE = add("blackstone_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).strength(3f))),
		BLACKSTONE_DIAMOND_ORE = add("blackstone_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).strength(3f), 3, 7)),
		BLACKSTONE_EMERALD_ORE = add("blackstone_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).strength(3f), 3, 7)),

		BASALT_COAL_ORE = add("basalt_coal_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(2.5f), 0, 2)),
		BASALT_IRON_ORE = add("basalt_iron_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(2.5f))),
		BASALT_GOLD_ORE = add("basalt_gold_ore", () -> ore(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(2.5f))),
		BASALT_LAPIS_ORE = add("basalt_lapis_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(2.5f), 2, 5)),
		BASALT_REDSTONE_ORE = add("basalt_redstone_ore", () -> redstoneOre(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(2.5f))),
		BASALT_DIAMOND_ORE = add("basalt_diamond_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(2.5f), 3, 7)),
		BASALT_EMERALD_ORE = add("basalt_emerald_ore", () -> xpOre(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(2.5f), 3, 7));

	public static void register()
	{
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	private static Block ore(BlockBehaviour.Properties properties)
	{
		return new DropExperienceBlock(properties.requiresCorrectToolForDrops(), UniformInt.of(0, 0));
	}

	private static Block xpOre(BlockBehaviour.Properties properties, int xpMin, int xpMax)
	{
		return new XpBlock(properties.requiresCorrectToolForDrops(), xpMin, xpMax);
	}

	private static Block redstoneOre(BlockBehaviour.Properties properties)
	{
		return new RedStoneOreBlock(properties.requiresCorrectToolForDrops());
	}

	private static BlockBehaviour.Properties moltenProps(Block block)
	{
		return BlockBehaviour.Properties.copy(block)
			.lightLevel(state -> 3)
			.emissiveRendering((state, world, pos) -> true);
	}

	private static Block encasedOre(BlockBehaviour.Properties properties)
	{
		return new EncasedOreBlock(properties.requiresCorrectToolForDrops());
	}

	public static <T extends Block> RegistryObject<T> add(String name, Supplier<T> supplier)
	{
		RegistryObject<T> reg = BLOCKS.register(name, supplier);
		SubWildItems.add(name, () -> isPebbleItem(name) ? new ThrowablePebbleItem(reg.get(), new Item.Properties()) : new BlockItem(reg.get(), new Item.Properties()));
		return reg;
	}

	private static boolean isPebbleItem(String name)
	{
		return "pebble".equals(name) || "sandstone_pebble".equals(name) || "red_sandstone_pebble".equals(name);
	}
}
