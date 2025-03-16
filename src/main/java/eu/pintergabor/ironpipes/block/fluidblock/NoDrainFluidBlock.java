package eu.pintergabor.ironpipes.block.fluidblock;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;


public class NoDrainFluidBlock extends FluidBlock {
    public NoDrainFluidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public ItemStack tryDrainFluid(
        @Nullable PlayerEntity player,
        WorldAccess world, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }
}
