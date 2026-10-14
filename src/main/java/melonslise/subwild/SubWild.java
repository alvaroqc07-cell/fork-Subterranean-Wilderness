package melonslise.subwild;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildBiomeModifiers;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildEntities;
import melonslise.subwild.common.init.SubWildFeatures;
import melonslise.subwild.common.init.SubWildItems;
import melonslise.subwild.common.init.SubWildPlacementModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(SubWild.ID)
public class SubWild
{
	public static final String ID = "subwild";

	public static final Logger LOGGER = LogManager.getLogger();

	public SubWild(IEventBus modEventBus, ModContainer modContainer)
	{
		modContainer.registerConfig(ModConfig.Type.COMMON, SubWildConfig.SPEC);

		SubWildBlocks.register(modEventBus);
		SubWildItems.register(modEventBus);
		SubWildEntities.register(modEventBus);
		SubWildPlacementModifiers.register(modEventBus);
		SubWildFeatures.register(modEventBus);
		SubWildBiomeModifiers.register(modEventBus);
	}
}
