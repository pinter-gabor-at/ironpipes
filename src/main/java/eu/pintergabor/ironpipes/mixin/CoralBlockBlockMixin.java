package eu.pintergabor.ironpipes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.CoralBlockBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(CoralBlockBlock.class)
public abstract class CoralBlockBlockMixin {

    // The coral block is wet, if water is dripping on it.
    @ModifyReturnValue(at = @At("TAIL"), method = "isInWater")
    private boolean isInWater(
        boolean original, BlockView blockView, BlockPos blockPos) {
        return original ||
            LeakingPipeManager.isWaterPipeNearby(blockView, blockPos, 2);
    }
}
