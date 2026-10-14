package melonslise.subwild.common.block;

import com.mojang.serialization.MapCodec;
import java.util.List;

import melonslise.subwild.common.util.SubWildLootHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EncasedOreBlock extends DropExperienceBlock implements ITranslucent
{
	private static final MapCodec<EncasedOreBlock> CODEC = MapCodec.unit((EncasedOreBlock) null);

	public EncasedOreBlock(Properties properties)
	{
		super(UniformInt.of(0, 0), properties);
	}

	@Override
	public MapCodec<EncasedOreBlock> codec()
	{
		return CODEC.unit(this);
	}

	@Override
	public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState)
	{
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean skipRendering(BlockState state, BlockState adjState, Direction side)
	{
		return ITranslucent.isAdjacentIce(adjState) || super.skipRendering(state, adjState, side);
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
	{
		return SubWildLootHelper.getOreDrops(state, builder);
	}
}
