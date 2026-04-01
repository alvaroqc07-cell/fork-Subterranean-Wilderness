package melonslise.subwild.common.init;

import com.mojang.serialization.Codec;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.world.gen.biome.SubWildCaveDecoBiomeModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class SubWildBiomeModifiers
{
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
		DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, SubWild.ID);

	public static final RegistryObject<Codec<SubWildCaveDecoBiomeModifier>> CAVE_DECO =
		BIOME_MODIFIER_SERIALIZERS.register("cave_deco", () -> SubWildCaveDecoBiomeModifier.CODEC);

	public static void register()
	{
		BIOME_MODIFIER_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}

