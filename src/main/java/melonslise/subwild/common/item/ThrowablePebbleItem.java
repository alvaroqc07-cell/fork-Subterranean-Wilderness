package melonslise.subwild.common.item;

import java.util.List;

import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.entity.PebbleProjectile;
import melonslise.subwild.common.init.SubWildBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ThrowablePebbleItem extends BlockItem
{
	private static final int THROW_COOLDOWN_TICKS = 9;

	public ThrowablePebbleItem(Block block, Properties properties)
	{
		super(block, properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if(!SubWildConfig.THROWABLE_PEBBLES.get())
			return InteractionResultHolder.pass(stack);

		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5f,
			0.4f / (level.random.nextFloat() * 0.4f + 0.8f));

		if(!level.isClientSide)
		{
			PebbleProjectile projectile = new PebbleProjectile(level, player);
			projectile.setItem(stack.copyWithCount(1));
			projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.35f, 1.0f);
			level.addFreshEntity(projectile);
		}

		for(Item pebbleItem : pebbleItems())
			player.getCooldowns().addCooldown(pebbleItem, THROW_COOLDOWN_TICKS);

		player.awardStat(Stats.ITEM_USED.get(this));
		if(!player.getAbilities().instabuild)
			stack.shrink(1);

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Player player = context.getPlayer();
		if(player != null && player.isShiftKeyDown() && SubWildConfig.THROWABLE_PEBBLES.get())
			return this.use(context.getLevel(), player, context.getHand()).getResult();
		return super.useOn(context);
	}

	private static List<Item> pebbleItems()
	{
		return List.of(SubWildBlocks.PEBBLE.get().asItem(), SubWildBlocks.SANDSTONE_PEBBLE.get().asItem(), SubWildBlocks.RED_SANDSTONE_PEBBLE.get().asItem());
	}
}
