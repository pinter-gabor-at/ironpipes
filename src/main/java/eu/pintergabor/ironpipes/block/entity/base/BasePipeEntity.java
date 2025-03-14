package eu.pintergabor.ironpipes.block.entity.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;


public abstract class BasePipeEntity extends BaseBlockEntity {

    public BasePipeEntity(
        BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
}
