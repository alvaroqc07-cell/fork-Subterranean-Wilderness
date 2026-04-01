package melonslise.subwild.common.init;

import melonslise.subwild.SubWild;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class SubWildTags
{
	public static final TagKey<Block>
		// Expedition
		FOXFIRE = wrapBlock("foxfire"),
		SPELEOTHEMS = wrapBlock("speleothems"),

		// Forge
		TERRACOTTA = wrapBlock("forge", "terracotta");

	public static final TagKey<Item>
		COAL_ORES = wrapItem("coal_ores"),
		IRON_ORES = wrapItem("iron_ores"),
		GOLD_ORES = wrapItem("gold_ores"),
		LAPIS_ORES = wrapItem("lapis_ores"),
		REDSTONE_ORES = wrapItem("redstone_ores"),
		DIAMOND_ORES = wrapItem("diamond_ores"),
		EMERALD_ORES = wrapItem("emerald_ores");

	public static TagKey<Block> wrapBlock(String name)
	{
		return wrapBlock(SubWild.ID, name);
	}

	public static TagKey<Block> wrapBlock(String id, String name)
	{
		return TagKey.create(Registries.BLOCK, new ResourceLocation(id, name));
	}

	public static TagKey<Item> wrapItem(String name)
	{
		return wrapItem(SubWild.ID, name);
	}

	public static TagKey<Item> wrapItem(String id, String name)
	{
		return TagKey.create(Registries.ITEM, new ResourceLocation(id, name));
	}
}
