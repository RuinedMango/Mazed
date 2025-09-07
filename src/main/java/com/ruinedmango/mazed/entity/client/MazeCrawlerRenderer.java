package com.ruinedmango.mazed.entity.client;

import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.entity.MazeCrawlerEntity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class MazeCrawlerRenderer<T extends MazeCrawlerEntity>
		extends MobRenderer<T, LivingEntityRenderState, MazeCrawlerModel> {
	private static final ResourceLocation MAZECRAWLER_LOCATION = ResourceLocation.fromNamespaceAndPath(Mazed.MODID,
			"textures/entity/mazecrawler/mazecrawler.png");

	public MazeCrawlerRenderer(EntityRendererProvider.Context p_174401_) {
		this(p_174401_, MazeCrawlerModel.LAYER_LOCATION);
	}

	public MazeCrawlerRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer) {
		super(context, new MazeCrawlerModel(context.bakeLayer(layer)), 0.8F);
	}

	@Override
	protected float getFlipDegrees() {
		return 180.0F;
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState p_361974_) {
		return MAZECRAWLER_LOCATION;
	}

	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	public void extractRenderState(T p_361550_, LivingEntityRenderState p_365245_, float p_361361_) {
		super.extractRenderState(p_361550_, p_365245_, p_361361_);
	}
}
