package com.ruinedmango.mazed.criteria;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AmazedTrigger extends SimpleCriterionTrigger<AmazedTrigger.Instance> {
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("mazed", "amazed");
	public static final AmazedTrigger INSTANCE = new AmazedTrigger();

	@Override
	public Codec<Instance> codec() {
		return Instance.CODEC;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, inst -> true); // always match
	}

	public static class Instance implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(i -> i.player))
				.apply(instance, Instance::new));

		private final Optional<ContextAwarePredicate> player;

		public Instance(Optional<ContextAwarePredicate> player) {
			this.player = player;
		}

		@Override
		public Optional<ContextAwarePredicate> player() {
			return player;
		}
	}
}
