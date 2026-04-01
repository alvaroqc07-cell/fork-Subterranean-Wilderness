package melonslise.subwild.common.world.gen.feature.cavetype;

import net.minecraft.util.RandomSource;
import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

public class SandyVolcanicCaveType extends BasicCaveType
{
	public final boolean red;

	public SandyVolcanicCaveType(String domain, String path, boolean red)
	{
		super(domain, path);
		this.red = red;
		this.ceilCh = 7f;
		this.defSlab = () -> Blocks.BLACKSTONE_SLAB;
		this.defStairs = () -> Blocks.BLACKSTONE_STAIRS;
		this.defSpel = SubWildBlocks.BLACKSTONE_SPELEOTHEM;
	}

	@Override
	public void genFloor(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(this.genVolcanicTransitionSurface(world, noise, pos, pass))
		{
			super.genFloor(world, noise, pos, depth, pass, rand);
			return;
		}
		if(this.genUnifiedDeepSurface(world, noise, pos, pass))
		{
			super.genFloor(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 0)
		{
			final double d = this.getNoise(noise, pos, 0.0625d);
			if(d < -0.85d )
				this.replaceBlock(world, pos, Blocks.MAGMA_BLOCK.defaultBlockState());
			else if(d < -0.1d)
				this.replaceBlock(world, pos, Blocks.BLACKSTONE.defaultBlockState());
			else if(d < 0.4d)
				this.replaceBlock(world, pos, Blocks.BASALT.defaultBlockState());
			else if(d < 0.7d)
				this.replaceBlock(world, pos, (this.red ? Blocks.SMOOTH_RED_SANDSTONE : Blocks.SMOOTH_SANDSTONE).defaultBlockState());
			else
				this.replaceBlock(world, pos, (this.red ? Blocks.RED_SANDSTONE : Blocks.SANDSTONE).defaultBlockState());
			if(rand.nextFloat() < 0.2f)
				this.modifyBlock(world, pos, SubWildLookups.MOLTEN);
		}
		super.genFloor(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genFloorExtra(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(this.genVolcanicTransitionFloorExtra(world, noise, pos, depth, pass, rand))
		{
			super.genFloorExtra(world, noise, pos, depth, pass, rand);
			return;
		}
		if(this.genUnifiedDeepFloorExtra(world, noise, pos, depth, pass, rand))
		{
			super.genFloorExtra(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 1 && SubWildConfig.GENERATE_PATCHES.get())
		{
			final double d = this.getNoise(noise, pos, 0.1d);
			if(d > 0.4d)
				this.genLayer(world, pos, (this.red ? SubWildBlocks.RED_SAND_PATCH : SubWildBlocks.SAND_PATCH).get().defaultBlockState(), d, 0.4d, 1d, 5);
		}
		super.genFloorExtra(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genCeil(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(this.genVolcanicTransitionSurface(world, noise, pos, pass))
		{
			super.genCeil(world, noise, pos, depth, pass, rand);
			return;
		}
		if(this.genUnifiedDeepSurface(world, noise, pos, pass))
		{
			super.genCeil(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 0)
		{
			final double d = this.getNoise(noise, pos, 0.0625d);
			if(d < -0.85d )
				this.replaceBlock(world, pos, Blocks.MAGMA_BLOCK.defaultBlockState());
			else if(d < -0.1d)
				this.replaceBlock(world, pos, Blocks.BLACKSTONE.defaultBlockState());
			else if(d < 0.4d)
				this.replaceBlock(world, pos, Blocks.BASALT.defaultBlockState());
			else if(d < 0.7d)
				this.replaceBlock(world, pos, (this.red ? Blocks.SMOOTH_RED_SANDSTONE : Blocks.SMOOTH_SANDSTONE).defaultBlockState());
			else
				this.replaceBlock(world, pos, (this.red ? Blocks.RED_SANDSTONE : Blocks.SANDSTONE).defaultBlockState());
			if(d < -0.7d)
				this.modifyBlock(world, pos, SubWildLookups.MOLTEN);
			if(rand.nextFloat() < 0.2f)
				this.modifyBlock(world, pos, SubWildLookups.HOT);
		}
		super.genCeil(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genWall(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(this.genVolcanicTransitionSurface(world, noise, pos, pass))
		{
			super.genWall(world, noise, pos, depth, pass, rand);
			return;
		}
		if(this.genUnifiedDeepSurface(world, noise, pos, pass))
		{
			super.genWall(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 0)
		{
			final double d = this.getNoise(noise, pos, 0.0625d);
			if(d < -0.85d )
				this.replaceBlock(world, pos, Blocks.MAGMA_BLOCK.defaultBlockState());
			else if(d < -0.1d)
				this.replaceBlock(world, pos, Blocks.BLACKSTONE.defaultBlockState());
			else if(d < 0.4d)
				this.replaceBlock(world, pos, Blocks.BASALT.defaultBlockState());
			else if(d < 0.7d)
				this.replaceBlock(world, pos, (this.red ? Blocks.SMOOTH_RED_SANDSTONE : Blocks.SMOOTH_SANDSTONE).defaultBlockState());
			else
				this.replaceBlock(world, pos, (this.red ? Blocks.RED_SANDSTONE : Blocks.SANDSTONE).defaultBlockState());
			if(rand.nextFloat() < 0.2f)
				this.modifyBlock(world, pos, SubWildLookups.MOLTEN);
		}
		super.genWall(world, noise, pos, depth, pass, rand);
	}

	@Override
	protected boolean supportsDeepslateDecor(WorldGenLevel world, BlockPos pos)
	{
		return false;
	}

	@Override
	protected boolean usesDeepslateAccessories(WorldGenLevel world, BlockPos pos)
	{
		return false;
	}
}
