package com.ruinedmango.mazed.registries;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.generator.MazeGenerator;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ChunkGeneratorRegistry {
	private static final DeferredRegister<MapCodec<? extends ChunkGenerator>> REGISTER = DeferredRegister
			.create(Registries.CHUNK_GENERATOR, Mazed.MODID);

	public static final Supplier<MapCodec<MazeGenerator>> MAZE_GENERATOR = REGISTER.register("maze",
			() -> MazeGenerator.MAP_CODEC);

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}
}
