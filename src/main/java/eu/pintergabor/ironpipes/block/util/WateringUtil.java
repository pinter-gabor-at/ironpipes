package eu.pintergabor.ironpipes.block.util;

import eu.pintergabor.ironpipes.block.CanCarryFluid;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;


/**
 * Watering from pipes and fittings.
 */
public final class WateringUtil {

	private WateringUtil() {
		// Static class.
	}

	/**
	 * @return true if the {@code entity} is affected by the water carrying pipe of fitting.
	 */
	private static boolean isLeakingWater(@NotNull Entity entity, @NotNull Vec3 blockCenterPos) {
		final Vec3 entityPos = entity.position();
		final Level level = entity.level();
		final BlockHitResult hitResult = level.clip(
			new ClipContext(entityPos,
				blockCenterPos,
				ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE,
				entity));
		final BlockPos pos = hitResult.getBlockPos();
		final BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof CanCarryFluid block) {
			final boolean watering =
				level.random.nextFloat() < block.getWateringProbability();
			final PipeFluid fluid = CanCarryFluid.getFluid(state);
			return watering && fluid == PipeFluid.WATER;
		}
		return false;
	}

	/**
	 * @return true if the block at {@code blockPos} is affected by the water carrying pipe of fitting.
	 */
	private static boolean isLeakingWater(@NotNull Level level, @NotNull BlockPos blockPos) {
		final BlockState state = level.getBlockState(blockPos);
		if (state.getBlock() instanceof CanCarryFluid block) {
			final boolean watering =
				level.random.nextFloat() < block.getWateringProbability();
			final PipeFluid fluid = CanCarryFluid.getFluid(state);
			return watering && fluid == PipeFluid.WATER;
		}
		return false;
	}

	/**
	 * Check if there is a leaking water pipe within range.
	 * <p>
	 * Y range is fixed [0..12].
	 *
	 * @param entity Target entity
	 * @param range  X and Z range [-range..+range]
	 * @return true if there is a leaking water pipe or fitting in range.
	 */
	public static boolean isWaterPipeNearby(@NotNull Entity entity, int range) {
		// Target coordinates.
		final int targetX = entity.getBlockX();
		final int targetY = entity.getBlockY();
		final int targetZ = entity.getBlockZ();
		// Search for a leaking water carrying pipe or fitting in range
		// [-range..+range, 0..12, -range..+range] of the target block.
		for (int y = targetY; y <= targetY + 12; y++) {
			for (int x = targetX - range; x <= targetX + range; x++) {
				for (int z = targetZ - range; z <= targetZ + range; z++) {
					if (isLeakingWater(entity, new Vec3(x + 0.5, y + 0.5, z + 0.5))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Check if there is a leaking water pipe within range.
	 * <p>
	 * Y range is fixed [0..12].
	 *
	 * @param level Level
	 * @param pos   Target position
	 * @param range X and Z range [-range..+range]
	 * @return true if there is a leaking water pipe or fitting in range.
	 */
	public static boolean isWaterPipeNearby(@NotNull Level level, @NotNull BlockPos pos, int range) {
		// Target coordinates.
		final int targetX = pos.getX();
		final int targetY = pos.getY();
		final int targetZ = pos.getZ();
		// Search for a leaking water carrying pipe or fitting in range
		// [-range..+range, 0..12, -range..+range] of the target block.
		for (int y = targetY; y <= targetY + 12; y++) {
			for (int x = targetX - range; x <= targetX + range; x++) {
				for (int z = targetZ - range; z <= targetZ + range; z++) {
					if (isLeakingWater(level, new BlockPos(x, y, z))) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
