package eu.pintergabor.ironpipes.block.entity.base;

import eu.pintergabor.ironpipes.block.base.BaseBlock;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;

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
    public static boolean isWaterSource(BlockState state) {
        Block block = state.getBlock();
        // If it is a still or flowing water block.
        boolean ret = (block == Blocks.WATER);
        // If it is a full water cauldron.
        ret = ret ||
            (block == Blocks.WATER_CAULDRON &&
                (state.get(Properties.LEVEL_3) == 3));
        if (!ret) {
            if (block instanceof BaseBlock) {
                // If it is a pipe or fitting carrying water.
                ret = state.contains(ModBlockStateProperties.FLUID) &&
                    (state.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER);
            } else {
                // If it is a waterlogged block.
                ret = state.contains(Properties.WATERLOGGED) &&
                    (state.get(Properties.WATERLOGGED));
            }
        }
        return ret;
    }

    /**
     * Check if the block can be used as a water source.
     *
     * @param state {@link BlockState} (which includes reference to the {@link Block})
     * @return true if it is a water source
     */
    public static boolean isLavaSource(BlockState state) {
        Block block = state.getBlock();
        // If it is a still or flowing lava block.
        boolean ret = (block == Blocks.LAVA);
        // If it is a lava cauldron.
        ret = ret || block == Blocks.LAVA_CAULDRON;
        if (!ret && block instanceof BaseBlock) {
            // If it is a pipe or fitting carrying lava.
            ret = state.contains(ModBlockStateProperties.FLUID) &&
                (state.get(ModBlockStateProperties.FLUID) == PipeFluid.LAVA);
        }
        return ret;
    }
}
