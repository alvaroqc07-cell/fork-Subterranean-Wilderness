package melonslise.subwild.common.world.gen.feature;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildCapabilities;
import melonslise.subwild.common.init.SubWildPlacementModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.util.RandomSource;

public class LiquidCavePlacement extends CavePlacement
{
	public static final Codec<LiquidCavePlacement> CODEC = Codec.unit(new LiquidCavePlacement());

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource rand, BlockPos pos)
	{
		WorldGenLevel level = context.getLevel();
		Level world = context.getLevel().getLevel();
		if(!SubWildConfig.isAllowed(world) || !world.getCapability(SubWildCapabilities.NOISE_CAPABILITY).isPresent())
			return Stream.empty();
		ChunkAccess chunk = level.getChunk(pos);
		ChunkPos chPos = chunk.getPos();

		Set<BlockPos> set = null;
		if(chunk instanceof ProtoChunk protoChunk)
		{
			CarvingMask mask = protoChunk.getOrCreateCarvingMask(GenerationStep.Carving.LIQUID);
			if(!SubWildConfig.EXPENSIVE_SCAN.get())
				return mask.stream(chPos).filter(carvedPos -> this.hasEnoughSurfaceCover(level, chunk, carvedPos));
			set = new HashSet<>(1024);
			mask.stream(chPos)
				.filter(carvedPos -> this.hasEnoughSurfaceCover(level, chunk, carvedPos))
				.forEach(set::add);
		}
		else if(!SubWildConfig.EXPENSIVE_SCAN.get())
		{
			return Stream.empty();
		}

		if(set == null)
			set = new HashSet<>(1024);

		BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
		int yMin = world.getMinBuildHeight();
		for(int x = 0; x < 16; ++x)
			for(int z = 0; z < 16; ++z)
				for(int y = yMin, yMax = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR, x, z); y < yMax; ++y)
				{
					mut.set(chPos.getMinBlockX() + x, y, chPos.getMinBlockZ() + z);
					if(chunk.getBlockState(mut).getBlock() == Blocks.WATER && rand.nextInt(4) == 0 && this.hasEnoughSurfaceCover(level, chunk, mut))
						set.add(mut.immutable());
				}

		return set.stream();
	}

	@Override
	public PlacementModifierType<?> type()
	{
		return SubWildPlacementModifiers.LIQUID_CAVE.get();
	}
}
