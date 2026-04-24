package melonslise.subwild.common.world.gen.feature.cavetype;

import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

public class VolcanicCaveType extends BasicCaveType
{
	protected static final double VOLCANIC_BLACKSTONE_BLEND_SCALE = 0.09375d;
	protected static final double VOLCANIC_BLACKSTONE_BLEND_THRESHOLD = 0.10d;

	public VolcanicCaveType(String domain, String path)
	{
		super(domain, path);
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
			else if(d < 0d)
				this.replaceBlock(world, pos, Blocks.BLACKSTONE.defaultBlockState());
			else if(d < 0.6d)
				this.replaceBlock(world, pos, Blocks.BASALT.defaultBlockState());
			this.blendSubtleBlackstone(world, noise, pos, VOLCANIC_BLACKSTONE_BLEND_SCALE, VOLCANIC_BLACKSTONE_BLEND_THRESHOLD);
			if(rand.nextFloat() < 0.2f)
				this.modifyBlock(world, pos, SubWildLookups.MOLTEN);
		}
		super.genFloor(world, noise, pos, depth, pass, rand);
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
			else if(d < 0d)
				this.replaceBlock(world, pos, Blocks.BLACKSTONE.defaultBlockState());
			else if(d < 0.6d)
				this.replaceBlock(world, pos, Blocks.BASALT.defaultBlockState());
			this.blendSubtleBlackstone(world, noise, pos, VOLCANIC_BLACKSTONE_BLEND_SCALE, VOLCANIC_BLACKSTONE_BLEND_THRESHOLD);
			if(d < -0.7d)
				this.modifyBlock(world, pos, SubWildLookups.MOLTEN);
			if(rand.nextFloat() < 0.2f)
				this.modifyBlock(world, pos, SubWildLookups.HOT);
		}
		super.genCeil(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genFloorExtra(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		boolean handled = false;
		if(this.genVolcanicTransitionFloorExtra(world, noise, pos, depth, pass, rand))
			handled = true;
		else if(this.genUnifiedDeepFloorExtra(world, noise, pos, depth, pass, rand))
			handled = true;
		if(handled)
		{
			super.genFloorExtra(world, noise, pos, depth, pass, rand);
			this.tryGenCoalShard(world, pos, rand, VOLCANIC_COAL_SHARD_RARITY);
			return;
		}
		super.genFloorExtra(world, noise, pos, depth, pass, rand);
		if(pass == 1)
			this.tryGenCoalShard(world, pos, rand, VOLCANIC_COAL_SHARD_RARITY);
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
			else if(d < 0d)
				this.replaceBlock(world, pos, Blocks.BLACKSTONE.defaultBlockState());
			else if(d < 0.6d)
				this.replaceBlock(world, pos, Blocks.BASALT.defaultBlockState());
			this.blendSubtleBlackstone(world, noise, pos, VOLCANIC_BLACKSTONE_BLEND_SCALE, VOLCANIC_BLACKSTONE_BLEND_THRESHOLD);
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
