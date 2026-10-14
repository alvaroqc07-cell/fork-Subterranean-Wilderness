package melonslise.subwild.common.util;

import java.util.List;

import melonslise.subwild.common.init.SubWildItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public final class SubWildLootHelper
{
	private record OreDrop(Item item, int min, int max) {}

	private SubWildLootHelper() {}

	public static List<ItemStack> getPatchDrops(BlockState state, LootParams.Builder builder, int layers)
	{
		LootParams params = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
		if(hasEnchantment(params, Enchantments.SILK_TOUCH))
			return singleDrop(applyExplosionCondition(params, new ItemStack(state.getBlock())));

		Item pile = switch(BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath())
		{
		case "dirt_patch", "mossy_dirt_patch", "podzol_patch" -> SubWildItems.DIRT_PILE.get();
		case "gravel_patch" -> SubWildItems.GRAVEL_PILE.get();
		case "sand_patch" -> SubWildItems.SAND_PILE.get();
		case "red_sand_patch" -> SubWildItems.RED_SAND_PILE.get();
		default -> null;
		};

		return pile == null ? List.of() : singleDrop(applyExplosionDecay(params, new ItemStack(pile, layers)));
	}

	public static List<ItemStack> getOreDrops(BlockState state, LootParams.Builder builder)
	{
		LootParams params = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
		if(hasEnchantment(params, Enchantments.SILK_TOUCH))
			return singleDrop(applyExplosionCondition(params, new ItemStack(state.getBlock())));

		OreDrop drop = getOreDrop(state);
		if(drop == null)
			return singleDrop(applyExplosionCondition(params, new ItemStack(state.getBlock())));

		int count = randomBetween(params.getLevel(), drop.min, drop.max);
		count = applyOreBonus(params.getLevel(), count, getEnchantmentLevel(params, Enchantments.FORTUNE));
		return singleDrop(applyExplosionDecay(params, new ItemStack(drop.item, count)));
	}

	private static OreDrop getOreDrop(BlockState state)
	{
		String path = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
		if(path.endsWith("_coal_ore"))
			return new OreDrop(Items.COAL, 1, 1);
		if(path.endsWith("_iron_ore"))
			return new OreDrop(Items.RAW_IRON, 1, 1);
		if(path.endsWith("_gold_ore"))
			return new OreDrop(Items.RAW_GOLD, 1, 1);
		if(path.endsWith("_lapis_ore"))
			return new OreDrop(Items.LAPIS_LAZULI, 4, 9);
		if(path.endsWith("_redstone_ore"))
			return new OreDrop(Items.REDSTONE, 4, 5);
		if(path.endsWith("_diamond_ore"))
			return new OreDrop(Items.DIAMOND, 1, 1);
		if(path.endsWith("_emerald_ore"))
			return new OreDrop(Items.EMERALD, 1, 1);
		return null;
	}

	private static boolean hasEnchantment(LootParams params, ResourceKey<Enchantment> key)
	{
		return getEnchantmentLevel(params, key) > 0;
	}

	private static int getEnchantmentLevel(LootParams params, ResourceKey<Enchantment> key)
	{
		ItemStack tool = params.getOptionalParameter(LootContextParams.TOOL);
		if(tool == null || tool.isEmpty())
			return 0;

		Holder<Enchantment> enchantment = params.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
		return tool.getEnchantmentLevel(enchantment);
	}

	private static int randomBetween(ServerLevel level, int min, int max)
	{
		return min == max ? min : min + level.random.nextInt(max - min + 1);
	}

	private static int applyOreBonus(ServerLevel level, int count, int fortuneLevel)
	{
		if(fortuneLevel <= 0)
			return count;

		int bonus = level.random.nextInt(fortuneLevel + 2) - 1;
		if(bonus < 0)
			bonus = 0;

		return count * (bonus + 1);
	}

	private static ItemStack applyExplosionCondition(LootParams params, ItemStack stack)
	{
		Float radius = params.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
		if(radius == null)
			return stack;

		float chance = 1.0F / radius;
		return params.getLevel().random.nextFloat() <= chance ? stack : ItemStack.EMPTY;
	}

	private static ItemStack applyExplosionDecay(LootParams params, ItemStack stack)
	{
		Float radius = params.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
		if(radius == null)
			return stack;

		float chance = 1.0F / radius;
		int kept = 0;
		for(int i = 0; i < stack.getCount(); i++)
		{
			if(params.getLevel().random.nextFloat() <= chance)
				kept++;
		}

		stack.setCount(kept);
		return stack;
	}

	private static List<ItemStack> singleDrop(ItemStack stack)
	{
		return stack.isEmpty() ? List.of() : List.of(stack);
	}
}
