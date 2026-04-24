package melonslise.subwild.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoalShardBlock extends PebbleBlock
{
	private static final int[][] PIXELS = new int[][] {
		{ 7, 5 }, { 8, 5 },
		{ 6, 6 }, { 7, 6 }, { 8, 6 }, { 9, 6 },
		{ 6, 7 }, { 7, 7 }, { 8, 7 }, { 9, 7 }, { 10, 7 },
		{ 5, 8 }, { 6, 8 }, { 7, 8 }, { 8, 8 }, { 9, 8 }, { 10, 8 },
		{ 5, 9 }, { 6, 9 }, { 7, 9 }, { 8, 9 }, { 9, 9 }, { 10, 9 },
		{ 6, 10 }, { 7, 10 }, { 8, 10 }, { 9, 10 }
	};
	private static final VoxelShape SHAPE_NORTH = createShape(Direction.NORTH);
	private static final VoxelShape SHAPE_EAST = createShape(Direction.EAST);
	private static final VoxelShape SHAPE_SOUTH = createShape(Direction.SOUTH);
	private static final VoxelShape SHAPE_WEST = createShape(Direction.WEST);

	public CoalShardBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx)
	{
		switch(state.getValue(BlockStateProperties.HORIZONTAL_FACING))
		{
		case EAST:
			return SHAPE_EAST;
		case SOUTH:
			return SHAPE_SOUTH;
		case WEST:
			return SHAPE_WEST;
		case NORTH:
		default:
			return SHAPE_NORTH;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation)
	{
		return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror)
	{
		return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	private static VoxelShape createShape(Direction facing)
	{
		VoxelShape shape = Shapes.empty();
		for(int[] pixel : PIXELS)
		{
			int x = pixel[0];
			int z = pixel[1];
			switch(facing)
			{
			case EAST:
				shape = Shapes.or(shape, Block.box(16d - (z + 1), 0d, x, 16d - z, 1d, x + 1d));
				break;
			case SOUTH:
				shape = Shapes.or(shape, Block.box(16d - (x + 1), 0d, 16d - (z + 1), 16d - x, 1d, 16d - z));
				break;
			case WEST:
				shape = Shapes.or(shape, Block.box(z, 0d, 16d - (x + 1), z + 1d, 1d, 16d - x));
				break;
			case NORTH:
			default:
				shape = Shapes.or(shape, Block.box(x, 0d, z, x + 1d, 1d, z + 1d));
				break;
			}
		}
		return shape;
	}
}
