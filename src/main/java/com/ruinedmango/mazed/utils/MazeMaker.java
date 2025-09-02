package com.ruinedmango.mazed.utils;

public class MazeMaker {
	private final long seed;
	private final int cellSize;

	public int getCellSize() {
		return cellSize;
	}

	public MazeMaker(long seed, int cellSize) {
		this.seed = seed;
		this.cellSize = cellSize;
	}

	// Deterministic hash (no Random object needed per call)
	private long hash(int x, int y, int z, long salt) {
		long h = x * 341873128712L + y * 42595037259L + z * 132897987541L + seed + salt;
		h ^= (h >> 13);
		h *= 0x5bd1e995;
		h ^= (h >> 15);
		return h;
	}

	// Decide if a passage is open EAST
	public boolean openEast(int cx, int cy, int cz) {
		return (hash(cx, cy, cz, 0) & 1L) == 0;
	}

	// Decide if a passage is open SOUTH
	public boolean openSouth(int cx, int cy, int cz) {
		return (hash(cx, cy, cz, 1337) & 1L) == 0;
	}

	public boolean openUp(int cx, int cy, int cz) {
		return (hash(cx, cy, cz, 4242) & 1L) == 0;
	}

	public boolean isWall(int worldX, int worldY, int worldZ) {
		int cx = Math.floorDiv(worldX, cellSize);
		int cy = Math.floorDiv(worldY, cellSize);
		int cz = Math.floorDiv(worldZ, cellSize);

		int bx = Math.floorMod(worldX, cellSize);
		int by = Math.floorMod(worldY, cellSize);
		int bz = Math.floorMod(worldZ, cellSize);

		// West / East walls
		if (bx == 0 && !openEast(cx - 1, cy, cz))
			return true;
		if (bx == cellSize - 1 && !openEast(cx, cy, cz))
			return true;

		// North / South walls
		if (bz == 0 && !openSouth(cx, cy, cz - 1))
			return true;
		if (bz == cellSize - 1 && !openSouth(cx, cy, cz))
			return true;

		// Floor / Ceiling walls
		if (by == 0 && !openUp(cx, cy - 1, cz))
			return true; // floor
		if (by == cellSize - 1 && !openUp(cx, cy, cz))
			return true; // ceiling

		// Otherwise it's a passage
		return false;
	}

	public boolean shouldSpawnChest(int worldX, int worldY, int worldZ) {
		int cx = Math.floorDiv(worldX, cellSize);
		int cy = Math.floorDiv(worldY, cellSize);
		int cz = Math.floorDiv(worldZ, cellSize);

		int exits = 0;
		if (openEast(cx, cy, cz))
			exits++;
		if (openEast(cx - 1, cy, cz))
			exits++;
		if (openSouth(cx, cy, cz))
			exits++;
		if (openSouth(cx, cy, cz - 1))
			exits++;
		if (openUp(cx, cy, cz))
			exits++;
		if (openUp(cx, cy - 1, cz))
			exits++;

		long h = hash(cx, cy, cz, 9999);
		return exits == 1 || (h & 31) == 0; // deterministic 1/32 chance fallback
	}

	public boolean shouldSpawnSuperChest(int worldX, int worldY, int worldZ) {
		int cx = Math.floorDiv(worldX, cellSize);
		int cy = Math.floorDiv(worldY, cellSize);
		int cz = Math.floorDiv(worldZ, cellSize);

		int exits = 0;
		if (openEast(cx, cy, cz))
			exits++;
		if (openEast(cx - 1, cy, cz))
			exits++;
		if (openSouth(cx, cy, cz))
			exits++;
		if (openSouth(cx, cy, cz - 1))
			exits++;
		if (openUp(cx, cy, cz))
			exits++;
		if (openUp(cx, cy - 1, cz))
			exits++;
		return exits == 0; // deterministic 1/32 chance fallback
	}
}
