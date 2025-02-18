package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.pintergabor.ironpipes.block.entity.CopperFittingEntity;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.config.SimpleCopperPipesConfig;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;
import eu.pintergabor.ironpipes.registry.ModStats;
import eu.pintergabor.ironpipes.tag.ModItemTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;


public class CopperFitting extends BlockWithEntity implements Waterloggable, Oxidizable {
    public static final MapCodec<CopperFitting> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
        OxidationLevel.CODEC.fieldOf("weather_state").forGetter((copperFitting -> copperFitting.weatherState)),
        createSettingsCodec(),
        Codec.INT.fieldOf("cooldown").forGetter((copperFitting) -> copperFitting.cooldown)
    ).apply(instance, CopperFitting::new));
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final EnumProperty<PipeFluid> FLUID = ModBlockStateProperties.FLUID;
    public static final BooleanProperty HAS_ELECTRICITY = ModBlockStateProperties.HAS_ELECTRICITY;
    private static final VoxelShape FITTING_SHAPE = Block.createCuboidShape(2.5D, 2.5D, 2.5D, 13.5D, 13.5D, 13.5D);
    public final int cooldown;
    private final OxidationLevel weatherState;

    public CopperFitting(OxidationLevel weatherState, Settings settings, int cooldown) {
        super(settings);
        this.weatherState = weatherState;
        this.cooldown = cooldown;
        this.setDefaultState(this.getStateManager().getDefaultState()
            .with(POWERED, false)
            .with(WATERLOGGED, false)
            .with(FLUID, PipeFluid.NONE)
            .with(HAS_ELECTRICITY, false)
        );
    }

    public CopperFitting(Settings settings, int cooldown) {
        this(OxidationLevel.UNAFFECTED, settings, cooldown);
    }

    public static void updateBlockEntityValues(World level, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperFitting) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof CopperFittingEntity fitting) {
                fitting.canWater = state.get(Properties.WATERLOGGED) && SimpleCopperPipesConfig.get().carryWater;
            }
        }
    }

    @Override
    @NotNull
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return FITTING_SHAPE;
    }

    @Override
    @NotNull
    public VoxelShape getRaycastShape(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return FITTING_SHAPE;
    }

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext itemPlacementContext) {
        return this.getDefaultState()
            .with(WATERLOGGED, itemPlacementContext.getWorld().getFluidState(itemPlacementContext.getBlockPos()).getFluid() == Fluids.WATER)
            .with(POWERED, itemPlacementContext.getWorld().isReceivingRedstonePower(itemPlacementContext.getBlockPos()));
    }

    @Override
    protected @NotNull BlockState getStateForNeighborUpdate(
        @NotNull BlockState blockState,
        WorldView levelReader,
        ScheduledTickView scheduledTickAccess,
        BlockPos blockPos,
        Direction direction,
        BlockPos neighborPos,
        BlockState neighborState,
        Random randomSource
    ) {
        if (blockState.get(WATERLOGGED)) {
            scheduledTickAccess.scheduleFluidTick(blockPos, Fluids.WATER, Fluids.WATER.getTickRate(levelReader));
        }
        boolean electricity = blockState.get(HAS_ELECTRICITY);
        if (neighborState.getBlock() instanceof LightningRodBlock) {
            if (neighborState.get(POWERED)) {
                electricity = true;
            }
        }
        return blockState.with(HAS_ELECTRICITY, electricity);
    }

    @Override
    protected void neighborUpdate(@NotNull BlockState blockState, @NotNull World level, BlockPos blockPos, Block block, @Nullable WireOrientation orientation, boolean bl) {
        if (level.isReceivingRedstonePower(blockPos)) {
            level.setBlockState(blockPos, blockState.with(CopperFitting.POWERED, true));
        } else {
            level.setBlockState(blockPos, blockState.with(CopperFitting.POWERED, false));
        }
        updateBlockEntityValues(level, blockPos, blockState);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperFittingEntity(pos, state);
    }

    @Override
    protected boolean isTransparent(@NotNull BlockState blockState) {
        return blockState.getFluidState().isEmpty();
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull World level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClient()) {
            return validateTicker(
                blockEntityType,
                ModBlockEntities.COPPER_FITTING_ENTITY,
                (level1, blockPos, blockState1, copperFittingEntity) ->
                    copperFittingEntity.serverTick(level1, blockPos, blockState1)
            );
        }
        return null;
    }

    @Override
    public void onPlaced(
        @NotNull World level, @NotNull BlockPos blockPos, @NotNull BlockState blockState,
        LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        super.onPlaced(level, blockPos, blockState, livingEntity, itemStack);
        updateBlockEntityValues(level, blockPos, blockState);
    }

    @Override
    @NotNull
    public FluidState getFluidState(@NotNull BlockState blockState) {
        if (blockState.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(blockState);
    }

    @Override
    protected @NotNull ActionResult onUse(
        BlockState state, World level, BlockPos pos,
        PlayerEntity player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CopperFittingEntity fittingEntity) {
            player.openHandledScreen(fittingEntity);
            player.incrementStat(Stats.CUSTOM.getOrCreateStat(
                ModStats.INSPECT_FITTING));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    protected @NotNull ActionResult onUseWithItem(
        @NotNull ItemStack stack,
        BlockState state,
        World level,
        BlockPos pos,
        PlayerEntity player,
        Hand hand,
        BlockHitResult hitResult
    ) {
        if (stack.isIn(ModItemTags.IGNORES_COPPER_PIPE_MENU)) {
            return ActionResult.PASS;
        }
        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    @Override
    @NotNull
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasComparatorOutput(BlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState blockState, @NotNull World level, BlockPos blockPos) {
        return ScreenHandler.calculateComparatorOutput(level.getBlockEntity(blockPos));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, POWERED, FLUID, HAS_ELECTRICITY);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType pathComputationType) {
        return false;
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, ServerWorld serverLevel, BlockPos blockPos, Random random) {
        this.tickDegradation(blockState, serverLevel, blockPos, random);
    }

    @Override
    public boolean hasRandomTicks(@NotNull BlockState blockState) {
        return Oxidizable.getIncreasedOxidationBlock(blockState.getBlock()).isPresent();
    }

    @Override
    public void randomDisplayTick(@NotNull BlockState blockState, @NotNull World level, @NotNull BlockPos blockPos, Random random) {
        // If it is electified, create sparks.
        if (blockState.get(HAS_ELECTRICITY)) {
            ParticleUtil.spawnParticle(
                Direction.UP.getAxis(), level, blockPos, 0.55,
                ParticleTypes.ELECTRIC_SPARK, UniformIntProvider.create(1, 2));
        }
    }

    @Override
    public void onStateReplaced(BlockState blockState, World level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        updateBlockEntityValues(level, blockPos, blockState);
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperFitting)) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperFittingEntity) {
                ItemScatterer.spawn(level, blockPos, (CopperFittingEntity) blockEntity);
                level.updateComparators(blockPos, this);
            }
            level.removeBlockEntity(blockPos);
        }
    }

    @Override
    public @NotNull OxidationLevel getDegradationLevel() {
        return this.weatherState;
    }

    @Override
    protected @NotNull MapCodec<? extends CopperFitting> getCodec() {
        return CODEC;
    }
}
