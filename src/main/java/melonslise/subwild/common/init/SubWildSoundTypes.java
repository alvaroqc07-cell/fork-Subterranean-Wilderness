package melonslise.subwild.common.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.util.DeferredSoundType;

public final class SubWildSoundTypes
{
	public static final SoundType WATER =
			new DeferredSoundType(1f, 1f, () -> SoundEvents.GENERIC_SPLASH,
					() -> SoundEvents.GENERIC_SWIM, () -> SoundEvents.GENERIC_SPLASH,
					() -> SoundEvents.BOAT_PADDLE_WATER, () -> SoundEvents.GENERIC_SPLASH);
}
