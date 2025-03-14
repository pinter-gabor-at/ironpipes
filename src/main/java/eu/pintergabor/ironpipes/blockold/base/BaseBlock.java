package eu.pintergabor.ironpipes.blockold.base;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


public abstract class BaseBlock extends BlockWithEntity {
//    public static final BooleanProperty POWERED =
//        Properties.POWERED;

    protected BaseBlock(Settings settings) {
        super(settings);
//        this.setDefaultState(this.getStateManager().getDefaultState()
//            .with(POWERED, false));
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
     * Pipes and fittings do not block light.
     *
     * @return true
     */
    @Override
    protected boolean isTransparent(@NotNull BlockState blockState) {
        return true;
    }

    /**
     * Entities cannot walk through a pipe or a fitting.
     *
     * @return false
     */
    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType navType) {
        return false;
    }

    @Override
    @NotNull
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    /**
     * Pipes and fitting have comparator output.
     * <p>
     * See {@link #getComparatorOutput(BlockState, World, BlockPos)}
     *
     * @return true
     */
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
}
