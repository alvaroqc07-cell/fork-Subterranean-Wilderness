package melonslise.subwild.common.event;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.world.gen.feature.CaveDecoFeature;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = SubWild.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class SubWildModEvents
{
	// FIXME Also event for updating the config when the gui for it is added
	@SubscribeEvent
	public static void onSetup(FMLCommonSetupEvent event)
	{
		CaveDecoFeature.yungHack = ModList.get().isLoaded("bettercaves");
		event.enqueueWork(() ->
		{
			ComposterBlock.COMPOSTABLES.put(SubWildBlocks.SHORT_FOXFIRE.get().asItem(), 0.65F);
			ComposterBlock.COMPOSTABLES.put(SubWildBlocks.LONG_FOXFIRE.get().asItem(), 0.65F);
			ComposterBlock.COMPOSTABLES.put(SubWildBlocks.LIGHT_BROWN_ROOTS.get().asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(SubWildBlocks.BROWN_ROOTS.get().asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(SubWildBlocks.WHITE_ROOTS.get().asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(SubWildBlocks.LIGHT_ORANGE_ROOTS.get().asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(SubWildBlocks.DARK_BROWN_ROOTS.get().asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(SubWildBlocks.ORANGE_ROOTS.get().asItem(), 0.3F);
		});
		if(CaveDecoFeature.yungHack)
		{
			SubWild.LOGGER.warn("");
			SubWild.LOGGER.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			SubWild.LOGGER.warn("Yung's Better Caves Present! Enabling workaround for wall feature placements!");
			SubWild.LOGGER.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			SubWild.LOGGER.warn("");
		}
	}

	@SubscribeEvent
	public static void registerCapability(RegisterCapabilitiesEvent event)
	{
		event.register(INoise.class);
	}
}
