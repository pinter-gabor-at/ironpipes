package eu.pintergabor.ironpipes.block.base;

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
public abstract class BaseBlock extends BlockWithEntity {

    protected BaseBlock(Settings settings) {
        super(settings);
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
