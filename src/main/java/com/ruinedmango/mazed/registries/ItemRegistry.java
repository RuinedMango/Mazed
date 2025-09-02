package com.ruinedmango.mazed.registries;

import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.item.MazeRod;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
	private static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(Mazed.MODID);

	public static final DeferredItem<Item> MAZE_ROD = REGISTER.register("maze_rod",
			() -> new MazeRod(new Item.Properties().setId(
					ResourceKey.create(REGISTER.getRegistryKey(), ResourceLocation.parse(Mazed.MODID + ":maze_rod")))));

	public static final DeferredItem<BlockItem> MAZE_PORTAL_ITEM = REGISTER
			.registerSimpleBlockItem(BlockRegistry.MAZE_PORTAL);

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}
}
