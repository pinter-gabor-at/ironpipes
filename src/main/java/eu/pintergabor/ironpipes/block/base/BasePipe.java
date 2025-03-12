package eu.pintergabor.ironpipes.block.base;

import eu.pintergabor.ironpipes.block.CopperPipe;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;

import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;


public abstract class BasePipe extends BaseBlock {
    // Properties.
    public static final EnumProperty<Direction> FACING =
        Properties.FACING;
    public static final BooleanProperty FRONT_CONNECTED =
        ModBlockStateProperties.FRONT_CONNECTED;
    public static final BooleanProperty BACK_CONNECTED =
        ModBlockStateProperties.BACK_CONNECTED;
    public static final BooleanProperty SMOOTH =
        ModBlockStateProperties.SMOOTH;
    // Shapes.
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

    protected BasePipe(Settings settings) {
        super(settings);
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
     * Check if a pipe can connect to another without an extension.
     * <p>
     * The pipe can connect to another pipe without an extension
     * if both pipes are facing the same or opposite direction.
     * The pipe can never connect to a fitting without an extension.
     *
     * @param otherBlockState State of the other block
     * @param direction       Direction of this pipe
     * @return true if an extension is needed.
     */
    private static boolean needExtension(
        @NotNull BlockState otherBlockState, @NotNull Direction direction) {
        // Get the block in front of the pipe.
        Block otherBlock = otherBlockState.getBlock();
        if (otherBlock instanceof BasePipe) {
            Direction facing = otherBlockState.get(BasePipe.FACING);
            // The pipe can connect to another pipe without an extension
            // if both pipes are facing the same or opposite direction.
            return facing != direction.getOpposite() && facing != direction;
        }
        // The pipe can never connect to a fitting without an extension,
        // but the pipe can connect to any other block without an extension.
        return otherBlock instanceof BaseFitting;
    }

    /**
     * Check if the front of the pipe can connect to another without an extension.
     *
     * @param world     The world
     * @param blockPos  Position of this pipe
     * @param direction Direction of this pipe
     * @return true if an extension is needed.
     */
    public static boolean needFrontExtension(
        @NotNull BlockView world,
        @NotNull BlockPos blockPos, Direction direction) {
        // Get the state of the block in front of the pipe.
        BlockState state = world.getBlockState(blockPos.offset(direction));
        // Check if an extension is needed to connect to it.
        return needExtension(state, direction);
    }

    /**
     * Check if the back of the pipe can connect to another without an extension.
     *
     * @param world     The world
     * @param blockPos  Position of this pipe
     * @param direction Direction of this pipe
     * @return true if an extension is needed.
     */
    public static boolean needBackExtension(
        @NotNull BlockView world,
        @NotNull BlockPos blockPos, @NotNull Direction direction) {
        // Get the state of the block at the back of the pipe.
        BlockState state = world.getBlockState(blockPos.offset(direction.getOpposite()));
        // Check if an extension is needed to connect to it.
        return needExtension(state, direction);
    }

    /**
     * Check if the front of the pipe shall be rendered smooth.
     * <p>
     * The pipe face is smooth, if it is facing this direction, and the front is not connected to anything.
     */
    public static boolean isSmooth(@NotNull BlockView world, @NotNull BlockPos blockPos, Direction direction) {
        // Get the state of the block in front of the pipe.
        BlockState otherBlockState = world.getBlockState(blockPos.offset(direction));
        Block otherBlock = otherBlockState.getBlock();
        if (otherBlock instanceof BasePipe) {
            Direction facing = otherBlockState.get(BasePipe.FACING);
            return facing == direction && !needExtension(otherBlockState, direction);
        }
        return false;
    }

    /**
     * Determine the initial state of the pipe based on its surroundings.
     *
     * @return the initial state of the block
     */
    @NotNull
    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext context) {
        Direction direction = context.getSide();
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        return getDefaultState()
            .with(FACING, direction)
            .with(FRONT_CONNECTED, needFrontExtension(world, pos, direction))
            .with(BACK_CONNECTED, needBackExtension(world, pos, direction))
            .with(SMOOTH, isSmooth(world, pos, direction));
    }

    /**
     * @return a random number in the range of [-0.25…+0.25]
     */
    private static double getDripRnd(Random random) {
        return random.nextDouble() / 2.0 - 0.25;
    }

    /**
     * Utility for calculating drip point for dripping pipes.
     *
     * @return random drip point X.
     */
    protected static double getDripX(@NotNull Direction direction, Random random) {
        return switch (direction) {
            case DOWN, SOUTH, NORTH -> 0.5 + getDripRnd(random);
            case UP -> 0.5;
            case EAST -> 1.05;
            case WEST -> -0.05;
        };
    }

    /**
     * Utility for calculating drip point for dripping pipes.
     *
     * @return random drip point Y.
     */
    protected static double getDripY(@NotNull Direction direction, Random random) {
        return switch (direction) {
            case DOWN -> -0.05;
            case UP -> 1.05;
            case NORTH, WEST, EAST, SOUTH -> 0.4375 + getDripRnd(random);
        };
    }

    /**
     * Utility for calculating drip point for dripping pipes.
     *
     * @return random drip point Z.
     */
    protected static double getDripZ(@NotNull Direction direction, Random random) {
        return switch (direction) {
            case DOWN, EAST, WEST -> 0.5 + getDripRnd(random);
            case UP -> 0.5;
            case NORTH -> -0.05;
            case SOUTH -> 1.05;
        };
    }
}
