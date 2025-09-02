package com.ruinedmango.mazed.feature;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class MazeChestFeature extends Feature<NoneFeatureConfiguration> {

	public MazeChestFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		// inside your Feature.generate(FeatureContext<NoneFeatureConfig> context)
		// method
		WorldGenLevel world = context.level();
		RandomSource random = context.random(); // or context.getRandom()
		BlockPos chestPos = context.origin();

		// 1) write the block into the worldgen level
		BlockState chestState = Blocks.CHEST.defaultBlockState();
		world.setBlock(chestPos, chestState, 3);

		RandomizableContainer.setBlockEntityLootTable(world, random, chestPos, BuiltInLootTables.ABANDONED_MINESHAFT);
		return true;
	}

}
