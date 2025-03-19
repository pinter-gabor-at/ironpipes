package eu.pintergabor.ironpipes.block.entity.base;

import eu.pintergabor.ironpipes.block.base.BaseFitting;
import eu.pintergabor.ironpipes.block.base.BasePipe;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModProperties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;


public class FluidUtil {

    /**
     * Check if the block can be used as a water source.
     *
     * @param state {@link BlockState} (which includes reference to the {@link Block})
     * @return true if it is a water source
     */
    @SuppressWarnings("RedundantIfStatement")
    public static boolean isWaterSource(BlockState state) {
        Block block = state.getBlock();
        // If it is a still or flowing water block.
        if (block == Blocks.WATER) {
            return true;
        }
        // If it is a full water cauldron.
        if (block == Blocks.WATER_CAULDRON &&
            (state.get(Properties.LEVEL_3) == 3)) {
            return true;
        }
        if (state.get(Properties.WATERLOGGED, false)) {
            // If it is a waterlogged block.
            return true;
        }
        if (block instanceof BasePipe) {
            // If it is a pipe carrying water.
            if (state.contains(ModProperties.FLUID) &&
                (state.get(ModProperties.FLUID) == PipeFluid.WATER)) {
                return true;
            }
        }
        if (block instanceof BaseFitting) {
            // If it is an unpowered fitting carrying water.
            if (!state.get(Properties.POWERED, false) &&
                state.get(ModProperties.FLUID, PipeFluid.NONE) == PipeFluid.WATER) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the block can be used as a water source.
     *
     * @param state {@link BlockState} (which includes reference to the {@link Block})
     * @return true if it is a water source
     */
    @SuppressWarnings("RedundantIfStatement")
    public static boolean isLavaSource(BlockState state) {
        Block block = state.getBlock();
        // If it is a still or flowing lava block.
        if (block == Blocks.LAVA) {
            return true;
        }
        // If it is a lava cauldron.
        if (block == Blocks.LAVA_CAULDRON) {
            return true;
        }
        if (block instanceof BasePipe) {
            // If it is a pipe carrying lava.
            if (state.get(ModProperties.FLUID, PipeFluid.NONE) == PipeFluid.LAVA) {
                return true;
            }
        }
        if (block instanceof BaseFitting) {
            // If it is an unpowered fitting carrying lava.
            if (!state.get(Properties.POWERED, false) &&
                state.get(ModProperties.FLUID, PipeFluid.NONE) == PipeFluid.LAVA) {
                return true;
            }
        }
        return false;
    }
}
