package melonslise.subwild.common.init;

import melonslise.subwild.SubWild;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class SubWildItems
{
	public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(SubWild.ID);

	public static final DeferredHolder<Item, Item>
		DIRT_PILE = add("dirt_pile", () -> new Item(new Item.Properties())),
		GRAVEL_PILE = add("gravel_pile", () -> new Item(new Item.Properties())),
		SAND_PILE = add("sand_pile", () -> new Item(new Item.Properties())),
		RED_SAND_PILE = add("red_sand_pile", () -> new Item(new Item.Properties()));

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SubWild.ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = TABS.register(SubWild.ID, () -> CreativeModeTab.builder()
		.title(Component.translatable("itemGroup." + SubWild.ID))
		.icon(() -> new ItemStack(SubWildBlocks.LONG_FOXFIRE.get()))
		.displayItems((params, output) -> ITEMS.getEntries().forEach(item -> output.accept(item.get())))
		.build());

	public static void register(IEventBus modEventBus)
	{
		ITEMS.register(modEventBus);
		TABS.register(modEventBus);
	}

	public static <T extends Item> DeferredHolder<Item, T> add(String name, Supplier<T> supplier)
	{
		return ITEMS.register(name, supplier);
	}
}
