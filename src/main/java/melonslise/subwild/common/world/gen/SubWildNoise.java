package melonslise.subwild.common.world.gen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import melonslise.subwild.common.capability.INoise;
import melonslise.subwild.common.capability.OpenSimplex2FNoise;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;

public final class SubWildNoise
{
	private static final Map<Long, INoise> BY_SEED = new ConcurrentHashMap<>();

	private SubWildNoise() {}

	public static INoise get(Level level)
	{
		if(level instanceof ServerLevel serverLevel)
			return get(serverLevel.getSeed());
		return new OpenSimplex2FNoise();
	}

	public static INoise get(WorldGenLevel level)
	{
		return get(level.getSeed());
	}

	public static INoise get(long seed)
	{
		return BY_SEED.computeIfAbsent(seed, OpenSimplex2FNoise::new);
	}
}
