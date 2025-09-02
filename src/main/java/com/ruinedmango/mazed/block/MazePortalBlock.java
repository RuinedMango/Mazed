package com.ruinedmango.mazed.block;

import java.util.Set;

import com.mojang.serialization.MapCodec;
import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.block.entity.MazePortalBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MazePortalBlock extends BaseEntityBlock implements Portal {
	public static final MapCodec<MazePortalBlock> CODEC = simpleCodec(MazePortalBlock::new);
	private static final VoxelShape SHAPE = Block.column(16.0, 0.0, 16.0);
	ResourceKey<Level> maze_key = ResourceKey.create(Registries.DIMENSION,
			ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "mazedim"));

	public MazePortalBlock(Properties p_49795_) {
		super(p_49795_);
	}

	@Override
	protected void entityInside(BlockState p_54915_, Level p_54916_, BlockPos p_54917_, Entity p_54918_,
			InsideBlockEffectApplier p_405383_) {
		if (p_54918_.canUsePortal(false)) {
			p_54918_.setAsInsidePortal(this, p_54917_);
		}
	}

	@Override
	public TeleportTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
		ResourceKey<Level> resourcekey = level.dimension() == maze_key ? Level.OVERWORLD : maze_key;
		ServerLevel serverlevel = level.getServer().getLevel(resourcekey);
		float f = level.getSharedSpawnAngle();
		Set<Relative> set = Relative.union(Relative.DELTA, Relative.ROTATION);
		Vec3 vec3 = level.getSharedSpawnPos().getBottomCenter();
		return new TeleportTransition(serverlevel, vec3, Vec3.ZERO, f, 0.0F, set,
				TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MazePortalBlockEntity(pos, state);
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
	protected boolean canBeReplaced(BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	protected RenderShape getRenderShape(BlockState p_389588_) {
		return RenderShape.INVISIBLE;
	}

}
