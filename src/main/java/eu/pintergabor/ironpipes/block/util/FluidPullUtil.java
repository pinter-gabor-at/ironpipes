package eu.pintergabor.ironpipes.block.util;

import static eu.pintergabor.ironpipes.block.BasePipe.FACING;
import static eu.pintergabor.ironpipes.block.util.FluidUtil.oneSideSourceFluid;

import eu.pintergabor.ironpipes.block.BaseBlock;
import eu.pintergabor.ironpipes.block.BaseFitting;
import eu.pintergabor.ironpipes.block.BasePipe;
import eu.pintergabor.ironpipes.block.FluidPipe;
import eu.pintergabor.ironpipes.block.entity.FluidPipeEntity;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.util.ModProperties;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


/**
 * Utilities for fluid pipes.
 * <p>
 * Pull.
 */
public final class FluidPullUtil {

	private FluidPullUtil() {
		// Static class.
	}

	/**
	 * Check if the block can be used as a natural water source.
	 *
	 * @param state {@link BlockState} (which includes reference to the {@link Block})
	 * @return true if it is a water source
	 */
	@SuppressWarnings("RedundantIfStatement")
	private static boolean isNaturalWaterSource(@NotNull BlockState state) {
		final Block block = state.getBlock();
		if (block == Blocks.WATER) {
			// If it is a still or flowing water block.
			return true;
		}
		if (block == Blocks.WATER_CAULDRON &&
			(state.getValue(BlockStateProperties.LEVEL_CAULDRON) == 3)) {
			// If it is a full water cauldron.
			return true;
		}
		if (state.getValueOrElse(BlockStateProperties.WATERLOGGED, false)) {
			// If it is a waterlogged block.
			return true;
		}
		return false;
	}

	/**
	 * Check if the block can be used as a water source defined in this mod.
	 *
	 * @param state {@link BlockState} (which includes reference to the {@link Block})
	 * @return true if it is a water source
	 */
	@SuppressWarnings("RedundantIfStatement")
	private static boolean isModWaterSource(@NotNull BlockState state) {
		final Block block = state.getBlock();
		if (block instanceof BasePipe) {
			if ((state.getValueOrElse(ModProperties.FLUID, PipeFluid.NONE) == PipeFluid.WATER)) {
				// If it is a pipe carrying water.
				return true;
			}
		}
		if (block instanceof BaseFitting) {
			if (!state.getValueOrElse(BlockStateProperties.POWERED, false) &&
				state.getValueOrElse(ModProperties.FLUID, PipeFluid.NONE) == PipeFluid.WATER) {
				// If it is an unpowered fitting carrying water.
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the block can be used as a water source.
	 *
	 * @param state {@link BlockState} (which includes reference to the {@link Block})
	 * @return true if it is a water source
	 */
	public static boolean isWaterSource(@NotNull BlockState state) {
		return isNaturalWaterSource(state) || isModWaterSource(state);
	}

	/**
	 * Check if the block can be used as a natural lava source.
	 *
	 * @param state {@link BlockState} (which includes reference to the {@link Block})
	 * @return true if it is a lava source
	 */
	@SuppressWarnings("RedundantIfStatement")
	private static boolean isNaturalLavaSource(@NotNull BlockState state) {
		final Block block = state.getBlock();
		if (block == Blocks.LAVA) {
			// If it is a still or flowing lava block.
			return true;
		}
		if (block == Blocks.LAVA_CAULDRON) {
			// If it is a lava cauldron.
			return true;
		}
		return false;
	}

	/**
	 * Check if the block can be used as a lava source defined in this mod.
	 *
	 * @param state {@link BlockState} (which includes reference to the {@link Block})
	 * @return true if it is a lava source
	 */
	@SuppressWarnings("RedundantIfStatement")
	private static boolean isModLavaSource(@NotNull BlockState state) {
		final Block block = state.getBlock();
		if (block instanceof BasePipe) {
			if (state.getValueOrElse(ModProperties.FLUID, PipeFluid.NONE) == PipeFluid.LAVA) {
				// If it is a pipe carrying lava.
				return true;
			}
		}
		if (block instanceof BaseFitting) {
			if (!state.getValueOrElse(BlockStateProperties.POWERED, false) &&
				state.getValueOrElse(ModProperties.FLUID, PipeFluid.NONE) == PipeFluid.LAVA) {
				// If it is an unpowered fitting carrying lava.
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the block can be used as a water source.
	 *
	 * @param state {@link BlockState} (which includes reference to the {@link Block})
	 * @return true if it is a water source
	 */
	public static boolean isLavaSource(@NotNull BlockState state) {
		return isNaturalLavaSource(state) || isModLavaSource(state);
	}

	/**
	 * Get the fluid coming from pipes pointing towards this pipe from a side.
	 *
	 * @param level         The world.
	 * @param pos           Pipe position.
	 * @param facing        Pipe orientation.
	 * @param opposite      = facing.getOpposite();
	 * @param canCarryWater Enable carrying water.
	 * @param canCarryLava  Enable carrying lava.
	 * @return The fluid coming from a side.
	 */
	public static PipeFluid sideSourceFluid(
		@NotNull Level level, @NotNull BlockPos pos,
		@NotNull Direction facing, @NotNull Direction opposite,
		boolean canCarryWater, boolean canCarryLava
	) {
		for (Direction d : BaseBlock.DIRECTIONS) {
			// Check all side directions, but not the front and the back.
			if (d != facing && d != opposite) {
				final PipeFluid nFluid = oneSideSourceFluid(
					level, pos, d, canCarryWater, canCarryLava);
				if (nFluid != PipeFluid.NONE) {
					return nFluid;
				}
			}
		}
		return PipeFluid.NONE;
	}

	/**
	 * Get the fluid coming from a pipe or fitting at the back of this pipe.
	 *
	 * @param backState     BlockState of the block at the back of the pipe.
	 * @param pipeFluid     Fluid in the pipe now.
	 * @param canCarryWater Enable carrying water.
	 * @param canCarryLava  Enable carrying lava.
	 * @return The fluid coming from a side.
	 */
	@SuppressWarnings("unused")
	public static PipeFluid backSourceFluid(
		@NotNull BlockState backState, @NotNull PipeFluid pipeFluid,
		boolean canCarryWater, boolean canCarryLava
	) {
		if (canCarryLava && isLavaSource(backState)) {
			// If a lava source from the back is supplying lava.
			return PipeFluid.LAVA;
		} else if (canCarryWater && isWaterSource(backState)) {
			// If a water source from the back is supplying water.
			return PipeFluid.WATER;
		}
		return PipeFluid.NONE;
	}

	/**
	 * Pull fluid from the block at the back of the pipe.
	 *
	 * @return true if the state is changed.
	 */
	@SuppressWarnings({"UnusedReturnValue", "unused"})
	public static boolean pull(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
		@NotNull FluidPipeEntity entity
	) {
		// This block.
		final Direction facing = state.getValue(FACING);
		final Direction opposite = facing.getOpposite();
		final PipeFluid pipeFluid = state.getValue(ModProperties.FLUID);
		final FluidPipe block = (FluidPipe) state.getBlock();
		final boolean canCarryWater = block.canCarryWater();
		final boolean canCarryLava = block.canCarryLava();
		// The block at the back of the pipe.
		final BlockPos backPos = pos.relative(opposite);
		final BlockState backState = level.getBlockState(backPos);
		// Check if water or lava is coming from the back.
		final PipeFluid backFluid = backSourceFluid(
			backState, pipeFluid,
			canCarryWater, canCarryLava);
		if (backFluid != PipeFluid.NONE) {
			// Water or lava is coming from the back.
			if (pipeFluid != backFluid) {
				level.setBlockAndUpdate(pos,
					state.setValue(ModProperties.FLUID, backFluid));
				return true;
			}
		} else {
			// If no source from the back then
			// find a pipe pointing to this pipe from any side.
			final PipeFluid sideFluid = sideSourceFluid(
				level, pos, facing, opposite,
				canCarryWater, canCarryLava);
			if (sideFluid != PipeFluid.NONE) {
				// Water or lava is coming from the side.
				if (pipeFluid != sideFluid) {
					level.setBlockAndUpdate(pos,
						state.setValue(ModProperties.FLUID, sideFluid));
					return true;
				}
			} else if (pipeFluid != PipeFluid.NONE) {
				// No source from any side.
				level.setBlockAndUpdate(pos,
					state.setValue(ModProperties.FLUID, PipeFluid.NONE));
				return true;
			}
		}
		return false;
	}
}
