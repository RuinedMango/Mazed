package com.ruinedmango.mazed;

import com.ruinedmango.mazed.block.entity.renderer.MazePortalRenderer;
import com.ruinedmango.mazed.entity.MazeCrawlerEntity;
import com.ruinedmango.mazed.entity.client.MazeCrawlerModel;
import com.ruinedmango.mazed.entity.client.MazeCrawlerRenderer;
import com.ruinedmango.mazed.registries.BlockEntityRegistry;
import com.ruinedmango.mazed.registries.EntityRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Mazed.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Mazed.MODID, value = Dist.CLIENT)
public class MazedClient {
	public MazedClient(ModContainer container) {
		// Allows NeoForge to create a config screen for this mod's configs.
		// The config screen is accessed by going to the Mods screen > clicking on your
		// mod > clicking on config.
		// Do not forget to add translations for your config options to the en_us.json
		// file.
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	@SubscribeEvent
	public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(BlockEntityRegistry.MAZE_PORTAL_ENTITY.get(), MazePortalRenderer::new);
		event.registerBlockEntityRenderer(BlockEntityRegistry.EXIT_MAZE_PORTAL_ENTITY.get(), MazePortalRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(MazeCrawlerModel.LAYER_LOCATION, MazeCrawlerModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(EntityRegistry.MAZECRAWLER.get(), MazeCrawlerEntity.createAttributes().build());
	}

	@SubscribeEvent
	static void onClientSetup(FMLClientSetupEvent event) {
		EntityRenderers.register(EntityRegistry.MAZECRAWLER.get(), MazeCrawlerRenderer::new);
		// Some client setup code
		Mazed.LOGGER.info("HELLO FROM CLIENT SETUP");
		Mazed.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
	}
}
