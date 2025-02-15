package eu.pintergabor.ironpipes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.FarmlandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin {

    // The farmland is wet, if water is dripping on it.
    @ModifyReturnValue(at = @At("RETURN"), method = "isWaterNearby")
    private static boolean isWaterNearby(
        boolean original, WorldView view, BlockPos pos) {
        return original ||
            LeakingPipeManager.isWaterPipeNearby(view, pos, 6);
    }
}
