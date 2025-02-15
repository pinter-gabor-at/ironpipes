package eu.pintergabor.ironpipes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eu.pintergabor.ironpipes.block.CopperPipe;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin {

    @WrapOperation(
        method = "isLitCampfireInRange",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/CampfireBlock;isLitCampfire(Lnet/minecraft/block/BlockState;)Z"
        )
    )
    private static boolean isSmokeyPos(BlockState blockState, Operation<Boolean> operation) {
        return operation.call(blockState) ||
            (blockState.getBlock() instanceof CopperPipe &&
                blockState.get(ModBlockStateProperties.FLUID) == PipeFluid.SMOKE);
    }
}
