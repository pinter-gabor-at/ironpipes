package eu.pintergabor.ironpipes.registry.util;

import java.util.function.Function;

import eu.pintergabor.ironpipes.Global;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;


public final class ModBlocksRegister {

	private ModBlocksRegister() {
		// Static class.
	}

	/**
	 * Create and register a {@link Block} without {@link Item}
	 * <p>
	 * See block registration in {@link Blocks} for details.
	 *
	 * @param path    The name of the block, without modid.
	 * @param factory The constructor of the block.
	 * @param props   Initial settings of the block.
	 * @param <T>     The returned block type.
	 * @return The registered block.
	 */
	public static <T extends Block> T registerBlock(
		String path,
		Function<Properties, T> factory,
		Properties props
	) {
		ResourceLocation id = Global.modId(path);
		/// See {@link Blocks#vanillaBlockId}.
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
		/// See {@link Blocks#register(String, Function, Properties)}.
		T block = factory.apply(props.setId(key));
		return Registry.register(BuiltInRegistries.BLOCK, id, block);
	}

	/**
	 * Create and register a {@link Block} and the corresponding {@link Item}
	 * <p>
	 * See {@link #registerBlock(String, Function, Properties)} for details.
	 */
	public static <T extends Block> T registerBlockAndItem(
		String path,
		Function<Properties, T> factory,
		Properties props
	) {
		// Register the block.
		T registered = registerBlock(path, factory, props);
		// Register the item.
		Items.registerBlock(registered);
		return registered;
	}
}
