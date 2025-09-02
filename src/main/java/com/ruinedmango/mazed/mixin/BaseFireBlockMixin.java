package com.ruinedmango.mazed.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ruinedmango.mazed.registries.BlockRegistry;
import com.ruinedmango.mazed.utils.MazePortalShape;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {
	@Inject(at = @At("HEAD"), method = "onPlace")
	private void onPlaceMazed(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving,
			CallbackInfo info) {
		if (!level.isClientSide) {
			MazePortalShape shape = new MazePortalShape(level, pos, Blocks.STONE_BRICKS);
			if (shape.tryFormPortal()) {
				level.setBlockAndUpdate(pos, BlockRegistry.MAZE_PORTAL.get().defaultBlockState());
			}
		}
	}
}
