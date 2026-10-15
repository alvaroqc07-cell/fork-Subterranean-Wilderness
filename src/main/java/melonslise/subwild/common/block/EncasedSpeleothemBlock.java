package melonslise.subwild.common.block;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;

import melonslise.subwild.common.util.SubWildUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EncasedSpeleothemBlock extends SpeleothemBlock implements ITranslucent
{
	private static final MapCodec<EncasedSpeleothemBlock> CODEC = MapCodec.unit((EncasedSpeleothemBlock) null);

	public final Supplier<Block> encased;

	// TODO
	public EncasedSpeleothemBlock(Properties properties, Supplier<Block> encased)
	{
		super(properties.randomTicks().isValidSpawn((state, world, pos, type) -> type == EntityType.POLAR_BEAR));
		this.encased = encased;
	}

	@Override
	protected MapCodec<EncasedSpeleothemBlock> codec()
	{
		return CODEC.unit(this);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx)
	{
		return Shapes.block();
	}

	@Override
	public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState)
	{
		return true;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {return true;}

	public boolean isHot(Level world, BlockPos pos, BlockState state)
	{
		return world.getBrightness(LightLayer.BLOCK, pos) > 11 - state.getLightBlock(world, pos);
	}

	public void melt(Level world, BlockPos pos, BlockState state)
	{
		world.setBlockAndUpdate(pos, SubWildUtil.copyStateProps(state, this.encased.get().defaultBlockState()));
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random)
	{}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random)
	{
		if(this.isHot(world, pos, state))
			this.melt(world, pos, state);
	}

	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack)
	{
		super.playerDestroy(world, player, pos, state, te, stack);
		if(stack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH)) == 0)
			world.setBlockAndUpdate(pos, SubWildUtil.copyStateProps(state, this.encased.get().defaultBlockState()));
	}

	@Override
	public boolean falling()
	{
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean skipRendering(BlockState state, BlockState adjState, Direction side)
	{
		return this.isIce(state) && ITranslucent.isAdjacentIce(adjState) || super.skipRendering(state, adjState, side);
	}

	@Override
	public boolean isIce(BlockState state)
	{
		return !state.canOcclude();
	}
}
