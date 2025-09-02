package com.ruinedmango.mazed.item;

import com.ruinedmango.mazed.Mazed;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class MazeRod extends Item {

	public MazeRod(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ServerLevel dim = level.getServer().getLevel(
				ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "maze")));
		player.teleportTo(dim, 0, 130, 0, null, 0, 0, true);
		return InteractionResult.PASS;
	}

}
