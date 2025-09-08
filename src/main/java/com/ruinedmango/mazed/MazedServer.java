package com.ruinedmango.mazed;

import com.ruinedmango.mazed.entity.MazeCrawlerEntity;
import com.ruinedmango.mazed.entity.MazeSpectreEntity;
import com.ruinedmango.mazed.entity.client.MazeCrawlerModel;
import com.ruinedmango.mazed.entity.client.MazeSpectreModel;
import com.ruinedmango.mazed.registries.EntityRegistry;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = Mazed.MODID)
public class MazedServer {
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(MazeCrawlerModel.LAYER_LOCATION, MazeCrawlerModel::createBodyLayer);
		event.registerLayerDefinition(MazeSpectreModel.LAYER_LOCATION, MazeSpectreModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(EntityRegistry.MAZECRAWLER.get(), MazeCrawlerEntity.createAttributes().build());
		event.put(EntityRegistry.MAZESPECTRE.get(), MazeSpectreEntity.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		event.register(EntityRegistry.MAZECRAWLER.get(), SpawnPlacementTypes.NO_RESTRICTIONS,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
				RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(EntityRegistry.MAZESPECTRE.get(), SpawnPlacementTypes.NO_RESTRICTIONS,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
				RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}
}
