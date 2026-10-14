package melonslise.subwild.common.event;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.world.gen.feature.CaveDecoFeature;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = SubWild.ID, bus = EventBusSubscriber.Bus.MOD)
public final class SubWildModEvents
{
	// FIXME Also event for updating the config when the gui for it is added
	@SubscribeEvent
	public static void onSetup(FMLCommonSetupEvent event)
	{
		CaveDecoFeature.yungHack = ModList.get().isLoaded("bettercaves");
		if(CaveDecoFeature.yungHack)
		{
			SubWild.LOGGER.warn("");
			SubWild.LOGGER.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			SubWild.LOGGER.warn("Yung's Better Caves Present! Enabling workaround for wall feature placements!");
			SubWild.LOGGER.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			SubWild.LOGGER.warn("");
		}
	}
}
