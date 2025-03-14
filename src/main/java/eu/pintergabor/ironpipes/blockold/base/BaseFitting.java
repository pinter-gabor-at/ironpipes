package eu.pintergabor.ironpipes.blockold.base;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import org.jetbrains.annotations.NotNull;


public abstract class BaseFitting extends BaseBlock {
    protected static final VoxelShape FITTING_SHAPE =
        Block.createCuboidShape(2.5D, 2.5D, 2.5D, 13.5D, 13.5D, 13.5D);

    protected BaseFitting(Settings settings) {
        super(settings);
    }

    @Override
    @NotNull
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return FITTING_SHAPE;
    }

    @Override
    @NotNull
    public VoxelShape getRaycastShape(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return FITTING_SHAPE;
    }

}
