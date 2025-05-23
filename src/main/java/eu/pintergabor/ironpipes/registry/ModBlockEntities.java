package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.entity.FluidFittingEntity;
import eu.pintergabor.ironpipes.block.entity.FluidPipeEntity;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;


public final class ModBlockEntities {
	// Wooden and stone pipes.
	public static final BlockEntityType<FluidPipeEntity> FLUID_PIPE_ENTITY = register(
		"fluid_pipe",
		FluidPipeEntity::new,
		ModBlocks.PIPES);
	// Wooden and stone fittings.
	public static final BlockEntityType<FluidFittingEntity> FLUID_FITTING_ENTITY = register(
		"fluid_fitting",
		FluidFittingEntity::new,
		ModBlocks.FITTINGS);

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
