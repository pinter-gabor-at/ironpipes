package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.entity.WoodenFittingEntity;
import eu.pintergabor.ironpipes.block.entity.WoodenPipeEntity;
import eu.pintergabor.ironpipes.blockold.entity.CopperFittingEntity;
import eu.pintergabor.ironpipes.blockold.entity.CopperPipeEntity;
import org.jetbrains.annotations.NotNull;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;


public final class ModBlockEntities {
    // Wooden pipes.
    public static final BlockEntityType<WoodenPipeEntity> WOODEN_PIPE_ENTITY = register(
        "wooden_pipe",
        WoodenPipeEntity::new,
        ModBlocks.WOODEN_PIPES);
    // Wooden fittings.
    public static final BlockEntityType<WoodenFittingEntity> WOODEN_FITTING_ENTITY = register(
        "wooden_fitting",
        WoodenFittingEntity::new,
        ModBlocks.WOODEN_FITTINGS);
    // Copper pipes.
    public static final BlockEntityType<CopperPipeEntity> COPPER_PIPE_ENTITY = register(
        "copper_pipe",
        CopperPipeEntity::new,
        ModBlocks.COPPER_PIPE,
        ModBlocks.EXPOSED_COPPER_PIPE,
        ModBlocks.WEATHERED_COPPER_PIPE,
        ModBlocks.OXIDIZED_COPPER_PIPE,
        ModBlocks.WAXED_COPPER_PIPE,
        ModBlocks.WAXED_EXPOSED_COPPER_PIPE,
        ModBlocks.WAXED_WEATHERED_COPPER_PIPE,
        ModBlocks.WAXED_OXIDIZED_COPPER_PIPE);
    // Copper fittings.
    public static final BlockEntityType<CopperFittingEntity> COPPER_FITTING_ENTITY = register(
        "copper_fitting",
        CopperFittingEntity::new,
        ModBlocks.COPPER_FITTING,
        ModBlocks.EXPOSED_COPPER_FITTING,
        ModBlocks.WEATHERED_COPPER_FITTING,
        ModBlocks.OXIDIZED_COPPER_FITTING,
        ModBlocks.WAXED_COPPER_FITTING,
        ModBlocks.WAXED_EXPOSED_COPPER_FITTING,
        ModBlocks.WAXED_WEATHERED_COPPER_FITTING,
        ModBlocks.WAXED_OXIDIZED_COPPER_FITTING);

    @NotNull
    private static <T extends BlockEntity> BlockEntityType<T> register(
        @NotNull String path,
        @NotNull FabricBlockEntityTypeBuilder.Factory<T> blockEntity,
        @NotNull Block... blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Global.modId(path),
            FabricBlockEntityTypeBuilder.create(blockEntity, blocks).build());
    }

    public static void init() {
        // Everything is done by static initializers.
    }
}
