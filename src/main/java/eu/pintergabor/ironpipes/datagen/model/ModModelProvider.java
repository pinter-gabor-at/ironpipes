package eu.pintergabor.ironpipes.datagen.model;

import java.util.Optional;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.CopperPipe;
import eu.pintergabor.ironpipes.registry.SimpleCopperPipesBlocks;
import org.jetbrains.annotations.NotNull;

import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariant;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.VariantSettings;
import net.minecraft.client.data.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public final class ModModelProvider extends FabricModelProvider {
    private static final Model PIPE_MODEL = new Model(
        Optional.of(Global.modId("block/template_pipe")),
        Optional.empty(),
        TextureKey.SIDE,
        TextureKey.FRONT
    );
    private static final Model PIPE_MODEL_BACK = new Model(
        Optional.of(Global.modId("block/template_pipe_back_extension")),
        Optional.empty(),
        TextureKey.SIDE,
        TextureKey.FRONT
    );
    private static final Model PIPE_MODEL_BACK_SMOOTH = new Model(
        Optional.of(Global.modId("block/template_pipe_back_smooth")),
        Optional.empty(),
        TextureKey.SIDE
    );
    private static final Model PIPE_MODEL_DOUBLE_EXTENSION = new Model(
        Optional.of(Global.modId("block/template_pipe_double_extension")),
        Optional.empty(),
        TextureKey.SIDE
    );
    private static final Model PIPE_MODEL_FRONT_EXTENSION = new Model(
        Optional.of(Global.modId("block/template_pipe_front_extension")),
        Optional.empty(),
        TextureKey.SIDE,
        TextureKey.FRONT
    );
    private static final Model PIPE_MODEL_SMOOTH = new Model(
        Optional.of(Global.modId("block/template_pipe_smooth")),
        Optional.empty(),
        TextureKey.SIDE,
        TextureKey.FRONT
    );
    private static final Model FITTING_MODEL = new Model(
        Optional.of(Global.modId("block/template_fitting")),
        Optional.empty(),
        TextureKey.TEXTURE
    );

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(
        BlockStateModelGenerator generator) {
        // Pipes.
        createPipe(generator, SimpleCopperPipesBlocks.COPPER_PIPE, SimpleCopperPipesBlocks.COPPER_PIPE);
        createPipe(generator, SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE, SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE);
        createPipe(generator, SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE, SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE);
        createPipe(generator, SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE, SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE);
        // Waxed pipes.
        createPipe(generator, SimpleCopperPipesBlocks.COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_COPPER_PIPE);
        createPipe(generator, SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE);
        createPipe(generator, SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE);
        createPipe(generator, SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE);
        // Fittings.
        createFitting(generator, SimpleCopperPipesBlocks.COPPER_FITTING, SimpleCopperPipesBlocks.COPPER_FITTING);
        createFitting(generator, SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING, SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING);
        createFitting(generator, SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING, SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING);
        createFitting(generator, SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING, SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING);
        // Waxed fittings.
        createFitting(generator, SimpleCopperPipesBlocks.COPPER_FITTING, SimpleCopperPipesBlocks.WAXED_COPPER_FITTING);
        createFitting(generator, SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING);
        createFitting(generator, SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING);
        createFitting(generator, SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);
    }

    @Override
    public void generateItemModels(@NotNull ItemModelGenerator generator) {
    }

    public static void createPipe(@NotNull BlockStateModelGenerator generators, Block pipeBlock, Block outputPipeBlock) {
        if (pipeBlock == outputPipeBlock) {
            TextureMap pipeTextureMapping = new TextureMap();
            pipeTextureMapping.put(TextureKey.SIDE, TextureMap.getId(pipeBlock));
            pipeTextureMapping.put(TextureKey.FRONT, TextureMap.getSubId(pipeBlock, "_front"));

            PIPE_MODEL.upload(pipeBlock, pipeTextureMapping, generators.modelCollector);
            PIPE_MODEL_BACK.upload(pipeBlock, "_back_extension", pipeTextureMapping, generators.modelCollector);
            PIPE_MODEL_BACK_SMOOTH.upload(pipeBlock, "_back_smooth", pipeTextureMapping, generators.modelCollector);
            PIPE_MODEL_DOUBLE_EXTENSION.upload(pipeBlock, "_double_extension", pipeTextureMapping, generators.modelCollector);
            PIPE_MODEL_FRONT_EXTENSION.upload(pipeBlock, "_front_extension", pipeTextureMapping, generators.modelCollector);
            PIPE_MODEL_SMOOTH.upload(pipeBlock, "_smooth", pipeTextureMapping, generators.modelCollector);
        }

        Identifier model = ModelIds.getBlockModelId(pipeBlock);
        Identifier frontExtensionModel = ModelIds.getBlockSubModelId(pipeBlock, "_front_extension");
        Identifier doubleExtensionModel = ModelIds.getBlockSubModelId(pipeBlock, "_double_extension");
        Identifier backExtensionModel = ModelIds.getBlockSubModelId(pipeBlock, "_back_extension");
        Identifier smoothModel = ModelIds.getBlockSubModelId(pipeBlock, "_smooth");
        Identifier backSmoothModel = ModelIds.getBlockSubModelId(pipeBlock, "_back_smooth");
        generators.registerParentedItemModel(outputPipeBlock, model);
        generators.blockStateCollector
            .accept(
                VariantsBlockStateSupplier.create(outputPipeBlock)
                    .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates())
                    .coordinate(
                        BlockStateVariantMap.create(CopperPipe.FRONT_CONNECTED, CopperPipe.BACK_CONNECTED, CopperPipe.SMOOTH)
                            .register(false, false, false,
                                BlockStateVariant.create().put(VariantSettings.MODEL, model))
                            .register(true, false, false,
                                BlockStateVariant.create().put(VariantSettings.MODEL, frontExtensionModel))
                            .register(true, true, false,
                                BlockStateVariant.create().put(VariantSettings.MODEL, doubleExtensionModel))
                            .register(true, true, true,
                                BlockStateVariant.create().put(VariantSettings.MODEL, doubleExtensionModel))
                            .register(true, false, true,
                                BlockStateVariant.create().put(VariantSettings.MODEL, frontExtensionModel))
                            .register(false, false, true,
                                BlockStateVariant.create().put(VariantSettings.MODEL, smoothModel))
                            .register(false, true, false,
                                BlockStateVariant.create().put(VariantSettings.MODEL, backExtensionModel))
                            .register(false, true, true,
                                BlockStateVariant.create().put(VariantSettings.MODEL, backSmoothModel))
                    )
            );
    }

    public static void createFitting(@NotNull BlockStateModelGenerator generators, Block fittingBlock, Block outputFittingBlock) {
        if (fittingBlock == outputFittingBlock) {
            TextureMap fittingTextureMapping = new TextureMap();
            fittingTextureMapping.put(TextureKey.TEXTURE, TextureMap.getId(fittingBlock));

            FITTING_MODEL.upload(fittingBlock, fittingTextureMapping, generators.modelCollector);
        }

        Identifier model = ModelIds.getBlockModelId(fittingBlock);
        generators.registerParentedItemModel(outputFittingBlock, model);
        generators.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(outputFittingBlock, model));
    }
}
