package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.pintergabor.ironpipes.block.base.BasePipe;
import eu.pintergabor.ironpipes.block.entity.CopperPipeEntity;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeDripBehaviors;
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
import net.minecraft.block.Blocks;
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
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;


public class CopperPipe extends BasePipe implements Waterloggable, Oxidizable {
    public static final EnumProperty<Direction> FACING =
        Properties.FACING;
    public static final BooleanProperty FRONT_CONNECTED =
        ModBlockStateProperties.FRONT_CONNECTED;
    public static final BooleanProperty BACK_CONNECTED =
        ModBlockStateProperties.BACK_CONNECTED;
    public static final BooleanProperty SMOOTH =
        ModBlockStateProperties.SMOOTH;
    public static final BooleanProperty WATERLOGGED =
        Properties.WATERLOGGED;
    public static final EnumProperty<PipeFluid> FLUID =
        ModBlockStateProperties.FLUID;
    public static final BooleanProperty HAS_ELECTRICITY =
        ModBlockStateProperties.HAS_ELECTRICITY;
    private static final VoxelShape UP_SHAPE =
        VoxelShapes.union(Block.createCuboidShape(4D, 0D, 4D, 12D, 14D, 12D),
            Block.createCuboidShape(3D, 14D, 3D, 13D, 16D, 13D));
    private static final VoxelShape DOWN_SHAPE =
        VoxelShapes.union(Block.createCuboidShape(4D, 2D, 4D, 12D, 16D, 12D),
            Block.createCuboidShape(3D, 0D, 3D, 13D, 2D, 13D));
    private static final VoxelShape NORTH_SHAPE =
        VoxelShapes.union(Block.createCuboidShape(4D, 4D, 2D, 12D, 12D, 16D),
            Block.createCuboidShape(3D, 3D, 0.D, 13D, 13D, 2D));
    private static final VoxelShape SOUTH_SHAPE =
        VoxelShapes.union(Block.createCuboidShape(4D, 4D, 0D, 12D, 12D, 14D),
            Block.createCuboidShape(3D, 3D, 14.D, 13D, 13D, 16D));
    private static final VoxelShape EAST_SHAPE =
        VoxelShapes.union(Block.createCuboidShape(0D, 4D, 4D, 14D, 12D, 12D),
            Block.createCuboidShape(14D, 3D, 3D, 16D, 13D, 13D));
    private static final VoxelShape WEST_SHAPE =
        VoxelShapes.union(Block.createCuboidShape(2D, 4D, 4D, 16D, 12D, 12D),
            Block.createCuboidShape(0D, 3D, 3D, 2D, 13D, 13D));
    private static final VoxelShape DOWN_FRONT =
        VoxelShapes.union(Block.createCuboidShape(4D, -2D, 4D, 12D, 16D, 12D),
            Block.createCuboidShape(3D, -4D, 3D, 13D, -2D, 13D));
    private static final VoxelShape EAST_FRONT =
        VoxelShapes.union(Block.createCuboidShape(0D, 4D, 4D, 18D, 12D, 12D),
            Block.createCuboidShape(18D, 3D, 3D, 20D, 13D, 13D));
    private static final VoxelShape NORTH_FRONT =
        VoxelShapes.union(Block.createCuboidShape(4D, 4D, -2D, 12D, 12D, 16D),
            Block.createCuboidShape(3D, 3D, -4D, 13D, 13D, -2D));
    private static final VoxelShape SOUTH_FRONT =
        VoxelShapes.union(Block.createCuboidShape(4D, 4D, 0D, 12D, 12D, 18D),
            Block.createCuboidShape(3D, 3D, 18.D, 13D, 13D, 20D));
    private static final VoxelShape WEST_FRONT =
        VoxelShapes.union(Block.createCuboidShape(-2D, 4D, 4D, 16D, 12D, 12D),
            Block.createCuboidShape(-4D, 3D, 3D, -2D, 13D, 13D));
    private static final VoxelShape UP_FRONT =
        VoxelShapes.union(Block.createCuboidShape(4D, 0D, 4D, 12D, 18D, 12D),
            Block.createCuboidShape(3D, 18D, 3D, 13D, 20D, 13D));
    private static final VoxelShape DOWN_BACK =
        VoxelShapes.union(Block.createCuboidShape(4D, 2D, 4D, 12D, 20D, 12D),
            Block.createCuboidShape(3D, 0D, 3D, 13D, 2D, 13D));
    private static final VoxelShape EAST_BACK =
        VoxelShapes.union(Block.createCuboidShape(-4D, 4D, 4D, 14D, 12D, 12D),
            Block.createCuboidShape(14D, 3D, 3D, 16D, 13D, 13D));
    private static final VoxelShape NORTH_BACK =
        VoxelShapes.union(Block.createCuboidShape(4D, 4D, 2D, 12D, 12D, 20D),
            Block.createCuboidShape(3D, 3D, 0.D, 13D, 13D, 2D));
    private static final VoxelShape SOUTH_BACK =
        VoxelShapes.union(Block.createCuboidShape(4D, 4D, -4D, 12D, 12D, 14D),
            Block.createCuboidShape(3D, 3D, 14.D, 13D, 13D, 16D));
    private static final VoxelShape WEST_BACK =
        VoxelShapes.union(Block.createCuboidShape(2D, 4D, 4D, 20D, 12D, 12D),
            Block.createCuboidShape(0D, 3D, 3D, 2D, 13D, 13D));
    private static final VoxelShape UP_BACK =
        VoxelShapes.union(Block.createCuboidShape(4D, -4D, 4D, 12D, 14D, 12D),
            Block.createCuboidShape(3D, 14D, 3D, 13D, 16D, 13D));
    private static final VoxelShape DOWN_DOUBLE =
        Block.createCuboidShape(4D, -4D, 4D, 12D, 20D, 12D);
    private static final VoxelShape NORTH_DOUBLE =
        Block.createCuboidShape(4D, 4D, -4D, 12D, 12D, 20D);
    private static final VoxelShape EAST_DOUBLE =
        Block.createCuboidShape(-4D, 4D, 4D, 20D, 12D, 12D);
    private static final VoxelShape DOWN_SMOOTH =
        Block.createCuboidShape(4D, 0D, 4D, 12D, 16D, 12D);
    private static final VoxelShape NORTH_SMOOTH =
        Block.createCuboidShape(4D, 4D, 0D, 12D, 12D, 16D);
    private static final VoxelShape EAST_SMOOTH =
        Block.createCuboidShape(0D, 4D, 4D, 16D, 12D, 12D);
    private static final VoxelShape DOWN_BACK_SMOOTH =
        Block.createCuboidShape(4D, 0D, 4D, 12D, 20D, 12D);
    private static final VoxelShape NORTH_BACK_SMOOTH =
        Block.createCuboidShape(4D, 4D, 0D, 12D, 12D, 20D);
    private static final VoxelShape SOUTH_BACK_SMOOTH =
        Block.createCuboidShape(4D, 4D, -4D, 12D, 12D, 16D);
    private static final VoxelShape EAST_BACK_SMOOTH =
        Block.createCuboidShape(-4D, 4D, 4D, 16D, 12D, 12D);
    private static final VoxelShape WEST_BACK_SMOOTH =
        Block.createCuboidShape(0D, 4D, 4D, 20D, 12D, 12D);
    private static final VoxelShape UP_BACK_SMOOTH =
        Block.createCuboidShape(4D, -4D, 4D, 12D, 16D, 12D);
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
    public static void updateBlockEntityValues(World world, BlockPos pos, @NotNull BlockState state) {
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
                //
                pipeEntity.canDispense = (frontState.isAir() || frontBlock == Blocks.WATER);
                pipeEntity.shootsControlled = (backBlock == Blocks.DROPPER);
                pipeEntity.shootsSpecial = (backBlock == Blocks.DISPENSER);
                pipeEntity.canAccept = !(
                    backBlock instanceof CopperPipe ||
                        backBlock instanceof CopperFitting ||
                        backState.isSolidBlock(world, pos));
                pipeEntity.canWater = SimpleCopperPipesConfig.get().carryWater &&
                    ((backBlock == Blocks.WATER) || state.get(Properties.WATERLOGGED) ||
                        (backState.contains(Properties.WATERLOGGED) && backState.get(Properties.WATERLOGGED)));
                pipeEntity.canLava = SimpleCopperPipesConfig.get().carryLava &&
                    (backBlock == Blocks.LAVA);
                if (pipeEntity.canWater && pipeEntity.canLava) {
                    pipeEntity.canWater = false;
                    pipeEntity.canLava = false;
                }
            }
        }
    }

    /**
     * Check if a pipe can connect to another without an extension.
     * <p>
     * The pipe can connect to another pipe without an extension if both pipes are looking in the same way.
     * The pipe can never connect to a fitting without an extension.
     *
     * @param state     State of the connecting pipe
     * @param direction Direction of this pipe
     * @return true if an extension is needed.
     */
    private static boolean canConnect(@NotNull BlockState state, @NotNull Direction direction) {
        Block block = state.getBlock();
        if (block instanceof CopperPipe) {
            Direction facing = state.get(CopperPipe.FACING);
            return facing != direction.getOpposite() && facing != direction;
        }
        return block instanceof CopperFitting;
    }

    /**
     * Check if the front of the pipe can connect to another without an extension.
     *
     * @param world     The world
     * @param blockPos  Position of this pipe
     * @param direction Direction of this pipe
     * @return true if an extension is needed.
     */
    public static boolean canConnectFront(@NotNull BlockView world, @NotNull BlockPos blockPos, Direction direction) {
        // Get the state of the block in front of the pipe.
        BlockState state = world.getBlockState(blockPos.offset(direction));
        // Check if it can be connected to.
        return canConnect(state, direction);
    }

    /**
     * Check if the back of the pipe can connect to another without an extension.
     *
     * @param world     The world
     * @param blockPos  Position of this pipe
     * @param direction Direction of this pipe
     * @return true if an extension is needed.
     */
    public static boolean canConnectBack(@NotNull BlockView world, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        // Get the state of the block at the back of the pipe.
        BlockState state = world.getBlockState(blockPos.offset(direction.getOpposite()));
        // Check if it can be connected to.
        return canConnect(state, direction);
    }

    /**
     * Check if the front of the pipe shall be rendered smooth.
     * <p>
     * The pipe face is smooth, if it is facing this direction, and the front is not connected to anything.
     */
    public static boolean isSmooth(@NotNull BlockView world, @NotNull BlockPos blockPos, Direction direction) {
        // Get the state of the block in front of the pipe.
        BlockState state = world.getBlockState(blockPos.offset(direction));
        Block block = state.getBlock();
        if (block instanceof CopperPipe) {
            Direction facing = state.get(CopperPipe.FACING);
            return facing == direction && !canConnect(state, direction);
        }
        return false;
    }

    public static Vec3d getOutputLocation(@NotNull BlockPos pos, @NotNull Direction facing) {
        return new Vec3d(
            ((double) pos.getX() + 0.5D) + 0.7D * (double) facing.getOffsetX(),
            ((double) pos.getY() + 0.5D) + 0.7D * (double) facing.getOffsetY(),
            ((double) pos.getZ() + 0.5D) + 0.7D * (double) facing.getOffsetZ()
        );
    }

    /**
     * Check if the pipe is receiving redstone power from any direction.
     *
     * @param world    The world
     * @param blockPos Position of this pipe
     * @return true if the pipe is receiving redstone power.
     */
    public static boolean isReceivingRedstonePower(World world, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            if (world.getEmittedRedstonePower(blockPos.offset(direction), direction) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get pipe shape for drawing the outline and detecting collisions.
     *
     * @param state The state of the pipe
     * @return the shape of the outline.
     */
    public VoxelShape getPipeShape(BlockState state) {
        boolean front = state.get(FRONT_CONNECTED);
        boolean back = state.get(BACK_CONNECTED);
        boolean smooth = state.get(SMOOTH);
        if (smooth && back) {
            return switch (state.get(FACING)) {
                case DOWN -> DOWN_BACK_SMOOTH;
                case UP -> UP_BACK_SMOOTH;
                case NORTH -> NORTH_BACK_SMOOTH;
                case SOUTH -> SOUTH_BACK_SMOOTH;
                case EAST -> EAST_BACK_SMOOTH;
                case WEST -> WEST_BACK_SMOOTH;
            };
        }
        if (smooth) {
            return switch (state.get(FACING)) {
                case DOWN, UP -> DOWN_SMOOTH;
                case NORTH, SOUTH -> NORTH_SMOOTH;
                case EAST, WEST -> EAST_SMOOTH;
            };
        }
        if (front && back) {
            return switch (state.get(FACING)) {
                case DOWN, UP -> DOWN_DOUBLE;
                case NORTH, SOUTH -> NORTH_DOUBLE;
                case EAST, WEST -> EAST_DOUBLE;
            };
        }
        if (front) {
            return switch (state.get(FACING)) {
                case DOWN -> DOWN_FRONT;
                case UP -> UP_FRONT;
                case NORTH -> NORTH_FRONT;
                case SOUTH -> SOUTH_FRONT;
                case EAST -> EAST_FRONT;
                case WEST -> WEST_FRONT;
            };
        }
        if (back) {
            return switch (state.get(FACING)) {
                case DOWN -> DOWN_BACK;
                case UP -> UP_BACK;
                case NORTH -> NORTH_BACK;
                case SOUTH -> SOUTH_BACK;
                case EAST -> EAST_BACK;
                case WEST -> WEST_BACK;
            };
        }
        return switch (state.get(FACING)) {
            case DOWN -> DOWN_SHAPE;
            case UP -> UP_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    @Override
    @NotNull
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return getPipeShape(blockState);
    }

    @Override
    @NotNull
    public VoxelShape getRaycastShape(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return getPipeShape(blockState);
    }

    /**
     * Determine the initial state of the pipe based on its surroundings.
     *
     * @return the initial state of the block
     */
    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext context) {
        Direction direction = context.getSide();
        BlockPos blockPos = context.getBlockPos();
        return getDefaultState()
            .with(FACING, direction)
            .with(FRONT_CONNECTED, canConnectFront(context.getWorld(), blockPos, direction))
            .with(BACK_CONNECTED, canConnectBack(context.getWorld(), blockPos, direction))
            .with(SMOOTH, isSmooth(context.getWorld(), blockPos, direction))
            .with(WATERLOGGED, context.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER);
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
        Random randomSource
    ) {
        if (blockState.get(WATERLOGGED)) {
            scheduledTickAccess.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        // The pipe is electrified if it is connected to a lightning rod, and lightnings strikes the lightning rod.
        boolean electricity = blockState.get(HAS_ELECTRICITY) ||
            ((neighborState.getBlock() instanceof LightningRodBlock) &&
                neighborState.get(POWERED));
        Direction facing = blockState.get(FACING);
        return blockState
            .with(FRONT_CONNECTED, canConnectFront(world, pos, facing))
            .with(BACK_CONNECTED, canConnectBack(world, pos, facing))
            .with(SMOOTH, isSmooth(world, pos, facing))
            .with(HAS_ELECTRICITY, electricity);
    }

    /**
     * Handle side effects when the neighboring block's state changes.
     */
    @Override
    protected void neighborUpdate(@NotNull BlockState blockState, @NotNull World world, BlockPos blockPos, Block block, @Nullable WireOrientation orientation, boolean bl) {
        boolean powered = isReceivingRedstonePower(world, blockPos);
        if (powered != blockState.get(POWERED)) {
            world.setBlockState(blockPos, blockState.with(POWERED, powered));
        }
        updateBlockEntityValues(world, blockPos, blockState);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperPipeEntity(pos, state);
    }

    /**
     * Pipes do not block light.
     *
     * @return true
     */
    @Override
    protected boolean isTransparent(@NotNull BlockState state) {
        return true;
    }

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
        BlockState state,
        World world,
        BlockPos pos,
        PlayerEntity player,
        Hand hand,
        BlockHitResult hitResult
    ) {
        // Allow placing pipes next to pipes and fittings.
        if (stack.isIn(ModItemTags.PIPES_AND_FITTINGS)) {
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

    /**
     * Calculate the comparator output the same way as for other blocks with inventory.
     */
    @Override
    public int getComparatorOutput(BlockState blockState, @NotNull World world, BlockPos blockPos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(blockPos));
    }

    @Override
    @NotNull
    public BlockState rotate(@NotNull BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @NotNull
    public BlockState mirror(@NotNull BlockState blockState, BlockMirror blockMirror) {
        return blockState.rotate(blockMirror.getRotation(blockState.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, FRONT_CONNECTED, BACK_CONNECTED, SMOOTH, WATERLOGGED, FLUID, HAS_ELECTRICITY, POWERED);
    }

    /**
     * Entities cannot walk through a pipe.
     */
    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType navType) {
        return false;
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
        return Oxidizable.getIncreasedOxidationBlock(blockState.getBlock()).isPresent() || blockState.get(FLUID) == PipeFluid.WATER || blockState.get(FLUID) == PipeFluid.LAVA;
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

    /**
     * Create a random number in the range of [-0.25…+0.25]
     */
    private double getRan(Random random) {
        return random.nextDouble() / 2.0 - 0.25;
    }

    public double getDripX(@NotNull Direction direction, Random random) {
        return switch (direction) {
            case DOWN, SOUTH, NORTH -> 0.5 + getRan(random);
            case UP -> 0.5;
            case EAST -> 1.05;
            case WEST -> -0.05;
        };
    }

    public double getDripY(@NotNull Direction direction, Random random) {
        return switch (direction) {
            case DOWN -> -0.05;
            case UP -> 1.05;
            case NORTH, WEST, EAST, SOUTH -> 0.4375 + MathHelper.clamp(getRan(random), -2, 0.625);
        };
    }

    public double getDripZ(@NotNull Direction direction, Random random) {
        return switch (direction) {
            case DOWN, EAST, WEST -> 0.5 + getRan(random);
            case UP -> 0.5;
            case NORTH -> -0.05;
            case SOUTH -> 1.05;
        };
    }

    @Override
    public @NotNull OxidationLevel getDegradationLevel() {
        return this.oxidation;
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
