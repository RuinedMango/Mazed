package com.ruinedmango.mazed.registries;

import java.util.function.Supplier;

import com.ruinedmango.mazed.Mazed;
import com.ruinedmango.mazed.criteria.AmazedTrigger;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CriteriaRegistry {
	private static final DeferredRegister<CriterionTrigger<?>> REGISTER = DeferredRegister
			.create(Registries.TRIGGER_TYPE, Mazed.MODID);

	public static final Supplier<AmazedTrigger> AMAZED_TRIGGER = REGISTER.register("amazed_trigger",
			AmazedTrigger::new);

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}
}
