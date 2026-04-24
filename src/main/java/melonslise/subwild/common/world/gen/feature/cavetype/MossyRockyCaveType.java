package melonslise.subwild.common.world.gen.feature.cavetype;

import net.minecraft.util.RandomSource;
import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MossyRockyCaveType extends BasicCaveType
{
	public MossyRockyCaveType(String domain, String path)
	{
		super(domain, path);
	}

	protected Block getRockBlock(WorldGenLevel world, BlockPos pos, INoise noise)
	{
		Block[] palette = this.supportsDeepslateDecor(world, pos) ? RockyCaveType.DEEP_STONE : RockyCaveType.STONE;
		return palette[(int) (this.getClampedNoise(noise, pos, 0.1d) * (double) palette.length)];
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
			if(-0.4d < d && d < 0.7d)
				this.replaceBlock(world, pos, this.supportsDeepslateDecor(world, pos) && this.getNoise(noise, pos, 0.0625d) < 0.2d ? Blocks.MOSS_BLOCK.defaultBlockState() : this.getRockBlock(world, pos, noise).defaultBlockState());
			else if(d < -0.4d)
				this.replaceBlock(world, pos, Blocks.GRAVEL.defaultBlockState());
			if(!this.supportsDeepslateDecor(world, pos) && -0.7d < d && d < 0.3d)
				this.modifyBlock(world, pos, SubWildLookups.MOSSY);
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
			if(SubWildConfig.GENERATE_PUDDLES.get() && rand.nextFloat() < (SubWildConfig.WATER_PUDDLE_GENERATION_CHANCE.get().floatValue() / 100.0f) && this.getNoise(noise, pos, 0.125d) < -0.2d)
				this.genBlock(world, pos, SubWildBlocks.WATER_PUDDLE.get().defaultBlockState());
			else if(rand.nextInt(36) == 0)
				this.genBlock(world, pos, LushCaveType.MUSHROOMS[rand.nextInt(LushCaveType.MUSHROOMS.length)].defaultBlockState());
			final double d = this.getNoise(noise, pos, 0.1d);
			if(SubWildConfig.GENERATE_PATCHES.get() && -0.1d < d && d < 0.4d)
				this.genLayer(world, pos, SubWildBlocks.GRAVEL_PATCH.get().defaultBlockState(), d, -0.1d, 0.4d, 5);
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
			final double d = this.getNoise(noise, pos, 0.125d);
			if(-0.4d < d && d < 0.7d)
				this.replaceBlock(world, pos, this.supportsDeepslateDecor(world, pos) && this.getNoise(noise, pos, 0.0625d) < 0.1d ? Blocks.MOSS_BLOCK.defaultBlockState() : this.getRockBlock(world, pos, noise).defaultBlockState());
			else if(d < -0.4d)
				this.replaceBlock(world, pos, Blocks.GRAVEL.defaultBlockState());
			if(!this.supportsDeepslateDecor(world, pos) && -0.7d < d && d < 0.3d)
				this.modifyBlock(world, pos, SubWildLookups.MOSSY);
			if(rand.nextFloat() < 0.15d)
				this.modifyBlock(world, pos, SubWildLookups.WET);
		}
		super.genCeil(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genCeilExtra(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass == 1 && this.supportsDeepslateDecor(world, pos))
		{
			super.genCeilExtra(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 1)
		{
			if(this.getNoise(noise, pos, 0.125d) < (double) -depth)
				this.genVines(world, pos, Direction.UP, 1);
		}
		super.genCeilExtra(world, noise, pos, depth, pass, rand);
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
			if(-0.4d < d && d < 0.7d)
				this.replaceBlock(world, pos, this.supportsDeepslateDecor(world, pos) && this.getNoise(noise, pos, 0.0625d) < 0.1d ? Blocks.MOSS_BLOCK.defaultBlockState() : this.getRockBlock(world, pos, noise).defaultBlockState());
			else if(d < -0.4d)
				this.replaceBlock(world, pos, Blocks.GRAVEL.defaultBlockState());
			if(!this.supportsDeepslateDecor(world, pos) && -0.7d < d && d < 0.3d)
				this.modifyBlock(world, pos, SubWildLookups.MOSSY);
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
		if(pass == 1)
		{
			double ch = (1f - depth) * 0.3f;
			if(world.getBlockState(pos.below()).isAir())
				ch *= 2f;
			if(rand.nextFloat() < ch)
				this.genVines(world, pos, wallDir, 1 + rand.nextInt((int) (7f - depth * 7f) + 1) + rand.nextInt(2));
		}
		super.genWallExtra(world, noise, pos, wallDir, depth, pass, rand);
	}

	@Override
	public void genFill(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass == 1 && this.supportsDeepslateDecor(world, pos))
			return;
		if(pass == 1)
		{
			if(SubWildConfig.GENERATE_LILYPADS.get() && rand.nextFloat() < (SubWildConfig.MOSSY_ROCKY_LILYPADS_CHANCE.get().floatValue() / 100) && world.getBlockState(pos.below()).getBlock() == Blocks.WATER)
				this.genBlock(world, pos, Blocks.LILY_PAD.defaultBlockState());
		}
	}
}
