package melonslise.subwild.common.world.gen.feature;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import melonslise.subwild.common.config.SubWildConfig;
import melonslise.subwild.common.init.SubWildCapabilities;
import melonslise.subwild.common.init.SubWildPlacementModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.util.RandomSource;

public class CavePlacement extends PlacementModifier
{
	public static final Codec<CavePlacement> CODEC = Codec.unit(new CavePlacement());
	protected static final int MIN_SURFACE_COVER = 7;
	protected static final int NEAR_SURFACE_COVER = 5;
	protected static final int FAR_SURFACE_COVER = 3;
	protected static final int SURFACE_COVER_RADIUS = 2;
	protected static final int MAX_NEAR_EXPOSED = 2;
	protected static final int MAX_TOTAL_EXPOSED = 10;

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource rand, BlockPos pos)
	{
		WorldGenLevel level = context.getLevel();
		Level world = context.getLevel().getLevel();
		if(!SubWildConfig.isAllowed(world) || !world.getCapability(SubWildCapabilities.NOISE_CAPABILITY).isPresent())
			return Stream.empty();
		Set<BlockPos> set = new HashSet<>(1024);
		ChunkAccess chunk = level.getChunk(pos);
		StructureManager structureManager = this.getStructureManager(level);
		ChunkPos chPos = chunk.getPos();
		if(SubWildConfig.EXPENSIVE_SCAN.get())
		{
			BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
			int yMin = world.getMinBuildHeight();
			for(int x = 0; x < 16; ++x)
				for(int z = 0; z < 16; ++z)
					for(int y = yMin, yMax = this.getSurfaceY(chunk, x, z); y < yMax; ++y)
						{
							mut.set(chPos.getMinBlockX() + x, y, chPos.getMinBlockZ() + z);
							if(this.isOpenCaveSpace(chunk, mut) && this.isValidGenerationPos(level, chunk, structureManager, mut))
								set.add(mut.immutable());
						}
		}
		if(chunk instanceof ProtoChunk protoChunk)
		{
			CarvingMask mask = protoChunk.getOrCreateCarvingMask(GenerationStep.Carving.AIR);
			mask.stream(chPos)
				.filter(carvedPos -> this.isValidGenerationPos(level, chunk, structureManager, carvedPos))
				.forEach(set::add);
		}
		return set.stream();
	}

	protected boolean isOpenCaveSpace(ChunkAccess chunk, BlockPos pos)
	{
		return chunk.getBlockState(pos).isAir() || SubWildConfig.GENERATE_FEATURES_IN_WATER.get() && chunk.getFluidState(pos).is(FluidTags.WATER);
	}

	protected boolean isValidGenerationPos(WorldGenLevel world, ChunkAccess chunk, StructureManager structureManager, BlockPos pos)
	{
		return this.hasEnoughSurfaceCover(world, chunk, pos) && this.canDecorateAt(chunk, structureManager, pos);
	}

	protected boolean canDecorateAt(ChunkAccess chunk, StructureManager structureManager, BlockPos pos)
	{
		if(SubWildConfig.GENERATE_IN_STRUCTURES.get() || structureManager == null || !chunk.hasAnyStructureReferences())
			return true;
		Registry<Structure> structureRegistry = structureManager.registryAccess().registryOrThrow(Registries.STRUCTURE);
		for(Structure structure : chunk.getAllReferences().keySet())
		{
			if(!structureManager.getStructureWithPieceAt(pos, structure).isValid())
				continue;
			if(!structureRegistry.wrapAsHolder(structure).is(StructureTags.MINESHAFT))
				return false;
		}
		return true;
	}

	protected StructureManager getStructureManager(WorldGenLevel world)
	{
		if(world instanceof WorldGenRegion region)
			return region.getLevel().structureManager().forWorldGenRegion(region);
		return null;
	}

	protected boolean hasEnoughSurfaceCover(WorldGenLevel world, ChunkAccess chunk, BlockPos pos)
	{
		int localX = pos.getX() - chunk.getPos().getMinBlockX();
		int localZ = pos.getZ() - chunk.getPos().getMinBlockZ();
		if(!this.hasEnoughSurfaceCover(chunk, localX, pos.getY(), localZ, MIN_SURFACE_COVER))
			return false;
		// Keep decorations underground while still allowing some generation along ravines and cave mouths.
		int nearExposed = 0;
		int totalExposed = 0;
		for(int xOff = -SURFACE_COVER_RADIUS; xOff <= SURFACE_COVER_RADIUS; ++xOff)
			for(int zOff = -SURFACE_COVER_RADIUS; zOff <= SURFACE_COVER_RADIUS; ++zOff)
			{
				if(xOff == 0 && zOff == 0)
					continue;
				boolean near = Math.abs(xOff) <= 1 && Math.abs(zOff) <= 1;
				int requiredCover = near ? NEAR_SURFACE_COVER : FAR_SURFACE_COVER;
				if(this.hasEnoughSurfaceCover(world, pos.getX() + xOff, pos.getY(), pos.getZ() + zOff, requiredCover))
					continue;
				++totalExposed;
				if(near && ++nearExposed > MAX_NEAR_EXPOSED || totalExposed > MAX_TOTAL_EXPOSED)
					return false;
			}
		return true;
	}

	protected boolean hasEnoughSurfaceCover(ChunkAccess chunk, int x, int y, int z)
	{
		return this.hasEnoughSurfaceCover(chunk, x, y, z, MIN_SURFACE_COVER);
	}

	protected boolean hasEnoughSurfaceCover(WorldGenLevel world, int x, int y, int z)
	{
		return this.hasEnoughSurfaceCover(world, x, y, z, MIN_SURFACE_COVER);
	}

	protected boolean hasEnoughSurfaceCover(ChunkAccess chunk, int x, int y, int z, int requiredCover)
	{
		return y <= this.getSurfaceY(chunk, x, z) - requiredCover;
	}

	protected boolean hasEnoughSurfaceCover(WorldGenLevel world, int x, int y, int z, int requiredCover)
	{
		return y <= this.getSurfaceY(world, x, z) - requiredCover;
	}

	protected int getSurfaceY(ChunkAccess chunk, int x, int z)
	{
		return Math.min(chunk.getHeight(Heightmap.Types.OCEAN_FLOOR, x, z), chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z));
	}

	protected int getSurfaceY(WorldGenLevel world, int x, int z)
	{
		return Math.min(world.getHeight(Heightmap.Types.OCEAN_FLOOR, x, z), world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z));
	}

	@Override
	public PlacementModifierType<?> type()
	{
		return SubWildPlacementModifiers.CAVE.get();
	}
}
