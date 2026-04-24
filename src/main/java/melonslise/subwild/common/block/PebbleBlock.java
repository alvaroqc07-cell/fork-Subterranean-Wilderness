package melonslise.subwild.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PebbleBlock extends Block
{
	private static final VoxelShape SHAPE = Block.box(6d, 0d, 6d, 10d, 1d, 10d);

	public PebbleBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx)
	{
		return SHAPE;
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
		return down.isFaceSturdy(world, pos.below(), Direction.UP);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!world.isClientSide)
		{
			ItemStack stack = new ItemStack(this.asItem());
			if(player.addItem(stack))
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7f + 1.0f) * 2.0f);
			else
				popResource(world, pos, stack);
			world.removeBlock(pos, false);
		}
		return InteractionResult.sidedSuccess(world.isClientSide);
	}
}
