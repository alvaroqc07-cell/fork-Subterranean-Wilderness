package melonslise.subwild.common.init;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.world.gen.feature.CavePlacement;
import melonslise.subwild.common.world.gen.feature.LiquidCavePlacement;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class SubWildPlacementModifiers
{
	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
		DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, SubWild.ID);

	public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<CavePlacement>> CAVE =
		PLACEMENT_MODIFIERS.register("cave", () -> () -> CavePlacement.CODEC);

	public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<LiquidCavePlacement>> LIQUID_CAVE =
		PLACEMENT_MODIFIERS.register("liquid_cave", () -> () -> LiquidCavePlacement.CODEC);

	public static void register(IEventBus modEventBus)
	{
		PLACEMENT_MODIFIERS.register(modEventBus);
	}
}
