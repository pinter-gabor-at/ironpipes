package eu.pintergabor.ironpipes.mixin;

import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin {

    // The sugarcane is placable where water is dropping from a pipe.
    @Inject(
        method = "canPlaceAt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/math/BlockPos;down()Lnet/minecraft/util/math/BlockPos;",
            ordinal = 1,
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void canPlaceAt(
        BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (LeakingPipeManager.isWaterPipeNearby(world, pos, 3)) {
            cir.setReturnValue(true);
        }
    }

}
