package eu.pintergabor.ironpipes.block.entity.leaking;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;


public class LeakingPipeDripBehaviors {
    /**
     * Map blocks to drip actions.
     */
    private static final Map<Block, DripOn> BLOCKS_TO_DRIPS = new HashMap<>();

    private LeakingPipeDripBehaviors() {
        // Static class.
    }

    public static void register(Block block, DripOn drip) {
        BLOCKS_TO_DRIPS.put(block, drip);
    }

    /**
     * Check if there is any effect of dripping water or lave on a block.
     *
     * @param block Block to test
     * @return Drip action
     */
    @Nullable
    public static DripOn getDrip(Block block) {
        if (BLOCKS_TO_DRIPS.containsKey(block)) {
            return BLOCKS_TO_DRIPS.get(block);
        }
        return null;
    }

    /**
     * Initialize the lookup table.
     */
    public static void init() {
        // Water dripping on an empty cauldron will start filling it with water.
        // Lava dripping on an empty cauldron will instantly fill it with lava.
        register(Blocks.CAULDRON, ((isLava, world, pos, state) -> {
            if (!isLava) {
                world.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState().with(Properties.LEVEL_3, 1));
            } else {
                world.setBlockState(pos, Blocks.LAVA_CAULDRON.getDefaultState());
            }
        }));
        // Water dripping on a water cauldron slowly fills the cauldron.
        register(Blocks.WATER_CAULDRON, ((isLava, world, pos, state) -> {
            if (state.get(Properties.LEVEL_3) != 3 && !isLava) {
                world.setBlockState(pos, state.cycle(Properties.LEVEL_3));
            }
        }));
        // Water dripping on dirt wil change it to mud.
        register(Blocks.DIRT, ((isLava, world, pos, state) -> {
            if (!isLava) {
                world.setBlockState(pos, Blocks.MUD.getDefaultState());
            }
        }));
        // Water dripping on fire will extinguish the fire.
        register(Blocks.FIRE, ((isLava, world, pos, state) -> {
            if (!isLava) {
                world.breakBlock(pos, true);
            }
        }));
    }

    @FunctionalInterface
    public interface DripOn {
        /**
         * Drip action.
         *
         * @param isLava true = lava; false = water
         * @param world  World
         * @param pos    Position of the block
         * @param state  State of the block
         */
        void dripOn(boolean isLava, ServerWorld world, BlockPos pos, BlockState state);
    }
}
