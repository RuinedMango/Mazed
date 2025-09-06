package com.ruinedmango.mazed.registries;

import java.util.function.Supplier;

import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.block.entity.MazePortalBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityRegistry {
	private static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister
			.create(Registries.BLOCK_ENTITY_TYPE, Mazed.MODID);

	public static final Supplier<BlockEntityType<MazePortalBlockEntity>> MAZE_PORTAL_ENTITY = REGISTER.register(
			"maze_portal", () -> new BlockEntityType<>((pos, state) -> new MazePortalBlockEntity(pos, state, false),
					BlockRegistry.MAZE_PORTAL.get()));
	public static final Supplier<BlockEntityType<MazePortalBlockEntity>> EXIT_MAZE_PORTAL_ENTITY = REGISTER.register(
			"exit_maze_portal", () -> new BlockEntityType<>((pos, state) -> new MazePortalBlockEntity(pos, state, true),
					BlockRegistry.EXIT_MAZE_PORTAL.get()));

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}
}
