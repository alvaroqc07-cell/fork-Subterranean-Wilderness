package melonslise.subwild.common.event;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.init.SubWildCapabilities;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildLookups;
import melonslise.subwild.common.util.SubWildUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SubWild.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SubWildForgeEvents
{
	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event)
	{
		SubWildLookups.init();
	}

	@SubscribeEvent
	public static void attachCapabilitiesToWorld(AttachCapabilitiesEvent<Level> event)
	{
		SubWildCapabilities.attachToWorld(event);
	}

	@SubscribeEvent
	public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event)
	{
		if(event.getItemStack().is(SubWildBlocks.COAL_SHARD.get().asItem()))
			event.setBurnTime(400);
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		ItemStack stack = event.getItemStack();
		if(!stack.is(Items.SHEARS))
			return;

		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		Block cleanBlock = SubWildLookups.DEMOSSY.get(state.getBlock());
		if(cleanBlock == null && SubWildConfig.SHEAR_VANILLA_MOSSY_BLOCKS.get())
			cleanBlock = SubWildLookups.VANILLA_DEMOSSY.get(state.getBlock());
		if(cleanBlock == null)
			return;

		event.setCanceled(true);
		event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
		if(level.isClientSide)
			return;

		Player player = event.getEntity();
		level.setBlock(pos, SubWildUtil.copyStateProps(state, cleanBlock.defaultBlockState()), 11);
		level.playSound(null, pos, Blocks.VINE.defaultBlockState().getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
		ItemEntity vineDrop = new ItemEntity(level, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, new ItemStack(Items.VINE));
		vineDrop.setDeltaMovement(0.0d, 0.0d, 0.0d);
		level.addFreshEntity(vineDrop);
		level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

		if(!player.getAbilities().instabuild)
			stack.hurtAndBreak(1, player, brokenPlayer -> brokenPlayer.broadcastBreakEvent(event.getHand()));
	}
}
