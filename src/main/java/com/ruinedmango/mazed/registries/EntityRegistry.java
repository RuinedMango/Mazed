package com.ruinedmango.mazed.registries;

import java.util.function.Supplier;

import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.entity.MazeCrawlerEntity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegistry {
	private static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister
			.create(BuiltInRegistries.ENTITY_TYPE, Mazed.MODID);

	public static ResourceKey<EntityType<?>> MAZECRAWLER_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
			ResourceLocation.fromNamespaceAndPath("mazed", "mazecrawler"));

	public static final Supplier<EntityType<MazeCrawlerEntity>> MAZECRAWLER = REGISTER.register("mazecrawler",
			() -> EntityType.Builder.of(MazeCrawlerEntity::new, MobCategory.MONSTER).sized(0.75f, 0.35f)
					.build(MAZECRAWLER_KEY));

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}
}
