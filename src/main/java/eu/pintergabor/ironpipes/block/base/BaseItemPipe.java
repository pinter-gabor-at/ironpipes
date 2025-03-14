package eu.pintergabor.ironpipes.block.base;

import net.minecraft.screen.ScreenHandler;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public abstract class BaseItemPipe extends BasePipe {

    protected BaseItemPipe(Settings settings) {
        super(settings);
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
