package eu.pintergabor.ironpipes.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.Waterloggable;

import net.minecraft.state.StateManager;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.ai.pathing.NavigationType;


/**
 * Base block of the mod.
 * <p>
 * Pipes and fittings are rendered normally, they do not block light,
 * and entities cannot walk through them.
 */
public abstract class BaseBlock extends BlockWithEntity implements Waterloggable {
    public static final BooleanProperty WATERLOGGED =
        Properties.WATERLOGGED;

    protected BaseBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
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

    /**
     * Pipes and fittings are rendered normally.
     */
    @Override
    @NotNull
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }
}
