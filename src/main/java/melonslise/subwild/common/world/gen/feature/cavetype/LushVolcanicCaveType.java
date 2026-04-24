package melonslise.subwild.common.world.gen.feature.cavetype;

import net.minecraft.util.RandomSource;
import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LushVolcanicCaveType extends BasicCaveType
{
	protected static final double LUSH_VOLCANIC_BLACKSTONE_BLEND_SCALE = 0.09375d;
	protected static final double LUSH_VOLCANIC_BLACKSTONE_BLEND_THRESHOLD = -0.20d;

	public LushVolcanicCaveType(String domain, String path)
	{
		super(domain, path);
		this.floorCh = 5f;
		this.ceilCh = 15f;
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
			final double d = this.getNoise(noise, pos, 0.125d);
			this.blendSubtleBlackstone(world, noise, pos, LUSH_VOLCANIC_BLACKSTONE_BLEND_SCALE, LUSH_VOLCANIC_BLACKSTONE_BLEND_THRESHOLD);
			if(d < -0.55d )
				this.replaceBlock(world, pos, Blocks.MAGMA_BLOCK.defaultBlockState());
			else if(d < -0.2d)
				this.modifyBlock(world, pos, SubWildLookups.MOLTEN);
			else if(d > 0.4d)
				this.replaceBlock(world, pos, Blocks.DIRT.defaultBlockState());
			else if (d > 0.2d)
				this.modifyBlock(world, pos, SubWildLookups.MOSSY);
		}
		super.genFloor(world, noise, pos, depth, pass, rand);
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
		if(pass == 1)
		{
			if(this.getNoise(noise, pos, 0.2d) > 0.4d && rand.nextFloat() < (SubWildConfig.LUSH_VOLCANIC_LEAVES_CHANCE.get().floatValue() / 100.0f))
				this.genBlock(world, pos, LushCaveType.LEAVES[(int) (this.getClampedNoise(noise, pos, 0.015625d) * (double) LushCaveType.LEAVES.length)].defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true));
			if(this.getNoise(noise, pos, 0.16d) > 0.5d)
				this.genBlock(world, pos, LushCaveType.PLANTS[(int) (this.getClampedNoise(noise, pos, 0.03125d) * (double) LushCaveType.PLANTS.length)].defaultBlockState());
			if(SubWildConfig.GENERATE_SAPLINGS.get() && rand.nextFloat() < (SubWildConfig.LUSH_VOLCANIC_SAPLINGS_CHANCE.get().floatValue() / 100))
				this.genBlock(world, pos, LushCaveType.SAPLINGS[rand.nextInt(LushCaveType.SAPLINGS.length)].defaultBlockState());
			else if(rand.nextInt(45) == 0)
				this.genBlock(world, pos, LushCaveType.MUSHROOMS[rand.nextInt(LushCaveType.MUSHROOMS.length)].defaultBlockState());
		}
		super.genFloorExtra(world, noise, pos, depth, pass, rand);
		if(pass == 1)
			this.tryGenCoalShard(world, pos, rand, VOLCANIC_COAL_SHARD_RARITY);
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
			final double d = this.getNoise(noise, pos, 0.1d);
			this.blendSubtleBlackstone(world, noise, pos, LUSH_VOLCANIC_BLACKSTONE_BLEND_SCALE, LUSH_VOLCANIC_BLACKSTONE_BLEND_THRESHOLD);
			if(d < -0.4d)
				this.modifyBlock(world, pos, SubWildLookups.MOLTEN);
			else if (d > 0.2d)
				this.modifyBlock(world, pos, SubWildLookups.MOSSY);
		}
		super.genCeil(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genCeilExtra(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass == 1 && this.usesVolcanicTransitionSurface(world, noise, pos))
		{
			super.genCeilExtra(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 1 && this.supportsDeepslateDecor(world, pos))
		{
			super.genCeilExtra(world, noise, pos, depth, pass, rand);
			return;
		}
		if(pass == 1)
		{
			if(this.getNoise(noise, pos, 0.125d) < 0.1d)
				this.genVines(world, pos, Direction.UP, 1);
		}
		super.genCeilExtra(world, noise, pos, depth, pass, rand);
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
			final double d = this.getNoise(noise, pos, 0.125d);
			this.blendSubtleBlackstone(world, noise, pos, LUSH_VOLCANIC_BLACKSTONE_BLEND_SCALE, LUSH_VOLCANIC_BLACKSTONE_BLEND_THRESHOLD);
			if(d < -0.5d)
				this.modifyBlock(world, pos, SubWildLookups.MOLTEN);
			else if (d > 0.4d)
				this.modifyBlock(world, pos, SubWildLookups.MOSSY);
		}
		super.genWall(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genWallExtra(WorldGenLevel world, INoise noise, BlockPos pos, Direction wallDir, float depth, int pass, RandomSource rand)
	{
		if(pass == 1 && this.usesVolcanicTransitionSurface(world, noise, pos))
		{
			super.genWallExtra(world, noise, pos, wallDir, depth, pass, rand);
			return;
		}
		if(pass == 1 && this.supportsDeepslateDecor(world, pos))
		{
			super.genWallExtra(world, noise, pos, wallDir, depth, pass, rand);
			return;
		}
		if(pass == 1)
		{
			int len = 3 + rand.nextInt(2);
			float ch = 0.2f;
			if(world.getBlockState(pos.below()).isAir())
			{
				ch += 0.35f;
				len += rand.nextInt(8);
			}
			if(rand.nextFloat() < ch)
				this.genVines(world, pos, wallDir, len);
		}
		super.genWallExtra(world, noise, pos, wallDir, depth, pass, rand);
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
