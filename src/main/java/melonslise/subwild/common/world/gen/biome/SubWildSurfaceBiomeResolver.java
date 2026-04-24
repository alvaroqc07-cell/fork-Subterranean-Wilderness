package melonslise.subwild.common.world.gen.biome;

import java.util.List;

import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildFeatures;
import melonslise.subwild.common.world.gen.feature.CaveRangeConfig;
import melonslise.subwild.common.world.gen.feature.cavetype.CaveType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

public final class SubWildSurfaceBiomeResolver
{
	private static final TagKey<Biome> SUBWILD_DESERT = subwild("desert");
	private static final TagKey<Biome> SUBWILD_EXTREME_HILLS = subwild("extreme_hills");
	private static final TagKey<Biome> SUBWILD_FOREST = subwild("forest");
	private static final TagKey<Biome> SUBWILD_ICY = subwild("icy");
	private static final TagKey<Biome> SUBWILD_JUNGLE = subwild("jungle");
	private static final TagKey<Biome> SUBWILD_MESA = subwild("mesa");
	private static final TagKey<Biome> SUBWILD_MUSHROOM = subwild("mushroom");
	private static final TagKey<Biome> SUBWILD_PLAINS = subwild("plains");
	private static final TagKey<Biome> SUBWILD_SAVANNA = subwild("savanna");
	private static final TagKey<Biome> SUBWILD_SWAMP = subwild("swamp");
	private static final TagKey<Biome> SUBWILD_TAIGA = subwild("taiga");
	private static final TagKey<Biome> SUBWILD_WET = subwild("wet");

	private static final TagKey<Biome> MINECRAFT_IS_BADLANDS = minecraft("is_badlands");
	private static final TagKey<Biome> MINECRAFT_IS_BEACH = minecraft("is_beach");
	private static final TagKey<Biome> MINECRAFT_IS_FOREST = minecraft("is_forest");
	private static final TagKey<Biome> MINECRAFT_IS_JUNGLE = minecraft("is_jungle");
	private static final TagKey<Biome> MINECRAFT_IS_MOUNTAIN = minecraft("is_mountain");
	private static final TagKey<Biome> MINECRAFT_IS_OCEAN = minecraft("is_ocean");
	private static final TagKey<Biome> MINECRAFT_IS_PLAINS = minecraft("is_plains");
	private static final TagKey<Biome> MINECRAFT_IS_RIVER = minecraft("is_river");
	private static final TagKey<Biome> MINECRAFT_IS_SAVANNA = minecraft("is_savanna");
	private static final TagKey<Biome> MINECRAFT_IS_TAIGA = minecraft("is_taiga");

	private static final TagKey<Biome> C_AQUATIC = common("is_aquatic");
	private static final TagKey<Biome> C_COLD_OVERWORLD = common("is_cold/overworld");
	private static final TagKey<Biome> C_DENSE_VEGETATION = common("is_dense_vegetation/overworld");
	private static final TagKey<Biome> C_DESERT = common("is_desert");
	private static final TagKey<Biome> C_HOT_OVERWORLD = common("is_hot/overworld");
	private static final TagKey<Biome> C_ICY = common("is_icy");
	private static final TagKey<Biome> C_LUSH = common("is_lush");
	private static final TagKey<Biome> C_MOUNTAIN = common("is_mountain");
	private static final TagKey<Biome> C_MUSHROOM = common("is_mushroom");
	private static final TagKey<Biome> C_OLD_GROWTH = common("is_old_growth");
	private static final TagKey<Biome> C_PLAINS = common("is_plains");
	private static final TagKey<Biome> C_PLATEAU = common("is_plateau");
	private static final TagKey<Biome> C_SANDY = common("is_sandy");
	private static final TagKey<Biome> C_SNOWY = common("is_snowy");
	private static final TagKey<Biome> C_SPARSE_VEGETATION = common("is_sparse_vegetation/overworld");
	private static final TagKey<Biome> C_SPOOKY = common("is_spooky");
	private static final TagKey<Biome> C_SWAMP = common("is_swamp");
	private static final TagKey<Biome> C_TREE_CONIFEROUS = common("is_tree/coniferous");
	private static final TagKey<Biome> C_TREE_JUNGLE = common("is_tree/jungle");
	private static final TagKey<Biome> C_WASTELAND = common("is_wasteland");
	private static final TagKey<Biome> C_WET_OVERWORLD = common("is_wet/overworld");

	private static final CaveRangeConfig ROCKY_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.ROCKY_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig DESERT_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.SANDY_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.SANDY_VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig FOREST_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.MOSSY_ROCKY_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.LUSH_VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig ICY_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.ICY_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.ICY_ROCKY_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig JUNGLE_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.LUSH_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.LUSH_VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig MESA_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.MESA_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.SANDY_RED_VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig MOUNTAIN_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.ROCKY_CAVE, 0.0d, 0.4d),
		range(SubWildFeatures.ICY_ROCKY_CAVE, 0.4d, 0.8d),
		range(SubWildFeatures.ROCKY_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig MUSHROOM_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.FUNGAL_CAVE, 0.0d, 1.0d)));
	private static final CaveRangeConfig PLAINS_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.MUDDY_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig SAVANNA_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.SANDY_ROCKY_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.SANDY_VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig SWAMP_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.MOSSY_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.LUSH_VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig TAIGA_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.PODZOL_CAVE, 0.0d, 0.4d),
		range(SubWildFeatures.MUDDY_CAVE, 0.4d, 0.8d),
		range(SubWildFeatures.LUSH_VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig WET_AIR_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.DEAD_CORAL_CAVE, 0.0d, 0.8d),
		range(SubWildFeatures.VOLCANIC_CAVE, 0.8d, 1.0d)));
	private static final CaveRangeConfig WET_LIQUID_PROFILE = new CaveRangeConfig(List.of(
		range(SubWildFeatures.CORAL_CAVE, 0.0d, 1.0d)));

	private static final List<CaveType> AIR_PROFILE_TYPES = List.of(
		SubWildFeatures.ROCKY_CAVE,
		SubWildFeatures.VOLCANIC_CAVE,
		SubWildFeatures.SANDY_CAVE,
		SubWildFeatures.SANDY_VOLCANIC_CAVE,
		SubWildFeatures.MOSSY_ROCKY_CAVE,
		SubWildFeatures.LUSH_VOLCANIC_CAVE,
		SubWildFeatures.ICY_CAVE,
		SubWildFeatures.ICY_ROCKY_CAVE,
		SubWildFeatures.LUSH_CAVE,
		SubWildFeatures.MESA_CAVE,
		SubWildFeatures.SANDY_RED_VOLCANIC_CAVE,
		SubWildFeatures.FUNGAL_CAVE,
		SubWildFeatures.MUDDY_CAVE,
		SubWildFeatures.SANDY_ROCKY_CAVE,
		SubWildFeatures.MOSSY_CAVE,
		SubWildFeatures.PODZOL_CAVE,
		SubWildFeatures.DEAD_CORAL_CAVE);
	private static final List<CaveType> LIQUID_PROFILE_TYPES = List.of(SubWildFeatures.CORAL_CAVE);

	private SubWildSurfaceBiomeResolver()
	{
	}

	public static CaveRangeConfig resolveSurfaceProfile(WorldGenLevel world, BlockPos pos, CaveRangeConfig.BiomeProfileMode mode)
	{
		Holder<Biome> surfaceBiome = getSurfaceBiome(world, pos);
		SurfaceBiomeProfile profile = classifySurfaceBiome(surfaceBiome);
		return switch(mode)
		{
		case FIXED -> null;
		case SURFACE_AIR -> profile.airProfile();
		case SURFACE_LIQUID -> profile.liquidProfile();
		};
	}

	public static boolean canGenerateAnyProfile(CaveRangeConfig.BiomeProfileMode mode)
	{
		return switch(mode)
		{
		case FIXED -> false;
		case SURFACE_AIR -> SubWildConfig.canGenerateAnyCaveType(AIR_PROFILE_TYPES);
		case SURFACE_LIQUID -> SubWildConfig.canGenerateAnyCaveType(LIQUID_PROFILE_TYPES);
		};
	}

	private static Holder<Biome> getSurfaceBiome(WorldGenLevel world, BlockPos pos)
	{
		int surfaceY = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) - 1;
		if(surfaceY < world.getMinBuildHeight())
			surfaceY = world.getMinBuildHeight();
		return world.getBiome(new BlockPos(pos.getX(), surfaceY, pos.getZ()));
	}

	private static SurfaceBiomeProfile classifySurfaceBiome(Holder<Biome> biome)
	{
		if(biome.is(SUBWILD_MUSHROOM) || biome.is(C_MUSHROOM))
			return SurfaceBiomeProfile.MUSHROOM;

		if(biome.is(SUBWILD_MESA) || biome.is(MINECRAFT_IS_BADLANDS))
			return SurfaceBiomeProfile.MESA;

		if(biome.is(SUBWILD_SWAMP) || biome.is(C_SWAMP))
			return SurfaceBiomeProfile.SWAMP;

		if(isAquaticSurfaceBiome(biome))
			return SurfaceBiomeProfile.WET;

		if(biome.is(SUBWILD_DESERT) || biome.is(C_DESERT) || biome.is(C_SANDY))
			return SurfaceBiomeProfile.DESERT;

		if(biome.is(SUBWILD_ICY) || biome.is(C_ICY) || biome.is(C_SNOWY))
			return SurfaceBiomeProfile.ICY;

		if(biome.is(SUBWILD_JUNGLE) || biome.is(MINECRAFT_IS_JUNGLE) || biome.is(C_TREE_JUNGLE) || isHotWetLushBiome(biome))
			return SurfaceBiomeProfile.JUNGLE;

		if(biome.is(SUBWILD_SAVANNA) || biome.is(MINECRAFT_IS_SAVANNA) || isHotSparseBiome(biome))
			return SurfaceBiomeProfile.SAVANNA;

		if(biome.is(SUBWILD_TAIGA) || biome.is(MINECRAFT_IS_TAIGA) || biome.is(C_TREE_CONIFEROUS) || isColdWetBiome(biome))
			return SurfaceBiomeProfile.TAIGA;

		if(biome.is(SUBWILD_EXTREME_HILLS) || biome.is(MINECRAFT_IS_MOUNTAIN) || biome.is(C_MOUNTAIN) || biome.is(C_PLATEAU) || biome.is(C_WASTELAND))
			return SurfaceBiomeProfile.MOUNTAIN;

		if(biome.is(SUBWILD_FOREST) || biome.is(MINECRAFT_IS_FOREST) || biome.is(C_LUSH) || biome.is(C_DENSE_VEGETATION) || biome.is(C_OLD_GROWTH) || biome.is(C_SPOOKY))
			return SurfaceBiomeProfile.FOREST;

		if(biome.is(SUBWILD_PLAINS) || biome.is(MINECRAFT_IS_PLAINS) || biome.is(C_PLAINS))
			return SurfaceBiomeProfile.PLAINS;

		return classifyFallback(biome.value());
	}

	private static boolean isAquaticSurfaceBiome(Holder<Biome> biome)
	{
		return biome.is(SUBWILD_WET) || biome.is(MINECRAFT_IS_OCEAN) || biome.is(MINECRAFT_IS_RIVER) || biome.is(MINECRAFT_IS_BEACH) || biome.is(C_AQUATIC);
	}

	private static boolean isHotWetLushBiome(Holder<Biome> biome)
	{
		return biome.is(C_HOT_OVERWORLD) && (biome.is(C_WET_OVERWORLD) || biome.is(C_LUSH) || biome.is(C_DENSE_VEGETATION));
	}

	private static boolean isHotSparseBiome(Holder<Biome> biome)
	{
		return biome.is(C_HOT_OVERWORLD) && (biome.is(C_SPARSE_VEGETATION) || !biome.value().hasPrecipitation()) && !biome.is(C_LUSH);
	}

	private static boolean isColdWetBiome(Holder<Biome> biome)
	{
		return biome.is(C_COLD_OVERWORLD) && biome.is(C_WET_OVERWORLD) && !biome.is(C_SNOWY) && !biome.is(C_ICY);
	}

	private static SurfaceBiomeProfile classifyFallback(Biome biome)
	{
		float temperature = biome.getBaseTemperature();
		boolean hasPrecipitation = biome.hasPrecipitation();
		if(!hasPrecipitation)
		{
			if(temperature >= 1.1f)
				return SurfaceBiomeProfile.DESERT;
			if(temperature >= 0.85f)
				return SurfaceBiomeProfile.SAVANNA;
			return SurfaceBiomeProfile.ROCKY;
		}
		if(temperature <= 0.15f)
			return SurfaceBiomeProfile.ICY;
		if(temperature <= 0.4f)
			return SurfaceBiomeProfile.TAIGA;
		if(temperature >= 0.9f)
			return SurfaceBiomeProfile.JUNGLE;
		return SurfaceBiomeProfile.FOREST;
	}

	private static CaveRangeConfig.CaveRange range(CaveType type, double min, double max)
	{
		return new CaveRangeConfig.CaveRange(type, min, max);
	}

	private static TagKey<Biome> minecraft(String path)
	{
		return tag("minecraft", path);
	}

	private static TagKey<Biome> subwild(String path)
	{
		return tag("subwild", path);
	}

	private static TagKey<Biome> common(String path)
	{
		return tag("c", path);
	}

	private static TagKey<Biome> tag(String namespace, String path)
	{
		return TagKey.create(Registries.BIOME, new ResourceLocation(namespace, path));
	}

	private enum SurfaceBiomeProfile
	{
		ROCKY(ROCKY_AIR_PROFILE, null),
		DESERT(DESERT_AIR_PROFILE, null),
		FOREST(FOREST_AIR_PROFILE, null),
		ICY(ICY_AIR_PROFILE, null),
		JUNGLE(JUNGLE_AIR_PROFILE, null),
		MESA(MESA_AIR_PROFILE, null),
		MOUNTAIN(MOUNTAIN_AIR_PROFILE, null),
		MUSHROOM(MUSHROOM_AIR_PROFILE, null),
		PLAINS(PLAINS_AIR_PROFILE, null),
		SAVANNA(SAVANNA_AIR_PROFILE, null),
		SWAMP(SWAMP_AIR_PROFILE, null),
		TAIGA(TAIGA_AIR_PROFILE, null),
		WET(WET_AIR_PROFILE, WET_LIQUID_PROFILE);

		private final CaveRangeConfig airProfile;
		private final CaveRangeConfig liquidProfile;

		SurfaceBiomeProfile(CaveRangeConfig airProfile, CaveRangeConfig liquidProfile)
		{
			this.airProfile = airProfile;
			this.liquidProfile = liquidProfile;
		}

		private CaveRangeConfig airProfile()
		{
			return this.airProfile;
		}

		private CaveRangeConfig liquidProfile()
		{
			return this.liquidProfile;
		}
	}
}
