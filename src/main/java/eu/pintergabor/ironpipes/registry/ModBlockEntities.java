package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.entity.FluidFittingEntity;
import eu.pintergabor.ironpipes.block.entity.FluidPipeEntity;
import eu.pintergabor.ironpipes.block.entity.ItemFittingEntity;
import eu.pintergabor.ironpipes.block.entity.ItemPipeEntity;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;


public final class ModBlockEntities {
	// Fluid pipes.
	public static final BlockEntityType<FluidPipeEntity> FLUID_PIPE_ENTITY = register(
		"fluid_pipe",
		FluidPipeEntity::new,
		ModFluidBlocks.FLUID_PIPES);
	// Fluid fittings.
	public static final BlockEntityType<FluidFittingEntity> FLUID_FITTING_ENTITY = register(
		"fluid_fitting",
		FluidFittingEntity::new,
		ModFluidBlocks.FLUID_FITTINGS);
	// Item pipes.
	public static final BlockEntityType<ItemPipeEntity> ITEM_PIPE_ENTITY = register(
		"item_pipe",
		ItemPipeEntity::new,
		ModItemBlocks.ITEM_PIPES);
	// Item fittings.
	public static final BlockEntityType<ItemFittingEntity> ITEM_FITTING_ENTITY = register(
		"item_fitting",
		ItemFittingEntity::new,
		ModItemBlocks.ITEM_FITTINGS);

	@NotNull
	private static <T extends BlockEntity> BlockEntityType<T> register(
		@NotNull String path,
		@NotNull FabricBlockEntityTypeBuilder.Factory<T> blockEntity,
		@NotNull Block... blocks) {
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Global.modId(path),
			FabricBlockEntityTypeBuilder.create(blockEntity, blocks).build());
	}

	private ModBlockEntities() {
		// Static class.
	}

	public static void init() {
		// Everything has been done by static initializers.
	}
}
