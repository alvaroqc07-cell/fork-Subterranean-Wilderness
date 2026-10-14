package melonslise.subwild.common.block;

import java.util.List;

import com.mojang.serialization.MapCodec;

import melonslise.subwild.common.util.SubWildLootHelper;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

public class SubWildOreBlock extends DropExperienceBlock
{
	private static final MapCodec<SubWildOreBlock> CODEC = MapCodec.unit((SubWildOreBlock) null);

	public SubWildOreBlock(Properties properties)
	{
		super(UniformInt.of(0, 0), properties);
	}

	@Override
	public MapCodec<SubWildOreBlock> codec()
	{
		return CODEC.unit(this);
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
	{
		return SubWildLootHelper.getOreDrops(state, builder);
	}
}
