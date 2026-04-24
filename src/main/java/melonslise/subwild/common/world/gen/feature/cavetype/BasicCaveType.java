package melonslise.subwild.common.world.gen.feature.cavetype;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;

import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildLookups;
import melonslise.subwild.common.init.SubWildProperties;
import melonslise.subwild.common.init.SubWildTags;
import melonslise.subwild.common.util.SubWildUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.Tags;

public class BasicCaveType extends CaveType
{
	public static final Block[] GENERIC_DEEP = new Block[] { Blocks.TUFF, Blocks.DEEPSLATE };
	protected static final int DEEP_CAVE_START_Y = -15;
	protected static final int DEEP_CAVE_TRANSITION = 4;
	protected static final int DEEP_COAL_SHARD_RARITY = 56;
	protected static final int VOLCANIC_COAL_SHARD_RARITY = 16;
	protected static final double VOLCANIC_CAVE_START_DEPTH = 0.8d;
	protected static final int VOLCANIC_CAVE_TRANSITION = 6;

	public ImmutableSet<Direction> dirs = ImmutableSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
	public float floorCh = 2f, ceilCh = 3f;
	public Supplier<Block>
		defSpel = SubWildBlocks.STONE_SPELEOTHEM,
		defStairs = () -> Blocks.STONE_STAIRS,
		defSlab = () -> Blocks.STONE_SLAB;

	public BasicCaveType(ResourceLocation name)
	{
		super(name);
	}

	public BasicCaveType(String domain, String path)
	{
		super(domain, path);
	}

	@Override
	public void genFloor(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass == 0)
			this.genGenericDeepPalette(world, noise, pos);
	}

	@Override
	public void genFloorExtra(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass == 1)
		{
			BlockState support = world.getBlockState(pos.below());
			if(support.is(BlockTags.PLANKS) || support.is(BlockTags.LOGS) || support.is(BlockTags.WOODEN_SLABS))
			{
				if(SubWildConfig.GENERATE_FOXFIRES.get() && this.getNoise(noise, pos, 0.1d) > 0.6d)
					this.tryGenFoxfire(world, pos, Direction.UP, rand);
			}
			else if(depth > 0d && !support.is(BlockTags.DIRT) && this.getSpelChance(depth, this.floorCh, rand))
				this.genRandSpel(world, pos, this.getSpeleothem(world, pos, support).defaultBlockState().setValue(SubWildProperties.VERTICAL_FACING, Direction.UP), depth, rand);
		}
	}

	@Override
	public void genCeil(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass == 0)
			this.genGenericDeepPalette(world, noise, pos);
	}

	@Override
	public void genCeilExtra(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass == 1)
		{
			BlockState support = world.getBlockState(pos.above());
			if(support.is(BlockTags.PLANKS) || support.is(BlockTags.LOGS) || support.is(BlockTags.WOODEN_SLABS))
			{
				if(SubWildConfig.GENERATE_FOXFIRES.get() && this.getNoise(noise, pos, 0.1d) > 0.6d)
					this.tryGenFoxfire(world, pos, Direction.DOWN, rand);
			}
			else if(!support.is(BlockTags.DIRT))
			{
				if(depth > 0d && this.getSpelChance(depth, this.ceilCh, rand))
					this.genRandSpel(world, pos, this.getSpeleothem(world, pos, support).defaultBlockState().setValue(SubWildProperties.VERTICAL_FACING, Direction.DOWN), depth, rand);
			}
			else if(this.getNoise(noise, pos, 0.125d) > 0.4d)
				this.genRoots(world, noise, pos);
		}
	}

	@Override
	public void genWall(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass == 0)
			this.genGenericDeepPalette(world, noise, pos);
	}

	@Override
	public void genWallExtra(WorldGenLevel world, INoise noise, BlockPos pos, Direction wallDir, float depth, int pass, RandomSource rand)
	{
		if(pass == 1)
		{
			BlockState support = world.getBlockState(pos.relative(wallDir));
			if(support.is(BlockTags.PLANKS) || support.is(BlockTags.LOGS) || support.is(BlockTags.WOODEN_SLABS))
			{
				if(SubWildConfig.GENERATE_FOXFIRES.get() && this.getNoise(noise, pos, 0.1d) > 0.6d)
					this.tryGenFoxfire(world, pos, wallDir.getOpposite(), rand);
			}
			else
				this.genSlope(world, pos, wallDir, rand);
		}
	}

	@Override
	public void genFill(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand) {}

	@Override
	public boolean canGenSide(WorldGenLevel world, BlockPos pos, BlockState state, float depth, int pass, Direction dir)
	{
		return pass == 0 && (state.is(Tags.Blocks.STONE) || state.is(BlockTags.DIRT) || state.is(Tags.Blocks.GRAVEL) || state.getBlock() == Blocks.DEEPSLATE || state.getBlock() == Blocks.COBBLED_DEEPSLATE) || state.is(Tags.Blocks.ORES);
	}

	@Override
	public boolean canGenExtra(WorldGenLevel world, BlockPos pos, BlockState state, BlockPos sidePos, BlockState sideState, float depth, int pass, Direction dir)
	{
		return pass == 1 && this.isOpenExtraSpace(state) && (sideState.is(Tags.Blocks.ORES) || sideState.is(BlockTags.PLANKS) || sideState.is(BlockTags.LOGS) || sideState.is(BlockTags.WOODEN_SLABS) || this.isNatural(world, sidePos, sideState));
	}

	@Override
	public boolean canGenFill(WorldGenLevel world, BlockPos pos, BlockState state, float depth, int pass)
	{
		return pass == 1 && this.isOpenExtraSpace(state);
	}

	protected boolean isOpenExtraSpace(BlockState state)
	{
		return state.isAir() || SubWildConfig.GENERATE_FEATURES_IN_WATER.get() && state.getFluidState().is(FluidTags.WATER);
	}

	@Override
	public Set<Direction> getGenOrder(int pass)
	{
		return this.dirs;
	}

	@Override
	public int getPasses()
	{
		return 2;
	}

	@Override
	public boolean isDeepVariantAt(WorldGenLevel world, BlockPos pos)
	{
		return this.supportsDeepslateDecor(world, pos);
	}

	public boolean getSpelChance(float depth, float baseCh, RandomSource rand)
	{
		return (float) rand.nextInt(100) < baseCh + depth * 3f;
	}

	protected Map<Block, Block> getSpeleoLookup()
	{
		return SubWildLookups.SPELEOS;
	}

	protected boolean supportsDeepslateDecor(WorldGenLevel world, BlockPos pos)
	{
		if(SubWildConfig.shouldReplaceDeepTuffVariants())
			return false;
		return pos.getY() <= DEEP_CAVE_START_Y - this.getDeepslateTransitionOffset(pos);
	}

	protected boolean usesDeepslateAccessories(WorldGenLevel world, BlockPos pos)
	{
		if(SubWildConfig.shouldReplaceDeepTuffVariants())
			return false;
		return pos.getY() <= DEEP_CAVE_START_Y - this.getDeepslateTransitionOffset(pos);
	}

	protected int getDeepslateTransitionOffset(BlockPos pos)
	{
		return Math.floorMod(pos.getX() * 73428767 ^ pos.getZ() * 91227153, DEEP_CAVE_TRANSITION);
	}

	protected void genGenericDeepPalette(WorldGenLevel world, INoise noise, BlockPos pos)
	{
		if(!this.supportsDeepslateDecor(world, pos))
			return;
		Block block = world.getBlockState(pos).getBlock();
		if(this.isDeepPaletteBlock(block))
			return;
		if(block != Blocks.PRISMARINE && !block.defaultBlockState().is(BlockTags.CORAL_BLOCKS) && !block.defaultBlockState().is(BlockTags.ICE) && !block.defaultBlockState().is(Tags.Blocks.SANDSTONE) && !block.defaultBlockState().is(SubWildTags.TERRACOTTA) && !block.defaultBlockState().is(Tags.Blocks.SAND) && !block.defaultBlockState().is(Tags.Blocks.GRAVEL) && !block.defaultBlockState().is(BlockTags.DIRT) && !block.defaultBlockState().is(Tags.Blocks.STONE) && block != Blocks.DEEPSLATE && block != Blocks.COBBLED_DEEPSLATE)
			return;
		this.replaceBlock(world, pos, this.getGenericDeepBlock(noise, pos).defaultBlockState());
	}

	protected boolean isDeepPaletteBlock(Block block)
	{
		return block == Blocks.TUFF || block == Blocks.DEEPSLATE;
	}

	protected Block getGenericDeepBlock(INoise noise, BlockPos pos)
	{
		return GENERIC_DEEP[(int) (this.getClampedNoise(noise, pos, 0.1d) * (double) GENERIC_DEEP.length)];
	}

	protected boolean genUnifiedDeepSurface(WorldGenLevel world, INoise noise, BlockPos pos, int pass)
	{
		if(pass != 0 || !this.supportsDeepslateDecor(world, pos))
			return false;
		this.replaceBlock(world, pos, this.getGenericDeepBlock(noise, pos).defaultBlockState());
		return true;
	}

	protected boolean genUnifiedDeepFloorExtra(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass != 1 || !this.supportsDeepslateDecor(world, pos))
			return false;
		if(SubWildConfig.GENERATE_PATCHES.get())
		{
			final double d = this.getNoise(noise, pos, 0.1d);
			if(d > 0.55d)
				this.genLayer(world, pos, SubWildBlocks.GRAVEL_PATCH.get().defaultBlockState(), d, 0.55d, 1d, 4);
		}
		this.tryGenCoalShard(world, pos, rand, DEEP_COAL_SHARD_RARITY);
		return true;
	}

	protected boolean usesVolcanicTransitionSurface(WorldGenLevel world, INoise noise, BlockPos pos)
	{
		int surfaceY = world.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX(), pos.getZ());
		int minY = world.getMinBuildHeight();
		int range = surfaceY - minY;
		if(range <= 0)
			return false;
		int boundaryY = minY + (int) Math.floor((1d - VOLCANIC_CAVE_START_DEPTH) * (double) range);
		int depthBelowBoundary = boundaryY - pos.getY();
		return 0 <= depthBelowBoundary && depthBelowBoundary < VOLCANIC_CAVE_TRANSITION && this.getClampedNoise(noise, pos, 0.0625d) >= (double) (depthBelowBoundary + 1) / (double) VOLCANIC_CAVE_TRANSITION;
	}

	protected boolean genVolcanicTransitionSurface(WorldGenLevel world, INoise noise, BlockPos pos, int pass)
	{
		if(pass != 0 || !this.usesVolcanicTransitionSurface(world, noise, pos))
			return false;
		this.replaceBlock(world, pos, this.getGenericDeepBlock(noise, pos).defaultBlockState());
		return true;
	}

	protected boolean genVolcanicTransitionFloorExtra(WorldGenLevel world, INoise noise, BlockPos pos, float depth, int pass, RandomSource rand)
	{
		if(pass != 1 || !this.usesVolcanicTransitionSurface(world, noise, pos))
			return false;
		if(SubWildConfig.GENERATE_PATCHES.get())
		{
			final double d = this.getNoise(noise, pos, 0.1d);
			if(d > 0.55d)
				this.genLayer(world, pos, SubWildBlocks.GRAVEL_PATCH.get().defaultBlockState(), d, 0.55d, 1d, 4);
		}
		return true;
	}

	protected void tryGenCoalShard(WorldGenLevel world, BlockPos pos, RandomSource rand, int rarity)
	{
		double chance = SubWildConfig.COAL_SHARD_CHANCE.get() * ((double) DEEP_COAL_SHARD_RARITY / (double) rarity);
		if(world.getBlockState(pos).isAir() && SubWildConfig.GENERATE_BUTTONS.get() && rand.nextFloat() < (float) (Math.min(100.0d, chance) / 100.0d))
			this.genBlock(world, pos, SubWildBlocks.COAL_SHARD.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.from2DDataValue(rand.nextInt(4))));
	}

	protected void blendSubtleBlackstone(WorldGenLevel world, INoise noise, BlockPos pos, double scale, double threshold)
	{
		BlockState state = world.getBlockState(pos);
		if(!this.isSubtleBlackstoneTarget(state))
			return;
		if(this.getNoise(noise, pos, scale) >= threshold)
			this.replaceBlock(world, pos, Blocks.BLACKSTONE.defaultBlockState());
	}

	protected boolean isSubtleBlackstoneTarget(BlockState state)
	{
		Block block = state.getBlock();
		return state.is(Tags.Blocks.STONE) || state.is(Tags.Blocks.SANDSTONE) || block == Blocks.DEEPSLATE || block == Blocks.COBBLED_DEEPSLATE || block == Blocks.TUFF || block == Blocks.CALCITE || block == Blocks.BASALT;
	}

	protected Block getSpeleothem(WorldGenLevel world, BlockPos pos, BlockState support)
	{
		if(this.usesDeepslateAccessories(world, pos))
			return SubWildBlocks.DEEPSLATE_SPELEOTHEM.get();
		Block block = support.getBlock();
		if(block == Blocks.DEEPSLATE || block == Blocks.COBBLED_DEEPSLATE)
			return this.defSpel.get();
		return this.getSpeleoLookup().getOrDefault(block, this.defSpel.get());
	}

	protected Block getStairs(WorldGenLevel world, BlockPos pos, BlockState support)
	{
		if(this.usesDeepslateAccessories(world, pos))
			return SubWildBlocks.DEEPSLATE_STAIRS.get();
		Block block = support.getBlock();
		if(block == Blocks.DEEPSLATE || block == Blocks.COBBLED_DEEPSLATE)
			return this.defStairs.get();
		return SubWildLookups.STAIRS.getOrDefault(block, this.defStairs.get());
	}

	protected Block getSlab(WorldGenLevel world, BlockPos pos, BlockState support)
	{
		if(this.usesDeepslateAccessories(world, pos))
			return SubWildBlocks.DEEPSLATE_SLAB.get();
		Block block = support.getBlock();
		if(block == Blocks.DEEPSLATE || block == Blocks.COBBLED_DEEPSLATE)
			return this.defSlab.get();
		return SubWildLookups.SLABS.getOrDefault(block, this.defSlab.get());
	}

	public void genRandSpel(WorldGenLevel world, BlockPos pos, BlockState state, float depth, RandomSource rand)
	{
		double chance = SubWildConfig.getSpeleothemGenerationChance(state.getBlock());
		if(SubWildConfig.GENERATE_SPELEOTHEMS.get() && chance > 0.0d && rand.nextFloat() < (float) (chance / 100.0d))
			this.genSpel(world, pos, state, 1 + rand.nextInt(2) + rand.nextInt((int) (depth * 10f) + 1));
	}

	private void tryGenFoxfire(WorldGenLevel world, BlockPos pos, Direction facing, RandomSource rand)
	{
		Block foxfire = this.selectFoxfireVariant(rand);
		if(foxfire != null)
			this.genBlock(world, pos, foxfire.defaultBlockState().setValue(BlockStateProperties.FACING, facing).setValue(SubWildProperties.GLOWING, true));
	}

	private Block selectFoxfireVariant(RandomSource rand)
	{
		boolean shortFoxfire = rand.nextFloat() < (SubWildConfig.SHORT_FOXFIRE_GENERATION_CHANCE.get().floatValue() / 100.0f);
		boolean longFoxfire = rand.nextFloat() < (SubWildConfig.LONG_FOXFIRE_GENERATION_CHANCE.get().floatValue() / 100.0f);
		if(shortFoxfire && longFoxfire)
			return rand.nextBoolean() ? SubWildBlocks.SHORT_FOXFIRE.get() : SubWildBlocks.LONG_FOXFIRE.get();
		if(shortFoxfire)
			return SubWildBlocks.SHORT_FOXFIRE.get();
		if(longFoxfire)
			return SubWildBlocks.LONG_FOXFIRE.get();
		return null;
	}

	public void genSlope(WorldGenLevel world, BlockPos pos, Direction wallDir, RandomSource rand)
	{
		BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos().set(pos);
		BlockState support = world.getBlockState(mutPos.set(pos).move(wallDir));
		final boolean isDown = this.isNatural(world, mutPos.set(pos).move(0, -1, 0), world.getBlockState(mutPos));
		final boolean isUp = this.isNatural(world, mutPos.set(pos).move(0, 1, 0), world.getBlockState(mutPos));
		if(!isDown && !isUp)
			return;
		mutPos.set(pos);
		int air = 0;
		Direction oppDir = wallDir.getOpposite();
		while(air < 16 && !world.getBlockState(mutPos.move(oppDir)).isFaceSturdy(world, mutPos, wallDir))
			++air;
		int chance = SubWildConfig.SLOPE_CHANCE.get();
		if(air <= SubWildConfig.SLOPE_THRESHOLD.get())
			chance = SubWildConfig.SLOPE_THRESHOLD_CHANCE.get();
		if(rand.nextInt(10) >= chance)
			return;
		if(SubWildConfig.GENERATE_STAIRS.get() && rand.nextInt(5) <= 2)
			this.genBlock(world, pos, SubWildUtil.waterlog(this.getStairs(world, pos, support).defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, wallDir).setValue(BlockStateProperties.HALF, isDown ? Half.BOTTOM : Half.TOP), world, pos));
		else if(SubWildConfig.GENERATE_SLABS.get())
			this.genBlock(world, pos, SubWildUtil.waterlog(this.getSlab(world, pos, support).defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, isDown ? SlabType.BOTTOM : SlabType.TOP), world, pos));
	}
}
