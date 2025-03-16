package eu.pintergabor.ironpipes.block.entity;

import eu.pintergabor.ironpipes.block.entity.base.BaseFluidPipeEntity;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;


public class WoodenFittingEntity extends BaseFluidPipeEntity {

    public WoodenFittingEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.WOODEN_FITTING_ENTITY, blockPos, blockState);
    }
}
