package melonslise.subwild.common.world.gen.feature.cavetype;

import java.util.Map;

import net.minecraft.util.RandomSource;
import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.common.Tags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class IcyCaveType extends BasicCaveType
{
	public IcyCaveType(String domain, String path)
	{
		super(domain, path);
		this.defSpel = SubWildBlocks.FROZEN_STONE_SPELEOTHEM;
		this.ceilCh = 6f;
	}

	@Override
	protected Map<Block, Block> getSpeleoLookup()
	{
		return SubWildLookups.FROZEN_SPELEOS;
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
			this.replaceIcyBlock(world, pos, this.getNoise(noise, pos, 0.125d));
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
		if(pass == 1 && SubWildConfig.GENERATE_PATCHES.get())
		{
			final double d = this.getNoise(noise, pos, 0.1d);
			if(d > -0.1d)
				this.genLayer(world, pos, Blocks.SNOW.defaultBlockState(), d, -0.1d, 1d, 7);
			else if(d > -0.7d)
				this.genLayer(world, pos, SubWildBlocks.ICE_PATCH.get().defaultBlockState(), d, -0.7d, -0.1d, 5);
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
			this.replaceIcyBlock(world, pos, this.getNoise(noise, pos, 0.125d));
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
			this.replaceIcyBlock(world, pos, this.getNoise(noise, pos, 0.125d));
		super.genWall(world, noise, pos, depth, pass, rand);
	}

	@Override
	public void genWallExtra(WorldGenLevel world, INoise noise, BlockPos pos, Direction wallDir, float depth, int pass, RandomSource rand) {}

	@Override
	public void genFill(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass == 1 && this.supportsDeepslateDecor(world, pos))
			return;
		if(pass == 1)
		{
			BlockPos down = pos.below();
			if(world.getBlockState(down).getBlock() == Blocks.WATER)
				this.genBlock(world, down, Blocks.ICE.defaultBlockState());
		}
	}

	@Override
	protected boolean supportsDeepslateDecor(WorldGenLevel world, BlockPos pos)
	{
		return super.supportsDeepslateDecor(world, pos);
	}

	private void replaceIcyBlock(WorldGenLevel world, BlockPos pos, double noise)
	{
		if(noise > 0.8d)
			this.replaceBlock(world, pos, Blocks.BLUE_ICE.defaultBlockState());
		else if(noise > 0.6d)
			this.replaceBlock(world, pos, Blocks.PACKED_ICE.defaultBlockState());
		else if(noise > 0d)
			this.replaceBlock(world, pos, Blocks.ICE.defaultBlockState());
		else
			this.replaceBlock(world, pos, Blocks.SNOW_BLOCK.defaultBlockState());
	}
}
