package com.ruinedmango.mazed.registries;

import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.feature.MazeChestFeature;
import com.ruinedmango.mazed.feature.MazeExitFeature;
import com.ruinedmango.mazed.feature.SuperMazeBarrelFeature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FeatureRegistry {
	private static final DeferredRegister<Feature<?>> REGISTER = DeferredRegister.create(Registries.FEATURE,
			Mazed.MODID);

	public static final DeferredHolder<Feature<?>, MazeChestFeature> MAZE_CHEST_FEATURE = REGISTER
			.register("maze_chest_feature", () -> new MazeChestFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, MazeExitFeature> MAZE_EXIT_FEATURE = REGISTER
			.register("maze_exit_feature", () -> new MazeExitFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, SuperMazeBarrelFeature> SUPER_MAZE_CHEST_FEATURE = REGISTER
			.register("super_maze_chest_feature", () -> new SuperMazeBarrelFeature(NoneFeatureConfiguration.CODEC));

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}
}
