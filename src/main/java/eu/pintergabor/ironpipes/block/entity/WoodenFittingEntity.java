package eu.pintergabor.ironpipes.block.entity;

import static eu.pintergabor.ironpipes.block.entity.base.TickUtil.getTickPos;

import eu.pintergabor.ironpipes.block.entity.base.BaseFluidPipeEntity;
import eu.pintergabor.ironpipes.block.entity.base.TickUtil;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class WoodenFittingEntity extends BaseFluidPipeEntity {

    public WoodenFittingEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.WOODEN_FITTING_ENTITY, blockPos, blockState);
    }

    private static void pull(
        World world, BlockPos pos, BlockState state, WoodenFittingEntity entity) {
    }

    /**
     * Called at every tick on the server.
     */
    public static void serverTick(
        World world, BlockPos pos, BlockState state, WoodenFittingEntity entity) {
        TickUtil.TickPos tickPos = getTickPos(world, 10);
        if (tickPos == TickUtil.TickPos.START) {
            // Pull fluid.
            pull(world, pos, state, entity);
        }
        // Watering plants and other water sensitive blocks and entities.
        if (state.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER) {
            LeakingPipeManager.addPos(world, pos);
        }
    }
}
