package melonslise.subwild.common.init;

import com.mojang.serialization.MapCodec;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.world.gen.biome.SubWildCaveDecoBiomeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class SubWildBiomeModifiers
{
	public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
		DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, SubWild.ID);

	public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<SubWildCaveDecoBiomeModifier>> CAVE_DECO =
		BIOME_MODIFIER_SERIALIZERS.register("cave_deco", () -> SubWildCaveDecoBiomeModifier.CODEC);

	public static void register(IEventBus modEventBus)
	{
		BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
	}
}
