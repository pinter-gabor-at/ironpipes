package eu.pintergabor.ironpipes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.AbstractCoralBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(AbstractCoralBlock.class)
public abstract class CoralParentBlockMixin {

    // The coral block is wet, if water is dripping on it.
    @ModifyReturnValue(at = @At("TAIL"), method = "isInWater")
    private static boolean isInWater(
        boolean original, BlockState state, BlockView view, BlockPos pos) {
        return original ||
            LeakingPipeManager.isWaterPipeNearby(view, pos, 2);
    }

}
