package melonslise.subwild.common.world.gen.feature;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.world.gen.biome.SubWildSurfaceBiomeResolver;
import melonslise.subwild.common.world.gen.feature.cavetype.CaveType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CaveRangeConfig implements FeatureConfiguration
{
	public static final Codec<CaveRangeConfig> CODEC = RecordCodecBuilder.create(record -> record
		.group(
			CaveRange.CODEC.listOf().fieldOf("cave_ranges").forGetter(inst -> inst.caveRanges),
			BiomeProfileMode.CODEC.optionalFieldOf("biome_profile_mode", BiomeProfileMode.FIXED).forGetter(inst -> inst.biomeProfileMode))
		.apply(record, CaveRangeConfig::new));

	public static class CaveRange
	{
		public static final Codec<CaveRange> CODEC = RecordCodecBuilder.create(record -> record
			.group(
				CaveType.CODEC.fieldOf("cave_type").forGetter(inst -> inst.type),
				Codec.FLOAT.fieldOf("min").forGetter(inst -> (float) inst.min),
				Codec.FLOAT.fieldOf("max").forGetter(inst -> (float) inst.max))
			.apply(record, CaveRange::new));

		public final CaveType type;
		public final double min, max;

		public CaveRange(CaveType inst, double min, double max)
		{
			this.type = inst;
			this.min = Math.min(min, max);
			this.max = Math.max(min, max);
		}

		public boolean contains(double depth)
		{
			return this.min <= depth && depth < this.max;
		}
	}

	public final List<CaveRange> caveRanges;
	public final BiomeProfileMode biomeProfileMode;

	public CaveRangeConfig(List<CaveRange> caveRanges)
	{
		this(caveRanges, BiomeProfileMode.FIXED);
	}

	public CaveRangeConfig(List<CaveRange> caveRanges, BiomeProfileMode biomeProfileMode)
	{
		this.caveRanges = List.copyOf(caveRanges);
		this.biomeProfileMode = biomeProfileMode;
	}

	public CaveType getCaveTypeAt(double depth)
	{
		if(this.biomeProfileMode != BiomeProfileMode.FIXED)
			return null;
		return this.getFixedCaveTypeAt(depth);
	}

	private CaveType getFixedCaveTypeAt(double depth)
	{
		for(CaveRange caveDepth : this.caveRanges)
			if(caveDepth.contains(depth))
				return SubWildConfig.resolveCaveType(caveDepth.type);
		return null;
	}

	public CaveType getCaveTypeAt(WorldGenLevel world, BlockPos pos, double depth)
	{
		CaveRangeConfig activeConfig = this.getActiveConfig(world, pos);
		if(activeConfig != this)
			return activeConfig == null ? null : activeConfig.getFixedCaveTypeAt(depth);

		return this.getFixedCaveTypeAt(world, pos, depth);
	}

	private CaveType getFixedCaveTypeAt(WorldGenLevel world, BlockPos pos, double depth)
	{
		for(CaveRange caveDepth : this.caveRanges)
			if(caveDepth.contains(depth))
				return SubWildConfig.resolveCaveType(caveDepth.type, world, pos);
		return null;
	}

	public SubWildConfig.ResolvedCaveType getResolvedCaveTypeAt(WorldGenLevel world, BlockPos pos, double depth)
	{
		CaveRangeConfig activeConfig = this.getActiveConfig(world, pos);
		if(activeConfig != this)
			return activeConfig == null ? new SubWildConfig.ResolvedCaveType(null, false) : activeConfig.getFixedResolvedCaveTypeAt(world, pos, depth);

		return this.getFixedResolvedCaveTypeAt(world, pos, depth);
	}

	private SubWildConfig.ResolvedCaveType getFixedResolvedCaveTypeAt(WorldGenLevel world, BlockPos pos, double depth)
	{
		for(int i = 0; i < this.caveRanges.size(); ++i)
		{
			CaveRange caveDepth = this.caveRanges.get(i);
			if(!caveDepth.contains(depth))
				continue;

			SubWildConfig.ResolvedCaveType resolvedType = SubWildConfig.resolveCaveTypeSelection(caveDepth.type, world, pos);
			if(resolvedType.replaceDeepTuff())
				return resolvedType;

			for(int j = i - 1; j >= 0; --j)
			{
				SubWildConfig.ResolvedCaveType inheritedType = SubWildConfig.resolveCaveTypeSelection(this.caveRanges.get(j).type, world, pos);
				if(inheritedType.type() != null && inheritedType.replaceDeepTuff())
					return new SubWildConfig.ResolvedCaveType(inheritedType.type(), true);
			}

			return resolvedType;
		}
		return new SubWildConfig.ResolvedCaveType(null, false);
	}

	public boolean hasEnabledCaveTypes()
	{
		if(this.biomeProfileMode != BiomeProfileMode.FIXED)
			return SubWildSurfaceBiomeResolver.canGenerateAnyProfile(this.biomeProfileMode);

		for(CaveRange caveRange : this.caveRanges)
			if(SubWildConfig.resolveCaveType(caveRange.type) != null)
				return true;
		return SubWildConfig.getConfiguredGlobalReplacement() != null;
	}

	private CaveRangeConfig getActiveConfig(WorldGenLevel world, BlockPos pos)
	{
		if(this.biomeProfileMode == BiomeProfileMode.FIXED)
			return this;
		return SubWildSurfaceBiomeResolver.resolveSurfaceProfile(world, pos, this.biomeProfileMode);
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder
	{
		public final List<CaveRange> caveRanges = Lists.newArrayList();

		public Builder add(CaveType type, double min, double max)
		{
			this.caveRanges.add(new CaveRange(type, min, max));
			return this;
		}

		public CaveRangeConfig build()
		{
			return new CaveRangeConfig(this.caveRanges);
		}
	}

	public enum BiomeProfileMode
	{
		FIXED("fixed"),
		SURFACE_AIR("surface_air"),
		SURFACE_LIQUID("surface_liquid");

		public static final Codec<BiomeProfileMode> CODEC = Codec.STRING.xmap(BiomeProfileMode::fromSerializedName, BiomeProfileMode::serializedName);

		private final String serializedName;

		BiomeProfileMode(String serializedName)
		{
			this.serializedName = serializedName;
		}

		public String serializedName()
		{
			return this.serializedName;
		}

		private static BiomeProfileMode fromSerializedName(String name)
		{
			for(BiomeProfileMode value : values())
				if(value.serializedName.equals(name))
					return value;
			throw new IllegalArgumentException("Unknown cave biome profile mode: " + name);
		}
	}
}
