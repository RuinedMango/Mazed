package com.ruinedmango.mazed.block.entity.renderer;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.block.entity.MazePortalBlockEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class MazePortalRenderer<T extends MazePortalBlockEntity> implements BlockEntityRenderer<T> {
	public static final ResourceLocation MAZE_SKY_LOCATION = ResourceLocation.fromNamespaceAndPath(Mazed.MODID,
			"textures/environment/maze_sky.png");
	public static final ResourceLocation MAZE_PORTAL_LOCATION = ResourceLocation.fromNamespaceAndPath(Mazed.MODID,
			"textures/entity/maze_portal.png");

	public MazePortalRenderer(BlockEntityRendererProvider.Context context) {
	}

	public void render(T p_112650_, float p_112651_, PoseStack p_112652_, MultiBufferSource p_112653_, int p_112654_,
			int p_112655_, Vec3 p_401153_) {
		Matrix4f matrix4f = p_112652_.last().pose();
		this.renderCube(p_112650_, matrix4f, p_112653_.getBuffer(this.renderType()));
	}

	private void renderCube(T blockEntity, Matrix4f pose, VertexConsumer consumer) {
		float f = this.getOffsetDown();
		float f1 = this.getOffsetUp();
		this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
		this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
		this.renderFace(blockEntity, pose, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
		this.renderFace(blockEntity, pose, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
		this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
		this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, f1, f1, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
	}

	private void renderFace(T blockEntity, Matrix4f pose, VertexConsumer consumer, float x0, float x1, float y0,
			float y1, float z0, float z1, float z2, float z3, Direction direction) {
		if (blockEntity.shouldRenderFace(direction)) {
			consumer.addVertex(pose, x0, y0, z0);
			consumer.addVertex(pose, x1, y0, z1);
			consumer.addVertex(pose, x1, y1, z2);
			consumer.addVertex(pose, x0, y1, z3);
		}
	}

	protected float getOffsetUp() {
		return 1F;
	}

	protected float getOffsetDown() {
		return 0F;
	}

	public static final RenderType MAZE_PORTAL = RenderType.create("maze_portal", 1536, false, false,
			RenderPipelines.END_PORTAL,
			RenderType.CompositeState.builder()
					.setTextureState(RenderStateShard.MultiTextureStateShard.builder()
							.add(MazePortalRenderer.MAZE_SKY_LOCATION, false)
							.add(MazePortalRenderer.MAZE_PORTAL_LOCATION, false).build())
					.createCompositeState(false));

	protected RenderType renderType() {
		return MAZE_PORTAL;
	}
}
