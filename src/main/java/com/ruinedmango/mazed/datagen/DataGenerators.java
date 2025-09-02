package com.ruinedmango.mazed.datagen;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.ruinedmango.mazed.Mazed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Mazed.MODID)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherClientData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new MazedModelProvider(packOutput, Mazed.MODID));

		generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(), List
				.of(new LootTableProvider.SubProviderEntry(MazedLootTableProvider::new, LootContextParamSets.CHEST)),
				lookupProvider));
	}

	@SubscribeEvent
	public static void gatherServerData(GatherDataEvent.Server event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new MazedModelProvider(packOutput, Mazed.MODID));

		generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(), List
				.of(new LootTableProvider.SubProviderEntry(MazedLootTableProvider::new, LootContextParamSets.CHEST)),
				lookupProvider));
	}
}
