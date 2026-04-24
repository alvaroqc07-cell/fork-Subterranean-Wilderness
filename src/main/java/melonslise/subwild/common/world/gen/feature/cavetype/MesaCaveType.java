package melonslise.subwild.common.world.gen.feature.cavetype;

import net.minecraft.util.RandomSource;
import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.world.gen.BadlandsBands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

public class MesaCaveType extends BasicCaveType
{
	public MesaCaveType(String domain, String path)
	{
		super(domain, path);
		this.floorCh = 0f;
		this.defSpel = SubWildBlocks.RED_SANDSTONE_SPELEOTHEM;
		this.defStairs = () -> Blocks.RED_SANDSTONE_STAIRS;
		this.defSlab = () -> Blocks.RED_SANDSTONE_SLAB;
	}

	@Override
	public void genFloor(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(this.genUnifiedDeepSurface(world, noise, pos, pass))
		{
			super.genFloor(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 0)
		{
			final double d = this.getNoise(noise, pos, 0.125d);
			if(d > 0.2d)
				this.replaceBlock(world, pos, Blocks.RED_SAND.defaultBlockState());
			else
				this.genTerracotta(world, pos);
		}
		super.genFloor(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genFloorExtra(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(this.genUnifiedDeepFloorExtra(world, noise, pos, depth, pass, rand))
		{
			super.genFloorExtra(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 1)
		{
			final double d = this.getNoise(noise, pos, 0.1d);
			if(SubWildConfig.GENERATE_PATCHES.get() && d > -0.5d && d < 0.5d)
				this.genLayer(world, pos, SubWildBlocks.RED_SAND_PATCH.get().defaultBlockState(), d, -0.5d, 0.5d, 5);
			else if(SubWildConfig.GENERATE_DEAD_BUSHES.get() && rand.nextFloat() < (SubWildConfig.MESA_DEAD_BUSHES_CHANCE.get().floatValue() / 100))
				this.genBlock(world, pos, Blocks.DEAD_BUSH.defaultBlockState());
			else if(SubWildConfig.GENERATE_BUTTONS.get() && rand.nextFloat() < (SubWildConfig.RED_SANDSTONE_PEBBLE_CHANCE.get().floatValue() / 100))
				this.genBlock(world, pos, SubWildBlocks.RED_SANDSTONE_PEBBLE.get().defaultBlockState());
		}
		super.genFloorExtra(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genCeil(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(this.genUnifiedDeepSurface(world, noise, pos, pass))
		{
			super.genCeil(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 0)
		{
			this.genTerracotta(world, pos);
		}
		super.genCeil(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genWall(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(this.genUnifiedDeepSurface(world, noise, pos, pass))
		{
			super.genWall(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 0)
		{
			this.genTerracotta(world, pos);
		}
		super.genWall(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genWallExtra(WorldGenLevel world, INoise noise, BlockPos pos, Direction wallDir, float depth, int pass, RandomSource rand) {}

	public void genTerracotta(WorldGenLevel world, BlockPos pos)
	{
		this.replaceBlock(world, pos, BadlandsBands.getBand(world.getSeed(), pos.getX(), pos.getY(), pos.getZ()));
	}

	@Override
	protected boolean supportsDeepslateDecor(WorldGenLevel world, BlockPos pos)
	{
		return super.supportsDeepslateDecor(world, pos);
	}
}
