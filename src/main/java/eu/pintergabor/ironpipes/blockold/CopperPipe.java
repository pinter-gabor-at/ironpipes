package eu.pintergabor.ironpipes.blockold;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.pintergabor.ironpipes.blockold.base.BasePipe;
import eu.pintergabor.ironpipes.blockold.entity.CopperPipeEntity;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeDripBehaviors;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.config.ModConfig;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModProperties;
import eu.pintergabor.ironpipes.registry.ModStats;
import eu.pintergabor.ironpipes.tag.ModItemTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.registry.tag.FluidTags;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;

import static net.minecraft.block.LightningRodBlock.POWERED;


public class CopperPipe extends BasePipe implements Waterloggable, Oxidizable {
    public static final BooleanProperty WATERLOGGED =
        Properties.WATERLOGGED;
    public static final EnumProperty<PipeFluid> FLUID =
        ModProperties.FLUID;
    public static final BooleanProperty HAS_ELECTRICITY =
        ModProperties.HAS_ELECTRICITY;
    public static final MapCodec<CopperPipe> CODEC =
        RecordCodecBuilder.mapCodec((instance) -> instance.group(
            OxidationLevel.CODEC.fieldOf("oxidation")
                .forGetter((copperPipe -> copperPipe.oxidation)),
            createSettingsCodec(),
            Codec.INT.fieldOf("cooldown")
                .forGetter((copperPipe) -> copperPipe.cooldown),
            Codec.INT.fieldOf("dispense_shot_length")
                .forGetter((copperPipe) -> copperPipe.dispenseShotLength)
        ).apply(instance, CopperPipe::new));
    public final int cooldown;
    public final int dispenseShotLength;
    private final OxidationLevel oxidation;

    public CopperPipe(OxidationLevel oxidation, Settings settings, int cooldown, int dispenseShotLength) {
        super(settings);
        this.oxidation = oxidation;
        this.cooldown = cooldown;
        this.dispenseShotLength = dispenseShotLength;
        this.setDefaultState(this.getStateManager().getDefaultState()
            .with(FACING, Direction.DOWN)
            .with(SMOOTH, false)
            .with(WATERLOGGED, false)
            .with(FLUID, PipeFluid.NONE)
            .with(HAS_ELECTRICITY, false));
    }

    @SuppressWarnings("unused")
    public CopperPipe(Settings settings, int cooldown, int dispenseShotLength) {
        this(OxidationLevel.UNAFFECTED, settings, cooldown, dispenseShotLength);
    }

    /**
     * Update the associated block entity parameters and behaviour.
     * <p>
     * Called when the state of this pipe block, or the state of its neighbours change
     *
     * @param world This world
     * @param pos   Pipe position
     * @param state New state
     */
    private static void updateBlockEntityValues(World world, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            Direction direction = state.get(Properties.FACING);
            // The state of the block in front of the pipe.
            BlockState frontState = world.getBlockState(pos.offset(direction));
            Block frontBlock = frontState.getBlock();
            // The state of the block behind the pipe.
            BlockState backState = world.getBlockState(pos.offset(direction.getOpposite()));
            Block backBlock = backState.getBlock();
            // Always true.
            if (world.getBlockEntity(pos) instanceof CopperPipeEntity pipeEntity) {
                // The pipe can dispense if there is air or water in fron of it.
                pipeEntity.canDispense = (frontState.isAir() || frontBlock == Blocks.WATER);
                // The dispensing is controlled if there is dropper behind it.
                pipeEntity.shootsControlled = (backBlock == Blocks.DROPPER);
                pipeEntity.shootsSpecial = (backBlock == Blocks.DISPENSER);
                pipeEntity.canAccept = !(
                    backBlock instanceof CopperPipe ||
                        backBlock instanceof CopperFitting ||
                        backState.isSolidBlock(world, pos));
                pipeEntity.canWater = ModConfig.get().carryWater &&
                    ((backBlock == Blocks.WATER) || state.get(Properties.WATERLOGGED) ||
                        (backState.contains(Properties.WATERLOGGED) && backState.get(Properties.WATERLOGGED)));
                pipeEntity.canLava = ModConfig.get().carryLava &&
                    (backBlock == Blocks.LAVA);
                if (pipeEntity.canWater && pipeEntity.canLava) {
                    pipeEntity.canWater = false;
                    pipeEntity.canLava = false;
                }
            }
        }
    }

    /**
     * The item output location is just in front of the pipe.
     */
    public static Vec3d getOutputLocation(@NotNull BlockPos pos, @NotNull Direction facing) {
        return new Vec3d(
            ((double) pos.getX() + 0.5D) + 0.7D * (double) facing.getOffsetX(),
            ((double) pos.getY() + 0.5D) + 0.7D * (double) facing.getOffsetY(),
            ((double) pos.getZ() + 0.5D) + 0.7D * (double) facing.getOffsetZ()
        );
    }

    /**
     * Determine the initial state of the pipe based on its surroundings.
     *
     * @return the initial state of the block
     */
    @NotNull
    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext context) {
        BlockState state = super.getPlacementState(context);
        BlockPos pos = context.getBlockPos();
        return state
            .with(WATERLOGGED, context.getWorld().getFluidState(pos).getFluid() == Fluids.WATER);
    }

    /**
     * Handle state changes when the neighboring block's state changes.
     *
     * @return the state of the pipe after a neighboring block's state changes.
     */
    @Override
    protected @NotNull BlockState getStateForNeighborUpdate(
        @NotNull BlockState blockState,
        WorldView world,
        ScheduledTickView scheduledTickAccess,
        BlockPos pos,
        Direction direction,
        BlockPos neighborPos,
        BlockState neighborState,
        Random random) {
        if (blockState.get(WATERLOGGED)) {
            scheduledTickAccess.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        // The pipe is electrified if it is connected to a lightning rod, and lightnings strikes the lightning rod.
        boolean electricity = blockState.get(HAS_ELECTRICITY) ||
            ((neighborState.getBlock() instanceof LightningRodBlock) && neighborState.get(POWERED));
        Direction facing = blockState.get(FACING);
        return blockState
            .with(FRONT_CONNECTED, needFrontExtension(world, pos, facing))
            .with(BACK_CONNECTED, needBackExtension(world, pos, facing))
            .with(SMOOTH, isSmooth(world, pos, facing))
            .with(HAS_ELECTRICITY, electricity);
    }

    /**
     * Handle side effects when the neighboring block's state changes.
     */
    @Override
    protected void neighborUpdate(
        @NotNull BlockState blockState, @NotNull World world,
        BlockPos blockPos, Block block, @Nullable WireOrientation orientation, boolean notify) {
//        boolean powered = isReceivingRedstonePower(world, blockPos);
//        if (powered != blockState.get(POWERED)) {
//            world.setBlockState(blockPos, blockState.with(POWERED, powered));
//        }
        updateBlockEntityValues(world, blockPos, blockState);
    }

    /**
     * Create a block entity.
     */
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperPipeEntity(pos, state);
    }

    /**
     * Create a ticker, which will be called at every tick both on the client and on the server.
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull World world, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!world.isClient()) {
            return validateTicker(
                blockEntityType, ModBlockEntities.COPPER_PIPE_ENTITY,
                (world1, pos1, state1, copperPipeEntity) ->
                    copperPipeEntity.serverTick(world1, pos1, state1)
            );
        }
        return null;
    }

    @Override
    public void onPlaced(
        World world, BlockPos blockPos, BlockState blockState,
        LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        super.onPlaced(world, blockPos, blockState, livingEntity, itemStack);
        updateBlockEntityValues(world, blockPos, blockState);
    }

    @Override
    @NotNull
    public FluidState getFluidState(@NotNull BlockState blockState) {
        if (blockState.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(blockState);
    }

    /**
     * Use empty hand on a pipe.
     * <p>
     * Open gui.
     */
    @Override
    protected @NotNull ActionResult onUse(
        BlockState state, @NotNull World world, BlockPos pos,
        PlayerEntity player, BlockHitResult hitResult) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CopperPipeEntity copperPipeEntity) {
            player.openHandledScreen(copperPipeEntity);
            player.incrementStat(Stats.CUSTOM.getOrCreateStat(
                ModStats.INSPECT_PIPE));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    /**
     * Use item on a pipe.
     * <p>
     * If it is another piece of pipe or fitting then place it,
     * otherwise open the GUI.
     */
    @Override
    protected @NotNull ActionResult onUseWithItem(
        @NotNull ItemStack stack,
        BlockState state, World world, BlockPos pos,
        PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        // Allow placing pipes next to pipes and fittings.
        if (stack.isIn(ModItemTags.PIPES_AND_FITTINGS)) {
            return ActionResult.PASS;
        }
        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, FRONT_CONNECTED, BACK_CONNECTED, SMOOTH, WATERLOGGED, FLUID, HAS_ELECTRICITY, POWERED);
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, ServerWorld serverLevel, BlockPos blockPos, Random random) {
        Direction direction = blockState.get(FACING);
        boolean isLava = blockState.get(FLUID) == PipeFluid.LAVA;
        boolean isWater = blockState.get(FLUID) == PipeFluid.WATER;
        // Water can drip from any pipe containing water.
        // Lava can drip from any pipe containing lava, except thos that face upwards.
        if (isWater || (isLava && direction != Direction.UP)) {
            // Adjust probability.
            if (random.nextFloat() <= (isLava ? 0.05859375F : 0.17578125F) * 2) {
                BlockPos.Mutable mutableBlockPos = blockPos.mutableCopy();
                if (direction != Direction.DOWN) {
                    mutableBlockPos.move(direction);
                }
                // Search down to 12 blocks.
                for (int i = 0; i < 12; i++) {
                    mutableBlockPos.move(Direction.DOWN);
                    BlockState state = serverLevel.getBlockState(mutableBlockPos);
                    if (serverLevel.getFluidState(mutableBlockPos).isEmpty()) {
                        // A block that reacts with the drip stops the drip.
                        LeakingPipeDripBehaviors.DripOn dripOn =
                            LeakingPipeDripBehaviors.getDrip(state.getBlock());
                        if (dripOn != null) {
                            dripOn.dripOn(isLava, serverLevel, mutableBlockPos, state);
                            break;
                        }
                        // A solid block stops the drip.
                        if (state.getCollisionShape(serverLevel, mutableBlockPos) != VoxelShapes.empty()) {
                            break;
                        }
                    } else {
                        // A block containing any liquid stops the drip.
                        break;
                    }
                }
            }
        }
        // Copper oxidation.
        tickDegradation(blockState, serverLevel, blockPos, random);
    }

    @Override
    public boolean hasRandomTicks(@NotNull BlockState blockState) {
        return Oxidizable.getIncreasedOxidationBlock(blockState.getBlock()).isPresent() ||
            blockState.get(FLUID) == PipeFluid.WATER || blockState.get(FLUID) == PipeFluid.LAVA;
    }

    @Override
    public void randomDisplayTick(
        @NotNull BlockState blockState, @NotNull World world, @NotNull BlockPos blockPos, Random random) {
        Direction direction = blockState.get(FACING);
        BlockPos offsetPos = blockPos.offset(direction);
        BlockState offsetState = world.getBlockState(offsetPos);
        FluidState fluidState = offsetState.getFluidState();
        boolean canWater = blockState.get(FLUID) == PipeFluid.WATER && direction != Direction.UP;
        boolean canLava = blockState.get(FLUID) == PipeFluid.LAVA && direction != Direction.UP &&
            random.nextInt(2) == 0;
        boolean canWaterOrLava = canWater || canLava;
        if (canWaterOrLava) {
            double outX = blockPos.getX() + getDripX(direction, random);
            double outY = blockPos.getY() + getDripY(direction, random);
            double outZ = blockPos.getZ() + getDripZ(direction, random);
            if ((fluidState.isEmpty() || ((fluidState.getHeight(world, offsetPos)) + (double) offsetPos.getY()) < outY)) {
                world.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA,
                    outX, outY, outZ, 0, 0, 0);
            }
            if ((!offsetState.isAir() && fluidState.isEmpty())) {
                double x = blockPos.getX() + getDripX(direction, random);
                double y = blockPos.getY() + getDripY(direction, random);
                double z = blockPos.getZ() + getDripZ(direction, random);
                if (direction == Direction.DOWN) {
                    world.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA,
                        x, y, z, 0, 0, 0);
                }
            }
        }
        // If it is electified, create sparks.
        if (blockState.get(HAS_ELECTRICITY)) {
            ParticleUtil.spawnParticle(
                direction.getAxis(), world, blockPos, 0.4D,
                ParticleTypes.ELECTRIC_SPARK, UniformIntProvider.create(1, 2));
        }
        // If the pipe is in water.
        if (fluidState.isIn(FluidTags.WATER))
            if (random.nextFloat() < 0.1F || offsetState.getCollisionShape(world, offsetPos).isEmpty()) {
                world.addParticle(ParticleTypes.BUBBLE,
                    blockPos.getX() + getDripX(direction, random),
                    blockPos.getY() + getDripY(direction, random),
                    blockPos.getZ() + getDripZ(direction, random),
                    direction.getOffsetX() * 0.7D,
                    direction.getOffsetY() * 0.7D,
                    direction.getOffsetZ() * 0.7D);
                if (canLava && random.nextFloat() < 0.5F) {
                    world.addParticle(ParticleTypes.SMOKE,
                        blockPos.getX() + getDripX(direction, random),
                        blockPos.getY() + getDripY(direction, random),
                        blockPos.getZ() + getDripZ(direction, random),
                        direction.getOffsetX() * 0.05D,
                        direction.getOffsetY() * 0.05D,
                        direction.getOffsetZ() * 0.05D);
                }
            }
    }

    @Override
    public @NotNull OxidationLevel getDegradationLevel() {
        return oxidation;
    }

    /**
     * The pipe was removed or its state changed.
     */
    @Override
    public void onStateReplaced(
        BlockState oldState, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (oldState.isOf(newState.getBlock())) {
            // Update block entity with new values.
            updateBlockEntityValues(world, pos, newState);
        } else {
            // Remove block and block entity.
            if (world.getBlockEntity(pos) instanceof CopperPipeEntity copperPipeEntity) {
                // Drop inventory.
                ItemScatterer.spawn(world, pos, copperPipeEntity);
                world.updateComparators(pos, this);
            }
            world.removeBlockEntity(pos);
        }
    }

    @Override
    protected @NotNull MapCodec<? extends CopperPipe> getCodec() {
        return CODEC;
    }
}
