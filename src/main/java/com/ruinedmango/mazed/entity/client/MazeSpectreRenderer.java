package com.ruinedmango.mazed.entity.client;

import com.ruinedmango.mazed.entity.MazeSpectreEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vex;

public class MazeSpectreRenderer extends MobRenderer<MazeSpectreEntity, ArmedEntityRenderState, MazeSpectreModel> {
	private static final ResourceLocation MAZESPECTRE_LOCATION = ResourceLocation.fromNamespaceAndPath("mazed",
			"textures/entity/mazespectre/mazespectre.png");

	public MazeSpectreRenderer(EntityRendererProvider.Context p_174435_) {
		super(p_174435_, new MazeSpectreModel(p_174435_.bakeLayer(MazeSpectreModel.LAYER_LOCATION)), 0.3F);
		this.addLayer(new ItemInHandLayer<>(this));
	}

	protected int getBlockLightLevel(Vex entity, BlockPos pos) {
		return 15;
	}

	public ResourceLocation getTextureLocation(ArmedEntityRenderState p_364652_) {
		return MAZESPECTRE_LOCATION;
	}

	public void extractRenderState(MazeSpectreEntity p_360574_, ArmedEntityRenderState p_364312_, float p_362582_) {
		super.extractRenderState(p_360574_, p_364312_, p_362582_);
		ArmedEntityRenderState.extractArmedEntityRenderState(p_360574_, p_364312_, this.itemModelResolver);
	}

	@Override
	public ArmedEntityRenderState createRenderState() {
		// TODO Auto-generated method stub
		return new ArmedEntityRenderState();
	}
}
