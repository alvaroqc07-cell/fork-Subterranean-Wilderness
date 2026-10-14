package melonslise.subwild.common.init;

import melonslise.subwild.SubWild;
import melonslise.subwild.common.entity.PebbleProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class SubWildEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, SubWild.ID);

	public static final DeferredHolder<EntityType<?>, EntityType<PebbleProjectile>> PEBBLE_PROJECTILE = ENTITY_TYPES.register("pebble_projectile",
		() -> EntityType.Builder.<PebbleProjectile>of(PebbleProjectile::new, MobCategory.MISC)
			.sized(0.25f, 0.25f)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build("pebble_projectile"));

	private SubWildEntities()
	{
	}

	public static void register(IEventBus modEventBus)
	{
		ENTITY_TYPES.register(modEventBus);
	}
}
