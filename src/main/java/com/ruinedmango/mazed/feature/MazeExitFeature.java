package com.ruinedmango.mazed.feature;

import com.mojang.serialization.Codec;
import com.ruinedmango.mazed.registries.BlockRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MazeExitFeature extends Feature<NoneFeatureConfiguration> {

	public MazeExitFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos origin = context.origin();

		world.setBlock(origin, BlockRegistry.EXIT_MAZE_PORTAL.get().defaultBlockState(),
				Block.UPDATE_NONE | Block.UPDATE_SUPPRESS_DROPS);
		return true;
	}

}
