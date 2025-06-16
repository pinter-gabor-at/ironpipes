package eu.pintergabor.ironpipes.block.util;

import eu.pintergabor.ironpipes.block.CanCarryFluid;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.util.ModProperties;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.Shapes;


/**
 * More utilities common to fluid pipes and fittings.
 * <p>
 * Drips.
 */
public final class DripActionUtil {

	private DripActionUtil() {
		// Static class.
	}

	/**
	 * Drip water on, or push water into, a cauldron.
	 *
	 * @param level The world.
	 * @param pos   Position of the cauldron.
	 * @param state BlockState of the cauldron.
	 * @return true if state changed.
	 */
	@SuppressWarnings("unused")
	private static boolean dripWaterOnCauldron(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// Start filling an empty cauldron with water.
		level.setBlockAndUpdate(pos,
			Blocks.WATER_CAULDRON.defaultBlockState()
				.setValue(BlockStateProperties.LEVEL_CAULDRON, 1));
		return true;
	}

	/**
	 * Drip lava on, or push lava into, a cauldron.
	 *
	 * @param level The world.
	 * @param pos   Position of the cauldron.
	 * @param state BlockState of the cauldron.
	 * @return true if state changed.
	 */
	@SuppressWarnings("unused")
	private static boolean dripLavaOnCauldron(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// Fill an empty cauldron with lava.
		level.setBlockAndUpdate(pos,
			Blocks.LAVA_CAULDRON.defaultBlockState());
		return true;
	}

	/**
	 * Drip water on, or push water into,
	 * a partially filled water cauldron.
	 *
	 * @param level The world.
	 * @param pos   Position of the cauldron.
	 * @param state BlockState of the cauldron.
	 * @return true if state changed.
	 */
	@SuppressWarnings("unused")
	private static boolean dripWaterOnWaterCauldron(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// Continue filling a water cauldron.
		level.setBlockAndUpdate(pos,
			state.cycle(BlockStateProperties.LEVEL_CAULDRON));
		return true;
	}

	/**
	 * Drip water on, or push water into, a dirt block.
	 *
	 * @param level The world.
	 * @param pos   Position of the block.
	 * @param state BlockState of the block.
	 * @return true if state changed.
	 */
	@SuppressWarnings("unused")
	private static boolean dripWaterOnDirt(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// Water dripping on dirt changes it to mud.
		level.setBlockAndUpdate(pos,
			Blocks.MUD.defaultBlockState());
		return true;
	}

	/**
	 * Drip water on, or push water into, a fire block.
	 *
	 * @param level The world.
	 * @param pos   Position of the block.
	 * @param state BlockState of the block.
	 * @return true if state changed.
	 */
	@SuppressWarnings("unused")
	private static boolean dripWaterOnFire(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// Water dripping on fire extinguishes the fire.
		level.destroyBlock(pos, true);
		return true;
	}

	/**
	 * Drip water on, or push water into, a block.
	 *
	 * @param level The world.
	 * @param pos   Position of the block.
	 * @param state BlockState of the block.
	 * @return true if state changed.
	 */
	public static boolean dripWaterOnBlock(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		final Block block = state.getBlock();
		if (block == Blocks.CAULDRON) {
			// Cauldron.
			return dripWaterOnCauldron(level, pos, state);
		} else if (block == Blocks.WATER_CAULDRON) {
			// Partially filled water cauldron.
			return state.getValue(BlockStateProperties.LEVEL_CAULDRON) < 3 &&
				dripWaterOnWaterCauldron(level, pos, state);
		} else if (block == Blocks.DIRT) {
			// Dirt.
			return dripWaterOnDirt(level, pos, state);
		} else if (block == Blocks.FIRE) {
			// Fire.
			return dripWaterOnFire(level, pos, state);
		}
		return false;
	}

	/**
	 * Drip lava on, or push lava into, a block.
	 *
	 * @param level The world.
	 * @param pos   Position of the block.
	 * @param state BlockState of the block.
	 * @return true if state changed.
	 */
	public static boolean dripLavaOnBlock(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		final Block block = state.getBlock();
		if (block == Blocks.CAULDRON) {
			// Cauldron.
			return dripLavaOnCauldron(level, pos, state);
		}
		return false;
	}

	/**
	 * Start a fire above the block, because lava is dripping on it.
	 *
	 * @param level The world.
	 * @param pos   Position of the block.
	 * @param state BlockState of the block.
	 */
	@SuppressWarnings("unused")
	public static void dripLavaStartFire(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		final BlockPos uPos = pos.above();
		final BlockState uState = level.getBlockState(uPos);
		if (uState.isAir()) {
			level.setBlockAndUpdate(uPos,
				Blocks.FIRE.defaultBlockState());
		}
	}

	/**
	 * Drip water on a block.
	 *
	 * @param level The world.
	 * @param nPos  The block position the water is dripping on.
	 * @return true if there was an interaction.
	 */
	private static boolean dripWaterOn(@NotNull ServerLevel level, BlockPos nPos) {
		final BlockState nState = level.getBlockState(nPos);
		if (!level.getFluidState(nPos).isEmpty()) {
			// A block containing any liquid stops the drip.
			return true;
		}
		if (dripWaterOnBlock(level, nPos, nState)) {
			// A block that reacts with the drip stops the drip.
			return true;
		}
		// A solid block stops the drip.
		return nState.getCollisionShape(level, nPos) != Shapes.empty();
	}

	/**
	 * Dripping water action for pipes and fittings.
	 *
	 * @param level The world.
	 * @param pos   Position of the pipe or the fitting.
	 * @param state BlockState of the pipe or the fitting.
	 */
	@SuppressWarnings("unusedReturnValue")
	public static void dripWaterDown(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		final CanCarryFluid source = (CanCarryFluid) state.getBlock();
		final boolean waterDripping =
			level.random.nextFloat() < source.getWaterDrippingProbability();
		if (waterDripping) {
			// Search down.
			for (int dy = 1; dy <= 12; dy++) {
				if (dripWaterOn(level, pos.below(dy))) break;
			}
		}
	}

	/**
	 * Drip lava on a block.
	 *
	 * @param level  The world.
	 * @param nPos   The block position the lava is dripping on.
	 * @param source Source of the dripping.
	 * @return true if there was an interaction.
	 */
	private static boolean dripLavaOn(
		@NotNull ServerLevel level, @NotNull BlockPos nPos,
		CanCarryFluid source
	) {
		final BlockState nState = level.getBlockState(nPos);
		if (!level.getFluidState(nPos).isEmpty()) {
			// A block containing any liquid stops the drip.
			return true;
		}
		if (dripLavaOnBlock(level, nPos, nState)) {
			// A block that reacts with the drip stops the drip.
			return true;
		}
		if (nState.getCollisionShape(level, nPos) != Shapes.empty()) {
			// A solid block stops the drip, but may start a fire.
			final boolean startFire =
				level.random.nextFloat() < source.getFireDripProbability();
			if (startFire) {
				dripLavaStartFire(level, nPos, nState);
			}
			return true;
		}
		return false;
	}

	/**
	 * Dripping lava action for pipes and fittings.
	 *
	 * @param level The world.
	 * @param pos   Position of the pipe or the fitting.
	 * @param state BlockState of the pipe or the fitting.
	 */
	public static void dripLavaDown(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		final CanCarryFluid source = (CanCarryFluid) state.getBlock();
		final boolean lavaDripping =
			level.random.nextFloat() < source.getLavaDrippingProbability();
		if (lavaDripping) {
			// Search down.
			for (int dy = 1; dy <= 12; dy++) {
				if (dripLavaOn(level, pos.below(dy), source)) break;
			}
		}
	}

	/**
	 * Dripping action for pipes and fittings.
	 *
	 * @param level The world.
	 * @param pos   Position of the pipe or the fitting.
	 * @param state BlockState of the pipe or the fitting.
	 */
	public static void dripDown(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		final PipeFluid fluid = state.getValue(ModProperties.FLUID);
		switch (fluid) {
			case WATER -> dripWaterDown(level, pos, state);
			case LAVA -> dripLavaDown(level, pos, state);
		}
	}
}
