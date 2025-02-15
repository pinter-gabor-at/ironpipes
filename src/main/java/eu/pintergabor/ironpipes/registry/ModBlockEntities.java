package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.entity.CopperFittingEntity;
import eu.pintergabor.ironpipes.block.entity.CopperPipeEntity;

import net.minecraft.block.Block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;


public final class ModBlockEntities {

    public static final BlockEntityType<CopperPipeEntity> COPPER_PIPE_ENTITY = register(
        "copper_pipe",
        CopperPipeEntity::new,
        SimpleCopperPipesBlocks.COPPER_PIPE,
        SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE,
        SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE,
        SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE,
        SimpleCopperPipesBlocks.WAXED_COPPER_PIPE,
        SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE,
        SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE,
        SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE
    );

    public static final BlockEntityType<CopperFittingEntity> COPPER_FITTING_ENTITY = register(
        "copper_fitting",
        CopperFittingEntity::new,
        SimpleCopperPipesBlocks.COPPER_FITTING,
        SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING,
        SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING,
        SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING,
        SimpleCopperPipesBlocks.WAXED_COPPER_FITTING,
        SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING,
        SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING,
        SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING
    );

    @NotNull
    private static <T extends BlockEntity> BlockEntityType<T> register(
        @NotNull String path,
        @NotNull FabricBlockEntityTypeBuilder.Factory<T> blockEntity,
        @NotNull Block... blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Global.modId(path),
            FabricBlockEntityTypeBuilder.create(blockEntity, blocks).build(null));
    }

    public static void init() {
    }
}
