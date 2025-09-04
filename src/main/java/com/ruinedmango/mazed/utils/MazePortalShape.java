package com.ruinedmango.mazed.utils;

import com.ruinedmango.mazed.registries.BlockRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MazePortalShape {
	private final Level level;
	private final BlockPos origin;
	private final Block frameBlock;
	private int leftWidth = 0;
	private int rightWidth = 0;
	private int upHeight = 0;
	private int downHeight = 0;
	private boolean zaligned = false;

	private static int SEARCH_RANGE = 21;

	public MazePortalShape(Level level, BlockPos origin, Block frameBlock) {
		this.level = level;
		this.origin = origin;
		this.frameBlock = frameBlock;
	}

	/** Try to form the portal. Returns true if successful. */
	public boolean tryFormPortal() {
		if (!isValidFrame()) {
			return false;
		}
		fillPortal();
		return true;
	}

	/** Check the 4x5 rectangle for a valid frame */
	private boolean isValidFrame() {
		for (int x = 0; x < SEARCH_RANGE; x++) {
			BlockPos left = origin;
			if (level.getBlockState(left.offset(x, 0, 0)).getBlock() == frameBlock) {
				leftWidth = x;
				break;
			}
		}
		for (int x = 0; x > -SEARCH_RANGE; x--) {
			BlockPos right = origin;
			if (level.getBlockState(right.offset(x, 0, 0)).getBlock() == frameBlock) {
				rightWidth = x;
				break;
			}
		}
		for (int y = 0; y < SEARCH_RANGE; y++) {
			BlockPos up = origin;
			if (level.getBlockState(up.offset(0, y, 0)).getBlock() == frameBlock) {
				upHeight = y;
				break;
			}
		}
		for (int y = 0; y > -SEARCH_RANGE; y--) {
			BlockPos down = origin;
			if (level.getBlockState(down.offset(0, y, 0)).getBlock() == frameBlock) {
				downHeight = y;
				break;
			}
		}
		if (leftWidth == 0 && rightWidth == 0) {
			for (int z = 0; z < SEARCH_RANGE; z++) {
				BlockPos left = origin;
				if (level.getBlockState(left.offset(0, 0, z)).getBlock() == frameBlock) {
					leftWidth = z;
					break;
				}
			}
			for (int z = 0; z > -SEARCH_RANGE; z--) {
				BlockPos right = origin;
				if (level.getBlockState(right.offset(0, 0, z)).getBlock() == frameBlock) {
					rightWidth = z;
					break;
				}
			}
			for (int y = 0; y < SEARCH_RANGE; y++) {
				BlockPos up = origin;
				if (level.getBlockState(up.offset(0, y, 0)).getBlock() == frameBlock) {
					upHeight = y;
					break;
				}
			}
			for (int y = 0; y > -SEARCH_RANGE; y--) {
				BlockPos down = origin;
				if (level.getBlockState(down.offset(0, y, 0)).getBlock() == frameBlock) {
					downHeight = y;
					break;
				}
			}
			if (leftWidth != 0 && rightWidth != 0) {
				zaligned = true;
			}
		}
		if (zaligned) {
			for (int z = rightWidth; z <= leftWidth; z++) {
				for (int y = downHeight; y <= upHeight; y++) {
					if (z == rightWidth || z == leftWidth) {
						if (level.getBlockState(origin.offset(0, y, z)).getBlock() != frameBlock) {
							return false;
						}
					}
					if (y == downHeight || y == upHeight) {
						if (level.getBlockState(origin.offset(0, y, z)).getBlock() != frameBlock) {
							return false;
						}
					}
				}
			}
		} else {
			for (int x = rightWidth; x <= leftWidth; x++) {
				for (int y = downHeight; y <= upHeight; y++) {
					if (x == rightWidth || x == leftWidth) {
						if (level.getBlockState(origin.offset(x, y, 0)).getBlock() != frameBlock) {
							return false;
						}
					}
					if (y == downHeight || y == upHeight) {
						if (level.getBlockState(origin.offset(x, y, 0)).getBlock() != frameBlock) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/** Fill the hollow inside with portal blocks */
	private void fillPortal() {
		BlockPos ptr = origin;
		if (zaligned) {
			for (int z = rightWidth; z <= leftWidth; z++) {
				for (int y = downHeight; y <= upHeight; y++) {
					if (z != rightWidth && z != leftWidth && y != downHeight && y != upHeight) {
						level.setBlock(ptr.offset(0, y, z), BlockRegistry.MAZE_PORTAL.get().defaultBlockState(), 2);
					}
				}
			}
		} else {
			for (int x = rightWidth; x <= leftWidth; x++) {
				for (int y = downHeight; y <= upHeight; y++) {
					if (x != rightWidth && x != leftWidth && y != downHeight && y != upHeight) {
						level.setBlock(ptr.offset(x, y, 0), BlockRegistry.MAZE_PORTAL.get().defaultBlockState(), 2);
					}
				}
			}
		}
	}
}
