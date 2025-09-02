package com.ruinedmango.mazed.block.entity;

import com.ruinedmango.mazed.registries.BlockEntityRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MazePortalBlockEntity extends BlockEntity {

	public MazePortalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
		// TODO Auto-generated constructor stub
	}

	public MazePortalBlockEntity(BlockPos pos, BlockState blockState) {
		this(BlockEntityRegistry.MAZE_PORTAL_ENTITY.get(), pos, blockState);
	}

	public boolean shouldRenderFace(Direction face) {
		return true;
	}

}
