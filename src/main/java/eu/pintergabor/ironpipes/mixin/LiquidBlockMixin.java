package eu.pintergabor.ironpipes.mixin;

import eu.pintergabor.ironpipes.block.FluidPipe;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;


@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin {

	@Final
	@Shadow
	protected FlowingFluid fluid;

	/**
	 * A fluid block is not drainable if it is the outflow of a pipe.
	 */
	@Inject(at = @At("HEAD"), method = "pickupBlock", cancellable = true)
	private void pickupBlock(
		@Nullable LivingEntity entity,
		LevelAccessor level,
		BlockPos pos,
		BlockState state,
		CallbackInfoReturnable<ItemStack> cir) {
		if (FluidPipe.isOutflow(level, pos, fluid)) {
			cir.setReturnValue(ItemStack.EMPTY);
		}
	}
}
