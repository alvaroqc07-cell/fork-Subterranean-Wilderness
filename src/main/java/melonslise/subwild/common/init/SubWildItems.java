package melonslise.subwild.common.init;

import melonslise.subwild.SubWild;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class SubWildItems
{
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SubWild.ID);

	public static final RegistryObject<Item>
		DIRT_PILE = add("dirt_pile", () -> new Item(new Item.Properties())),
		GRAVEL_PILE = add("gravel_pile", () -> new Item(new Item.Properties())),
		SAND_PILE = add("sand_pile", () -> new Item(new Item.Properties())),
		RED_SAND_PILE = add("red_sand_pile", () -> new Item(new Item.Properties()));

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SubWild.ID);

	public static final RegistryObject<CreativeModeTab> TAB = TABS.register(SubWild.ID, () -> CreativeModeTab.builder()
		.title(Component.translatable("itemGroup." + SubWild.ID))
		.icon(() -> new ItemStack(SubWildBlocks.LONG_FOXFIRE.get()))
		.displayItems((params, output) -> ITEMS.getEntries().forEach(item -> output.accept(item.get())))
		.build());

	public static void register()
	{
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static <T extends Item> RegistryObject<T> add(String name, Supplier<T> supplier)
	{
		return ITEMS.register(name, supplier);
	}
}
