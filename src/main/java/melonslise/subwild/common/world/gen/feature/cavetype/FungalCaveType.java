package melonslise.subwild.common.world.gen.feature.cavetype;

import net.minecraft.util.RandomSource;
import com.google.common.collect.ImmutableSet;

import melonslise.subwild.common.capability.INoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

public class FungalCaveType extends BasicCaveType
{
	public FungalCaveType(String domain, String path)
	{
		super(domain, path);
		this.dirs = ImmutableSet.of(Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP);
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
			if(this.supportsDeepslateDecor(world, pos) && d > 0.3d)
				this.replaceBlock(world, pos, Blocks.CALCITE.defaultBlockState());
			else if(this.supportsDeepslateDecor(world, pos) && d > -0.5d)
				this.replaceBlock(world, pos, Blocks.TUFF.defaultBlockState());
			else if(d > -0.5d)
				this.replaceBlock(world, pos, Blocks.MYCELIUM.defaultBlockState());
			else if( d > 0.8d)
				this.replaceBlock(world, pos, Blocks.DIRT.defaultBlockState());
			else
				this.replaceBlock(world, pos, Blocks.COARSE_DIRT.defaultBlockState());
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
		if(pass == 1 && rand.nextInt(34) == 0)
		{
			int len = -2;
			BlockPos.MutableBlockPos next = new BlockPos.MutableBlockPos().set(pos);
			for(int a = 0; a < 6; ++a)
				if(world.getBlockState(next.move(0, 1, 0)).isAir())
					++len;
			if(len < 1)
				return;
			if(rand.nextBoolean())
				this.genBigBrownShroom(world, pos, 2 + rand.nextInt(len));
			else
				this.genBigRedShroom(world, pos, 1 + rand.nextInt(len));
		}
		else
		{
			if(pass == 1 && rand.nextInt(6) == 0)
				this.genBlock(world, pos, (rand.nextBoolean() ? Blocks.RED_MUSHROOM : Blocks.BROWN_MUSHROOM).defaultBlockState());
			super.genFloorExtra(world, noise, pos, depth, pass, rand);
		}
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
			final double d = this.getNoise(noise, pos, 0.125d);
			if(d > 0d)
				this.replaceBlock(world, pos, Blocks.TERRACOTTA.defaultBlockState());
			else if(d > -0.5d)
				this.replaceBlock(world, pos, Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState());
			else if(this.supportsDeepslateDecor(world, pos) && d > -0.8d)
				this.replaceBlock(world, pos, Blocks.CALCITE.defaultBlockState());
			else if(d > -0.8d)
				this.replaceBlock(world, pos, Blocks.DIRT.defaultBlockState());
			else if(this.supportsDeepslateDecor(world, pos))
				this.replaceBlock(world, pos, Blocks.TUFF.defaultBlockState());
			else
				this.replaceBlock(world, pos, Blocks.COARSE_DIRT.defaultBlockState());
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
			final double d = this.getNoise(noise, pos, 0.125d);
			if(d > 0d)
				this.replaceBlock(world, pos, Blocks.TERRACOTTA.defaultBlockState());
			else if(d > -0.5d)
				this.replaceBlock(world, pos, Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState());
			else if(this.supportsDeepslateDecor(world, pos) && d > -0.8d)
				this.replaceBlock(world, pos, Blocks.CALCITE.defaultBlockState());
			else if(d > -0.8d)
				this.replaceBlock(world, pos, Blocks.DIRT.defaultBlockState());
			else if(this.supportsDeepslateDecor(world, pos))
				this.replaceBlock(world, pos, Blocks.TUFF.defaultBlockState());
			else
				this.replaceBlock(world, pos, Blocks.COARSE_DIRT.defaultBlockState());
		}
		super.genWall(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genWallExtra(WorldGenLevel world, INoise noise, BlockPos pos, Direction wallDir, float depth, int pass, RandomSource rand)
	{
		if(pass == 1 && this.supportsDeepslateDecor(world, pos))
		{
			super.genWallExtra(world, noise, pos, wallDir, depth, pass, rand);
			return;
		}
		/*
		if(rand.nextInt(14) == 0 && !world.getBlockState(pos.up()).isAir() || !world.getBlockState(pos.down()).isAir())
			this.genBlock(world, pos, Blocks.COBWEB.getDefaultState());
		*/
		if(pass == 1)
		{
			int len = 1 + rand.nextInt(3);
			float ch = 0.1f;
			if(world.getBlockState(pos.below()).isAir())
			{
				ch += 0.1f;
				len += rand.nextInt(6);
			}
			if(rand.nextFloat() < ch)
				this.genVines(world, pos, wallDir, len);
		}
		//super.genWallExtra(world, noise, pos, wallDir, depth, rand);
	}
}
