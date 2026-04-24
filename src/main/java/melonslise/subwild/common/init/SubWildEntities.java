package melonslise.subwild.common.init;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.entity.PebbleProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class SubWildEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SubWild.ID);

	public static final RegistryObject<EntityType<PebbleProjectile>> PEBBLE_PROJECTILE = ENTITY_TYPES.register("pebble_projectile",
		() -> EntityType.Builder.<PebbleProjectile>of(PebbleProjectile::new, MobCategory.MISC)
			.sized(0.25f, 0.25f)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build("pebble_projectile"));

	public static void register()
	{
		ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
