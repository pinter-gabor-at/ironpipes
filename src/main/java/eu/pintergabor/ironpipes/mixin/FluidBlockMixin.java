package eu.pintergabor.ironpipes.mixin;

import eu.pintergabor.ironpipes.block.base.BaseFluidPipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;


@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin {

    @Final
    @Shadow
    protected FlowableFluid fluid;

    // A fluid block is not drainable if it is the outflow of a pipe.
    @Inject(at = @At("HEAD"), method = "tryDrainFluid", cancellable = true)
    private void tryDrainFluid(
        PlayerEntity player,
        WorldAccess world,
        BlockPos pos,
        BlockState state,
        CallbackInfoReturnable<ItemStack> cir) {
        if (BaseFluidPipe.isOutflow(world, pos, fluid)) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
