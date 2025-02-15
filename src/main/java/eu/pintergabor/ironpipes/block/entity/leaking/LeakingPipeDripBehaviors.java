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

    private static final Map<Block, DripOn> BLOCKS_TO_DRIPS = new HashMap<>();

    public static void register(Block block, DripOn drip) {
        BLOCKS_TO_DRIPS.put(block, drip);
    }

    @Nullable
    public static DripOn getDrip(Block block) {
        if (BLOCKS_TO_DRIPS.containsKey(block)) {
            return BLOCKS_TO_DRIPS.get(block);
        }
        return null;
    }

    public static void init() {
        register(Blocks.CAULDRON, ((lava, world, pos, state) -> {
            if (!lava) {
                world.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState().with(Properties.LEVEL_3, 1));
            } else {
                world.setBlockState(pos, Blocks.LAVA_CAULDRON.getDefaultState());
            }
        }));

        register(Blocks.WATER_CAULDRON, ((lava, world, pos, state) -> {
            if (state.get(Properties.LEVEL_3) != 3 && !lava) {
                world.setBlockState(pos, state.cycle(Properties.LEVEL_3));
            }
        }));

        register(Blocks.DIRT, ((lava, world, pos, state) -> {
            if (!lava) {
                world.setBlockState(pos, Blocks.MUD.getDefaultState());
            }
        }));

        register(Blocks.FIRE, ((lava, world, pos, state) -> {
            if (!lava) {
                world.breakBlock(pos, true);
            }
        }));
    }

    @FunctionalInterface
    public interface DripOn {
        void dripOn(boolean lava, ServerWorld world, BlockPos pos, BlockState state);
    }

}
