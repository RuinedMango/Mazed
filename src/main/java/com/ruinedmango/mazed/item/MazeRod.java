package com.ruinedmango.mazed.item;

import com.ruinedmango.mazed.block.MazePortalBlock;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
		if (!level.isClientSide()) {
			if (player instanceof ServerPlayer sp) {
				if (level instanceof ServerLevel sl) {
					sp.teleport(MazePortalBlock.getPortalDestinationUtil(sl, player, sp.getOnPos()));
					return InteractionResult.PASS;
				}
			}
		}
		return InteractionResult.FAIL;
	}

}
