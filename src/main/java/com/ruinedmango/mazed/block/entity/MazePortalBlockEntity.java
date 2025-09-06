package com.ruinedmango.mazed.block.entity;

import com.ruinedmango.mazed.registries.BlockEntityRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MazePortalBlockEntity extends BlockEntity {
	public boolean isExit;

	public MazePortalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
		// TODO Auto-generated constructor stub
	}

	public MazePortalBlockEntity(BlockPos pos, BlockState blockState, boolean isExit) {
		this(isExit ? BlockEntityRegistry.EXIT_MAZE_PORTAL_ENTITY.get() : BlockEntityRegistry.MAZE_PORTAL_ENTITY.get(),
				pos, blockState);
		this.isExit = isExit;
	}

	public boolean shouldRenderFace(Direction face) {
		if (isExit) {
			return Direction.fromAxisAndDirection(face.getAxis(), face.getAxisDirection()) == Direction.Axis.Y
					.getNegative();
		}
		if (!isExit) {
			return face.getAxis() != Direction.Axis.Y;
		}
		return true;
	}

}
