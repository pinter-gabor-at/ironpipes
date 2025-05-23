package eu.pintergabor.ironpipes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import eu.pintergabor.ironpipes.block.util.WateringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;


@Mixin(FarmBlock.class)
public abstract class FarmBlockMixin {

	// The farmland is wet, if water is dripping on it.
	@ModifyReturnValue(at = @At("RETURN"), method = "isNearWater")
	private static boolean isNearWater(
		boolean original,
		LevelReader levelReader, BlockPos pos
	) {
		return original ||
			(levelReader instanceof Level level &&
				WateringUtil.isWaterPipeNearby(level, pos, 6));
	}
}
