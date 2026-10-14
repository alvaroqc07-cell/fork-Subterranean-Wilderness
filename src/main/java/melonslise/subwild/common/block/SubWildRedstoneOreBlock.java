package melonslise.subwild.common.block;

import java.util.List;

import melonslise.subwild.common.util.SubWildLootHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

public class SubWildRedstoneOreBlock extends RedStoneOreBlock
{
	public SubWildRedstoneOreBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
	{
		return SubWildLootHelper.getOreDrops(state, builder);
	}
}
