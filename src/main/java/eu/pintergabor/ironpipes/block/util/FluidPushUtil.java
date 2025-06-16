package eu.pintergabor.ironpipes.block.util;

import static eu.pintergabor.ironpipes.block.BasePipe.FACING;
import static eu.pintergabor.ironpipes.block.util.DripActionUtil.dripLavaOnBlock;
import static eu.pintergabor.ironpipes.block.util.DripActionUtil.dripWaterOnBlock;
import static net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.SLOT_FUEL;
import static net.minecraft.world.level.block.entity.HopperBlockEntity.getContainerAt;

import eu.pintergabor.ironpipes.block.FluidPipe;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.util.ModProperties;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;


/**
 * More utilities for fluid pipes.
 * <p>
 * Push.
 */
public final class FluidPushUtil {

	private FluidPushUtil() {
		// Static class.
	}

	/**
	 * Fuel a furnace.
	 *
	 * @param level The world.
	 * @param pos   Position of the block in front of the pipe.
	 * @param state BlockState of the block in front of the pipe.
	 * @return true if state changed.
	 */
	@SuppressWarnings("unused")
	private static boolean fuelFurnace(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		final Block block = state.getBlock();
		if (block instanceof AbstractFurnaceBlock) {
			// If it is a furnace ...
			final Container inventory = getContainerAt(level, pos);
			if (inventory != null) {
				final ItemStack stack = inventory.getItem(SLOT_FUEL);
				if (stack.is(Items.BUCKET)) {
					// ... and has an empty bucket in its fuel slot,
					// then replace the emtpy bucket with a lava bucket.
					inventory.setItem(SLOT_FUEL,
						new ItemStack(Items.LAVA_BUCKET));
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Push water into any block that can accept it.
	 *
	 * @param world The world.
	 * @param pos   Position of the block in front of the pipe.
	 * @param state BlockState of the block in front of the pipe.
	 * @return true if state changed.
	 */
	public static boolean pushWaterToBlock(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// Same as drip.
		return dripWaterOnBlock(level, pos, state);
	}

	/**
	 * Push lava into any block that can accept it.
	 *
	 * @param level The world.
	 * @param pos   Position of the block in front of the pipe.
	 * @param state BlockState of the block in front of the pipe.
	 * @return true if state changed.
	 */
	public static boolean pushLavaToBlock(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// Same as drip + Fuel a furnace.
		return dripLavaOnBlock(level, pos, state) ||
			fuelFurnace(level, pos, state);
	}

	/**
	 * Push fluid into the block at the front of the pipe.
	 *
	 * @return true if the state is changed.
	 */
	@SuppressWarnings({"UnusedReturnValue", "unused"})
	public static boolean push(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// This block.
		final Direction facing = state.getValue(FACING);
		final Direction opposite = facing.getOpposite();
		final PipeFluid pipeFluid = state.getValue(ModProperties.FLUID);
		final FluidPipe block = (FluidPipe) state.getBlock();
		// The block in front of this.
		final BlockPos frontPos = pos.relative(facing);
		final BlockState frontState = level.getBlockState(frontPos);
		final Block frontBlock = frontState.getBlock();
		// Logic.
		if (pipeFluid != PipeFluid.NONE) {
			final float rnd = level.random.nextFloat();
			final boolean waterFilling = rnd < block.getWaterFillingProbability();
			final boolean lavaFilling = rnd < block.getLavaFillingProbability();
			// Try to push into the block in front of the pipe.
			if (lavaFilling && pipeFluid == PipeFluid.LAVA) {
				return pushLavaToBlock(level, frontPos, frontState);
			}
			if (waterFilling && pipeFluid == PipeFluid.WATER) {
				return pushWaterToBlock(level, frontPos, frontState);
			}
		}
		return false;
	}
}
