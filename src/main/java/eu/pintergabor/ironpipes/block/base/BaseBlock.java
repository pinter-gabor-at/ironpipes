package eu.pintergabor.ironpipes.block.base;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;


public abstract class BaseBlock extends BlockWithEntity {
    public static final BooleanProperty POWERED =
        Properties.POWERED;

    protected BaseBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
            .with(POWERED, false));
    }
}
