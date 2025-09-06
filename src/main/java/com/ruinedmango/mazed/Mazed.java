package com.ruinedmango.mazed;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.ruinedmango.mazed.registries.BlockEntityRegistry;
import com.ruinedmango.mazed.registries.BlockRegistry;
import com.ruinedmango.mazed.registries.ChunkGeneratorRegistry;
import com.ruinedmango.mazed.registries.CreativeTabRegistry;
import com.ruinedmango.mazed.registries.EntityRegistry;
import com.ruinedmango.mazed.registries.FeatureRegistry;
import com.ruinedmango.mazed.registries.ItemRegistry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Mazed.MODID)
public class Mazed {
	// Define mod id in a common place for everything to reference
	public static final String MODID = "mazed";
	// Directly reference a slf4j logger
	public static final Logger LOGGER = LogUtils.getLogger();

	public Mazed(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::commonSetup);

		BlockRegistry.register(modEventBus);
		ItemRegistry.register(modEventBus);
		CreativeTabRegistry.register(modEventBus);
		BlockEntityRegistry.register(modEventBus);
		FeatureRegistry.register(modEventBus);
		ChunkGeneratorRegistry.register(modEventBus);
		EntityRegistry.register(modEventBus);

		NeoForge.EVENT_BUS.register(this);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		// Some common setup code
		LOGGER.info("HELLO FROM COMMON SETUP");
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// Do something when the server starts
		LOGGER.info("HELLO from server starting");
	}
}
