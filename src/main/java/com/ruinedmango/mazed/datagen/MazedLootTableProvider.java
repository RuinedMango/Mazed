package com.ruinedmango.mazed.datagen;

import java.util.function.BiConsumer;

import com.ruinedmango.mazed.Mazed;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class MazedLootTableProvider implements LootTableSubProvider {
	private HolderLookup.Provider lookupProvider;

	public static ResourceKey<LootTable> SUPER_MAZE_BARREL = ResourceKey.create(Registries.LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "chests/super_maze_barrel"));
	public static ResourceKey<LootTable> MAZE_CHEST_TIER1 = ResourceKey.create(Registries.LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "chests/maze_chest_tier1"));
	public static ResourceKey<LootTable> MAZE_CHEST_TIER2 = ResourceKey.create(Registries.LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "chests/maze_chest_tier2"));
	public static ResourceKey<LootTable> MAZE_CHEST_TIER3 = ResourceKey.create(Registries.LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "chests/maze_chest_tier3"));
	public static ResourceKey<LootTable> MAZE_CHEST_TIER4 = ResourceKey.create(Registries.LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "chests/maze_chest_tier4"));
	public static ResourceKey<LootTable> MAZE_CHEST_TIER5 = ResourceKey.create(Registries.LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "chests/maze_chest_tier5"));
	public static ResourceKey<LootTable> MAZE_CHEST_TIER6 = ResourceKey.create(Registries.LOOT_TABLE,
			ResourceLocation.fromNamespaceAndPath(Mazed.MODID, "chests/maze_chest_tier6"));

	public MazedLootTableProvider(HolderLookup.Provider lookupProvider) {
		this.lookupProvider = lookupProvider;
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, Builder> output) {
		output.accept(SUPER_MAZE_BARREL,
				LootTable.lootTable().apply(SetItemCountFunction.setCount(ConstantValue.exactly(30)))
						.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(10, 20))
								.setBonusRolls(ConstantValue.exactly(3)).add(LootItem.lootTableItem(Items.DIAMOND))
								.add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE))
								.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE))));
		output.accept(MAZE_CHEST_TIER1, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2, 3))
						.add(LootItem.lootTableItem(Items.APPLE)).add(LootItem.lootTableItem(Items.BREAD)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(12, 14))
						.add(LootItem.lootTableItem(Items.COBBLESTONE)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
						.apply(new EnchantRandomlyFunction.Builder()
								.withEnchantment(lookupProvider.getOrThrow(Enchantments.UNBREAKING)))
						.add(LootItem.lootTableItem(Items.STICK)).add(LootItem.lootTableItem(Items.WOODEN_PICKAXE))));
		output.accept(MAZE_CHEST_TIER2, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
						.add(LootItem.lootTableItem(Items.STICK)).add(LootItem.lootTableItem(Items.STONE_PICKAXE)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(12, 14))
						.add(LootItem.lootTableItem(Items.STONE)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3, 5))
						.add(LootItem.lootTableItem(Items.BEETROOT)).add(LootItem.lootTableItem(Items.BEETROOT_SOUP))
						.add(LootItem.lootTableItem(Items.BREAD))));
		output.accept(MAZE_CHEST_TIER3,
				LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
						.apply(new EnchantRandomlyFunction.Builder()
								.withEnchantment(lookupProvider.getOrThrow(Enchantments.EFFICIENCY)))
						.add(LootItem.lootTableItem(Items.STONE_PICKAXE)).add(LootItem.lootTableItem(Items.IRON_INGOT)))
						.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(12, 14))
								.add(LootItem.lootTableItem(Items.OAK_PLANKS)))
						.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3, 7))
								.add(LootItem.lootTableItem(Items.MELON_SLICE)).add(LootItem.lootTableItem(Items.BREAD))
								.add(LootItem.lootTableItem(Items.BEETROOT_SOUP))));
		output.accept(MAZE_CHEST_TIER4, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 3))
						.add(LootItem.lootTableItem(Items.COAL)).add(LootItem.lootTableItem(Items.IRON_INGOT)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(Items.IRON_PICKAXE)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(12, 14))
						.add(LootItem.lootTableItem(Items.OAK_PLANKS)).add(LootItem.lootTableItem(Items.BRICKS)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3, 8))
						.add(LootItem.lootTableItem(Items.MELON_SLICE)).add(LootItem.lootTableItem(Items.BREAD))
						.add(LootItem.lootTableItem(Items.BEETROOT_SOUP))));
		output.accept(MAZE_CHEST_TIER5,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 5))
								.add(LootItem.lootTableItem(Items.DIAMOND)).add(LootItem.lootTableItem(Items.COAL))
								.add(LootItem.lootTableItem(Items.IRON_INGOT)))
						.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
								.apply(new EnchantRandomlyFunction.Builder()
										.withEnchantment(lookupProvider.getOrThrow(Enchantments.EFFICIENCY)))
								.add(LootItem.lootTableItem(Items.IRON_PICKAXE)))
						.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(12, 14))
								.add(LootItem.lootTableItem(Items.BRICKS)))
						.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3, 8))
								.add(LootItem.lootTableItem(Items.MUSHROOM_STEW))
								.add(LootItem.lootTableItem(Items.RABBIT_STEW))
								.add(LootItem.lootTableItem(Items.BEETROOT_SOUP))));
		output.accept(MAZE_CHEST_TIER6, LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3, 5))
						.add(LootItem.lootTableItem(Items.DIAMOND)).add(LootItem.lootTableItem(Items.IRON_INGOT)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(12, 14))
						.add(LootItem.lootTableItem(Items.COBBLED_DEEPSLATE)))
				.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3, 8))
						.add(LootItem.lootTableItem(Items.GOLDEN_CARROT))
						.add(LootItem.lootTableItem(Items.GOLDEN_APPLE)).add(LootItem.lootTableItem(Items.CAKE))));
	}

}
