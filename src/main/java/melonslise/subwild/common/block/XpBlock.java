package melonslise.subwild.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class XpBlock extends DropExperienceBlock implements ITranslucent
{
	public XpBlock(Properties properties, int xpMin, int xpMax)
	{
		super(properties, UniformInt.of(xpMin, xpMax));
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
		return this.isIce(state) && ITranslucent.isAdjacentIce(adjState) || super.skipRendering(state, adjState, side);
	}

	@Override
	public boolean isIce(BlockState state)
	{
		return !state.canOcclude();
	}
}
