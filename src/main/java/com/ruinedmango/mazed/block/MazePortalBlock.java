package com.ruinedmango.mazed.block;

import java.util.Set;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.block.entity.MazePortalBlockEntity;
import com.ruinedmango.mazed.registries.BlockRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MazePortalBlock extends BaseEntityBlock implements Portal {
	public static final MapCodec<MazePortalBlock> CODEC = simpleCodec(MazePortalBlock::new);
	private static final VoxelShape SHAPE = Block.column(16.0, 0.0, 16.0);
	ResourceKey<Level> maze_key = ResourceKey.create(Registries.DIMENSION,
			ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "mazedim"));
	private boolean isExit = false;

	public MazePortalBlock(Properties p_49795_) {
		super(p_49795_);
	}

	public MazePortalBlock(Properties p_49795_, boolean isExit) {
		super(p_49795_);
		this.isExit = isExit;
	}

	@Override
	protected void entityInside(BlockState p_54915_, Level p_54916_, BlockPos p_54917_, Entity p_54918_,
			InsideBlockEffectApplier p_405383_) {
		if (p_54918_.canUsePortal(false)) {
			p_54918_.setAsInsidePortal(this, p_54917_);
		}
	}

	public boolean isCompatible(Block block) {
		return block == Blocks.STONE_BRICKS || block == BlockRegistry.MAZE_PORTAL.get();
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
			@Nullable Orientation orientation, boolean movedByPiston) {
		if (!isExit) {
			boolean zoriented = false;
			if (isCompatible(level.getBlockState(pos.east()).getBlock())
					&& isCompatible(level.getBlockState(pos.west()).getBlock())) {
				zoriented = true;
			}
			if (isCompatible(level.getBlockState(pos.north()).getBlock())
					&& isCompatible(level.getBlockState(pos.south()).getBlock())) {
				zoriented = false;
			}
			int surrounding = 0;
			if (zoriented) {
				if (isCompatible(level.getBlockState(pos.east()).getBlock())) {
					surrounding++;
				}
				if (isCompatible(level.getBlockState(pos.west()).getBlock())) {
					surrounding++;
				}
				if (isCompatible(level.getBlockState(pos.above()).getBlock())) {
					surrounding++;
				}
				if (isCompatible(level.getBlockState(pos.below()).getBlock())) {
					surrounding++;
				}
			}
			if (!zoriented) {
				if (isCompatible(level.getBlockState(pos.south()).getBlock())) {
					surrounding++;
				}
				if (isCompatible(level.getBlockState(pos.north()).getBlock())) {
					surrounding++;
				}
				if (isCompatible(level.getBlockState(pos.above()).getBlock())) {
					surrounding++;
				}
				if (isCompatible(level.getBlockState(pos.below()).getBlock())) {
					surrounding++;
				}
			}
			if (surrounding < 4) {
				level.destroyBlock(pos, false);
			}
		}
	}

	@Override
	public TeleportTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
		ResourceKey<Level> resourcekey = level.dimension() == maze_key ? Level.OVERWORLD : maze_key;
		if (level.dimension() == Level.OVERWORLD) {
			entity.getPersistentData().putInt("maze_entry_x", pos.getX());
			entity.getPersistentData().putInt("maze_entry_y", pos.getY());
			entity.getPersistentData().putInt("maze_entry_z", pos.getZ());
		}
		ServerLevel serverlevel = level.getServer().getLevel(resourcekey);
		float f = level.getSharedSpawnAngle();
		Set<Relative> set = Relative.union(Relative.DELTA, Relative.ROTATION);
		Vec3 vec3 = null;
		if (level.dimension() == maze_key) {
			vec3 = findNearestSafePos(serverlevel,
					new BlockPos(entity.getPersistentData().getIntOr("maze_entry_x", 0),
							entity.getPersistentData().getIntOr("maze_entry_y", 100),
							entity.getPersistentData().getIntOr("maze_entry_z", 0)),
					32).getBottomCenter();
		} else {
			vec3 = findNearestSafePos(serverlevel, new BlockPos(0, 124, 0), 32).getBottomCenter();
		}
		return new TeleportTransition(serverlevel, vec3, Vec3.ZERO, f, 0.0F, set,
				TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET));
	}

	private static boolean isSafe(ServerLevel level, BlockPos pos) {
		return level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir()
				&& level.getBlockState(pos.below()).isSolidRender();
	}

	private static BlockPos findNearestSafePos(ServerLevel level, BlockPos center, int maxRadius) {
		if (isSafe(level, center))
			return center;

		for (int r = 1; r <= maxRadius; r++) {
			for (BlockPos pos : BlockPos.betweenClosed(center.offset(-r, -r, -r), center.offset(r, r, r))) {
				if (isSafe(level, pos)) {
					return pos;
				}
			}
		}
		return center; // fallback if nothing found
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MazePortalBlockEntity(pos, state, isExit);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected VoxelShape getEntityInsideCollisionShape(BlockState p_371319_, BlockGetter p_399758_, BlockPos p_371244_,
			Entity p_399952_) {
		return p_371319_.getShape(p_399758_, p_371244_);
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader p_304768_, BlockPos p_53004_, BlockState p_53005_,
			boolean p_387386_) {
		return ItemStack.EMPTY;
	}

	@Override
	protected boolean canBeReplaced(BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	protected RenderShape getRenderShape(BlockState p_389588_) {
		return RenderShape.INVISIBLE;
	}

}
