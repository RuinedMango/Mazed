package com.ruinedmango.mazed.registries;

import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.block.MazePortalBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {
	private static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(Mazed.MODID);

	public static final DeferredBlock<Block> MAZE_PORTAL = REGISTER
			.register("maze_portal",
					() -> new MazePortalBlock(Properties.ofFullCopy(Blocks.END_PORTAL).setId(
							ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(Mazed.MODID + ":maze_portal"))),
							false));
	public static final DeferredBlock<Block> EXIT_MAZE_PORTAL = REGISTER.register("exit_maze_portal",
			() -> new MazePortalBlock(Properties.ofFullCopy(Blocks.END_PORTAL).setId(
					ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(Mazed.MODID + ":exit_maze_portal"))),
					true));

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}
}
