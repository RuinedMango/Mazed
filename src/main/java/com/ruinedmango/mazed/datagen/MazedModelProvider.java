package com.ruinedmango.mazed.datagen;

import com.ruinedmango.mazed.registries.BlockRegistry;
import com.ruinedmango.mazed.registries.ItemRegistry;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class MazedModelProvider extends ModelProvider {

	public MazedModelProvider(PackOutput output, String modId) {
		super(output, modId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		itemModels.generateFlatItem(ItemRegistry.MAZE_ROD.get(), ModelTemplates.FLAT_ITEM);

		blockModels.createTrivialCube(BlockRegistry.MAZE_PORTAL.get());
	}
}
