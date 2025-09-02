package com.ruinedmango.mazed.registries;

import com.ruinedmango.mazed.Mazed;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabRegistry {
	private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, Mazed.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = REGISTER.register("example_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.mazed")) // The language key for the
																								// title of your
																								// CreativeModeTab
					.withTabsBefore(CreativeModeTabs.COMBAT)
					.icon(() -> ItemRegistry.MAZE_ROD.get().getDefaultInstance()).displayItems((parameters, output) -> {
						output.accept(ItemRegistry.MAZE_ROD.get());
					}).build());

	private static void addCreative(BuildCreativeModeTabContentsEvent event) {
		/*
		 * if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
		 * event.accept(ItemRegistry.EXAMPLE_BLOCK_ITEM); }
		 */
	}

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
		eventBus.addListener(CreativeTabRegistry::addCreative);
	}
}
