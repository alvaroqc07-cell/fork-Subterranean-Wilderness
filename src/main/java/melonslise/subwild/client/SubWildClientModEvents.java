package melonslise.subwild.client;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.init.SubWildBlocks;
import melonslise.subwild.common.init.SubWildEntities;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SubWild.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SubWildClientModEvents
{
	private static final int FALLBACK_WATER_COLOR = 0x3F76E4;

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->
		{
			ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new SubWildConfigScreen(screen)));

			EntityRenderers.register(SubWildEntities.PEBBLE_PROJECTILE.get(), ThrownItemRenderer::new);

			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.SHORT_FOXFIRE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.LONG_FOXFIRE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.LIGHT_BROWN_ROOTS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.BROWN_ROOTS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.WHITE_ROOTS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.LIGHT_ORANGE_ROOTS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.ORANGE_ROOTS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.DARK_BROWN_ROOTS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.COAL_SHARD.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_STONE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_GRANITE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_DIORITE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_ANDESITE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_SANDSTONE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_SMOOTH_SANDSTONE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_RED_SANDSTONE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_SMOOTH_RED_SANDSTONE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_OBSIDIAN.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_BLACKSTONE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.MOLTEN_BASALT.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.FROZEN_STONE_SPELEOTHEM.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.FROZEN_GRANITE_SPELEOTHEM.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.FROZEN_DIORITE_SPELEOTHEM.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.FROZEN_ANDESITE_SPELEOTHEM.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.FROZEN_SANDSTONE_SPELEOTHEM.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.FROZEN_RED_SANDSTONE_SPELEOTHEM.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.FROZEN_OBSIDIAN_SPELEOTHEM.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.FROZEN_BLACKSTONE_SPELEOTHEM.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.FROZEN_BASALT_SPELEOTHEM.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.ICE_COAL_ORE.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.ICE_IRON_ORE.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.ICE_GOLD_ORE.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.ICE_LAPIS_ORE.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.ICE_REDSTONE_ORE.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.ICE_DIAMOND_ORE.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.ICE_EMERALD_ORE.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SubWildBlocks.WATER_PUDDLE.get(), RenderType.translucent());
		});
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event)
	{
		event.register((state, level, pos, tintIndex) ->
		{
			if(tintIndex != 0)
				return 0xFFFFFFFF;
			if(level == null || pos == null)
				return FALLBACK_WATER_COLOR;
			return BiomeColors.getAverageWaterColor(level, pos);
		}, SubWildBlocks.WATER_PUDDLE.get());
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event)
	{
		event.register((stack, tintIndex) ->
		{
			if(tintIndex != 0)
				return 0xFFFFFFFF;
			return FALLBACK_WATER_COLOR;
		}, SubWildBlocks.WATER_PUDDLE.get().asItem());
	}
}
