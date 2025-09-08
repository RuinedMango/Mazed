package com.ruinedmango.mazed.datagen;

import java.util.Optional;
import java.util.function.Consumer;

import com.ruinedmango.mazed.criteria.AmazedTrigger;
import com.ruinedmango.mazed.registries.CriteriaRegistry;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class MazedAdvancementProvider implements AdvancementSubProvider {

	@Override
	public void generate(Provider registries, Consumer<AdvancementHolder> writer) {
		Advancement.Builder.advancement()
				.display(new DisplayInfo(Items.STONE_BRICKS.getDefaultInstance(),
						Component.translatable("advancement.mazed.amazed"),
						Component.translatable("advancement.mazed.amazed_desc"),
						Optional.of(new ClientAsset(
								ResourceLocation.withDefaultNamespace("gui/advancements/backgrounds/stone"))),
						AdvancementType.GOAL, true, true, false))
				.addCriterion("amazed", new Criterion<AmazedTrigger.Instance>(CriteriaRegistry.AMAZED_TRIGGER.get(),
						new AmazedTrigger.Instance(Optional.empty())))
				.save(writer, "mazed:amazed");
	}

}
