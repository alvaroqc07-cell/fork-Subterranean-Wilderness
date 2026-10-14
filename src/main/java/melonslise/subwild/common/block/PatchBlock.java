package melonslise.subwild.common.block;

import javax.annotation.Nullable;
import java.util.List;

import com.mojang.serialization.MapCodec;

import melonslise.subwild.common.util.SubWildLootHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

// FIXME  add Silk touch pickup (new)? and noDrops in props?

public class PatchBlock extends Block
{
	private static final MapCodec<PatchBlock> CODEC = MapCodec.unit((PatchBlock) null);
	public static final VoxelShape[] SHAPES = new VoxelShape[]
	{
		Shapes.empty(),
		Block.box(0d, 0d, 0d, 16d, 2d, 16d),
		Block.box(0d, 0d, 0d, 16d, 4d, 16d),
		Block.box(0d, 0d, 0d, 16d, 6d, 16d),
		Block.box(0d, 0d, 0d, 16d, 8d, 16d),
		Block.box(0d, 0d, 0d, 16d, 10d, 16d),
		Block.box(0d, 0d, 0d, 16d, 12d, 16d),
		Block.box(0d, 0d, 0d, 16d, 14d, 16d),
		Block.box(0d, 0d, 0d, 16d, 16d, 16d)
	};

	public PatchBlock(Properties props)
	{
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.LAYERS, 1));
	}

	@Override
	protected MapCodec<? extends Block> codec()
	{
		return CODEC.unit(this);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx)
	{
		return SHAPES[state.getValue(BlockStateProperties.LAYERS)];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.LAYERS);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type)
	{
		switch (type)
		{
		case LAND:
			return state.getValue(BlockStateProperties.LAYERS) < 5;
		case WATER:
			return false;
		case AIR:
			return false;
		default:
			return false;
		}
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
		if (state.getBlock() == this)
			return state.setValue(BlockStateProperties.LAYERS, Math.min(8, state.getValue(BlockStateProperties.LAYERS) + 1));
		return super.getStateForPlacement(ctx);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction side, BlockState adjState, LevelAccessor world, BlockPos pos, BlockPos adjPos)
	{
		return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, side, adjState, world, pos, adjPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos)
	{
		BlockState down = world.getBlockState(pos.below());
		Block block = down.getBlock();
		if (block == Blocks.HONEY_BLOCK || block == Blocks.SOUL_SAND)
			return true;
		return block == this && down.getValue(BlockStateProperties.LAYERS) == 8 || Block.isFaceFull(down.getBlockSupportShape(world, pos.below()), Direction.UP);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx)
	{
		int layer = state.getValue(BlockStateProperties.LAYERS);
		if (ctx.getItemInHand().getItem() == this.asItem() && layer < 8)
		{
			if (ctx.replacingClickedOnBlock())
				return ctx.getClickedFace() == Direction.UP;
			return true;
		}
		return layer == 1;
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
	{
		return SubWildLootHelper.getPatchDrops(state, builder, state.getValue(BlockStateProperties.LAYERS));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean skipRendering(BlockState state, BlockState adjState, Direction side)
	{
		return !state.canOcclude() && adjState.getBlock() == this && state.getValue(BlockStateProperties.LAYERS) <= adjState.getValue(BlockStateProperties.LAYERS) || super.skipRendering(state, adjState, side);
	}
}
