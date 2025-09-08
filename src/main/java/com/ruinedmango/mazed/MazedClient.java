package com.ruinedmango.mazed;

import com.ruinedmango.mazed.block.entity.renderer.MazePortalRenderer;
import com.ruinedmango.mazed.entity.client.MazeCrawlerRenderer;
import com.ruinedmango.mazed.entity.client.MazeSpectreRenderer;
import com.ruinedmango.mazed.registries.BlockEntityRegistry;
import com.ruinedmango.mazed.registries.EntityRegistry;

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

@Mod(value = Mazed.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Mazed.MODID, value = Dist.CLIENT)
public class MazedClient {
	public MazedClient(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	@SubscribeEvent
	public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(BlockEntityRegistry.MAZE_PORTAL_ENTITY.get(), MazePortalRenderer::new);
		event.registerBlockEntityRenderer(BlockEntityRegistry.EXIT_MAZE_PORTAL_ENTITY.get(), MazePortalRenderer::new);
	}

	@SubscribeEvent
	static void onClientSetup(FMLClientSetupEvent event) {
		EntityRenderers.register(EntityRegistry.MAZECRAWLER.get(), MazeCrawlerRenderer::new);
		EntityRenderers.register(EntityRegistry.MAZESPECTRE.get(), MazeSpectreRenderer::new);
	}
}
