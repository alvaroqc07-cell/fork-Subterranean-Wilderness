package melonslise.subwild.common.world.gen.feature.cavetype;

import net.minecraft.util.RandomSource;
import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

public class MuddyCaveType extends BasicCaveType
{
	public MuddyCaveType(String domain, String path)
	{
		super(domain, path);
		this.defSlab = SubWildBlocks.DIRT_SLAB;
		this.defStairs = SubWildBlocks.DIRT_STAIRS;
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
			if(this.supportsDeepslateDecor(world, pos))
			{
				if(d > 0.4d)
					this.replaceBlock(world, pos, Blocks.CLAY.defaultBlockState());
				else if(d > -0.2d)
					this.replaceBlock(world, pos, Blocks.TUFF.defaultBlockState());
			}
			else if(d > 0.4d)
				this.replaceBlock(world, pos, Blocks.FARMLAND.defaultBlockState());
			else if(d > -0.2d)
				this.replaceBlock(world, pos, (rand.nextBoolean() ? Blocks.COARSE_DIRT : Blocks.DIRT).defaultBlockState());
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
			final double d = this.getNoise(noise, pos, 0.0625d);
			if(d < 0d && SubWildConfig.GENERATE_PUDDLES.get())
				this.genBlock(world, pos, SubWildBlocks.WATER_PUDDLE.get().defaultBlockState());
			else if(this.supportsDeepslateDecor(world, pos) && SubWildConfig.GENERATE_PATCHES.get())
				this.genLayer(world, pos, SubWildBlocks.GRAVEL_PATCH.get().defaultBlockState(), d, 0.3d, 1d, 5);
			else if (SubWildConfig.GENERATE_PATCHES.get())
				this.genLayer(world, pos, SubWildBlocks.DIRT_PATCH.get().defaultBlockState(), d, 0.3d, 1d, 5);
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
			if(this.supportsDeepslateDecor(world, pos) && this.getNoise(noise, pos, 0.125d) > -0.2d)
				this.replaceBlock(world, pos, Blocks.TUFF.defaultBlockState());
			else if(this.getNoise(noise, pos, 0.125d) > -0.2d)
				this.replaceBlock(world, pos, (rand.nextBoolean() ? Blocks.COARSE_DIRT : Blocks.DIRT).defaultBlockState());
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
			if(this.supportsDeepslateDecor(world, pos) && this.getNoise(noise, pos, 0.125d) > -0.2d)
				this.replaceBlock(world, pos, Blocks.TUFF.defaultBlockState());
			else if(this.getNoise(noise, pos, 0.125d) > -0.2d)
				this.replaceBlock(world, pos, (rand.nextBoolean() ? Blocks.COARSE_DIRT : Blocks.DIRT).defaultBlockState());
		}
		super.genWall(world, noise, pos, depth, pass, rand);
	}
}
