package com.ruinedmango.mazed.generator;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ruinedmango.mazed.registries.FeatureRegistry;
import com.ruinedmango.mazed.utils.MazeMaker;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MazeGenerator extends ChunkGenerator {
	// Define a Codec for this generator (no extra fields).
	public static final MapCodec<MazeGenerator> MAP_CODEC = RecordCodecBuilder.mapCodec(
			inst -> inst.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(MazeGenerator::getBiomeSource))
					.apply(inst, MazeGenerator::new));

	private final BiomeSource biomeSource;

	public BiomeSource getBiomeSource() {
		return this.biomeSource;
	}

	// Constructor: call super with a BiomeSource (required).
	public MazeGenerator(BiomeSource biomeSource) {
		super(biomeSource);
		this.biomeSource = biomeSource;
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return MAP_CODEC;
	}

	// Example: override fillFromNoise to carve a simple maze.
	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState,
			StructureManager structureAccessor, ChunkAccess chunk) {

		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getSeaLevel() {
		return getMinY() + 64;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getGenDepth() {
		return 128;
	} // match dimension height

	@Override
	public int getSpawnHeight(LevelHeightAccessor accessor) {
		return getSeaLevel();
	}

	// Other methods (buildSurface, carve, etc.) can be left default/no-op for
	// simplicity.

	@Override
	public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager,
			StructureManager structureManager, ChunkAccess chunk) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		MazeMaker maze = new MazeMaker(seed, 4);
		Random r = new Random();
		int minY = this.getMinY();
		int maxY = this.getGenDepth();
		int chunkX = chunk.getPos().x; // chunk coordinate
		int chunkZ = chunk.getPos().z;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int wx = (chunkX << 4) + x; // world X
				int wz = (chunkZ << 4) + z; // world Z
				for (int y = minY; y < maxY; y++) {
					int rand = r.nextInt(6);
					BlockState brick = (rand <= 4) ? Blocks.STONE_BRICKS.defaultBlockState()
							: Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
					BlockState state = (!maze.isWall(wx, y, wz)) ? Blocks.AIR.defaultBlockState() : brick;

					chunk.setBlockState(pos.set(x, y, z), state, Block.UPDATE_NONE | Block.UPDATE_SUPPRESS_DROPS);
				}
				// Bedrock layer at bottom:
				chunk.setBlockState(pos.set(x, minY, z), Blocks.BEDROCK.defaultBlockState(),
						Block.UPDATE_NONE | Block.UPDATE_SUPPRESS_DROPS);
			}
		}

	}

	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
		// Get chunk coordinates
		int chunkX = chunk.getPos().x;
		int chunkZ = chunk.getPos().z;

		ChunkPos chunkPos = chunk.getPos();
		BlockPos posForRandom = new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ());
		RandomSource random = level.getRandom().forkPositional().at(posForRandom);

		// Your maze generation logic
		MazeMaker maze = new MazeMaker(level.getSeed(), 4);

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int worldX = (chunkX << 4) + x;
				int worldZ = (chunkZ << 4) + z;

				for (int y = this.getMinY(); y < this.getGenDepth(); y++) {
					if (maze.shouldSpawnChest(worldX, y, worldZ)) {
						int bx = Math.floorMod(worldX, maze.getCellSize());
						int bz = Math.floorMod(worldZ, maze.getCellSize());
						int by = Math.floorMod(y, maze.getCellSize());

						if (bx == maze.getCellSize() / 2 && bz == maze.getCellSize() / 2
								&& by == maze.getCellSize() / 2) {

							BlockPos chestPos = new BlockPos(worldX, y - 1, worldZ);
							BlockState belowState = chunk.getBlockState(chestPos.below());
							if (!belowState.isAir() && belowState.isSolidRender()) {
								// Create feature placement context
								FeaturePlaceContext<NoneFeatureConfiguration> context = new FeaturePlaceContext<>(
										Optional.empty(), level, this, random, chestPos,
										NoneFeatureConfiguration.INSTANCE);

								// Place the feature
								FeatureRegistry.MAZE_CHEST_FEATURE.get().place(context);
							}
						}
					} else if (maze.shouldSpawnSuperChest(worldX, y, worldZ)) {
						int bx = Math.floorMod(worldX, maze.getCellSize());
						int bz = Math.floorMod(worldZ, maze.getCellSize());
						int by = Math.floorMod(y, maze.getCellSize());

						if (bx == maze.getCellSize() / 2 && bz == maze.getCellSize() / 2
								&& by == maze.getCellSize() / 2) {

							BlockPos chestPos = new BlockPos(worldX, y - 1, worldZ);
							BlockState belowState = chunk.getBlockState(chestPos.below());
							if (!belowState.isAir() && belowState.isSolidRender()) {
								// Create feature placement context
								FeaturePlaceContext<NoneFeatureConfiguration> context = new FeaturePlaceContext<>(
										Optional.empty(), level, this, random, chestPos,
										NoneFeatureConfiguration.INSTANCE);

								// Place the feature
								FeatureRegistry.SUPER_MAZE_CHEST_FEATURE.get().place(context);
							}
						}
					}
				}
				FeaturePlaceContext<NoneFeatureConfiguration> context = new FeaturePlaceContext<>(Optional.empty(),
						level, this, random, new BlockPos(worldX, (this.getGenDepth() - 1), worldZ),
						NoneFeatureConfiguration.INSTANCE);
				FeatureRegistry.MAZE_EXIT_FEATURE.get().place(context);
			}
		}
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion level) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBaseHeight(int x, int z, Types type, LevelHeightAccessor level, RandomState random) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random,
			ChunkAccess chunk) {
		// TODO Auto-generated method stub

	}
}
