package melonslise.subwild.common.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.Lists;

import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildFeatures;
import melonslise.subwild.common.world.gen.feature.cavetype.CaveType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;

public final class SubWildConfig
{
	public static final ForgeConfigSpec SPEC;
	private static final List<String> DEFAULT_DIMENSION_WHITELIST_VALUES = List.of("minecraft:overworld");
	private static final boolean DEFAULT_EXPENSIVE_SCAN = true;
	private static final boolean DEFAULT_ADAPT_CAVE_BIOMES_TO_MODDED_BIOMES = true;
	private static final int DEFAULT_SLOPE_THRESHOLD = 3;
	private static final boolean DEFAULT_GENERATE_BUTTONS = true;
	private static final boolean DEFAULT_THROWABLE_PEBBLES = true;
	private static final boolean DEFAULT_GENERATE_VINES = true;
	private static final boolean DEFAULT_GENERATE_PUDDLES = true;
	private static final boolean DEFAULT_GENERATE_STAIRS = true;
	private static final boolean DEFAULT_GENERATE_SLABS = true;
	private static final boolean DEFAULT_GENERATE_PATCHES = true;
	private static final boolean DEFAULT_GENERATE_SPELEOTHEMS = true;
	private static final boolean DEFAULT_GENERATE_FEATURES_IN_WATER = true;
	private static final boolean DEFAULT_GENERATE_FOXFIRES = true;
	private static final boolean DEFAULT_GENERATE_SUBWILD_ORES = true;
	private static final boolean DEFAULT_GENERATE_DEAD_BUSHES = true;
	private static final boolean DEFAULT_GENERATE_LILYPADS = true;
	private static final boolean DEFAULT_GENERATE_WALL_CORAL = true;
	private static final boolean DEFAULT_GENERATE_DEAD_WALL_CORAL = true;
	private static final boolean DEFAULT_GENERATE_FLOOR_CORAL = true;
	private static final boolean DEFAULT_GENERATE_DEAD_FLOOR_CORAL = true;
	private static final boolean DEFAULT_GENERATE_SAPLINGS = true;
	private static final boolean DEFAULT_GENERATE_IN_STRUCTURES = false;
	private static final boolean DEFAULT_SHEAR_VANILLA_MOSSY_BLOCKS = true;
	private static final boolean DEFAULT_GLOBAL_REPLACE_ALL_CAVE_TYPES = false;
	private static final boolean DEFAULT_GLOBAL_REPLACE_DEEP_TUFF_CAVES = true;
	private static final boolean DEFAULT_CAVE_REPLACE_DEEP_TUFF_CAVES = true;
	private static final double DEFAULT_GLOBAL_SPELEOTHEM_GENERATION_CHANCE = 67.00d;
	private static final double DEFAULT_STONE_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_DEEPSLATE_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_GRANITE_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_DIORITE_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_ANDESITE_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_RED_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_OBSIDIAN_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_BLACKSTONE_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_BASALT_SPELEOTHEM_GENERATION_CHANCE = -1.00d;
	private static final double DEFAULT_SHORT_FOXFIRE_GENERATION_CHANCE = 100.00d;
	private static final double DEFAULT_LONG_FOXFIRE_GENERATION_CHANCE = 100.00d;
	private static final int DEFAULT_SLOPE_CHANCE = 6;
	private static final int DEFAULT_SLOPE_THRESHOLD_CHANCE = 2;
	private static final double DEFAULT_COAL_SHARD_CHANCE = 1.79d;
	private static final double DEFAULT_ROCKY_BUTTONS_CHANCE = 7.14d;
	private static final double DEFAULT_SANDY_ROCKY_BUTTONS_CHANCE = 7.14d;
	private static final double DEFAULT_RED_SANDSTONE_PEBBLE_CHANCE = 7.14d;
	private static final double DEFAULT_MESA_DEAD_BUSHES_CHANCE = 2.94d;
	private static final double DEFAULT_SANDY_DEAD_BUSHES_CHANCE = 3.85d;
	private static final double DEFAULT_SANDY_ROCKY_DEAD_BUSHES_CHANCE = 2.38d;
	private static final double DEFAULT_WATER_PUDDLE_GENERATION_CHANCE = 100.00d;
	private static final double DEFAULT_LUSH_LILYPADS_CHANCE = 8.33d;
	private static final double DEFAULT_LUSH_LEAVES_CHANCE = 65.00d;
	private static final double DEFAULT_MOSSY_LILYPADS_CHANCE = 8.33d;
	private static final double DEFAULT_MOSSY_ROCKY_LILYPADS_CHANCE = 8.33d;
	private static final double DEFAULT_LUSH_SAPLINGS_CHANCE = 10.00d;
	private static final double DEFAULT_LUSH_VOLCANIC_LEAVES_CHANCE = 65.00d;
	private static final double DEFAULT_LUSH_VOLCANIC_SAPLINGS_CHANCE = 10.00d;

	public static final ForgeConfigSpec.BooleanValue
			EXPENSIVE_SCAN, ADAPT_CAVE_BIOMES_TO_MODDED_BIOMES, GENERATE_BUTTONS, THROWABLE_PEBBLES, GENERATE_VINES, GENERATE_PUDDLES, GENERATE_STAIRS, GENERATE_SLABS,
			GENERATE_PATCHES, GENERATE_SPELEOTHEMS, GENERATE_FEATURES_IN_WATER, GENERATE_FOXFIRES, GENERATE_SUBWILD_ORES, GENERATE_DEAD_BUSHES, GENERATE_LILYPADS,
			GENERATE_WALL_CORAL, GENERATE_DEAD_WALL_CORAL, GENERATE_FLOOR_CORAL, GENERATE_DEAD_FLOOR_CORAL,
			GENERATE_SAPLINGS, GENERATE_IN_STRUCTURES, SHEAR_VANILLA_MOSSY_BLOCKS;
	public static final ForgeConfigSpec.IntValue SLOPE_THRESHOLD, SLOPE_CHANCE, SLOPE_THRESHOLD_CHANCE;
	public static final ForgeConfigSpec.DoubleValue
			GLOBAL_SPELEOTHEM_GENERATION_CHANCE, STONE_SPELEOTHEM_GENERATION_CHANCE, DEEPSLATE_SPELEOTHEM_GENERATION_CHANCE,
			GRANITE_SPELEOTHEM_GENERATION_CHANCE, DIORITE_SPELEOTHEM_GENERATION_CHANCE, ANDESITE_SPELEOTHEM_GENERATION_CHANCE,
			SANDSTONE_SPELEOTHEM_GENERATION_CHANCE, RED_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE, OBSIDIAN_SPELEOTHEM_GENERATION_CHANCE,
			BLACKSTONE_SPELEOTHEM_GENERATION_CHANCE, BASALT_SPELEOTHEM_GENERATION_CHANCE,
			SHORT_FOXFIRE_GENERATION_CHANCE, LONG_FOXFIRE_GENERATION_CHANCE,
			COAL_SHARD_CHANCE, ROCKY_BUTTONS_CHANCE, SANDY_ROCKY_BUTTONS_CHANCE,
			RED_SANDSTONE_PEBBLE_CHANCE, MESA_DEAD_BUSHES_CHANCE, SANDY_DEAD_BUSHES_CHANCE, SANDY_ROCKY_DEAD_BUSHES_CHANCE,
			WATER_PUDDLE_GENERATION_CHANCE,
			LUSH_LILYPADS_CHANCE, LUSH_LEAVES_CHANCE, MOSSY_LILYPADS_CHANCE, MOSSY_ROCKY_LILYPADS_CHANCE, LUSH_SAPLINGS_CHANCE,
			LUSH_VOLCANIC_LEAVES_CHANCE, LUSH_VOLCANIC_SAPLINGS_CHANCE;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSION_WHITELIST;
	public static final ForgeConfigSpec.BooleanValue GLOBAL_REPLACE_ALL_CAVE_TYPES;
	public static final ForgeConfigSpec.BooleanValue GLOBAL_REPLACE_DEEP_TUFF_CAVES;
	public static final ForgeConfigSpec.ConfigValue<String> GLOBAL_REPLACEMENT_CAVE_TYPE;
	public static final List<CaveTypeChoice> CAVE_TYPE_CHOICES;
	public static final List<CaveTypeToggle> CAVE_TYPE_OPTIONS;
	public static final Map<ResourceLocation, CaveTypeToggle> CAVE_TYPE_TOGGLES;
	private static final ThreadLocal<Boolean> ACTIVE_DEEP_TUFF_REPLACEMENT = ThreadLocal.withInitial(() -> false);

	static
	{
		final ForgeConfigSpec.Builder cfg = new ForgeConfigSpec.Builder();

		EXPENSIVE_SCAN = cfg.comment("Scans additional underground cave spaces to improve terrain generation compatibility at a lower performance cost (may drastically reduce cave feature generation in large caves).").define("Expensive Scan", DEFAULT_EXPENSIVE_SCAN);
		ADAPT_CAVE_BIOMES_TO_MODDED_BIOMES = cfg.comment("Adapts SubWild cave selection to the biome above the cave so modded surface biomes can use matching cave themes. Disable this to use the older tagged-biome behavior and rocky fallback.").define("Adapt Cave Biomes to Modded Biomes", DEFAULT_ADAPT_CAVE_BIOMES_TO_MODDED_BIOMES);
		SLOPE_THRESHOLD = cfg.comment("The amount of non-solid blocks in front of a cave wall at which stairs and slabs spawn at a reduced rate to avoid high density narrow caves").defineInRange("Slope Threshold", DEFAULT_SLOPE_THRESHOLD, 0, 16);
		DIMENSION_WHITELIST = cfg.comment("The dimensions in which cave biomes will generate in").defineList("Dimension Whitelist", Lists.newArrayList(DEFAULT_DIMENSION_WHITELIST_VALUES), e -> e instanceof String);

		cfg.push("Features");
			GENERATE_BUTTONS = cfg.comment("Enable pebble generation in rocky and sandy cave biomes.").define("Generate Pebbles", DEFAULT_GENERATE_BUTTONS);
			THROWABLE_PEBBLES = cfg.comment("Allow pebble items to be thrown as small projectiles with a short shared cooldown. Sneaking while aiming at a block will throw instead of place.").define("Throwable Pebbles", DEFAULT_THROWABLE_PEBBLES);
			GENERATE_VINES = cfg.comment("Enable to generate vines in fungal, lush and mossy cave biomes").define("Generate Vines", DEFAULT_GENERATE_VINES);
			GENERATE_PUDDLES = cfg.comment("Enable to generate puddles in lush, mossy muddy, dead coral cave biomes").define("Generate Puddles", DEFAULT_GENERATE_PUDDLES);
			GENERATE_STAIRS = cfg.comment("Enable to generate stairs in cave biomes. Slope Generation Chance must also be above 0 for stairs to generate.").define("Generate Stairs", DEFAULT_GENERATE_STAIRS);
			GENERATE_SLABS = cfg.comment("Enable to generate slabs in cave biomes. Slope Generation Chance must also be above 0 for slabs to generate.").define("Generate Slabs", DEFAULT_GENERATE_SLABS);
			GENERATE_PATCHES = cfg.comment("Enable to generate patches (varying heights of slabs and snow) in cave biomes.").define("Generate Patches", DEFAULT_GENERATE_PATCHES);
			GENERATE_SPELEOTHEMS = cfg.comment("Enable to generate speleothems in cave biomes.").define("Generate Speleothems", DEFAULT_GENERATE_SPELEOTHEMS);
			GENERATE_FEATURES_IN_WATER = cfg.comment("Allows waterloggable cave features like speleothems, slabs, and stairs to generate inside water-filled caves and cave lakes.").define("Generate Features in Water", DEFAULT_GENERATE_FEATURES_IN_WATER);
			GENERATE_FOXFIRES = cfg.comment("Enable to generate glowing foxfires in cave biomes.").define("Generate Foxfires", DEFAULT_GENERATE_FOXFIRES);
			GENERATE_SUBWILD_ORES = cfg.comment("Enable all SubWild custom ore variants. When disabled, no SubWild ores will generate.").define("Generate SubWild Ores", DEFAULT_GENERATE_SUBWILD_ORES);
			GENERATE_DEAD_BUSHES = cfg.comment("Enable to generate dead bushes in mesa, sandy and sandy rocky cave biomes.").define("Generate Dead Bushes", DEFAULT_GENERATE_DEAD_BUSHES);
			GENERATE_LILYPADS = cfg.comment("Enable to generate lily pads in lush, mossy and mossy rocky cave biomes.").define("Generate Lily Pads", DEFAULT_GENERATE_LILYPADS);
			GENERATE_WALL_CORAL = cfg.comment("Enable to generate wall coral in coral cave biomes.").define("Generate Wall Coral", DEFAULT_GENERATE_WALL_CORAL);
			GENERATE_DEAD_WALL_CORAL = cfg.comment("Enable to generate dead wall coral in dead coral cave biomes.").define("Generate Dead Wall Coral", DEFAULT_GENERATE_DEAD_WALL_CORAL);
			GENERATE_FLOOR_CORAL = cfg.comment("Enable to generate floor coral in coral cave biomes.").define("Generate Floor Coral", DEFAULT_GENERATE_FLOOR_CORAL);
			GENERATE_DEAD_FLOOR_CORAL = cfg.comment("Enable to generate dead floor coral in dead coral cave biomes.").define("Generate Dead Floor Coral", DEFAULT_GENERATE_DEAD_FLOOR_CORAL);
			GENERATE_SAPLINGS = cfg.comment("Enable to generate saplings in jungle and jungle volcanic cave biomes.").define("Generate Saplings", DEFAULT_GENERATE_SAPLINGS);
			GENERATE_IN_STRUCTURES = cfg.comment("Allows SubWild cave decorations to generate inside large structures and underground complexes (excluding mineshafts).").define("Generate In Structures", DEFAULT_GENERATE_IN_STRUCTURES);
			SHEAR_VANILLA_MOSSY_BLOCKS = cfg.comment("Allows shears to clean moss off vanilla mossy blocks, stairs, slabs, and walls, dropping a vine and restoring the clean variant.").define("Shear Vanilla Mossy Blocks", DEFAULT_SHEAR_VANILLA_MOSSY_BLOCKS);
		cfg.pop();

		cfg.push("Speleothems");
			GLOBAL_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Default generation chance for all speleothem families, as a percentage. Individual entries below use this value until they are given their own override. Default: 67 (recommended to reduce crowding). Max: 100.").defineInRange("Global Generation Chance", DEFAULT_GLOBAL_SPELEOTHEM_GENERATION_CHANCE, 0.00d, 100.00d);
			STONE_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Stone speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen stone speleothems follow this value in icy caves.").defineInRange("Stone Generation Chance", DEFAULT_STONE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
			DEEPSLATE_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Deepslate speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance.").defineInRange("Deepslate Generation Chance", DEFAULT_DEEPSLATE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
			GRANITE_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Granite speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen granite speleothems follow this value in icy caves.").defineInRange("Granite Generation Chance", DEFAULT_GRANITE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
			DIORITE_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Diorite speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen diorite speleothems follow this value in icy caves.").defineInRange("Diorite Generation Chance", DEFAULT_DIORITE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
			ANDESITE_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Andesite speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen andesite speleothems follow this value in icy caves.").defineInRange("Andesite Generation Chance", DEFAULT_ANDESITE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
			SANDSTONE_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Sandstone speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen sandstone speleothems follow this value in icy caves.").defineInRange("Sandstone Generation Chance", DEFAULT_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
			RED_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Red sandstone speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen red sandstone speleothems follow this value in icy caves.").defineInRange("Red Sandstone Generation Chance", DEFAULT_RED_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
			OBSIDIAN_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Obsidian speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen obsidian speleothems follow this value in icy caves.").defineInRange("Obsidian Generation Chance", DEFAULT_OBSIDIAN_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
			BLACKSTONE_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Blackstone speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen blackstone speleothems follow this value in icy caves.").defineInRange("Blackstone Generation Chance", DEFAULT_BLACKSTONE_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
			BASALT_SPELEOTHEM_GENERATION_CHANCE = cfg.comment("Basalt speleothem generation chance, as a percentage. Set to -1 to use the global speleothem generation chance. Frozen basalt speleothems follow this value in icy caves.").defineInRange("Basalt Generation Chance", DEFAULT_BASALT_SPELEOTHEM_GENERATION_CHANCE, -1.00d, 100.00d);
		cfg.pop();

		cfg.push("Foxfires");
			SHORT_FOXFIRE_GENERATION_CHANCE = cfg.comment("Short foxfire mushroom generation chance, as a percentage. Higher values increase how often this variant appears when foxfires generate.").defineInRange("Short Foxfire Generation Chance", DEFAULT_SHORT_FOXFIRE_GENERATION_CHANCE, 0.00d, 100.00d);
			LONG_FOXFIRE_GENERATION_CHANCE = cfg.comment("Long foxfire mushroom generation chance, as a percentage. Higher values increase how often this variant appears when foxfires generate.").defineInRange("Long Foxfire Generation Chance", DEFAULT_LONG_FOXFIRE_GENERATION_CHANCE, 0.00d, 100.00d);
		cfg.pop();

		cfg.push("Cave Types");
			CaveTypeToggleSet caveTypeToggleSet = createCaveTypeToggles(cfg);
			GLOBAL_REPLACE_ALL_CAVE_TYPES = caveTypeToggleSet.globalReplaceAll();
			GLOBAL_REPLACE_DEEP_TUFF_CAVES = caveTypeToggleSet.globalReplaceDeepTuff();
			GLOBAL_REPLACEMENT_CAVE_TYPE = caveTypeToggleSet.globalReplacementTarget();
			CAVE_TYPE_CHOICES = caveTypeToggleSet.choices();
			CAVE_TYPE_OPTIONS = caveTypeToggleSet.options();
			CAVE_TYPE_TOGGLES = caveTypeToggleSet.values();
		cfg.pop();

		cfg.push("Frequencies");
			SLOPE_CHANCE = cfg.comment("The chance of a slab or stairs generating normally in caves. Higher numbers increase the amount of slabs/stairs/slopes. Zero should stop generating them entirely.").defineInRange("Slope Generation Chance", DEFAULT_SLOPE_CHANCE, 0, 8);
			SLOPE_THRESHOLD_CHANCE = cfg.comment("The chance of a slab or stairs generating when within the Slope Threshold. Should be lower than the Slope Generation Chance.").defineInRange("Slope Threshold Generation Chance", DEFAULT_SLOPE_THRESHOLD_CHANCE, 0, 7);

			cfg.push("Deep and Volcanic Caves");
				COAL_SHARD_CHANCE = cfg.comment("Coal shards still respect the existing \"Generate Pebbles\" toggle.").defineInRange("Coal Shard Generation Chance", DEFAULT_COAL_SHARD_CHANCE, 1.00d, 100.00d);
			cfg.pop();

			cfg.push("Puddles");
				WATER_PUDDLE_GENERATION_CHANCE = cfg.comment("Water puddle generation chance in cave biomes that support puddles, as a percentage.").defineInRange("Water Puddle Generation Chance", DEFAULT_WATER_PUDDLE_GENERATION_CHANCE, 0.00d, 100.00d);
			cfg.pop();

			cfg.push("Jungle Caves");
				LUSH_LILYPADS_CHANCE = cfg.comment("Use the \"Generate Lily Pads\" config option to stop generating them entirely.").defineInRange("Lily Pad Generation Chance", DEFAULT_LUSH_LILYPADS_CHANCE, 1.00d, 100.00d);
				LUSH_LEAVES_CHANCE = cfg.comment("Controls how often decorative leaves generate in jungle caves. Zero disables them. Default: 65 (recommended to avoid overcrowding). Max: 100.").defineInRange("Leaves Generation Chance", DEFAULT_LUSH_LEAVES_CHANCE, 0.00d, 100.00d);
				LUSH_SAPLINGS_CHANCE = cfg.comment("Use the \"Generate Saplings\" config option to stop generating them entirely.").defineInRange("Sapling Generation Chance", DEFAULT_LUSH_SAPLINGS_CHANCE, 1.00d, 100.00d);
			cfg.pop();

			cfg.push("Jungle Volcanic Caves");
				LUSH_VOLCANIC_LEAVES_CHANCE = cfg.comment("Controls how often decorative leaves generate in jungle volcanic caves. Zero disables them. Default: 65 (recommended to avoid overcrowding). Max: 100.").defineInRange("Leaves Generation Chance", DEFAULT_LUSH_VOLCANIC_LEAVES_CHANCE, 0.00d, 100.00d);
				LUSH_VOLCANIC_SAPLINGS_CHANCE = cfg.comment("Use the \"Generate Saplings\" config option to stop generating them entirely.").defineInRange("Sapling Generation Chance", DEFAULT_LUSH_VOLCANIC_SAPLINGS_CHANCE, 1.00d, 100.00d);
			cfg.pop();

			cfg.push("Mesa Caves");
				MESA_DEAD_BUSHES_CHANCE = cfg.comment("Use the \"Generate Dead Bushes\" config option to stop generating them entirely.").defineInRange("Dead Bush Generation Chance", DEFAULT_MESA_DEAD_BUSHES_CHANCE, 1.00d, 100.00d);
			cfg.pop();

			cfg.push("Mossy Caves");
				MOSSY_LILYPADS_CHANCE = cfg.comment("Use the \"Generate Lily Pads\" config option to stop generating them entirely.").defineInRange("Lily Pad Generation Chance", DEFAULT_MOSSY_LILYPADS_CHANCE, 1.00d, 100.00d);
			cfg.pop();

			cfg.push("Mossy Rocky Caves");
				MOSSY_ROCKY_LILYPADS_CHANCE = cfg.comment("Use the \"Generate Lily Pads\" config option to stop generating them entirely.").defineInRange("Lily Pad Generation Chance", DEFAULT_MOSSY_ROCKY_LILYPADS_CHANCE, 1.00d, 100.00d);
			cfg.pop();

			cfg.push("Rocky Caves");
				ROCKY_BUTTONS_CHANCE = cfg.comment("Use the \"Generate Pebbles\" config option to stop generating them entirely.").defineInRange("Pebble Generation Chance", DEFAULT_ROCKY_BUTTONS_CHANCE, 1.00d, 100.00d);
			cfg.pop();

			cfg.push("Red Sandy Caves");
				RED_SANDSTONE_PEBBLE_CHANCE = cfg.comment("Use the \"Generate Pebbles\" config option to stop generating them entirely.").defineInRange("Red Sandstone Pebble Generation Chance", DEFAULT_RED_SANDSTONE_PEBBLE_CHANCE, 1.00d, 100.00d);
			cfg.pop();

			cfg.push("Sandy Caves");
				SANDY_DEAD_BUSHES_CHANCE = cfg.comment("Use the \"Generate Dead Bushes\" config option to stop generating them entirely.").defineInRange("Dead Bush Generation Chance", DEFAULT_SANDY_DEAD_BUSHES_CHANCE, 1.00d, 100.00d);
			cfg.pop();

			cfg.push("Sandy Rocky Caves");
				SANDY_ROCKY_BUTTONS_CHANCE = cfg.comment("Use the \"Generate Pebbles\" config option to stop generating them entirely.").defineInRange("Pebble Generation Chance", DEFAULT_SANDY_ROCKY_BUTTONS_CHANCE, 1.00d, 100.00d);
				SANDY_ROCKY_DEAD_BUSHES_CHANCE = cfg.comment("Use the \"Generate Dead Bushes\" config option to stop generating them entirely.").defineInRange("Dead Bush Generation Chance", DEFAULT_SANDY_ROCKY_DEAD_BUSHES_CHANCE, 1.00d, 100.00d);
			cfg.pop();
		cfg.pop();

		SPEC = cfg.build();
	}

	private SubWildConfig()
	{
	}

	private static CaveTypeToggleSet createCaveTypeToggles(ForgeConfigSpec.Builder cfg)
	{
		List<CaveType> caveTypes = new ArrayList<>(SubWildFeatures.CAVE_TYPES.values());
		caveTypes.sort(Comparator.comparing(type -> humanizeCaveTypeName(type.name.getPath())));

		cfg.push("Global");
		ForgeConfigSpec.BooleanValue globalReplaceAll = cfg.comment("Replace every SubWild cave type with the selected cave type. This overrides individual cave type settings.")
			.define("Replace All Cave Types", DEFAULT_GLOBAL_REPLACE_ALL_CAVE_TYPES);
		ForgeConfigSpec.BooleanValue globalReplaceDeepTuff = cfg.comment("Extends the selected replacement cave type through all lower cave layers down to bedrock.")
			.define("Replace Deep/Tuff Caves", DEFAULT_GLOBAL_REPLACE_DEEP_TUFF_CAVES);
		ForgeConfigSpec.ConfigValue<String> globalReplacementTarget = cfg.comment("The cave type used when the global replace-all setting is enabled.")
			.define("Replacement Cave Type", SubWildFeatures.BASIC_CAVE.name.toString());
		cfg.pop();

		List<CaveTypeChoice> choices = new ArrayList<>(caveTypes.size());
		List<CaveTypeToggle> options = new ArrayList<>(caveTypes.size());
		Map<ResourceLocation, CaveTypeToggle> values = new LinkedHashMap<>(caveTypes.size());
		for(CaveType type : caveTypes)
		{
			String label = humanizeCaveTypeName(type.name.getPath()) + " Caves";
			CaveTypeChoice choice = new CaveTypeChoice(type.name, label);
			choices.add(choice);

			cfg.push(label);
			ForgeConfigSpec.EnumValue<CaveTypeMode> mode = cfg.comment("How " + label + " should behave when selected by SubWild world generation.")
				.defineEnum("Mode", CaveTypeMode.ON);
			ForgeConfigSpec.BooleanValue affectDeepTuff = cfg.comment("Extends " + label + " through all lower cave layers down to bedrock when it replaces another cave type.")
				.define("Affect Deep/Tuff Caves", DEFAULT_CAVE_REPLACE_DEEP_TUFF_CAVES);
			ForgeConfigSpec.ConfigValue<String> replacementTarget = cfg.comment("The cave type used when " + label + " is disabled and set to replace with another SubWild cave type.")
				.define("Replacement Cave Type", type.name.toString());
			cfg.pop();

			CaveTypeToggle toggle = new CaveTypeToggle(type.name, label, "How " + label + " should behave during world generation.", mode, affectDeepTuff, replacementTarget);
			options.add(toggle);
			values.put(type.name, toggle);
		}

		return new CaveTypeToggleSet(globalReplaceAll, globalReplaceDeepTuff, globalReplacementTarget, List.copyOf(choices), List.copyOf(options), Map.copyOf(values));
	}

	private static String humanizeCaveTypeName(String path)
	{
		if("lush".equals(path))
			return "Jungle";
		if("lush_volcanic".equals(path))
			return "Jungle Volcanic";

		String[] parts = path.split("_");
		StringBuilder builder = new StringBuilder(path.length() + parts.length * 2);
		for(int i = 0; i < parts.length; ++i)
		{
			if(i > 0)
				builder.append(' ');
			String part = parts[i];
			if(part.isEmpty())
				continue;
			builder.append(Character.toUpperCase(part.charAt(0)));
			if(part.length() > 1)
				builder.append(part.substring(1).toLowerCase(Locale.ROOT));
		}
		return builder.toString();
	}

	public static boolean isCaveTypeEnabled(CaveType type)
	{
		CaveTypeToggle value = CAVE_TYPE_TOGGLES.get(type.name);
		return value == null || value.mode().get() != CaveTypeMode.LEAVE_VANILLA;
	}

	public static CaveType getConfiguredGlobalReplacement()
	{
		if(!GLOBAL_REPLACE_ALL_CAVE_TYPES.get())
			return null;
		return findCaveType(GLOBAL_REPLACEMENT_CAVE_TYPE.get());
	}

	public static double getSpeleothemGenerationChance(Block block)
	{
		if(block == SubWildBlocks.STONE_SPELEOTHEM.get() || block == SubWildBlocks.FROZEN_STONE_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(STONE_SPELEOTHEM_GENERATION_CHANCE);
		if(block == SubWildBlocks.DEEPSLATE_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(DEEPSLATE_SPELEOTHEM_GENERATION_CHANCE);
		if(block == SubWildBlocks.GRANITE_SPELEOTHEM.get() || block == SubWildBlocks.FROZEN_GRANITE_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(GRANITE_SPELEOTHEM_GENERATION_CHANCE);
		if(block == SubWildBlocks.DIORITE_SPELEOTHEM.get() || block == SubWildBlocks.FROZEN_DIORITE_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(DIORITE_SPELEOTHEM_GENERATION_CHANCE);
		if(block == SubWildBlocks.ANDESITE_SPELEOTHEM.get() || block == SubWildBlocks.FROZEN_ANDESITE_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(ANDESITE_SPELEOTHEM_GENERATION_CHANCE);
		if(block == SubWildBlocks.SANDSTONE_SPELEOTHEM.get() || block == SubWildBlocks.FROZEN_SANDSTONE_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(SANDSTONE_SPELEOTHEM_GENERATION_CHANCE);
		if(block == SubWildBlocks.RED_SANDSTONE_SPELEOTHEM.get() || block == SubWildBlocks.FROZEN_RED_SANDSTONE_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(RED_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE);
		if(block == SubWildBlocks.OBSIDIAN_SPELEOTHEM.get() || block == SubWildBlocks.FROZEN_OBSIDIAN_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(OBSIDIAN_SPELEOTHEM_GENERATION_CHANCE);
		if(block == SubWildBlocks.BLACKSTONE_SPELEOTHEM.get() || block == SubWildBlocks.FROZEN_BLACKSTONE_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(BLACKSTONE_SPELEOTHEM_GENERATION_CHANCE);
		if(block == SubWildBlocks.BASALT_SPELEOTHEM.get() || block == SubWildBlocks.FROZEN_BASALT_SPELEOTHEM.get())
			return resolveSpeleothemGenerationChance(BASALT_SPELEOTHEM_GENERATION_CHANCE);
		return GLOBAL_SPELEOTHEM_GENERATION_CHANCE.get();
	}

	private static double resolveSpeleothemGenerationChance(ForgeConfigSpec.DoubleValue value)
	{
		double chance = value.get();
		return chance < 0.00d ? GLOBAL_SPELEOTHEM_GENERATION_CHANCE.get() : chance;
	}

	public static CaveType getConfiguredGlobalReplacement(WorldGenLevel world, BlockPos pos, CaveType originalType)
	{
		CaveType globalReplacement = getConfiguredGlobalReplacement();
		if(globalReplacement == null)
			return null;
		if(!GLOBAL_REPLACE_DEEP_TUFF_CAVES.get() && originalType.isDeepVariantAt(world, pos))
			return null;
		return globalReplacement;
	}

	public static boolean shouldReplaceDeepTuffVariants()
	{
		return ACTIVE_DEEP_TUFF_REPLACEMENT.get();
	}

	public static void beginDeepTuffReplacementScope(boolean active)
	{
		ACTIVE_DEEP_TUFF_REPLACEMENT.set(active);
	}

	public static void endDeepTuffReplacementScope()
	{
		ACTIVE_DEEP_TUFF_REPLACEMENT.remove();
	}

	public static CaveType resolveCaveType(CaveType originalType)
	{
		CaveType globalReplacement = getConfiguredGlobalReplacement();
		if(globalReplacement != null)
			return globalReplacement;

		CaveTypeToggle toggle = CAVE_TYPE_TOGGLES.get(originalType.name);
		if(toggle == null)
			return originalType;

		return switch(toggle.mode().get())
		{
		case ON -> originalType;
		case LEAVE_VANILLA -> null;
		case REPLACE_WITH_SUBWILD -> findCaveType(toggle.replacementTarget().get());
		};
	}

	public static CaveType resolveCaveType(CaveType originalType, WorldGenLevel world, BlockPos pos)
	{
		return resolveCaveTypeSelection(originalType, world, pos).type();
	}

	public static ResolvedCaveType resolveCaveTypeSelection(CaveType originalType, WorldGenLevel world, BlockPos pos)
	{
		boolean deepVariant = originalType.isDeepVariantAt(world, pos);
		CaveType globalReplacement = getConfiguredGlobalReplacement();
		if(globalReplacement != null)
		{
			if(!GLOBAL_REPLACE_DEEP_TUFF_CAVES.get() && deepVariant)
				return resolvePerCaveTypeSelection(originalType, deepVariant);
			return new ResolvedCaveType(globalReplacement, deepVariant && GLOBAL_REPLACE_DEEP_TUFF_CAVES.get());
		}

		return resolvePerCaveTypeSelection(originalType, deepVariant);
	}

	private static ResolvedCaveType resolvePerCaveTypeSelection(CaveType originalType, boolean deepVariant)
	{
		CaveTypeToggle toggle = CAVE_TYPE_TOGGLES.get(originalType.name);
		if(toggle == null)
			return new ResolvedCaveType(originalType, false);

		return switch(toggle.mode().get())
		{
		case ON -> new ResolvedCaveType(originalType, false);
		case LEAVE_VANILLA -> new ResolvedCaveType(null, false);
		case REPLACE_WITH_SUBWILD ->
		{
			if(deepVariant && !toggle.affectDeepTuff().get())
				yield new ResolvedCaveType(originalType, false);
			yield new ResolvedCaveType(findCaveType(toggle.replacementTarget().get()), deepVariant && toggle.affectDeepTuff().get());
		}
		};
	}

	public static CaveType getDeepTuffExtensionReplacement(CaveType originalType)
	{
		CaveTypeToggle toggle = CAVE_TYPE_TOGGLES.get(originalType.name);
		if(toggle == null || toggle.mode().get() != CaveTypeMode.REPLACE_WITH_SUBWILD || !toggle.affectDeepTuff().get())
			return null;
		return findCaveType(toggle.replacementTarget().get());
	}

	public static boolean canGenerateAnyCaveType(Iterable<CaveType> caveTypes)
	{
		if(getConfiguredGlobalReplacement() != null)
			return true;
		for(CaveType type : caveTypes)
			if(resolveCaveType(type) != null)
				return true;
		return false;
	}

	public static CaveType findCaveType(String name)
	{
		try
		{
			return SubWildFeatures.CAVE_TYPES.get(new ResourceLocation(name));
		}
		catch(IllegalArgumentException ex)
		{
			return null;
		}
	}

	public static boolean isAllowed(Level world)
	{
		String name = world.dimension().location().toString();
		for(String key : DIMENSION_WHITELIST.get())
			if(key.equals(name))
				return true;
		return false;
	}

	public static void resetToDefaults()
	{
		setAndSave(EXPENSIVE_SCAN, DEFAULT_EXPENSIVE_SCAN);
		setAndSave(ADAPT_CAVE_BIOMES_TO_MODDED_BIOMES, DEFAULT_ADAPT_CAVE_BIOMES_TO_MODDED_BIOMES);
		setAndSave(SLOPE_THRESHOLD, DEFAULT_SLOPE_THRESHOLD);
		DIMENSION_WHITELIST.set(List.copyOf(DEFAULT_DIMENSION_WHITELIST_VALUES));
		DIMENSION_WHITELIST.save();

		setAndSave(GENERATE_BUTTONS, DEFAULT_GENERATE_BUTTONS);
		setAndSave(THROWABLE_PEBBLES, DEFAULT_THROWABLE_PEBBLES);
		setAndSave(GENERATE_VINES, DEFAULT_GENERATE_VINES);
		setAndSave(GENERATE_PUDDLES, DEFAULT_GENERATE_PUDDLES);
		setAndSave(GENERATE_STAIRS, DEFAULT_GENERATE_STAIRS);
		setAndSave(GENERATE_SLABS, DEFAULT_GENERATE_SLABS);
		setAndSave(GENERATE_PATCHES, DEFAULT_GENERATE_PATCHES);
		setAndSave(GENERATE_SPELEOTHEMS, DEFAULT_GENERATE_SPELEOTHEMS);
		setAndSave(GENERATE_FEATURES_IN_WATER, DEFAULT_GENERATE_FEATURES_IN_WATER);
		setAndSave(GENERATE_FOXFIRES, DEFAULT_GENERATE_FOXFIRES);
		setAndSave(GENERATE_SUBWILD_ORES, DEFAULT_GENERATE_SUBWILD_ORES);
		setAndSave(GENERATE_DEAD_BUSHES, DEFAULT_GENERATE_DEAD_BUSHES);
		setAndSave(GENERATE_LILYPADS, DEFAULT_GENERATE_LILYPADS);
		setAndSave(GENERATE_WALL_CORAL, DEFAULT_GENERATE_WALL_CORAL);
		setAndSave(GENERATE_DEAD_WALL_CORAL, DEFAULT_GENERATE_DEAD_WALL_CORAL);
		setAndSave(GENERATE_FLOOR_CORAL, DEFAULT_GENERATE_FLOOR_CORAL);
		setAndSave(GENERATE_DEAD_FLOOR_CORAL, DEFAULT_GENERATE_DEAD_FLOOR_CORAL);
		setAndSave(GLOBAL_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_GLOBAL_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(STONE_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_STONE_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(DEEPSLATE_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_DEEPSLATE_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(GRANITE_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_GRANITE_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(DIORITE_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_DIORITE_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(ANDESITE_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_ANDESITE_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(SANDSTONE_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(RED_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_RED_SANDSTONE_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(OBSIDIAN_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_OBSIDIAN_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(BLACKSTONE_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_BLACKSTONE_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(BASALT_SPELEOTHEM_GENERATION_CHANCE, DEFAULT_BASALT_SPELEOTHEM_GENERATION_CHANCE);
		setAndSave(SHORT_FOXFIRE_GENERATION_CHANCE, DEFAULT_SHORT_FOXFIRE_GENERATION_CHANCE);
		setAndSave(LONG_FOXFIRE_GENERATION_CHANCE, DEFAULT_LONG_FOXFIRE_GENERATION_CHANCE);
		setAndSave(GENERATE_SAPLINGS, DEFAULT_GENERATE_SAPLINGS);
		setAndSave(GENERATE_IN_STRUCTURES, DEFAULT_GENERATE_IN_STRUCTURES);
		setAndSave(SHEAR_VANILLA_MOSSY_BLOCKS, DEFAULT_SHEAR_VANILLA_MOSSY_BLOCKS);

		setAndSave(GLOBAL_REPLACE_ALL_CAVE_TYPES, DEFAULT_GLOBAL_REPLACE_ALL_CAVE_TYPES);
		setAndSave(GLOBAL_REPLACE_DEEP_TUFF_CAVES, DEFAULT_GLOBAL_REPLACE_DEEP_TUFF_CAVES);
		setAndSave(GLOBAL_REPLACEMENT_CAVE_TYPE, SubWildFeatures.BASIC_CAVE.name.toString());
		for(CaveTypeToggle toggle : CAVE_TYPE_OPTIONS)
		{
			setAndSave(toggle.mode(), CaveTypeMode.ON);
			setAndSave(toggle.affectDeepTuff(), DEFAULT_CAVE_REPLACE_DEEP_TUFF_CAVES);
			setAndSave(toggle.replacementTarget(), toggle.id().toString());
		}

		setAndSave(SLOPE_CHANCE, DEFAULT_SLOPE_CHANCE);
		setAndSave(SLOPE_THRESHOLD_CHANCE, DEFAULT_SLOPE_THRESHOLD_CHANCE);
		setAndSave(COAL_SHARD_CHANCE, DEFAULT_COAL_SHARD_CHANCE);
		setAndSave(ROCKY_BUTTONS_CHANCE, DEFAULT_ROCKY_BUTTONS_CHANCE);
		setAndSave(SANDY_ROCKY_BUTTONS_CHANCE, DEFAULT_SANDY_ROCKY_BUTTONS_CHANCE);
		setAndSave(RED_SANDSTONE_PEBBLE_CHANCE, DEFAULT_RED_SANDSTONE_PEBBLE_CHANCE);
		setAndSave(MESA_DEAD_BUSHES_CHANCE, DEFAULT_MESA_DEAD_BUSHES_CHANCE);
		setAndSave(SANDY_DEAD_BUSHES_CHANCE, DEFAULT_SANDY_DEAD_BUSHES_CHANCE);
		setAndSave(SANDY_ROCKY_DEAD_BUSHES_CHANCE, DEFAULT_SANDY_ROCKY_DEAD_BUSHES_CHANCE);
		setAndSave(WATER_PUDDLE_GENERATION_CHANCE, DEFAULT_WATER_PUDDLE_GENERATION_CHANCE);
		setAndSave(LUSH_LILYPADS_CHANCE, DEFAULT_LUSH_LILYPADS_CHANCE);
		setAndSave(LUSH_LEAVES_CHANCE, DEFAULT_LUSH_LEAVES_CHANCE);
		setAndSave(MOSSY_LILYPADS_CHANCE, DEFAULT_MOSSY_LILYPADS_CHANCE);
		setAndSave(MOSSY_ROCKY_LILYPADS_CHANCE, DEFAULT_MOSSY_ROCKY_LILYPADS_CHANCE);
		setAndSave(LUSH_SAPLINGS_CHANCE, DEFAULT_LUSH_SAPLINGS_CHANCE);
		setAndSave(LUSH_VOLCANIC_LEAVES_CHANCE, DEFAULT_LUSH_VOLCANIC_LEAVES_CHANCE);
		setAndSave(LUSH_VOLCANIC_SAPLINGS_CHANCE, DEFAULT_LUSH_VOLCANIC_SAPLINGS_CHANCE);
	}

	private static <T> void setAndSave(ForgeConfigSpec.ConfigValue<T> value, T defaultValue)
	{
		value.set(defaultValue);
		value.save();
	}

	public enum CaveTypeMode
	{
		ON,
		LEAVE_VANILLA,
		REPLACE_WITH_SUBWILD
	}

	public record CaveTypeChoice(ResourceLocation id, String label) {}

	public record CaveTypeToggle(
		ResourceLocation id,
		String label,
		String description,
		ForgeConfigSpec.EnumValue<CaveTypeMode> mode,
		ForgeConfigSpec.BooleanValue affectDeepTuff,
		ForgeConfigSpec.ConfigValue<String> replacementTarget)
	{}

	public record ResolvedCaveType(CaveType type, boolean replaceDeepTuff) {}

	private record CaveTypeToggleSet(
		ForgeConfigSpec.BooleanValue globalReplaceAll,
		ForgeConfigSpec.BooleanValue globalReplaceDeepTuff,
		ForgeConfigSpec.ConfigValue<String> globalReplacementTarget,
		List<CaveTypeChoice> choices,
		List<CaveTypeToggle> options,
		Map<ResourceLocation, CaveTypeToggle> values)
	{}
}
