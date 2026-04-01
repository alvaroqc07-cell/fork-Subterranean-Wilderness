package melonslise.subwild.common.world.gen;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class BadlandsBands
{
	private static final BlockState[] BANDS = new BlockState[]
	{
		Blocks.TERRACOTTA.defaultBlockState(),
		Blocks.ORANGE_TERRACOTTA.defaultBlockState(),
		Blocks.YELLOW_TERRACOTTA.defaultBlockState(),
		Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState(),
		Blocks.WHITE_TERRACOTTA.defaultBlockState(),
		Blocks.BROWN_TERRACOTTA.defaultBlockState(),
		Blocks.RED_TERRACOTTA.defaultBlockState()
	};

	private BadlandsBands() {}

	public static BlockState getBand(long seed, int x, int y, int z)
	{
		long hash = seed;
		hash ^= (long) x * 341873128712L;
		hash ^= (long) z * 132897987541L;
		hash ^= (long) y * 31L;
		hash = (hash ^ (hash >> 30)) * 0xbf58476d1ce4e5b9L;
		hash = (hash ^ (hash >> 27)) * 0x94d049bb133111ebL;
		hash ^= (hash >> 31);
		int idx = (int) (hash & 0x7fffffff) % BANDS.length;
		return BANDS[idx];
	}
}

