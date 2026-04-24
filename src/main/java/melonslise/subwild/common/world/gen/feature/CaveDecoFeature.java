package melonslise.subwild.common.world.gen.feature;

import com.mojang.serialization.Codec;

import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildCapabilities;
import melonslise.subwild.common.world.gen.feature.cavetype.CaveType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class CaveDecoFeature extends Feature<CaveRangeConfig>
{
	protected static final int MIN_FLOOR_DECOR_COVER = 9;

	public CaveDecoFeature(Codec<CaveRangeConfig> codec)
	{
		super(codec);
	}

	public static boolean yungHack = false;

	@Override
	public boolean place(FeaturePlaceContext<CaveRangeConfig> featurePlaceContext)
	{
		WorldGenLevel world = featurePlaceContext.level();
		RandomSource rand = featurePlaceContext.random();
		BlockPos pos = featurePlaceContext.origin();
		CaveRangeConfig cfg = featurePlaceContext.config();
		if(!cfg.hasEnabledCaveTypes())
			return false;

		final float depth = depthAt(world, pos);
		if(depth < 0f)
			return false;
		final boolean allowFloorDecor = hasEnoughFloorCover(world, pos);
		SubWildConfig.ResolvedCaveType resolvedType = cfg.getResolvedCaveTypeAt(world, pos, depth);
		CaveType type = resolvedType.type();
		if(type == null)
			return false;
		INoise noise = world.getLevel().getCapability(SubWildCapabilities.NOISE_CAPABILITY).orElse(null);
		BlockPos.MutableBlockPos adjPos = new BlockPos.MutableBlockPos();
		SubWildConfig.beginDeepTuffReplacementScope(resolvedType.replaceDeepTuff());
		try
		{
			for(int pass = 0; pass < type.getPasses(); ++pass)
			{
				for(Direction dir : type.getGenOrder(pass))
				{
					adjPos.set(pos).move(dir);
					if(type.canGenSide(world, adjPos, world.getBlockState(adjPos), depth, pass, dir))
						switch (dir)
						{
						case UP:
							type.genCeil(world, noise, adjPos, depth, pass, rand);
							break;
						case DOWN:
							type.genFloor(world, noise, adjPos, depth, pass, rand);
							break;
						default:
							if(yungHack) // can be shortened to 1 if but this is more readable
							{
								if(dir == Direction.EAST && adjPos.getX() % 16 == 0)
									break;
								if(dir == Direction.SOUTH && adjPos.getZ() % 16 == 0)
									break;
								if(dir == Direction.WEST && (adjPos.getX() + 1) % 16 == 0)
									break;
								if(dir == Direction.NORTH && (adjPos.getZ() + 1) % 16 == 0)
									break;
							}
							type.genWall(world, noise, adjPos, depth, pass, rand);
							break;
						}
					BlockState centerState = world.getBlockState(pos);
					if(type.canGenExtra(world, pos, centerState, adjPos, world.getBlockState(adjPos), depth, pass, dir))
						switch(dir)
						{
						case UP:
							type.genCeilExtra(world, noise, pos, depth, pass, rand);
							break;
						case DOWN:
							if(allowFloorDecor)
								type.genFloorExtra(world, noise, pos, depth, pass, rand);
							break;
						default:
							if(yungHack) // can be shortened to 1 if but this is more readable
							{
								if(dir == Direction.EAST && adjPos.getX() % 16 == 0)
									break;
								if(dir == Direction.SOUTH && adjPos.getZ() % 16 == 0)
									break;
								if(dir == Direction.WEST && (adjPos.getX() + 1) % 16 == 0)
									break;
								if(dir == Direction.NORTH && (adjPos.getZ() + 1) % 16 == 0)
									break;
							}
							type.genWallExtra(world, noise, pos, dir, depth, pass, rand);
							break;
						}
				}
				if(allowFloorDecor && type.canGenFill(world, pos, world.getBlockState(pos), depth, pass))
					type.genFill(world, noise, pos, depth, pass, rand);
			}
		}
		finally
		{
			SubWildConfig.endDeepTuffReplacementScope();
		}
		return true;
	}

	protected static boolean hasEnoughFloorCover(LevelAccessor world, BlockPos pos)
	{
		return getSurfaceY(world, pos) - pos.getY() >= MIN_FLOOR_DECOR_COVER;
	}

	public static float depthAt(LevelAccessor world, BlockPos pos)
	{
		int surfaceY = getSurfaceY(world, pos);
		int minY = world.getMinBuildHeight();
		if(pos.getY() > surfaceY)
			return -1f;
		int range = surfaceY - minY;
		if(range <= 0)
			return -1f;
		float normalized = (float) (pos.getY() - minY) / (float) range;
		return Mth.clamp(1f - normalized, 0f, 1f);
	}

	protected static int getSurfaceY(LevelAccessor world, BlockPos pos)
	{
		return Math.min(world.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX(), pos.getZ()), world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()));
	}
}
