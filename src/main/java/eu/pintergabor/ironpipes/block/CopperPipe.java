package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
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
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.tick.ScheduledTickView;

public class CopperPipe extends BlockWithEntity implements Waterloggable, Oxidizable {
    public static final EnumProperty<Direction> FACING =
        Properties.FACING;
    public static final BooleanProperty FRONT_CONNECTED =
        ModBlockStateProperties.FRONT_CONNECTED;
    public static final BooleanProperty BACK_CONNECTED =
        ModBlockStateProperties.BACK_CONNECTED;
    public static final BooleanProperty SMOOTH =
        ModBlockStateProperties.SMOOTH;
    public static final BooleanProperty POWERED =
        Properties.POWERED;
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
            OxidationLevel.CODEC.fieldOf("weather_state")
                .forGetter((copperPipe -> copperPipe.weatherState)),
            createSettingsCodec(),
            Codec.INT.fieldOf("cooldown")
                .forGetter((copperPipe) -> copperPipe.cooldown),
            Codec.INT.fieldOf("dispense_shot_length")
                .forGetter((copperPipe) -> copperPipe.dispenseShotLength)
        ).apply(instance, CopperPipe::new));
    public final int cooldown;
    public final int dispenseShotLength;
    private final OxidationLevel weatherState;

    public CopperPipe(OxidationLevel weatherState, Settings settings, int cooldown, int dispenseShotLength) {
        super(settings);
        this.weatherState = weatherState;
        this.cooldown = cooldown;
        this.dispenseShotLength = dispenseShotLength;
        this.setDefaultState(this.getStateManager().getDefaultState()
            .with(FACING, Direction.DOWN)
            .with(SMOOTH, false)
            .with(WATERLOGGED, false)
            .with(FLUID, PipeFluid.NONE)
            .with(HAS_ELECTRICITY, false)
            .with(POWERED, false)
        );
    }

    @SuppressWarnings("unused")
    public CopperPipe(Settings settings, int cooldown, int dispenseShotLength) {
        this(OxidationLevel.UNAFFECTED, settings, cooldown, dispenseShotLength);
    }

    public static void updateBlockEntityValues(World level, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            Direction direction = state.get(Properties.FACING);
            BlockState dirState = level.getBlockState(pos.offset(direction));
            BlockState oppState = level.getBlockState(pos.offset(direction.getOpposite()));
            Block oppBlock = oppState.getBlock();
            if (level.getBlockEntity(pos) instanceof CopperPipeEntity pipe) {
                pipe.canDispense =
                    (dirState.isAir() || dirState.getBlock() == Blocks.WATER) &&
                        (!oppState.isAir() && oppBlock != Blocks.WATER);
                pipe.shootsControlled = (oppBlock == Blocks.DROPPER);
                pipe.shootsSpecial = (oppBlock == Blocks.DISPENSER);
                pipe.canAccept = !(
                    oppBlock instanceof CopperPipe ||
                        oppBlock instanceof CopperFitting ||
                        oppState.isSolidBlock(level, pos));
                pipe.canWater = ((oppBlock == Blocks.WATER) ||
                    state.get(Properties.WATERLOGGED) ||
                    (oppState.contains(Properties.WATERLOGGED) && oppState.get(Properties.WATERLOGGED))) &&
                    SimpleCopperPipesConfig.get().carryWater;
                pipe.canLava =
                    (oppBlock == Blocks.LAVA) &&
                        SimpleCopperPipesConfig.get().carryLava;
                boolean canWaterAndLava = pipe.canWater && pipe.canLava;
                boolean canWaterOrLava = pipe.canWater || pipe.canLava;
                pipe.canSmoke = (oppBlock instanceof CampfireBlock &&
                    !canWaterOrLava ? oppState.get(Properties.LIT) : canWaterAndLava) &&
                    SimpleCopperPipesConfig.get().carrySmoke;
                if (canWaterAndLava) {
                    pipe.canWater = false;
                    pipe.canLava = false;
                }
            }
        }
    }

    public static boolean canConnectFront(@NotNull WorldView level, @NotNull BlockPos blockPos, Direction direction) {
        BlockState state = level.getBlockState(blockPos.offset(direction));
        if (state.getBlock() instanceof CopperPipe) {
            return state.get(CopperPipe.FACING) != direction.getOpposite() && state.get(CopperPipe.FACING) != direction;
        }
        return state.getBlock() instanceof CopperFitting;
    }

    public static boolean canConnectBack(@NotNull WorldView level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        BlockState state = level.getBlockState(blockPos.offset(direction.getOpposite()));
        if (state.getBlock() instanceof CopperPipe) {
            return state.get(CopperPipe.FACING) != direction.getOpposite() && state.get(CopperPipe.FACING) != direction;
        }
        return state.getBlock() instanceof CopperFitting;
    }

    public static boolean isSmooth(@NotNull WorldView level, @NotNull BlockPos blockPos, Direction direction) {
        BlockState state = level.getBlockState(blockPos.offset(direction));
        if (state.getBlock() instanceof CopperPipe) {
            return state.get(CopperPipe.FACING) == direction && !canConnectFront(level, blockPos, direction);
        }
        return false;
    }

    public static boolean canConnectFront(@NotNull WorldAccess level, @NotNull BlockPos blockPos, Direction direction) {
        BlockState state = level.getBlockState(blockPos.offset(direction));
        if (state.getBlock() instanceof CopperPipe) {
            return state.get(CopperPipe.FACING) != direction.getOpposite() && state.get(CopperPipe.FACING) != direction;
        }
        return state.getBlock() instanceof CopperFitting;
    }

    public static boolean canConnectBack(@NotNull WorldAccess level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        BlockState state = level.getBlockState(blockPos.offset(direction.getOpposite()));
        if (state.getBlock() instanceof CopperPipe) {
            return state.get(CopperPipe.FACING) != direction.getOpposite() && state.get(CopperPipe.FACING) != direction;
        }
        return state.getBlock() instanceof CopperFitting;
    }

    public static boolean isSmooth(@NotNull WorldAccess level, @NotNull BlockPos blockPos, Direction direction) {
        BlockState state = level.getBlockState(blockPos.offset(direction));
        if (state.getBlock() instanceof CopperPipe) {
            return state.get(CopperPipe.FACING) == direction && !canConnectFront(level, blockPos, direction);
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

    public static boolean isReceivingRedstonePower(BlockPos blockPos, World level) {
        for (Direction direction : Direction.values()) {
            if (level.getEmittedRedstonePower(blockPos.offset(direction), direction) > 0) {
                return true;
            }
        }
        return false;
    }

    public VoxelShape getPipeShape(BlockState blockState) {
        boolean front = blockState.get(FRONT_CONNECTED);
        boolean back = blockState.get(BACK_CONNECTED);
        boolean smooth = blockState.get(SMOOTH);
        if (smooth && back) {
            return switch (blockState.get(FACING)) {
                case DOWN -> DOWN_BACK_SMOOTH;
                case UP -> UP_BACK_SMOOTH;
                case NORTH -> NORTH_BACK_SMOOTH;
                case SOUTH -> SOUTH_BACK_SMOOTH;
                case EAST -> EAST_BACK_SMOOTH;
                case WEST -> WEST_BACK_SMOOTH;
            };
        }
        if (smooth) {
            return switch (blockState.get(FACING)) {
                case DOWN, UP -> DOWN_SMOOTH;
                case NORTH, SOUTH -> NORTH_SMOOTH;
                case EAST, WEST -> EAST_SMOOTH;
            };
        }
        if (front && back) {
            return switch (blockState.get(FACING)) {
                case DOWN, UP -> DOWN_DOUBLE;
                case NORTH, SOUTH -> NORTH_DOUBLE;
                case EAST, WEST -> EAST_DOUBLE;
            };
        }
        if (front) {
            return switch (blockState.get(FACING)) {
                case DOWN -> DOWN_FRONT;
                case UP -> UP_FRONT;
                case NORTH -> NORTH_FRONT;
                case SOUTH -> SOUTH_FRONT;
                case EAST -> EAST_FRONT;
                case WEST -> WEST_FRONT;
            };
        }
        if (back) {
            return switch (blockState.get(FACING)) {
                case DOWN -> DOWN_BACK;
                case UP -> UP_BACK;
                case NORTH -> NORTH_BACK;
                case SOUTH -> SOUTH_BACK;
                case EAST -> EAST_BACK;
                case WEST -> WEST_BACK;
            };
        }
        return switch (blockState.get(FACING)) {
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

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext itemPlacementContext) {
        Direction direction = itemPlacementContext.getSide();
        BlockPos blockPos = itemPlacementContext.getBlockPos();
        return this.getDefaultState()
            .with(FACING, direction)
            .with(FRONT_CONNECTED, canConnectFront(itemPlacementContext.getWorld(), blockPos, direction))
            .with(BACK_CONNECTED, canConnectBack(itemPlacementContext.getWorld(), blockPos, direction))
            .with(SMOOTH, isSmooth(itemPlacementContext.getWorld(), blockPos, direction))
            .with(WATERLOGGED, itemPlacementContext.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER);
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
        Direction facing = blockState.get(FACING);
        return blockState
            .with(FRONT_CONNECTED, canConnectFront(levelReader, blockPos, facing))
            .with(BACK_CONNECTED, canConnectBack(levelReader, blockPos, facing))
            .with(SMOOTH, isSmooth(levelReader, blockPos, facing))
            .with(HAS_ELECTRICITY, electricity);
    }

    @Override
    protected void neighborUpdate(@NotNull BlockState blockState, @NotNull World level, BlockPos blockPos, Block block, @Nullable WireOrientation orientation, boolean bl) {
        boolean powered = isReceivingRedstonePower(blockPos, level);
        if (powered != blockState.get(POWERED)) {
            level.setBlockState(blockPos, blockState.with(POWERED, powered));
        }
        updateBlockEntityValues(level, blockPos, blockState);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperPipeEntity(pos, state);
    }

    @Override
    protected boolean isTransparent(@NotNull BlockState blockState) {
        return blockState.getFluidState().isEmpty();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull World level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClient()) {
            return validateTicker(
                blockEntityType, ModBlockEntities.COPPER_PIPE_ENTITY,
                (level1, blockPos, blockState1, copperPipeEntity) ->
                    copperPipeEntity.serverTick(level1, blockPos, blockState1)
            );
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getGameEventListener(
        ServerWorld serverLevel, T blockEntity) {
        if (blockEntity instanceof CopperPipeEntity pipeEntity) {
            return pipeEntity.getEventListener();
        }
        return null;
    }

    @Override
    public void onPlaced(
        World level, BlockPos blockPos, BlockState blockState,
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
        BlockState state, @NotNull World level, BlockPos pos,
        PlayerEntity player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CopperPipeEntity copperPipeEntity) {
            player.openHandledScreen(copperPipeEntity);
            player.incrementStat(Stats.CUSTOM.getOrCreateStat(
                ModStats.INSPECT_PIPE));
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
    @NotNull
    public BlockState rotate(@NotNull BlockState blockState, BlockRotation blockRotation) {
        return blockState.with(FACING, blockRotation.rotate(blockState.get(FACING)));
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

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType pathComputationType) {
        return false;
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, ServerWorld serverLevel, BlockPos blockPos, Random random) {
        Direction direction = blockState.get(FACING);
        boolean isLava = blockState.get(FLUID) == PipeFluid.LAVA;
        if (blockState.get(FLUID) == PipeFluid.WATER || isLava && direction != Direction.UP) {
            if (random.nextFloat() <= (isLava ? 0.05859375F : 0.17578125F) * 2) {
                BlockPos.Mutable mutableBlockPos = blockPos.mutableCopy();
                boolean hasOffset = false;
                for (int i = 0; i < 12; i++) { //Searches for 12 blocks
                    if (direction != Direction.DOWN && !hasOffset) {
                        mutableBlockPos.move(direction);
                        hasOffset = true;
                    }
                    mutableBlockPos.move(Direction.DOWN);
                    BlockState state = serverLevel.getBlockState(mutableBlockPos);
                    if (serverLevel.getFluidState(mutableBlockPos).isEmpty()) {
                        LeakingPipeDripBehaviors.DripOn dripOn = LeakingPipeDripBehaviors.getDrip(state.getBlock());
                        if (dripOn != null) {
                            dripOn.dripOn(isLava, serverLevel, mutableBlockPos, state);
                            break;
                        }
                        if (state.getCollisionShape(serverLevel, mutableBlockPos) != VoxelShapes.empty()) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        this.tickDegradation(blockState, serverLevel, blockPos, random);
    }

    @Override
    public boolean hasRandomTicks(@NotNull BlockState blockState) {
        return Oxidizable.getIncreasedOxidationBlock(blockState.getBlock()).isPresent() || blockState.get(FLUID) == PipeFluid.WATER || blockState.get(FLUID) == PipeFluid.LAVA;
    }

    @Override
    public void randomDisplayTick(@NotNull BlockState blockState, @NotNull World level, @NotNull BlockPos blockPos, Random random) {
        Direction direction = blockState.get(FACING);
        BlockPos offsetPos = blockPos.offset(direction);
        BlockState offsetState = level.getBlockState(offsetPos);
        FluidState fluidState = offsetState.getFluidState();
        boolean canWater = blockState.get(FLUID) == PipeFluid.WATER && direction != Direction.UP;
        boolean canLava = blockState.get(FLUID) == PipeFluid.LAVA && random.nextInt(2) == 0 && direction != Direction.UP;
        boolean canSmoke = blockState.get(FLUID) == PipeFluid.SMOKE && random.nextInt(5) == 0;
        boolean canWaterOrLava = canWater || canLava;
        boolean hasSmokeOrWaterOrLava = canWaterOrLava || canSmoke;
        if (hasSmokeOrWaterOrLava) {
            double outX = blockPos.getX() + getDripX(direction, random);
            double outY = blockPos.getY() + getDripY(direction, random);
            double outZ = blockPos.getZ() + getDripZ(direction, random);
            if (canWaterOrLava && (fluidState.isEmpty() || ((fluidState.getHeight(level, offsetPos)) + (double) offsetPos.getY()) < outY)) {
                level.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA, outX, outY, outZ, 0, 0, 0);
            }
            if (canSmoke) {
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, outX, outY, outZ, 0, 0.07D, 0);
            }
            if ((!offsetState.isAir() && fluidState.isEmpty())) {
                double x = blockPos.getX() + getDripX(direction, random);
                double y = blockPos.getY() + getDripY(direction, random);
                double z = blockPos.getZ() + getDripZ(direction, random);
                if (canWaterOrLava && direction == Direction.DOWN) {
                    level.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA, x, outY, z, 0, 0, 0);
                }
                if (canSmoke && direction == Direction.UP) {
                    level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0.07D, 0);
                }
            }
        }
        // If it is electified, create sparks.
        if (blockState.get(HAS_ELECTRICITY)) {
            ParticleUtil.spawnParticle(
                direction.getAxis(), level, blockPos, 0.4D,
                ParticleTypes.ELECTRIC_SPARK, UniformIntProvider.create(1, 2));
        }
        if (fluidState.isIn(FluidTags.WATER) && (random.nextFloat() <= 0.1F || offsetState.getCollisionShape(level, offsetPos).isEmpty())) {
            level.addParticle(ParticleTypes.BUBBLE,
                blockPos.getX() + getDripX(direction, random),
                blockPos.getY() + getDripY(direction, random),
                blockPos.getZ() + getDripZ(direction, random),
                direction.getOffsetX() * 0.7D,
                direction.getOffsetY() * 0.7D,
                direction.getOffsetZ() * 0.7D
            );
            if ((canLava || canSmoke) && random.nextInt(2) == 0) {
                level.addParticle(ParticleTypes.SMOKE,
                    blockPos.getX() + getDripX(direction, random),
                    blockPos.getY() + getDripY(direction, random),
                    blockPos.getZ() + getDripZ(direction, random),
                    direction.getOffsetX() * 0.05D,
                    direction.getOffsetY() * 0.05D,
                    direction.getOffsetZ() * 0.05D
                );
            }
        }
    }

    public double getRan(Random random) {
        return UniformIntProvider.create(-25, 25).get(random) * 0.01;
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
        return this.weatherState;
    }

    @Override
    public void onStateReplaced(BlockState blockState, World level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        updateBlockEntityValues(level, blockPos, blockState);
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperPipe)) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) {
                ItemScatterer.spawn(level, blockPos, (CopperPipeEntity) blockEntity);
                level.updateComparators(blockPos, this);
            }
            level.removeBlockEntity(blockPos);
        }
    }

    @Override
    protected @NotNull MapCodec<? extends CopperPipe> getCodec() {
        return CODEC;
    }
}
